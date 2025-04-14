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

        Queries.loadPlayerProfile(player).thenAccept(optionalPlayerProfile -> {
            if(optionalPlayerProfile.isPresent()) {
                PlayerProfile profile = optionalPlayerProfile.get();

                // Change old player name to new player name
                if (!player.getName().equals(profile.getPlayer_name())) {
                    profile.setPlayer_name(player.getName());
                }

                CharacterCards.playerProfiles.put(player.getUniqueId(), profile);
            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Entity target = event.getRightClicked();

        if(!(target instanceof Player targetPlayer)) return;
        Player player = event.getPlayer();

        boolean isHoldingPaper = player.getInventory().getItemInMainHand().getType() == Material.PAPER ||
            player.getInventory().getItemInOffHand().getType() == Material.PAPER;

        boolean isEmptyHanded = player.getInventory().getItemInMainHand().getType() == Material.AIR;

        if ((Settings.getRightClickPaperEnabled() && isHoldingPaper) ||
            (Settings.getShiftRightClickEnabled() && player.isSneaking() && isEmptyHanded)) {
            Cards.displayCard(player, targetPlayer);
        }
    }
}
