package io.github.alathra.charactercards.core;

import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.Settings;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.wordweaver.Translation;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Cards {
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("America/New_York");
    private static final DateTimeFormatter DEFAULT_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a z");

    /**
     * Displays the character card of an online player to a player, showing all saved character data.
     */
    public static void displayCard(Player player, Player targetPlayer){
        displayGenericCard(player, targetPlayer, null);
    }

    /**
     * Displays the character card of an offline player to a player, showing all saved character data.
     */
    public static void displayOfflinePlayerCard(Player player, OfflinePlayer targetPlayer){
        displayGenericCard(player, targetPlayer, Instant.ofEpochMilli(targetPlayer.getLastSeen()));
    }

    /**
     * Displays all relevant CharacterCards commands to a player.
     */
    public static void displayHelp(Player viewer) {
        sendFormattedMessage(viewer, "<dark_gray>╔<b><st>===========================================</st></b>╗");
        sendFormattedMessage(viewer, "                         <dark_aqua>CharacterCards <aqua><b>" + CharacterCards.getInstance().getPluginMeta().getVersion().substring(0, 5));
        sendFormattedMessage(viewer, "<dark_gray>╚<b><st>===========================================</st></b>╝");
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char edit <aqua>title <gray><<aqua>Title<gray>> <dark_gray>- <white>" + Translation.of("cards.help.title", "Set your character's title"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char edit <aqua>firstname <gray><<aqua>First Name<gray>> <dark_gray>- <white>" + Translation.of("cards.help.firstname", "Set your character's first name"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char edit <aqua>lastname <gray><<aqua>Last Name<gray>> <dark_gray>- <white>" + Translation.of("cards.help.lastname", "Set your character's last name"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char edit <aqua>suffix <gray><<aqua>Suffix<gray>> <dark_gray>- <white>" + Translation.of("cards.help.suffix", "Set your character's suffix"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char edit <aqua>gender <gray><<aqua>Gender<gray>> <dark_gray>- <white>" + Translation.of("cards.help.gender", "Set your character's gender"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char edit <aqua>age <gray><<aqua>" + Settings.getMinimumAge() + "<gray>-<aqua>"
            + Settings.getMaximumAge() + "<gray>> <dark_gray>- <white>" + Translation.of("cards.help.age", "Set your character's age."));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char edit <aqua>desc <gray><<aqua>Description<gray>> <dark_gray>- <white>" + Translation.of("cards.help.desc", "Set your character's description, <red><b>512 CHARACTER LIMIT"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char help <dark_gray>- <white>" + Translation.of("cards.help.help", "Displays the plugin commands"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char view <gray>{<aqua>Player<gray>} <dark_gray>- <white>" + Translation.of("cards.help.view", "View your card, or another player's"));
        sendFormattedMessage(viewer, "  <gray>/<dark_aqua>char viewoffline <gray>{<aqua>Player<gray>} <dark_gray>- <white>" + Translation.of("cards.help.viewoffline", "Views an offline player's card"));
    }

    private static void displayGenericCard(Player viewer, OfflinePlayer targetPlayer, Instant lastSeen) {
        if(targetPlayer == null) return;

        String formattedLastSeen = lastSeen != null
            ? lastSeen.atZone(DEFAULT_ZONE).format(DEFAULT_TIME_FORMAT)
            : null;

        String targetName = targetPlayer.getName();

        PlayerProfile targetPlayerProfile = CharacterCards.playerProfiles.get(targetPlayer.getUniqueId());
        if (targetPlayerProfile == null) {
            sendFormattedMessage(viewer, "<red>Player profile not found.");
            return;
        }

        String dashDecor = getDashPaddingForName(targetPlayer);

        sendFormattedMessage(viewer, "<dark_gray>╔<b>" + dashDecor + "</b></dark_gray><gray>[<dark_aqua>" + targetName + "’s Card</dark_aqua><gray>]<dark_gray><b>" + dashDecor + "</b>╗");
        if (formattedLastSeen != null) {
            sendFormattedMessage(viewer, "<dark_aqua>   " + Translation.of("cards.player.lastonline", "Last Online") + " <dark_gray>≫ <aqua>" + formattedLastSeen);
        }
        sendFormattedMessage(viewer, "<dark_aqua>   " + Translation.of("cards.player.ign", "IGN ") + " <dark_gray>≫ <aqua>" + targetName);
        sendFormattedMessage(viewer, "<dark_aqua>   " + Translation.of("cards.player.name", "Name") + " <dark_gray>≫ <aqua>" + returnFullName(targetPlayerProfile));
        sendFormattedMessage(viewer, "<dark_aqua>   " + Translation.of("cards.player.gender", "Gender") + " <dark_gray>≫ <aqua>" + targetPlayerProfile.getCharacterGender());
        sendFormattedMessage(viewer, "<dark_aqua>   " + Translation.of("cards.player.age", "Age") + " <dark_gray>≫ <aqua>" + targetPlayerProfile.getCharacterAge());
        displayCustomFields(viewer, targetPlayer);
        sendFormattedMessage(viewer, "<dark_gray><b> <st>==============</st></b><gray>[<dark_aqua>" + Translation.of("cards.player.description", "Description") +"<gray>]<b><dark_gray><st>==============");
        sendFormattedMessage(viewer, "<aqua> " + targetPlayerProfile.getCharacterDescription());
        sendFormattedMessage(viewer, "<dark_gray>╚<b>===============</b><dark_aqua>≪ °❈° ≫<dark_gray><b>================</b>╝");
    }

    private static void sendFormattedMessage(Player viewer, String string) {
        viewer.sendMessage(ColorParser.of(string).build());
    }

    private static String returnFullName(PlayerProfile profile) {
        String[] profileNameParts = {
            profile.getCharacterTitle(),
            profile.getCharacterFirstName(),
            profile.getCharacterLastName(),
            profile.getCharacterSuffix()
        };

        return Arrays.stream(profileNameParts)
            .filter(part -> part != null && !part.isEmpty())
            .collect(Collectors.joining(" "));
    }

    private static void displayCustomFields(Player viewer, OfflinePlayer targetPlayer) {
        Settings.getRawCustomFields().forEach((fieldLabel, placeholder) -> {
            String fieldPlaceholder = PlaceholderAPI.setPlaceholders(targetPlayer, placeholder);
            sendFormattedMessage(viewer, "<dark_aqua>   " + fieldLabel + " <dark_gray>≫ <aqua>" + fieldPlaceholder);
        });
    }

    private static String getDashPaddingForName(OfflinePlayer targetPlayer) {
        if(targetPlayer.getName() == null) return "===============";

        int nameLength = targetPlayer.getName().length();
        int dashCount = Math.max(0, 16 - nameLength / 2);

        return "=".repeat(dashCount);
    }
}
