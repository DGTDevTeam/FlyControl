package me.JamieSinn.Bukkit;

import org.bukkit.entity.Player;

/**
* This class stores the current {@link Flystate} of a {@link Player}.
* The time since the last item consume is also stored if enabled in configuration.
*
* @author s1mplex
*
*/
public class FlyPlayerStatus {
public static enum Flystate{FLY, HOVER}

private Flystate state = Flystate.FLY;
private int time_since_consume = 0;

public Flystate getState() {
return state;
}

public void setState(Flystate state) {
this.state = state;
}

public int getTime_since_consume() {
return time_since_consume;
}

public void increaseTime() {
time_since_consume++;
}

public void resetTime(){
time_since_consume = 0;
}
}