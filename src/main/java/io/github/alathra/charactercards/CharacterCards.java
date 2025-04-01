package io.github.alathra.charactercards;

import io.github.alathra.charactercards.core.PlayerProfile;
import io.github.alathra.charactercards.database.handler.DatabaseHandler;
import io.github.alathra.charactercards.hook.HookManager;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.charactercards.command.CommandHandler;
import io.github.alathra.charactercards.config.ConfigHandler;
import io.github.alathra.charactercards.database.handler.DatabaseHandlerBuilder;
import io.github.alathra.charactercards.listener.ListenerHandler;
import io.github.alathra.charactercards.utility.DB;
import io.github.alathra.charactercards.utility.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class CharacterCards extends JavaPlugin {
    public static Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();

    private static CharacterCards instance;

    // Handlers/Managers
    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;
    private HookManager hookManager;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;

    // Handlers list (defines order of load/enable/disable)
    private List<? extends Reloadable> handlers;

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static CharacterCards getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;

        configHandler = new ConfigHandler(this);
        databaseHandler = new DatabaseHandlerBuilder()
            .withConfigHandler(configHandler)
            .withLogger(getComponentLogger())
            .build();
        hookManager = new HookManager(this);
        commandHandler = new CommandHandler(this);
        listenerHandler = new ListenerHandler(this);

        handlers = List.of(
            configHandler,
            databaseHandler,
            hookManager,
            commandHandler,
            listenerHandler
        );

        DB.init(databaseHandler);
        for (Reloadable handler : handlers)
            handler.onLoad(instance);
    }

    @Override
    public void onEnable() {
        for (Reloadable handler : handlers)
            handler.onEnable(instance);

        if (!DB.isReady()) {
            Logger.get().warn(ColorParser.of("<yellow>DatabaseHolder handler failed to start. Database support has been disabled.").build());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        for (Reloadable handler : handlers.reversed()) // If reverse doesn't work implement a new List with your desired disable order
            handler.onDisable(instance);
    }

    /**
     * Use to reload the entire plugin.
     */
    public void onReload() {
        onDisable();
        onLoad();
        onEnable();
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    @NotNull
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }


    /**
     * Gets hook manager.
     * @return the hook manager
     */
    @NotNull
    public HookManager getHookManager() {
        return hookManager;
    }

}
