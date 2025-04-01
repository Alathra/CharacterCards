package io.github.alathra.charactercards.listener;

import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.Settings;
import io.github.alathra.charactercards.core.Cards;
import io.github.alathra.charactercards.core.PlayerProfile;
import io.github.alathra.charactercards.database.Queries;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings({"unused"})
public class PlayerListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Queries.loadPlayerProfile(player).thenAccept(data -> {
            if(data.isPresent()) {
                PlayerProfile profile = data.get();

                profile.setPlayer_name(player.getName()); //This is for players who changed their name
                CharacterCards.playerProfiles.put(player.getUniqueId(), profile);
            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Entity interactedPlayer = event.getRightClicked();

        if(interactedPlayer instanceof Player) {
            Player player = event.getPlayer();

            if(Settings.getRightClickPaperEnabled() &&
                (player.getInventory().getItemInMainHand().getType() == Material.PAPER || player.getInventory().getItemInOffHand().getType() == Material.PAPER)) {
                Cards.displayCard(player, (Player) interactedPlayer);
            }

            if(Settings.getShiftRightClickEnabled() && player.isSneaking() && player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                Cards.displayCard(player, (Player) interactedPlayer);
            }
        }
    }
}
