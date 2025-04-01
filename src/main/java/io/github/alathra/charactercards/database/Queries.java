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
    public static void savePlayerProfile(PlayerProfile playerProfile) {
        final PlayerProfile profile = playerProfile.clone();
        CompletableFuture.runAsync(() -> {
            try (Connection con = DB.getConnection()) {
                DSLContext context = DB.getContext(con);
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
                        UUIDUtil.toBytes(profile.getPlayer_uuid()),
                        profile.getPlayer_name(),
                        profile.getCharacter_title(),
                        profile.getCharacter_first_name(),
                        profile.getCharacter_last_name(),
                        profile.getCharacter_suffix(),
                        profile.getCharacter_gender(),
                        profile.getCharacter_age(),
                        profile.getCharacter_description()
                    ).onDuplicateKeyUpdate()
                    .set(CARDS.PLAYER_NAME, profile.getPlayer_name())
                    .set(CARDS.CHARACTER_TITLE, profile.getCharacter_title())
                    .set(CARDS.CHARACTER_FIRST_NAME, profile.getCharacter_first_name())
                    .set(CARDS.CHARACTER_LAST_NAME, profile.getCharacter_last_name())
                    .set(CARDS.CHARACTER_SUFFIX, profile.getCharacter_suffix())
                    .set(CARDS.CHARACTER_GENDER, profile.getCharacter_gender())
                    .set(CARDS.CHARACTER_AGE, profile.getCharacter_age())
                    .set(CARDS.CHARACTER_DESCRIPTION, profile.getCharacter_description())
                    .execute();
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
                        QueryUtils.UUIDUtil.toBytes(profile.getPlayer_uuid()),
                        profile.getPlayer_name(),
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
                        QueryUtils.UUIDUtil.fromBytes(record.get(CARDS.PLAYER_UUID)),
                        record.get(CARDS.PLAYER_NAME),
                        record.get(CARDS.CHARACTER_TITLE),
                        record.get(CARDS.CHARACTER_FIRST_NAME),
                        record.get(CARDS.CHARACTER_LAST_NAME),
                        record.get(CARDS.CHARACTER_SUFFIX),
                        record.get(CARDS.CHARACTER_GENDER),
                        record.get(CARDS.CHARACTER_AGE),
                        record.get(CARDS.CHARACTER_DESCRIPTION)
                    );
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return Optional.of(profile);
        });
    }

    public static CompletableFuture<Optional<PlayerProfile>> loadOfflinePlayerProfile(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerProfile profile;

            try (Connection con = DB.getConnection()) {
                DSLContext context = DB.getContext(con);

                Record record = context.select()
                    .from(CARDS)
                    .where(CARDS.PLAYER_UUID.eq(UUIDUtil.toBytes(player.getUniqueId())))
                    .fetchOne();

                if(record == null) {
                    return Optional.empty();
                }

                profile = new PlayerProfile(
                    QueryUtils.UUIDUtil.fromBytes(record.get(CARDS.PLAYER_UUID)),
                    record.get(CARDS.PLAYER_NAME),
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
