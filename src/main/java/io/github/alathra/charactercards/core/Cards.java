package io.github.alathra.charactercards.core;

import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.Settings;
import io.github.milkdrinkers.colorparser.ColorParser;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

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
        player.sendMessage(ColorParser.of("&3Name &8- &b"
            + targetPlayerProfile.getCharacter_title() + " "
            + targetPlayerProfile.getCharacter_first_name() + " "
            + targetPlayerProfile.getCharacter_last_name() + " "
            + targetPlayerProfile.getCharacter_suffix()
        ).parseLegacy().build());
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
        player.sendMessage(ColorParser.of("&8&l&m-----------&r&7[&3CharacterCards &r&7v&b" + CharacterCards.getInstance().getPluginMeta().getVersion().substring(0, 6).trim() + "&7]&8&l&m-----------\n").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3charedit &btitle &7<&bTitle&7> &8- &rSet your character's title").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3charedit &bfirstname &7<&bFirst Name&7> &8- &rSet your character's first name").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3charedit &blastname &7<&bLast Name&7> &8- &rSet your character's last name").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3charedit &bsuffix &7<&bSuffix&7> &8- &rSet your character's suffix").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3charedit &bgender &7<&bGender&7> &8- &rSet your character's gender").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3charedit &bage &7<&b" + Settings.getMinimumAge() + "&7-&b"
            + Settings.getMaximumAge() + "&7> &8- &rSet your character's age.").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3charedit &bdesc &7<&bDescription&7> &8- &rSet your character's description, &c512 CHARACTER LIMIT").parseLegacy().build());
        player.sendMessage(ColorParser.of("&7/&3char view &7{&bPlayer&7} &8- &rView your card, or another player's!").parseLegacy().build());
    }
}
