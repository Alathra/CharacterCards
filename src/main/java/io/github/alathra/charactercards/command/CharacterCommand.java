package io.github.alathra.charactercards.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.Settings;
import io.github.alathra.charactercards.core.Cards;
import io.github.alathra.charactercards.core.PlayerProfile;
import io.github.alathra.charactercards.database.Queries;
import io.github.milkdrinkers.colorparser.ColorParser;
import org.bukkit.entity.Player;

public class CharacterCommand {
    protected CharacterCommand() {
        // /char
        new CommandAPICommand("char")
            .withAliases("character")
            .executesPlayer(((player, commandArguments) -> {
                Cards.displayHelp(player);
            }))
            .register();

        new CommandAPICommand("char")
            .withAliases("character")
            .withArguments(new StringArgument("view"))
            .withOptionalArguments(new PlayerArgument("target"))
            .executesPlayer(((player, commandArguments) -> {
                if(commandArguments.get("target") != null) {
                    Player target = (Player) commandArguments.get("target");
                    Cards.displayCard(player, target);
                }
                else {
                    Cards.displayCard(player, player);
                }
            }))
            .register();

        new CommandAPICommand("charedit")
            .withAliases("characteredit")
            .withArguments(new StringArgument("subarg")
                .replaceSuggestions(ArgumentSuggestions.strings(
                    "title", "firstname", "lastname", "suffix", "gender", "age", "desc")
                ))
            .withOptionalArguments(new GreedyStringArgument("value"))
            .executesPlayer(((player, commandArguments) -> {
                if(commandArguments.get("value") != null) {
                    switch ((String) commandArguments.get("subarg")) {
                        case "title":
                            titleHandler((String) commandArguments.get("value"), player);
                            return;
                        case "firstname":
                            firstNameHandler((String) commandArguments.get("value"), player);
                            return;
                        case "lastname":
                            lastNameHandler((String) commandArguments.get("value"), player);
                            return;
                        case "suffix":
                            suffixHandler((String) commandArguments.get("value"), player);
                            return;
                        case "gender":
                            genderHandler((String) commandArguments.get("value"), player);
                            return;
                        case "age":
                            ageHandler(commandArguments.get("value"), player);
                            return;
                        case "desc":
                            descriptionHandler((String) commandArguments.get("value"), player);
                    }
                }
            }))
            .register();
    }

    // remember to add exception handling if save is not successful
    private void titleHandler(String title, Player player) {
        // to add into config
        int titleMaxLength = 13;

        if(title.length() <= titleMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacter_title(title);
            Queries.savePlayerProfile(profile);
            player.sendMessage(ColorParser.of("Character card saved successfully").parseLegacy().build());
        }
        else {
            player.sendMessage(ColorParser.of("&cTitle is too long, limited to " + titleMaxLength + " characters only").parseLegacy().build());
        }
    }

    // remember to add exception handling if save is not successful
    private void firstNameHandler(String firstname, Player player) {
        // to add into config
        int firstNameMaxLength = 15;

        if(firstname.length() <= firstNameMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacter_first_name(firstname);
            Queries.savePlayerProfile(profile);
            player.sendMessage(ColorParser.of("Character card saved successfully").parseLegacy().build());
        }
        else {
            player.sendMessage(ColorParser.of("&cFirst Name is too long, limited to " + firstNameMaxLength + " characters only").parseLegacy().build());
        }
    }

    // remember to add exception handling if save is not successful
    private void lastNameHandler(String lastname, Player player) {
        // to add into config
        int lastNameMaxLength = 10;

        if(lastname.length() <= lastNameMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacter_last_name(lastname);
            Queries.savePlayerProfile(profile);
            player.sendMessage(ColorParser.of("Character card saved successfully").parseLegacy().build());
        }
        else {
            player.sendMessage(ColorParser.of("&cLast Name is too long, limited to " + lastNameMaxLength + " characters only").parseLegacy().build());
        }
    }

    private void suffixHandler(String suffix, Player player) {
        // to add into config
        int suffixMaxLength = 5;

        if(suffix.length() <= suffixMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacter_suffix(suffix);
            Queries.savePlayerProfile(profile);
            player.sendMessage(ColorParser.of("Character card saved successfully").parseLegacy().build());
        }
        else {
            player.sendMessage(ColorParser.of("&cSuffix is too long, limited to " + suffixMaxLength + " characters only").parseLegacy().build());
        }
    }

    private void genderHandler(String gender, Player player) {
        // to add into config
        int genderMaxLength = 15;

        if(gender.length() <= genderMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacter_gender(gender);
            Queries.savePlayerProfile(profile);
            player.sendMessage(ColorParser.of("Character card saved successfully").parseLegacy().build());
        }
        else {
            player.sendMessage(ColorParser.of("&cGender input is too long, limited to " + genderMaxLength + " characters only").parseLegacy().build());
        }
    }

    private void ageHandler(Object age, Player player) {
        int minimumAge = Settings.getMinimumAge();
        int maximumAge = Settings.getMaximumAge();

        if(age instanceof String) {
            player.sendMessage(ColorParser.of("&cPlease input a number from" + minimumAge + "-" + maximumAge).parseLegacy().build());
            return;
        }

        int inputAge = (Integer) age;

        if(inputAge >= minimumAge && inputAge <= maximumAge) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacter_age(inputAge);
            Queries.savePlayerProfile(profile);
            player.sendMessage(ColorParser.of("Character card saved successfully").parseLegacy().build());
        }
        else {
            player.sendMessage(ColorParser.of("&cAge number too small/large, limited to a range of " + minimumAge + "-" + maximumAge).parseLegacy().build());
        }
    }

    private void descriptionHandler(String desc, Player player) {
        int descriptionCharacterLimit = Settings.getDescriptionCharacterLimit();

        if(desc.length() <= descriptionCharacterLimit) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacter_description(desc);
            Queries.savePlayerProfile(profile);
            player.sendMessage(ColorParser.of("Character card saved successfully").parseLegacy().build());
        }
        else {
            player.sendMessage(ColorParser.of("&cDescription too long, limited to "  + descriptionCharacterLimit + " characters only").parseLegacy().build());
        }
    }
}
