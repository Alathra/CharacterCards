package io.github.alathra.charactercards.utility;


import io.github.alathra.charactercards.CharacterCards;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides shorthand access to {@link CharacterCards#getComponentLogger}.
 */
public class Logger {
    /**
     * Get component logger. Shorthand for:
     *
     * @return the component logger {@link CharacterCards#getComponentLogger}.
     */
    @NotNull
    public static ComponentLogger get() {
        return CharacterCards.getInstance().getComponentLogger();
    }
}
