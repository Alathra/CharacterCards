package io.github.alathra.charactercards.database;

import io.github.alathra.charactercards.core.PlayerProfile;
import io.github.alathra.charactercards.utility.DB;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.wordweaver.Translation;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;

import static io.github.alathra.charactercards.database.schema.Tables.*;
import static io.github.alathra.charactercards.database.QueryUtils.*;

/**
 * A class providing access to all SQL queries.
 */
public abstract class Queries {
    public static void savePlayerProfile(PlayerProfile playerProfile, String field, Player player) {
        final PlayerProfile clonedProfile = playerProfile.clone();

        CompletableFuture.runAsync(() -> {
            try (Connection con = DB.getConnection()) {
                DSLContext context = DB.getContext(con);
                byte[] uuidBytes = UUIDUtil.toBytes(clonedProfile.getPlayerUuid());

                boolean success = switch (field) {
                    case "NAME" ->
                        context.update(CARDS)
                            .set(CARDS.PLAYER_NAME, clonedProfile.getPlayerName())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    case "TITLE" ->
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_TITLE, clonedProfile.getCharacterTitle())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    case "FIRST_NAME" ->
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_FIRST_NAME, clonedProfile.getCharacterFirstName())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    case "LAST_NAME" ->
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_LAST_NAME, clonedProfile.getCharacterLastName())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    case "SUFFIX" ->
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_SUFFIX, clonedProfile.getCharacterSuffix())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    case "GENDER" ->
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_GENDER, clonedProfile.getCharacterGender())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    case "AGE" ->
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_AGE, clonedProfile.getCharacterAge())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    case "DESCRIPTION" ->
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_DESCRIPTION, clonedProfile.getCharacterDescription())
                            .where(CARDS.PLAYER_UUID.eq(uuidBytes))
                            .execute() > 0;
                    default -> false;
                };

                if(success) {
                    player.sendMessage(ColorParser.of(Translation.of("cards.error.card_savesuccess")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("cards.error.card_savefail")).build());
                }

            } catch (DataAccessException | SQLException e) {
                player.sendMessage(ColorParser.of(Translation.of("cards.error.card_savefail")).build());
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<Optional<PlayerProfile>> loadPlayerProfile(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerProfile profile;

            try (Connection con = DB.getConnection()) {
                DSLContext context = DB.getContext(con);

                Record record = context.select()
                    .from(CARDS)
                    .where(CARDS.PLAYER_UUID.eq(UUIDUtil.toBytes(player.getUniqueId())))
                    .fetchOne();

                if (record == null) {
                    //loads in a new entry into server memory
                    profile = new PlayerProfile(
                        player.getUniqueId(),
                        player.getName(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        ""
                    );

                    //saves the previous created entry into the database
                    context.insertInto(
                        CARDS,

                        CARDS.PLAYER_UUID,
                        CARDS.PLAYER_NAME,
                        CARDS.CHARACTER_TITLE,
                        CARDS.CHARACTER_FIRST_NAME,
                        CARDS.CHARACTER_LAST_NAME,
                        CARDS.CHARACTER_SUFFIX,
                        CARDS.CHARACTER_GENDER,
                        CARDS.CHARACTER_AGE,
                        CARDS.CHARACTER_DESCRIPTION
                    ).values(
                        QueryUtils.UUIDUtil.toBytes(player.getUniqueId()),
                        player.getName(),
                        profile.getCharacterTitle(),
                        profile.getCharacterFirstName(),
                        profile.getCharacterLastName(),
                        profile.getCharacterSuffix(),
                        profile.getCharacterGender(),
                        profile.getCharacterAge(),
                        profile.getCharacterDescription()
                    ).execute();
                } else {
                    profile = new PlayerProfile(
                        player.getUniqueId(),
                        player.getName(),
                        record.get(CARDS.CHARACTER_TITLE),
                        record.get(CARDS.CHARACTER_FIRST_NAME),
                        record.get(CARDS.CHARACTER_LAST_NAME),
                        record.get(CARDS.CHARACTER_SUFFIX),
                        record.get(CARDS.CHARACTER_GENDER),
                        record.get(CARDS.CHARACTER_AGE),
                        record.get(CARDS.CHARACTER_DESCRIPTION)
                    );

                    //check if player name has changed
                    if(!player.getName().equals(record.get(CARDS.PLAYER_NAME))) {
                        savePlayerProfile(profile, "NAME", player);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.of(profile);
        });
    }

    public static void loadOfflinePlayerProfile(OfflinePlayer player) {
        CompletableFuture.supplyAsync(() -> {
            PlayerProfile profile;

            try (Connection con = DB.getConnection()) {
                DSLContext context = DB.getContext(con);

                Record record = context.select()
                    .from(CARDS)
                    .where(CARDS.PLAYER_UUID.eq(UUIDUtil.toBytes(player.getUniqueId())))
                    .fetchOne();

                // if no record exists, player has not joined the server
                // if no player name, player does not exist in Mojang auth servers
                // This check is already done in CharacterCommand class, but it is here for the sake of it
                if (record == null || player.getName() == null) {
                    return Optional.empty();
                }

                profile = new PlayerProfile(
                    player.getUniqueId(),
                    player.getName(),
                    record.get(CARDS.CHARACTER_TITLE),
                    record.get(CARDS.CHARACTER_FIRST_NAME),
                    record.get(CARDS.CHARACTER_LAST_NAME),
                    record.get(CARDS.CHARACTER_SUFFIX),
                    record.get(CARDS.CHARACTER_GENDER),
                    record.get(CARDS.CHARACTER_AGE),
                    record.get(CARDS.CHARACTER_DESCRIPTION)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.of(profile);
        });
    }
}
