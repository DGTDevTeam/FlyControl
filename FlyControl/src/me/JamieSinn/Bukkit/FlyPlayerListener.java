package me.JamieSinn.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class FlyPlayerListener implements Listener{

/**
* Fly uses this event to allow players to switch between the different modes, flying, hovering and walking
*
* @param event the {@link PlayerInteractEvent}
*/
@EventHandler(priority = EventPriority.NORMAL)
public void onPlayerInteract(PlayerInteractEvent event) {
ItemStack stack = event.getItem();
if (stack == null) {
return;
}
if (stack.getType().equals(FlyConfiguration.getItem()) && checkAction(event.getAction())) {
Player player = event.getPlayer();
if (Fly.isFlyingOrHovering(player)) {
FlyPlayerStatus status = Fly.getPlayerStatus(player);
switch (status.getState()) {
case FLY: // Fly > Hover
Fly.performSwitchToHover(player, status);
break;
case HOVER: // Hover > Land
Fly.performSwitchToLand(player);
break;
}
} // Land > Fly
else {
Fly.performSwitchToFly(player);
}
}
}

/**
* Remove the quitting player from the data model
* @param event
*/
@EventHandler(priority = EventPriority.NORMAL)
public void onPlayerQuit(PlayerQuitEvent event) {
Fly.handlePlayerQuit(event.getPlayer());
}

/**
* Cancel a kick event if the player is flying or was flying
* @param event
*/
@EventHandler(priority = EventPriority.HIGH)
public void onPlayerKick(PlayerKickEvent event) {
if (event.getReason().startsWith("Flying") && (Fly.isFlyingOrHovering(event.getPlayer()) || Fly.isDmgImmune(event.getPlayer()))) {
event.setCancelled(true);
}
}

/**
* Checks if the {@link PlayerInteractEvent} fits for Fly
*
* @param action the {@link Action} to check
* @return true if the player right clicked on air or a block, false otherwise
*/
private static boolean checkAction(Action action){
return action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
}
}