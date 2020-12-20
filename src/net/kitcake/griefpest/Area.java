package net.kitcake.griefpest;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class Area {

    private final String name;
    private final String ownerUUID;
    private final Location location1;
    private final Location location2;

    private final List<String> allowedPlayers;

    private final String optionsPath;
    private boolean canPlace;
    private boolean canBreakBlocks;
    private boolean canPvP;
    private boolean canPvE_Animals;
    private boolean canPvE_Monsters;
    private boolean canOpenChests;
    private boolean canInteract;
    private boolean canPvE_NPC;
    private boolean creepersExplosions;
    private boolean TNTExplosions;
    private boolean othersExplosions;
    private boolean canBreakItemFrames;
    private boolean canMountEntities;
    private boolean canEnterVehicles;
    private boolean canBreakVehicles;
    private boolean fallDamage;
    private boolean naturalAnimalsSpawn;
    private boolean naturalMonstersSpawn;
    private boolean canInteractWithNPC;
    private boolean canLightningStrike;
    private boolean canBurnBlocks;

    private Area(PlayerAreas areas, String name, Location location1, Location location2) {
        this.name = name;
        this.ownerUUID = areas.getOwnerUUID();
        this.location1 = location1;
        this.location2 = location2;

        allowedPlayers = areas.getYaml().getStringList("areas." + name + ".allowed-players");
        if(!allowedPlayers.contains(ownerUUID)) {
            allowedPlayers.add(ownerUUID);
            areas.getYaml().set("areas." + name + ".allowed-players", allowedPlayers);
        }

        optionsPath = "areas." + name + ".options.";
        if(areas.getYaml().get(optionsPath + "canPlace") == null) {
            areas.getYaml().set(optionsPath + "canPlace", false);
        }
        if(areas.getYaml().get(optionsPath + "canBreakBlocks") == null) {
            areas.getYaml().set(optionsPath + "canBreakBlocks", false);
        }
        if(areas.getYaml().get(optionsPath + "canPvP") == null) {
            areas.getYaml().set(optionsPath + "canPvP", false);
        }
        if(areas.getYaml().get(optionsPath + "canPvE_Animals") == null) {
            areas.getYaml().set(optionsPath + "canPvE_Animals", false);
        }
        if(areas.getYaml().get(optionsPath + "canPvE_Monsters") == null) {
            areas.getYaml().set(optionsPath + "canPvE_Monsters", false);
        }
        if(areas.getYaml().get(optionsPath + "canOpenChests") == null) {
            areas.getYaml().set(optionsPath + "canOpenChests", false);
        }
        if(areas.getYaml().get(optionsPath + "canInteract") == null) {
            areas.getYaml().set(optionsPath + "canInteract", false);
        }
        if(areas.getYaml().get(optionsPath + "canPvE_NPC") == null) {
            areas.getYaml().set(optionsPath + "canPvE_NPC", false);
        }
        if(areas.getYaml().get(optionsPath + "creepersExplosions") == null) {
            areas.getYaml().set(optionsPath + "creepersExplosions", false);
        }
        if(areas.getYaml().get(optionsPath + "TNTExplosions") == null) {
            areas.getYaml().set(optionsPath + "TNTExplosions", false);
        }
        if(areas.getYaml().get(optionsPath + "othersExplosions") == null) {
            areas.getYaml().set(optionsPath + "othersExplosions", false);
        }
        if(areas.getYaml().get(optionsPath + "canBreakItemFrames") == null) {
            areas.getYaml().set(optionsPath + "canBreakItemFrames", false);
        }
        if(areas.getYaml().get(optionsPath + "canMountEntities") == null) {
            areas.getYaml().set(optionsPath + "canMountEntities", false);
        }
        if(areas.getYaml().get(optionsPath + "canEnterVehicles") == null) {
            areas.getYaml().set(optionsPath + "canEnterVehicles", false);
        }
        if(areas.getYaml().get(optionsPath + "canBreakVehicles") == null) {
            areas.getYaml().set(optionsPath + "canBreakVehicles", false);
        }
        if(areas.getYaml().get(optionsPath + "fallDamage") == null) {
            areas.getYaml().set(optionsPath + "fallDamage", false);
        }
        if(areas.getYaml().get(optionsPath + "naturalAnimalsSpawn") == null) {
            areas.getYaml().set(optionsPath + "naturalAnimalsSpawn", false);
        }
        if(areas.getYaml().get(optionsPath + "naturalMonstersSpawn") == null) {
            areas.getYaml().set(optionsPath + "naturalMonstersSpawn", false);
        }
        if(areas.getYaml().get(optionsPath + "canInteractWithNPC") == null) {
            areas.getYaml().set(optionsPath + "canInteractWithNPC", false);
        }
        if(areas.getYaml().get(optionsPath + "canLightningStrike") == null) {
            areas.getYaml().set(optionsPath + "canLightningStrike", false);
        }
        if(areas.getYaml().get(optionsPath + "canBurnBlocks") == null) {
            areas.getYaml().set(optionsPath + "canBurnBlocks", false);
        }
        areas.save();

        canPlace = areas.getYaml().getBoolean(optionsPath + "canPlace");
        canBreakBlocks = areas.getYaml().getBoolean(optionsPath + "canBreakBlocks");
        canPvP = areas.getYaml().getBoolean(optionsPath + "canPvP");
        canPvE_Animals = areas.getYaml().getBoolean(optionsPath + "canPvE_Animals");
        canPvE_Monsters = areas.getYaml().getBoolean(optionsPath + "canPvE_Monsters");
        canOpenChests = areas.getYaml().getBoolean(optionsPath + "canOpenChests");
        canInteract = areas.getYaml().getBoolean(optionsPath + "canInteract");
        canPvE_NPC = areas.getYaml().getBoolean(optionsPath + "canPvE_NPC");
        creepersExplosions = areas.getYaml().getBoolean(optionsPath + "creepersExplosions");
        TNTExplosions = areas.getYaml().getBoolean(optionsPath + "TNTExplosions");
        othersExplosions = areas.getYaml().getBoolean(optionsPath + "othersExplosions");
        canBreakItemFrames = areas.getYaml().getBoolean(optionsPath + "canBreakItemFrames");
        canMountEntities = areas.getYaml().getBoolean(optionsPath + "canMountEntities");
        canEnterVehicles = areas.getYaml().getBoolean(optionsPath + "canEnterVehicles");
        canBreakVehicles = areas.getYaml().getBoolean(optionsPath + "canBreakVehicles");
        fallDamage = areas.getYaml().getBoolean(optionsPath + "fallDamage");
        naturalAnimalsSpawn = areas.getYaml().getBoolean(optionsPath + "naturalAnimalsSpawn");
        naturalMonstersSpawn = areas.getYaml().getBoolean(optionsPath + "naturalMonstersSpawn");
        canInteractWithNPC = areas.getYaml().getBoolean(optionsPath + "canInteractWithNPC");
        canLightningStrike = areas.getYaml().getBoolean(optionsPath + "canLightningStrike");
        canBurnBlocks = areas.getYaml().getBoolean(optionsPath + "canBurnBlocks");
    }

    /**
     * Charge une zone claim déjà existante, il y a besoin d'autant d'arguments car tout se passe dans <code>PlayerAreas</code>, mais je pourrais changer
     * et mettre le code ici
     * @param areas Zones du joueur
     * @param name Nom de la zone
     * @param location1 Position 1 de la zone
     * @param location2 Position 2 de la zone
     * @return La zone chargée
     */
    public static Area loadArea(PlayerAreas areas, String name, Location location1, Location location2) {
        return new Area(areas, name, location1, location2);
    }


    public long size() {
        double x = location1.getX() - location2.getX();
        if(x < 0) {
            x *= -1;
        }
        x++;
        double y = location1.getY() - location2.getY();
        if(y < 0) {
            y *= -1;
        }
        y++;
        double z = location1.getZ() - location2.getZ();
        if(z < 0) {
            z *= -1;
        }
        z++;
        return (long) (x * y * z);
    }

    public boolean isAllowed(String UUID) {
        return allowedPlayers.contains(UUID);
    }

    public boolean isAllowed(Player player) {
        return isAllowed(player.getUniqueId().toString());
    }

    public void resetAllowedPlayers(PlayerAreas areas) {
        allowedPlayers.clear();
        allowedPlayers.add(ownerUUID);
        areas.getYaml().set("areas." + name + ".allowed-players", allowedPlayers);
        areas.save();
    }

    public boolean addAllowedPlayer(PlayerAreas areas, Player player) {
        String UUID = player.getUniqueId().toString();
        if(!allowedPlayers.contains(UUID)) {
            allowedPlayers.add(UUID);
            areas.getYaml().set("areas." + name + ".allowed-players", allowedPlayers);
            areas.save();
            return true;
        }
        return false;
    }

    public boolean removeAllowedPlayer(PlayerAreas areas, Player player) {
        if(allowedPlayers.remove(player.getUniqueId().toString())) {
            areas.getYaml().set("areas." + name + ".allowed-players", allowedPlayers);
            areas.save();
            return true;
        }
        return false;
    }

    public boolean removeAllowedPlayer(PlayerAreas areas, String playerUUID) {
        if(allowedPlayers.remove(playerUUID)) {
            areas.getYaml().set("areas." + name + ".allowed-players", allowedPlayers);
            areas.save();
            return true;
        }
        return false;
    }

    // ********** Setters ********** \\
    public void setCanPlace(PlayerAreas areas, boolean canPlace) {
        this.canPlace = canPlace;
        areas.getYaml().set(optionsPath + "canPlace", canPlace);
        areas.save();
    }

    public void setCanBreakBlocks(PlayerAreas areas, boolean canBreakBlocks) {
        this.canBreakBlocks = canBreakBlocks;
        areas.getYaml().set(optionsPath + "canBreakBlocks", canBreakBlocks);
        areas.save();
    }

    public void setCanPvP(PlayerAreas areas, boolean canPvP) {
        this.canPvP = canPvP;
        areas.getYaml().set(optionsPath + "canPvP", canPvP);
        areas.save();
    }

    public void setCanPvE_Animals(PlayerAreas areas, boolean canPvE_Animals) {
        this.canPvE_Animals = canPvE_Animals;
        areas.getYaml().set(optionsPath + "canPvE_Animals", canPvE_Animals);
        areas.save();
    }

    public void setCanPvE_Monsters(PlayerAreas areas, boolean canPvE_Monsters) {
        this.canPvE_Monsters = canPvE_Monsters;
        areas.getYaml().set(optionsPath + "canPvE_Monsters", canPvE_Monsters);
        areas.save();
    }

    public void setCanOpenChests(PlayerAreas areas, boolean canOpenChests) {
        this.canOpenChests = canOpenChests;
        areas.getYaml().set(optionsPath + "canOpenChests", canOpenChests);
        areas.save();
    }

    public void setCanInteract(PlayerAreas areas, boolean canInteract) {
        this.canInteract = canInteract;
        areas.getYaml().set(optionsPath + "canInteract", canInteract);
        areas.save();
    }

    public void setCanPvE_NPC(PlayerAreas areas, boolean canPvE_NPC) {
        this.canPvE_NPC = canPvE_NPC;
        areas.getYaml().set(optionsPath + "canPvE_NPC", canPvE_NPC);
        areas.save();
    }

    public void setCreepersExplosions(PlayerAreas areas, boolean creepersExplosions) {
        this.creepersExplosions = creepersExplosions;
        areas.getYaml().set(optionsPath + "creepersExplosions", creepersExplosions);
        areas.save();
    }

    public void setTNTExplosions(PlayerAreas areas, boolean TNTExplosions) {
        this.TNTExplosions = TNTExplosions;
        areas.getYaml().set(optionsPath + "TNTExplosions", TNTExplosions);
        areas.save();
    }

    public void setOthersExplosions(PlayerAreas areas, boolean othersExplosions) {
        this.othersExplosions = othersExplosions;
        areas.getYaml().set(optionsPath + "othersExplosions", othersExplosions);
        areas.save();
    }

    public void setCanBreakItemFrames(PlayerAreas areas, boolean canBreakItemFrames) {
        this.canBreakItemFrames = canBreakItemFrames;
        areas.getYaml().set(optionsPath + "canBreakItemFrames", canBreakItemFrames);
        areas.save();
    }

    public void setCanMountEntities(PlayerAreas areas, boolean canMountEntities) {
        this.canMountEntities = canMountEntities;
        areas.getYaml().set(optionsPath + "canMountEntities", canMountEntities);
        areas.save();
    }

    public void setCanEnterVehicles(PlayerAreas areas, boolean canEnterVehicles) {
        this.canEnterVehicles = canEnterVehicles;
        areas.getYaml().set(optionsPath + "canEnterVehicles", canEnterVehicles);
        areas.save();
    }

    public void setCanBreakVehicles(PlayerAreas areas, boolean canBreakVehicles) {
        this.canBreakVehicles = canBreakVehicles;
        areas.getYaml().set(optionsPath + "canBreakVehicles", canBreakVehicles);
        areas.save();
    }

    public void setFallDamage(PlayerAreas areas, boolean fallDamage) {
        this.fallDamage = fallDamage;
        areas.getYaml().set(optionsPath + "fallDamage", fallDamage);
        areas.save();
    }

    public void setNaturalAnimalsSpawn(PlayerAreas areas, boolean naturalAnimalsSpawn) {
        this.naturalAnimalsSpawn = naturalAnimalsSpawn;
        areas.getYaml().set(optionsPath + "naturalAnimalsSpawn", naturalAnimalsSpawn);
        areas.save();
    }

    public void setNaturalMonstersSpawn(PlayerAreas areas, boolean naturalMonstersSpawn) {
        this.naturalMonstersSpawn = naturalMonstersSpawn;
        areas.getYaml().set(optionsPath + "naturalMonstersSpawn", naturalMonstersSpawn);
        areas.save();
    }

    public void setCanInteractWithNPC(PlayerAreas areas, boolean canInteractWithNPC) {
        this.canInteractWithNPC = canInteractWithNPC;
        areas.getYaml().set(optionsPath + "canInteractWithNPC", canInteractWithNPC);
        areas.save();
    }

    public void setCanLightningStrike(PlayerAreas areas, boolean canLightningStrike) {
        this.canLightningStrike = canLightningStrike;
        areas.getYaml().set(optionsPath + "canLightningStrike", canLightningStrike);
        areas.save();
    }

    public void setCanBurnBlocks(PlayerAreas areas, boolean canBurnBlocks) {
        this.canBurnBlocks = canBurnBlocks;
        areas.getYaml().set(optionsPath + "canBurnBlocks", canBurnBlocks);
        areas.save();
    }

    public String getName() {
        return name;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public List<String> getAllowedPlayers() {
        return allowedPlayers;
    }

    public boolean canPlace() {
        return canPlace;
    }

    public boolean canBreakBlocks() {
        return canBreakBlocks;
    }

    public boolean canPvP() {
        return canPvP;
    }

    public boolean canPvE_Animals() {
        return canPvE_Animals;
    }

    public boolean canPvE_Monsters() {
        return canPvE_Monsters;
    }

    public boolean canOpenChests() {
        return canOpenChests;
    }

    public boolean canInteract() {
        return canInteract;
    }

    public boolean canPvE_NPC() {
        return canPvE_NPC;
    }

    public boolean creepersExplosions() {
        return creepersExplosions;
    }

    public boolean TNTExplosions() {
        return TNTExplosions;
    }

    public boolean othersExplosions() {
        return othersExplosions;
    }

    public boolean canBreakItemFrames() {
        return canBreakItemFrames;
    }

    public boolean canMountEntities() {
        return canMountEntities;
    }

    public boolean canEnterVehicles() {
        return canEnterVehicles;
    }

    public boolean canBreakVehicles() {
        return canBreakVehicles;
    }

    public boolean fallDamage() {
        return fallDamage;
    }

    public boolean naturalAnimalsSpawn() {
        return naturalAnimalsSpawn;
    }

    public boolean naturalMonstersSpawn() {
        return naturalMonstersSpawn;
    }

    public boolean canInteractWithNPC() {
        return canInteractWithNPC;
    }

    public boolean canLightningStrike() {
        return canLightningStrike;
    }

    public boolean canBurnBlocks() {
        return canBurnBlocks;
    }
}
