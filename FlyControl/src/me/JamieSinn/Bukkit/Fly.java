package me.JamieSinn.Bukkit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import me.JamieSinn.Bukkit.FlyPlayerStatus.Flystate;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
* Main class of FlyControl
*
* @author JamieSinn
*
*/
public class Fly extends JavaPlugin implements Filter 
{
protected static final String PREFIX = "[FlyControl] ";
private static HashSet<String> dmgImmunePlayers = new HashSet<String>();
private static HashMap<String, FlyPlayerStatus> flyingPlayers = new HashMap<String, FlyPlayerStatus>();
public final Logger logger = Logger.getLogger("Minecraft");

@Override
public void onDisable() 
{
	PluginDescriptionFile pdfFile = this.getDescription();
	this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() +  pdfFile.getAuthors() + "Has Been Disabled!");
}

@Override
public void onEnable()
{
	PluginDescriptionFile pdfFile = this.getDescription();
	this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() +  pdfFile.getAuthors() + "Has Been Successfully Enabled!");


FlyConfiguration.loadConfiguration(this);

FlyPlayerListener player_listener = new FlyPlayerListener();
FlyEntityListner entity_listener = new FlyEntityListner();

getServer().getPluginManager().registerEvents(player_listener, this);
getServer().getPluginManager().registerEvents(entity_listener, this);

getServer().getScheduler().scheduleSyncRepeatingTask(this, new FlyTask(this), 1, 1);


}

/**
* Prints the given msg to the server log/console
* @param msg
*/


/* (non-Javadoc)
* @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
*/
@Override
public boolean isLoggable(LogRecord record) 
{
return !record.getMessage().contains("was kicked for floating too long!");
}

/**
* Called when a {@link Player} switches to flying
* @param player the {@link Player} to switch
*/
public static void performSwitchToFly(Player player)
{
if (player.hasPermission("Fly.fly")) 
{
if (FlyConfiguration.consume_item()) 
{
if (!player.getInventory().contains(FlyConfiguration.getConsume_Item())) 
{
return;
}
}
flyingPlayers.put(player.getName(), new FlyPlayerStatus());
player.sendMessage(ChatColor.BLUE + PREFIX + ChatColor.WHITE + FlyConfiguration.getMessage_Fly());
}
}

/**
* Called when a {@link Player} switches to hovering
*
* @param player the {@link Player} to switch
* @param status the {@link FlyPlayerStatus} of this player
*/
public static void performSwitchToHover(Player player, FlyPlayerStatus status){
status.setState(Flystate.HOVER);
dmgImmunePlayers.remove(player.getName());
player.sendMessage(ChatColor.BLUE + PREFIX + ChatColor.WHITE + FlyConfiguration.getMessage_Hover());
}

/**
* Called when a {@link Player} lands
*
* @param player to force to walk again
*/
public static void performSwitchToLand(Player player)
{
flyingPlayers.remove(player.getName());
dmgImmunePlayers.add(player.getName());
player.sendMessage(ChatColor.BLUE + Fly.PREFIX + ChatColor.WHITE + FlyConfiguration.getMessage_Land());
}

/**
* Checks if a player is currently flying
*
* @param player the {@link Player} to check
* @return true/false whether the {@link Player} is flying/hovering or not
*/
public static boolean isFlyingOrHovering(Player player)
{
return flyingPlayers.containsKey(player.getName());
}

/**
* Checks if a player is immune to fall damage
*
* @param player the {@link Player} to check
* @return true/false whether the {@link Player} is immune to fall dmg
*/
public static boolean isDmgImmune(Player player)
{
return dmgImmunePlayers.contains(player.getName());
}

/**
* Removes a {@link Player} from fall dmg immunity
*
* @param player to remove
*/
public static void removeFromImmunity(Player player)
{
dmgImmunePlayers.remove(player.getName());
}

/**
* Gets the {@link FlyPlayerStatus} for the given player
*
* @param player to get the {@link FlyPlayerStatus} for
* @return the {@link FlyPlayerStatus} for this {@link Player}, null if there is none
*/
public static FlyPlayerStatus getPlayerStatus(Player player)
{
return flyingPlayers.get(player.getName());
}

/**
* Returns a {@link Set}<String> with all player names that are currently flying/hovering
*
* @return KeySet of the {@link Fly#flyingPlayers} {@link HashMap}
*/
public static Set<String> getFlyingPlayersNames()
{
return flyingPlayers.keySet();
}

/**
* Called when a player quits
*
* @param player the {@link Player} that quits
*/
public static void handlePlayerQuit(Player player)
{
flyingPlayers.remove(player.getName());
dmgImmunePlayers.remove(player.getName());
}
}