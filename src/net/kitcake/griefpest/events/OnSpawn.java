package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class OnSpawn implements Listener {
    @EventHandler
    public void entitySpawn(CreatureSpawnEvent e) {
        if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            Area area = PlayerAreas.getAreaOfLocation(e.getEntity());
            if(area != null) {
                if(e.getEntity() instanceof Monster) {
                    if(!area.naturalMonstersSpawn()) {
                        e.setCancelled(true);
                    }
                }else if(e.getEntity() instanceof Animals) {
                    if(!area.naturalAnimalsSpawn()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
