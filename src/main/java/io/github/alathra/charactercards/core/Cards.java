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
import java.util.Map;

public class Cards {
    public static void displayCard(Player player, Player targetPlayer){
        PlayerProfile targetPlayerProfile = CharacterCards.playerProfiles.get(targetPlayer.getUniqueId());

        StringBuilder dashDecor = new StringBuilder();
        int nameLength;
        nameLength = targetPlayer.getName().length() / 2;

        dashDecor.append("=".repeat(Math.max(0, 15 - nameLength)));

        player.sendMessage(ColorParser.of("<dark_gray>╔<b>" + dashDecor + "</b></dark_gray><gray>[<dark_aqua>" + targetPlayer.getName() + "’s Card</dark_aqua><gray>]<dark_gray><b>" + dashDecor + "</b>╗").build());
        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.ign", "IGN ") + " <dark_gray>≫ <aqua>" + targetPlayer.getName()).build());

        StringBuilder name = new StringBuilder();

        if(!targetPlayerProfile.getCharacter_title().isEmpty())
            name.append(targetPlayerProfile.getCharacter_title()).append(" ");

        if(!targetPlayerProfile.getCharacter_first_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_first_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_last_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_last_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_suffix().isEmpty())
            name.append(targetPlayerProfile.getCharacter_suffix()).append(" ");

        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.name", "Name") + " <dark_gray>≫ <aqua>" + name).build());
        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.gender", "Gender") + " <dark_gray>≫ <aqua>" + targetPlayerProfile.getCharacter_gender()).build());
        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.age", "Age") + " <dark_gray>≫ <aqua>" + targetPlayerProfile.getCharacter_age()).build());

        if(!Settings.getRawCustomFields().isEmpty()) {
            for (Map.Entry<String, String> entry : Settings.getRawCustomFields().entrySet()) {
                String fieldLabel = entry.getKey();
                String fieldPlaceholder = PlaceholderAPI.setPlaceholders(targetPlayer, entry.getValue());
                player.sendMessage(ColorParser.of("<dark_aqua>   " + fieldLabel + " <dark_gray>≫ <aqua>" + fieldPlaceholder).build());
            }
        }

        player.sendMessage(ColorParser.of("<dark_gray><b> <st>==============</st></b><gray>[<dark_aqua>" + Translation.of("cards.player.description", "Description") +"<gray>]<b><dark_gray><st>==============").build());
        player.sendMessage(ColorParser.of("<aqua> " + targetPlayerProfile.getCharacter_description()).build());
        player.sendMessage(ColorParser.of("<dark_gray>╚<b>===============</b><dark_aqua>≪ °❈° ≫<dark_gray><b>================</b>╝").build());
    }

    @SuppressWarnings({"all"})
    public static void displayOfflinePlayerCard(Player player, OfflinePlayer targetPlayer){
        PlayerProfile targetPlayerProfile = CharacterCards.playerProfiles.get(targetPlayer.getUniqueId());

        StringBuilder dashDecor = new StringBuilder();
        int nameLength;
        nameLength = targetPlayer.getPlayer().getName().length() / 2;

        dashDecor.append("=".repeat(Math.max(0, 15 - nameLength)));

        player.sendMessage(ColorParser.of("<dark_gray>╔<b>" + dashDecor + "</b></dark_gray><gray>[<dark_aqua>" + targetPlayer.getName() + "’s Card</dark_aqua><gray>]<dark_gray><b>" + dashDecor + "</b>╗").build());
        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.lastonline", "Last Online") + " <dark_gray>≫ <aqua>" +
            Instant.ofEpochMilli(targetPlayer.getLastSeen()).atZone(ZoneId.of("America/New_York")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a z"))
        ).build());
        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.ign", "IGN ") + " <dark_gray>≫ <aqua>" + targetPlayer.getName()).build());

        StringBuilder name = new StringBuilder();

        if(!targetPlayerProfile.getCharacter_title().isEmpty())
            name.append(targetPlayerProfile.getCharacter_title()).append(" ");

