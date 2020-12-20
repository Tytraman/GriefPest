package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class OnLightningStrike implements Listener {
    @EventHandler
    public void lightning(LightningStrikeEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getLightning());
        if(area != null && !area.canLightningStrike()) {
            e.setCancelled(true);
        }
    }
}
