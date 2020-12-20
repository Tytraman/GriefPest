package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.NPC;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnInteract implements Listener {

    @EventHandler
    public void rightClickEntity(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof ItemFrame) {
            Area area = PlayerAreas.getAreaOfLocation(e.getRightClicked());
            if(area != null && !area.canInteract() && !area.isAllowed(e.getPlayer())) {
                e.setCancelled(true);
            }
        }else if(e.getRightClicked() instanceof NPC) {
            Area area = PlayerAreas.getAreaOfLocation(e.getRightClicked());
            if(area != null && !area.canInteractWithNPC() && !area.isAllowed(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Area area = PlayerAreas.getAreaOfLocation(e.getClickedBlock());
            if(area != null) {
                if(e.getItem() != null && e.getItem().getType() == Material.FLINT_AND_STEEL && !area.canBurnBlocks() && !area.isAllowed(e.getPlayer())) {
                    e.setUseItemInHand(Event.Result.DENY);
                }
                switch(e.getClickedBlock().getType()) {
                    case SHULKER_BOX:
                    case ENDER_CHEST:
                    case TRAPPED_CHEST:
                    case CHEST:
                    case BARREL:
                    case HOPPER:
                    case DROPPER:
                        if(!area.canOpenChests() && !area.isAllowed(e.getPlayer())) {
                            e.setUseInteractedBlock(Event.Result.DENY);
                        }
                        break;
                    case ANVIL:
                    case CHIPPED_ANVIL:
                    case DAMAGED_ANVIL:
                    case BLACK_BED:
                    case BLUE_BED:
                    case BROWN_BED:
                    case CYAN_BED:
                    case GRAY_BED:
                    case GREEN_BED:
                    case LIGHT_BLUE_BED:
                    case LIGHT_GRAY_BED:
                    case LIME_BED:
                    case MAGENTA_BED:
                    case ORANGE_BED:
                    case PINK_BED:
                    case PURPLE_BED:
                    case RED_BED:
                    case WHITE_BED:
                    case YELLOW_BED:
                    case BELL:
                    case BLAST_FURNACE:
                    case BREWING_STAND:
                    case CARTOGRAPHY_TABLE:
                    case CAULDRON:
                    case COMMAND_BLOCK:
                    case COMPOSTER:
                    case CRAFTING_TABLE:
                    case DARK_OAK_DOOR:
                    case ACACIA_DOOR:
                    case BIRCH_DOOR:
                    case CRIMSON_DOOR:
                    case IRON_DOOR:
                    case JUNGLE_DOOR:
                    case OAK_DOOR:
                    case SPRUCE_DOOR:
                    case WARPED_DOOR:
                    case ENCHANTING_TABLE:
                    case ACACIA_FENCE_GATE:
                    case BIRCH_FENCE_GATE:
                    case CRIMSON_FENCE_GATE:
                    case DARK_OAK_FENCE_GATE:
                    case JUNGLE_FENCE_GATE:
                    case OAK_FENCE_GATE:
                    case SPRUCE_FENCE_GATE:
                    case WARPED_FENCE_GATE:
                    case FURNACE:
                    case GRINDSTONE:
                    case JUKEBOX:
                    case LECTERN:
                    case LEVER:
                    case LODESTONE:
                    case LOOM:
                    case NOTE_BLOCK:
                    case PISTON:
                    case PUMPKIN:
                    case RESPAWN_ANCHOR:
                    case SMITHING_TABLE:
                    case SMOKER:
                    case SPAWNER:
                    case STONECUTTER:
                    case TNT:
                    case ACACIA_TRAPDOOR:
                    case BIRCH_TRAPDOOR:
                    case CRIMSON_TRAPDOOR:
                    case DARK_OAK_TRAPDOOR:
                    case IRON_TRAPDOOR:
                    case JUNGLE_TRAPDOOR:
                    case OAK_TRAPDOOR:
                    case SPRUCE_TRAPDOOR:
                    case WARPED_TRAPDOOR:
                    case BIRCH_BUTTON:
                    case ACACIA_BUTTON:
                    case CRIMSON_BUTTON:
                    case DARK_OAK_BUTTON:
                    case JUNGLE_BUTTON:
                    case OAK_BUTTON:
                    case POLISHED_BLACKSTONE_BUTTON:
                    case SPRUCE_BUTTON:
                    case STONE_BUTTON:
                    case WARPED_BUTTON:
                    case END_PORTAL_FRAME:
                    case FLOWER_POT:
                    case DAYLIGHT_DETECTOR:
                    case COMPARATOR:
                    case REPEATER:
                    case REDSTONE_WIRE:
                    case BEACON:
                    case CAKE:
                    case DRAGON_EGG:
                        if(!area.canInteract() && !area.isAllowed(e.getPlayer())) {
                            e.setUseInteractedBlock(Event.Result.DENY);
                        }
                        break;
                }
            }
        }
    }

    @EventHandler
    public void armorStand(PlayerArmorStandManipulateEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getRightClicked());
        if(area != null && !area.canInteract() && !area.isAllowed(e.getPlayer())) {
            e.setCancelled(true);
        }

    }
}