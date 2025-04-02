package io.github.alathra.charactercards.database;

import io.github.alathra.charactercards.core.PlayerProfile;
import io.github.alathra.charactercards.utility.DB;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jooq.Record;

import static io.github.alathra.charactercards.database.schema.Tables.*;
import static io.github.alathra.charactercards.database.QueryUtils.*;

/**
 * A class providing access to all SQL queries.
 */
public abstract class Queries {
    public static void savePlayerProfile(PlayerProfile playerProfile, String field) {
        final PlayerProfile profile = playerProfile.clone();
        CompletableFuture.runAsync(() -> {
            try (Connection con = DB.getConnection()) {
                DSLContext context = DB.getContext(con);

                switch (field) {
                    case "NAME":
                        context.update(CARDS)
                            .set(CARDS.PLAYER_NAME, profile.getPlayer_name())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                        return;
                    case "TITLE":
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_TITLE, profile.getCharacter_title())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                        return;
                    case "FIRST_NAME":
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_FIRST_NAME, profile.getCharacter_first_name())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                        return;
                    case "LAST_NAME":
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_LAST_NAME, profile.getCharacter_last_name())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                        return;
                    case "SUFFIX":
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_SUFFIX, profile.getCharacter_suffix())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                        return;
                    case "GENDER":
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_GENDER, profile.getCharacter_gender())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                        return;
                    case "AGE":
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_AGE, profile.getCharacter_age())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                        return;
                    case "DESCRIPTION":
                        context.update(CARDS)
                            .set(CARDS.CHARACTER_DESCRIPTION, profile.getCharacter_description())
                            .where(CARDS.PLAYER_UUID.eq(QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid())))
                            .execute();
                }
            } catch (SQLException e) {
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
                        profile.getCharacter_title(),
                        profile.getCharacter_first_name(),
                        profile.getCharacter_last_name(),
                        profile.getCharacter_suffix(),
                        profile.getCharacter_gender(),
                        profile.getCharacter_age(),
                        profile.getCharacter_description()
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
                        savePlayerProfile(profile, "NAME");
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

                //check if player name has changed
                if(!player.getName().equals(record.get(CARDS.PLAYER_NAME))) {
                    savePlayerProfile(profile, "NAME");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.of(profile);
        });
    }
}
