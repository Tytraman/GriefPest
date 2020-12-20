package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class OnExplosion implements Listener {

    @EventHandler
    public void entityExplode(EntityExplodeEvent e) {
        switch(e.getEntityType()) {
            case CREEPER:
                Area area0;
                Iterator<Block> blocks0 = e.blockList().iterator();
                Block block0;
                while(blocks0.hasNext()) {
                    block0 = blocks0.next();
                    area0 = PlayerAreas.getAreaOfLocation(block0);
                    if(area0 != null && !area0.creepersExplosions()) {
                        blocks0.remove();
                    }
                }
                break;
            case PRIMED_TNT:
            case MINECART_TNT:
                Area area1;
                Iterator<Block> blocks1 = e.blockList().iterator();
                Block block1;
                while(blocks1.hasNext()) {
                    block1 = blocks1.next();
                    area1 = PlayerAreas.getAreaOfLocation(block1);
                    if(area1 != null && !area1.TNTExplosions()) {
                        blocks1.remove();
                    }
                }
                break;
            default:
                Area area2;
                Iterator<Block> blocks2 = e.blockList().iterator();
                Block block2;
                while(blocks2.hasNext()) {
                    block2 = blocks2.next();
                    area2 = PlayerAreas.getAreaOfLocation(block2);
                    if(area2 != null && !area2.othersExplosions()) {
                        blocks2.remove();
                    }
                }
        }
    }

    @EventHandler
    public void blockExplode(BlockExplodeEvent e) {
        Area area;
        Iterator<Block> blocks = e.blockList().iterator();
        Block block;
        while(blocks.hasNext()) {
            block = blocks.next();
            area = PlayerAreas.getAreaOfLocation(block);
            if(area != null && !area.othersExplosions()) {
                blocks.remove();
            }
        }
    }
}
