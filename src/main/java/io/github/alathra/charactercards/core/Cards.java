package io.github.alathra.charactercards.core;

import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.Settings;
import io.github.milkdrinkers.colorparser.ColorParser;
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

        dashDecor.append("-".repeat(Math.max(0, 16 - nameLength)));

        player.sendMessage(ColorParser.of("&8&l&m" + dashDecor + "&r&7[&3" + targetPlayer.getName() + "’s Card&7]&8&l&m" + dashDecor).parseLegacy().build());
        player.sendMessage(ColorParser.of("&3IGN &8- &b" + targetPlayer.getName()).parseLegacy().build());

        StringBuilder name = new StringBuilder();

        if(!targetPlayerProfile.getCharacter_title().isEmpty())
            name.append(targetPlayerProfile.getCharacter_title()).append(" ");

        if(!targetPlayerProfile.getCharacter_first_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_first_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_last_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_last_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_suffix().isEmpty())
            name.append(targetPlayerProfile.getCharacter_suffix()).append(" ");

        player.sendMessage(ColorParser.of("&3Name &8- &b" + name).parseLegacy().build());
        player.sendMessage(ColorParser.of("&3Gender &8- &b" + targetPlayerProfile.getCharacter_gender()).parseLegacy().build());
        player.sendMessage(ColorParser.of("&3Age &8- &b" + targetPlayerProfile.getCharacter_age()).parseLegacy().build());

        if(!Settings.getRawCustomFields().isEmpty()) {
            for (Map.Entry<String, String> entry : Settings.getRawCustomFields().entrySet()) {
                String fieldLabel = entry.getKey();
                String fieldPlaceholder = PlaceholderAPI.setPlaceholders(targetPlayer, entry.getValue());
                player.sendMessage(ColorParser.of(fieldLabel + " &8- " + fieldPlaceholder).parseLegacy().build());
            }
        }

        player.sendMessage(ColorParser.of("&8&l&m---------------&r&7[&3Description&7]&8&l&m---------------").parseLegacy().build());
        player.sendMessage(ColorParser.of("&3Desc &8- &b" + targetPlayerProfile.getCharacter_description()).parseLegacy().build());
        player.sendMessage(ColorParser.of("&8&l&m---------------------------------------").parseLegacy().build());
    }

    @SuppressWarnings({"all"})
    public static void displayOfflinePlayerCard(Player player, OfflinePlayer targetPlayer){
        PlayerProfile targetPlayerProfile = CharacterCards.playerProfiles.get(targetPlayer.getUniqueId());

        StringBuilder dashDecor = new StringBuilder();
        int nameLength;
        nameLength = targetPlayer.getPlayer().getName().length() / 2;

        dashDecor.append("-".repeat(Math.max(0, 16 - nameLength)));

        player.sendMessage(ColorParser.of("&8&l&m" + dashDecor + "&r&7[&3" + targetPlayer.getName() + "’s Card&7]&8&l&m" + dashDecor).parseLegacy().build());
        player.sendMessage(ColorParser.of("&3Last Online &8- &b" +
            Instant.ofEpochMilli(targetPlayer.getLastSeen()).atZone(ZoneId.of("America/New_York")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a z"))
        ).parseLegacy().build());
        player.sendMessage(ColorParser.of("&3IGN &8- &b" + targetPlayer.getName()).parseLegacy().build());

        StringBuilder name = new StringBuilder();

        if(!targetPlayerProfile.getCharacter_title().isEmpty())
            name.append(targetPlayerProfile.getCharacter_title()).append(" ");

        if(!targetPlayerProfile.getCharacter_first_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_first_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_last_name().isEmpty())
            name.append(targetPlayerProfile.getCharacter_last_name()).append(" ");

        if(!targetPlayerProfile.getCharacter_suffix().isEmpty())
            name.append(targetPlayerProfile.getCharacter_suffix()).append(" ");

        player.sendMessage(ColorParser.of("&3Name &8- &b" + name).parseLegacy().build());
        player.sendMessage(ColorParser.of("&3Gender &8- &b" + targetPlayerProfile.getCharacter_gender()).parseLegacy().build());
        player.sendMessage(ColorParser.of("&3Age &8- &b" + targetPlayerProfile.getCharacter_age()).parseLegacy().build());

        if(!Settings.getRawCustomFields().isEmpty()) {
            for (Map.Entry<String, String> entry : Settings.getRawCustomFields().entrySet()) {
                String fieldLabel = entry.getKey();
                String fieldPlaceholder = PlaceholderAPI.setPlaceholders(targetPlayer, entry.getValue());
                player.sendMessage(ColorParser.of(fieldLabel + " &8- " + fieldPlaceholder).parseLegacy().build());
            }
        }

        player.sendMessage(ColorParser.of("&8&l&m---------------&r&7[&3Description&7]&8&l&m---------------").parseLegacy().build());
        player.sendMessage(ColorParser.of("&3Desc &8- &b" + targetPlayerProfile.getCharacter_description()).parseLegacy().build());
        player.sendMessage(ColorParser.of("&8&l&m---------------------------------------").parseLegacy().build());
    }

    public static void displayHelp(Player player) {
        player.sendMessage(ColorParser.of("&8&l&m-----------&r&7[&3CharacterCards &r&7v&b" + CharacterCards.getInstance().getPluginMeta().getVersion().substring(0, 5) + "&7]&8&l&m-----------\n").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char edit &btitle &7<&bTitle&7> &8- &rSet your character's title").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char edit &bfirstname &7<&bFirst Name&7> &8- &rSet your character's first name").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char edit &blastname &7<&bLast Name&7> &8- &rSet your character's last name").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char edit &bsuffix &7<&bSuffix&7> &8- &rSet your character's suffix").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char edit &bgender &7<&bGender&7> &8- &rSet your character's gender").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char edit &bage &7<&b" + Settings.getMinimumAge() + "&7-&b"
            + Settings.getMaximumAge() + "&7> &8- &rSet your character's age.").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char edit &bdesc &7<&bDescription&7> &8- &rSet your character's description, &c512 CHARACTER LIMIT").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char help &8- &rDisplays the plugin commands").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char view &7{&bPlayer&7} &8- &rView your card, or another player's").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char viewoffline &7{&bPlayer&7} &8- &rViews an offline player's card").parseLegacy().build());
    }
}
