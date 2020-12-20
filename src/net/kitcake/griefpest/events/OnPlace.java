package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class OnPlace implements Listener {

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType() != Material.FIRE) {
            Area area = PlayerAreas.getAreaOfLocation(e.getBlock());
            if(area != null && !area.canPlace() && !area.isAllowed(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hangingPlace(HangingPlaceEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getEntity());
        if(area != null && !area.canPlace() && !area.isAllowed(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void liquidPlace(PlayerBucketEmptyEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getBlock());
        if(area != null && !area.canPlace() && !area.isAllowed(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}