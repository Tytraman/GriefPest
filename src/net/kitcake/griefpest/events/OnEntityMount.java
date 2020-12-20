package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

public class OnEntityMount implements Listener {

    @EventHandler
    public void mount(EntityMountEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getMount());
        if(area != null) {
            switch(e.getMount().getType()) {
                case MINECART:
                case BOAT:
                    if(!area.canEnterVehicles()) {
                        if(e.getEntityType() == EntityType.PLAYER) {
                            if(!area.isAllowed(e.getEntity().getUniqueId().toString())) {
                                e.setCancelled(true);
                            }
                        }else {
                            e.setCancelled(true);
                        }
                    }
                    break;
                default:
                    if(!area.canMountEntities()) {
                        if(e.getEntityType() == EntityType.PLAYER) {
                            if(!area.isAllowed(e.getEntity().getUniqueId().toString())) {
                                e.setCancelled(true);
                            }
                        }else {
                            e.setCancelled(true);
                        }
                    }
            }
        }
    }
}
