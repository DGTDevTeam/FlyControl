package me.JamieSinn.Bukkit;

import me.JamieSinn.Bukkit.FlyPlayerStatus.Flystate;
import util.InventoryUtil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

/**
* This class is scheduled using the {@link BukkitScheduler}.
* It gets called on every server tick performing the fly/hover velocity changes to a {@link Player}
*
* @author s1mpl3x
*
*/
public class FlyTask implements Runnable {
private Fly main;

public FlyTask(Fly main) {
this.main = main;
}

private static final Vector HOVER = new Vector(0, FlyConfiguration.getHover_boost(), 0);
private static final Vector HOVER_BOOST = new Vector(0, 1, 0);

/* (non-Javadoc)
* @see java.lang.Runnable#run()
*/
public void run() {
for (final String playername : Fly.getFlyingPlayersNames()) {
Player player = main.getServer().getPlayer(playername);
FlyPlayerStatus status = Fly.getPlayerStatus(player);

if (player != null) {
switch (status.getState()) {
case FLY: doFly(player); break;
case HOVER: doHover(player); break;
}
performConsume(player, status);
}
}
}

/**
* Give the player the fly boost
* @param player
*/
private static void doFly(Player player){
Vector view = player.getLocation().getDirection();
player.setVelocity(view.multiply(FlyConfiguration.getFly_boost()));
}

/**
* Give the player the hover boost
* @param player
*/
private static void doHover(Player player){
if (player.isSneaking()) {
player.setVelocity(HOVER_BOOST);
}
else {
player.setVelocity(HOVER);
}
}

/**
* Only called if enabled in the configuration. This method checks if a player has the configured item in the inventory and removes it every
* configured seconds
* @param player to handle
* @param status of the given {@link Player}
*/
private static void performConsume(Player player, FlyPlayerStatus status){
if (FlyConfiguration.consume_item()) {
int limit = 20 * (status.getState() == Flystate.FLY ? FlyConfiguration.getConsume_Seconds_Fly() :
FlyConfiguration.getConsume_Seconds_Hover());
int current = status.getTime_since_consume();

if (current >= limit) {
PlayerInventory inv = player.getInventory();
Material type = FlyConfiguration.getConsume_Item();
if (inv.contains(type)) {
InventoryUtil.removeOneFromPlayerInventory(inv, type);
status.resetTime();
if (!inv.contains(type)) {
Fly.performSwitchToLand(player);
return;
}
} else {
Fly.performSwitchToLand(player);
}
}
status.increaseTime();
}
}
}