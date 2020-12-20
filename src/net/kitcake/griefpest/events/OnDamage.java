package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class OnDamage implements Listener {

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e) {
        // Bloquer les dégâts liées aux explosions quand le joueur est dans une zone claim
        if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            Area areax = PlayerAreas.getAreaOfLocation(e.getEntity());
            if(areax != null) {
                switch (e.getDamager().getType()) {
                    case CREEPER:
                        if(!areax.creepersExplosions()) {
                            e.setCancelled(true);
                        }
                        break;
                    case PRIMED_TNT:
                    case MINECART_TNT:
                        if(!areax.TNTExplosions()) {
                            e.setCancelled(true);
                        }
                        break;
                    default:
                        if(!areax.othersExplosions()) {
                            e.setCancelled(true);
                        }
                }
            }
        }else {
            // Quand un joueur tape un autre joueur
            if (e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
                Area area1 = PlayerAreas.getAreaOfLocation(e.getEntity());
                Area area2 = PlayerAreas.getAreaOfLocation(e.getDamager());
                boolean pvp = true;
                if (area1 != null && !area1.canPvP()) {
                    pvp = false;
                } else if (area2 != null && !area2.canPvP()) {
                    pvp = false;
                }
                e.setCancelled(!pvp);
            }else {
                // Quand un mob est tapé
                Area area = PlayerAreas.getAreaOfLocation(e.getEntity());
                if(area != null) {
                    if(e.getEntity() instanceof Animals) {    // Si un animal est tapé dans une zone claim
                        if(!area.canPvE_Animals()) {
                            if(e.getDamager().getType() == EntityType.PLAYER) {
                                if(!area.isAllowed(e.getDamager().getUniqueId().toString())) {
                                    e.setCancelled(true);
                                }
                            }else {
                                e.setCancelled(true);
                            }
                        }
                    }else if(e.getEntity() instanceof Monster) {    // Si un monstre est tapé dans une zone claim
                        if(!area.canPvE_Monsters()) {
                            if(e.getDamager().getType() == EntityType.PLAYER) {
                                if(!area.isAllowed(e.getDamager().getUniqueId().toString())) {
                                    e.setCancelled(true);
                                }
                            }else {
                                e.setCancelled(true);
                            }
                        }
                    }else if(e.getEntity() instanceof NPC) {                // Si un PNJ est tapé dans une zone claim
                        if(!area.canPvE_NPC()) {
                            if(e.getDamager().getType() == EntityType.PLAYER) {
                                if(!area.isAllowed(e.getDamager().getUniqueId().toString())) {
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }else if(e.getEntity() instanceof ItemFrame) {      // Si on tape le contenu d'un item frame ou une armor stand
                        if(!area.canBreakItemFrames()) {
                            if(e.getDamager().getType() == EntityType.PLAYER) {
                                if(!area.isAllowed(e.getDamager().getUniqueId().toString())) {
                                    e.setCancelled(true);
                                }
                            }else {
                                e.setCancelled(true);
                            }
                        }
                    }else if(e.getEntity() instanceof ArmorStand) {
                        if(!area.canBreakBlocks()) {
                            if(e.getDamager().getType() == EntityType.PLAYER) {
                                if(!area.isAllowed(e.getDamager().getUniqueId().toString())) {
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
    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        // Bloquer les dégâts liées aux explosions de blocs si l'entité est dans une zone claim
        if(e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            Area area = PlayerAreas.getAreaOfLocation(e.getEntity());
            if(area != null && !area.othersExplosions()) {
                e.setCancelled(true);
            }
        }else if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Area area = PlayerAreas.getAreaOfLocation(e.getEntity());
            if(area != null && !area.fallDamage()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void vehiclesDamage(VehicleDamageEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getVehicle());
        if(area != null && !area.canBreakVehicles()) {
            if(e.getAttacker().getType() == EntityType.PLAYER) {
                if(!area.isAllowed(e.getAttacker().getUniqueId().toString())) {
                    e.setCancelled(true);
                }
            }else {
                e.setCancelled(true);
            }
        }
    }
}
