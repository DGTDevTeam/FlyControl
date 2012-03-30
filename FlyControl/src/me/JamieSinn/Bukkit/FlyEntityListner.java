package me.JamieSinn.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FlyEntityListner implements Listener {

/**
* Fly uses this event to cancel fall damage for flying/was flying players
*
* @param event the {@link EntityDamageEvent}
*/
@EventHandler(priority = EventPriority.HIGH)
public void onPlayerDmg(EntityDamageEvent event) {
if (event.getEntity() instanceof Player && event.getCause().equals(DamageCause.FALL)) {
Player player = (Player) event.getEntity();
if (Fly.isFlyingOrHovering(player)) {
event.setCancelled(true);

} else if (Fly.isDmgImmune(player)) {
event.setCancelled(true);
Fly.removeFromImmunity(player);
}
}
}
}