        if(!targetPlayerProfile.getCharacter_first_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_first_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_last_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_last_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_suffix().isEmpty())
            name.append(targetPlayerProfile.getCharacter_suffix()).append(" ");

        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.name", "Name") + " <dark_gray>≫ <aqua>" + name).build());
        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.gender", "Gender") + " <dark_gray>≫ <aqua>" + targetPlayerProfile.getCharacter_gender()).build());
        player.sendMessage(ColorParser.of("<dark_aqua>   " + Translation.of("cards.player.age", "Age") + " <dark_gray>≫ <aqua>" + targetPlayerProfile.getCharacter_age()).build());

        if(!Settings.getRawCustomFields().isEmpty()) {
            for (Map.Entry<String, String> entry : Settings.getRawCustomFields().entrySet()) {
                String fieldLabel = entry.getKey();
                String fieldPlaceholder = PlaceholderAPI.setPlaceholders(targetPlayer, entry.getValue());
                player.sendMessage(ColorParser.of("<dark_aqua>   " + fieldLabel + " <dark_gray>≫ <aqua>" + fieldPlaceholder).build());
            }
        }

        player.sendMessage(ColorParser.of("<dark_gray><b> <st>==============</st></b><gray>[<dark_aqua>" + Translation.of("cards.player.description", "Description") +"<gray>]<b><dark_gray><st>==============").build());
        player.sendMessage(ColorParser.of("<aqua> " + targetPlayerProfile.getCharacter_description()).build());
        player.sendMessage(ColorParser.of("<dark_gray>╚<b>===============</b><dark_aqua>≪ °❈° ≫<dark_gray><b>================</b>╝").build());
    }

    public static void displayHelp(Player player) {
        player.sendMessage(ColorParser.of("<dark_gray>╔<b><st>===========================================</st></b>╗").build());
        player.sendMessage(ColorParser.of("                         <dark_aqua>CharacterCards <aqua><b>" + CharacterCards.getInstance().getPluginMeta().getVersion().substring(0, 5)).build());
        player.sendMessage(ColorParser.of("<dark_gray>╚<b><st>===========================================</st></b>╝").build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char edit <aqua>title <gray><<aqua>Title<gray>> <dark_gray>- <white>" + Translation.of("cards.help.title", "Set your character's title")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char edit <aqua>firstname <gray><<aqua>First Name<gray>> <dark_gray>- <white>" + Translation.of("cards.help.firstname", "Set your character's first name")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char edit <aqua>lastname <gray><<aqua>Last Name<gray>> <dark_gray>- <white>" + Translation.of("cards.help.lastname", "Set your character's last name")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char edit <aqua>suffix <gray><<aqua>Suffix<gray>> <dark_gray>- <white>" + Translation.of("cards.help.suffix", "Set your character's suffix")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char edit <aqua>gender <gray><<aqua>Gender<gray>> <dark_gray>- <white>" + Translation.of("cards.help.gender", "Set your character's gender")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char edit <aqua>age <gray><<aqua>" + Settings.getMinimumAge() + "<gray>-<aqua>"
            + Settings.getMaximumAge() + "<gray>> <dark_gray>- <white>" + Translation.of("cards.help.age", "Set your character's age.")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char edit <aqua>desc <gray><<aqua>Description<gray>> <dark_gray>- <white>" + Translation.of("cards.help.desc", "Set your character's description, <red><b>512 CHARACTER LIMIT")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char help <dark_gray>- <white>" + Translation.of("cards.help.help", "Displays the plugin commands")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char view <gray>{<aqua>Player<gray>} <dark_gray>- <white>" + Translation.of("cards.help.view", "View your card, or another player's")).build());
        player.sendMessage(ColorParser.of("  <gray>/<dark_aqua>char viewoffline <gray>{<aqua>Player<gray>} <dark_gray>- <white>" + Translation.of("cards.help.viewoffline", "Views an offline player's card")).build());
    }
}
