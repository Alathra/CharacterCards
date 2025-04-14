package io.github.alathra.charactercards.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.Settings;
import io.github.alathra.charactercards.core.Cards;
import io.github.alathra.charactercards.core.PlayerProfile;
import io.github.alathra.charactercards.database.Queries;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.wordweaver.Translation;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CharacterCommand {
    @SuppressWarnings("all")
    protected CharacterCommand() {
        new CommandAPICommand("char")
            .withAliases("character")
                // /char translation
                .withSubcommand(new TranslationCommand().command())
                // /char help
                .withSubcommand(new CommandAPICommand("help")
                    .executesPlayer(((player, commandArguments) -> {
                        Cards.displayHelp(player);
                    }))
                )
                // /char view [player]
                .withSubcommand(new CommandAPICommand("view")
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
                )
                // /char viewoffline [offline_player]
                .withSubcommand(new CommandAPICommand("viewoffline")
                    .withArguments(new OfflinePlayerArgument("target"))
                    .executesPlayer(((player, commandArguments) -> {
                        if(commandArguments.get("target") != null) {
                            OfflinePlayer target = (OfflinePlayer) commandArguments.get("target");

                            if(target.hasPlayedBefore()) {
                                Queries.loadOfflinePlayerProfile(target);

                                Cards.displayOfflinePlayerCard(player, target);
                            }
                            else {
                                player.sendMessage(ColorParser.of("<red>[<b>✖</b>] <red>Player has not joined the server.").build());
                            }
                        }
                        else {
                            player.sendMessage(ColorParser.of("<red>[<b>✖</b>] <red>Player does not exist.").build());
                        }
                    }))
                )
                // /char edit <subarg> <value>
                .withSubcommand(new CommandAPICommand("edit")
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
                                    ageHandler((String) commandArguments.get("value"), player);
                                    return;
                                case "desc":
                                    descriptionHandler((String) commandArguments.get("value"), player);
                    }
                }
            })))
            // if only using /char
            .executesPlayer(((player, commandArguments) -> {
                Cards.displayHelp(player);
            }))
            .register();
    }

    private void titleHandler(String title, Player player) {
        int titleMaxLength = Settings.getTitleMaxLength();

        if(title.length() <= titleMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacterTitle(title);
            Queries.savePlayerProfile(profile, "TITLE", player);
        }
        else {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.title").replace("%max_length%", String.valueOf(titleMaxLength))).build());
        }
    }

    private void firstNameHandler(String firstname, Player player) {
        int firstNameMaxLength = Settings.getFirstNameMaxLength();

        if(firstname.length() <= firstNameMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacterFirstName(firstname);
            Queries.savePlayerProfile(profile , "FIRST_NAME", player);
        }
        else {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.firstname").replace("%max_length%", String.valueOf(firstNameMaxLength))).build());
        }
    }

    private void lastNameHandler(String lastname, Player player) {
        int lastNameMaxLength = Settings.getLastNameMaxLength();

        if(lastname.length() <= lastNameMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacterLastName(lastname);
            Queries.savePlayerProfile(profile, "LAST_NAME", player);
        }
        else {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.lastname").replace("%max_length%", String.valueOf(lastNameMaxLength))).build());
        }
    }

    private void suffixHandler(String suffix, Player player) {
        int suffixMaxLength = Settings.getSuffixMaxLength();

        if(suffix.length() <= suffixMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacterSuffix(suffix);
            Queries.savePlayerProfile(profile, "SUFFIX", player);
        }
        else {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.suffix").replace("%max_length%", String.valueOf(suffixMaxLength))).build());
        }
    }

    private void genderHandler(String gender, Player player) {
        int genderMaxLength = Settings.getGenderMaxLength();

        if(gender.length() <= genderMaxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacterGender(gender);
            Queries.savePlayerProfile(profile, "GENDER", player);
        }
        else {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.gender").replace("%max_length%", String.valueOf(genderMaxLength))).build());
        }
    }

    private void ageHandler(String age, Player player) {
        int minimumAge = Settings.getMinimumAge();
        int maximumAge = Settings.getMaximumAge();

        int inputAge;

        try {
            inputAge = Integer.parseInt(age);
        } catch (ClassCastException | NumberFormatException e) {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.age_notanumber")
                .replace("%minimum_age%", String.valueOf(minimumAge))
                .replace("%maximum_age%", String.valueOf(maximumAge)))
                .build());
            return;
        }

        if(inputAge >= minimumAge && inputAge <= maximumAge) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacterAge(inputAge);
            Queries.savePlayerProfile(profile, "AGE", player);
        }
        else {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.age")
                    .replace("%minimum_age%", String.valueOf(minimumAge))
                    .replace("%maximum_age%", String.valueOf(maximumAge)))
                .build());
        }
    }

    private void descriptionHandler(String desc, Player player) {
        int descriptionCharacterLimit = Settings.getDescriptionCharacterLimit();

        if(desc.length() <= descriptionCharacterLimit) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            profile.setCharacterDescription(desc);
            Queries.savePlayerProfile(profile, "DESCRIPTION", player);
        }
        else {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.desc").replace("%max_length%", String.valueOf(descriptionCharacterLimit))).build());
        }
    }
}
