package io.github.alathra.charactercards.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.Settings;
import io.github.alathra.charactercards.core.Cards;
import io.github.alathra.charactercards.core.PlayerProfile;
import io.github.alathra.charactercards.core.ProfileField;
import io.github.alathra.charactercards.database.Queries;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.wordweaver.Translation;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

import static io.github.alathra.charactercards.core.ProfileField.*;

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
                        OfflinePlayer target = (OfflinePlayer) commandArguments.get("target");

                        if(!target.hasPlayedBefore()) {
                            player.sendMessage(ColorParser.of("<red>[<b>✖</b>] <red>Player has not joined the server.").build());
                            return;
                        }

                        Queries.loadOfflinePlayerProfile(target).thenAccept(optionalPlayerProfile -> {
                            optionalPlayerProfile.ifPresent(playerProfile -> {
                                if (Queries.hasPlayerNameChanged(player.getName(), playerProfile.getPlayerName())) {
                                    playerProfile.setPlayerName(player.getName());
                                }

                                CharacterCards.playerProfiles.put(player.getUniqueId(), playerProfile);
                            });

                            Cards.displayOfflinePlayerCard(player, target);
                        });
                    }))
                )
                // /char edit <subarg> <value>
                .withSubcommand(new CommandAPICommand("edit")
                    .withArguments(new StringArgument("subarg")
                        .replaceSuggestions(ArgumentSuggestions.strings(
                        "title", "firstname", "lastname", "suffix", "gender", "age", "desc"))
                    )
                    .withOptionalArguments(new GreedyStringArgument("value")
                        .replaceSuggestions((info, builder) -> {
                            CommandArguments subarg = info.previousArgs();
                            String previousArg = (String) subarg.get("subarg");

                            if ("gender".equalsIgnoreCase(previousArg)) {
                                builder.suggest("Male");
                                builder.suggest("Female");
                                builder.suggest("Other");
                            }
                            return builder.buildFuture();
                        })
                    )
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

    private void handleStringFieldUpdate(
        Player player,
        String input,
        int maxLength,
        ProfileField field,
        Consumer<PlayerProfile> updater,
        String errorKey
    ) {
        if (input.length() <= maxLength) {
            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());

            updater.accept(profile);
            Queries.savePlayerProfile(profile, field, player);
        } else {
            player.sendMessage(ColorParser.of(
                Translation.of(errorKey).replace("%max_length%", String.valueOf(maxLength))
            ).build());
        }
    }

    private void titleHandler(String title, Player player) {
        handleStringFieldUpdate(
            player, title, Settings.getTitleMaxLength(), TITLE,
            p -> p.setCharacterTitle(title),
            "cards.error.title"
        );
    }

    private void firstNameHandler(String firstname, Player player) {
        handleStringFieldUpdate(
            player, firstname, Settings.getFirstNameMaxLength(), FIRST_NAME,
            p -> p.setCharacterFirstName(firstname),
            "cards.error.firstname"
        );
    }

    private void lastNameHandler(String lastname, Player player) {
        handleStringFieldUpdate(
            player, lastname, Settings.getLastNameMaxLength(), LAST_NAME,
            p -> p.setCharacterLastName(lastname),
            "cards.error.lastname"
        );
    }

    private void suffixHandler(String suffix, Player player) {
        handleStringFieldUpdate(
            player, suffix, Settings.getSuffixMaxLength(), SUFFIX,
            p -> p.setCharacterSuffix(suffix),
            "cards.error.suffix"
        );
    }

    private void genderHandler(String gender, Player player) {
        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            player.sendMessage(ColorParser.of(Translation.of("cards.error.gender")).build());
            return;
        }

        handleStringFieldUpdate(
            player, gender, Settings.getGenderMaxLength(), GENDER,
            p -> p.setCharacterGender(gender),
            "cards.error.gender"
        );
    }

    private void ageHandler(String ageString, Player player) {
        int minimumAge = Settings.getMinimumAge();
        int maximumAge = Settings.getMaximumAge();

        try {
            int inputAge = Integer.parseInt(ageString);
            if (inputAge < minimumAge || inputAge > maximumAge)
                throw new IllegalArgumentException();

            PlayerProfile profile = CharacterCards.playerProfiles.get(player.getUniqueId());
            profile.setCharacterAge(inputAge);
            Queries.savePlayerProfile(profile, AGE, player);
        } catch (Exception e) {
            player.sendMessage(ColorParser.of(
                Translation.of("cards.error.age")
                    .replace("%minimum_age%", String.valueOf(minimumAge))
                    .replace("%maximum_age%", String.valueOf(maximumAge))
            ).build());
        }
    }

    private void descriptionHandler(String desc, Player player) {
        handleStringFieldUpdate(
            player, desc, Settings.getDescriptionCharacterLimit(), DESCRIPTION,
            p -> p.setCharacterDescription(desc),
            "cards.error.desc"
        );
    }
}
