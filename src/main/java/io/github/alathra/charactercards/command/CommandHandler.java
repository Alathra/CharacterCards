package io.github.alathra.charactercards.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.Reloadable;

/**
 * A class to handle registration of commands.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CommandHandler implements Reloadable {
    private final CharacterCards plugin;

    /**
     * Instantiates the Command handler.
     *
     * @param plugin the plugin
     */
    public CommandHandler(CharacterCards plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(CharacterCards plugin) {
        CommandAPI.onLoad(
            new CommandAPIBukkitConfig(plugin)
                .shouldHookPaperReload(true)
                .silentLogs(true)
                .usePluginNamespace()
                .beLenientForMinorVersions(true)
        );
    }

    @Override
    public void onEnable(CharacterCards plugin) {
        CommandAPI.onEnable();

        // Register commands here
        new CharacterCommand();
    }

    @Override
    public void onDisable(CharacterCards plugin) {
        CommandAPI.getRegisteredCommands().forEach(registeredCommand -> CommandAPI.unregister(registeredCommand.namespace() + ':' + registeredCommand.commandName(), true));
        CommandAPI.onDisable();
    }
}