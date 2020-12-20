package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class OnBreak implements Listener {

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getBlock());
        if(area != null && !area.canBreakBlocks() && !area.isAllowed(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void doorBreak(EntityBreakDoorEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getBlock());
        if(area != null && !area.canBreakBlocks()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void hangingBreak(HangingBreakByEntityEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getEntity());
        if(area != null && !area.canBreakItemFrames()) {
            if(e.getRemover().getType() == EntityType.PLAYER) {
                if(!area.isAllowed(e.getRemover().getUniqueId().toString())) {
                    e.setCancelled(true);
                }
            }else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hangingBreaak(HangingBreakEvent e) {
        if(!(e instanceof HangingBreakByEntityEvent)) {
            Area area = PlayerAreas.getAreaOfLocation(e.getEntity());
            if(area != null && !area.canBreakItemFrames()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void liquidRemove(PlayerBucketFillEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getBlock());
        if(area != null && !area.canBreakBlocks() && !area.isAllowed(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
