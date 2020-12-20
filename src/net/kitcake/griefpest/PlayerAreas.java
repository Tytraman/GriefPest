package net.kitcake.griefpest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerAreas {

    private final String ownerUUID;
    private Location location1;
    private Location location2;
    private final List<Area> areas;
    private final File file;
    private final YamlConfiguration yaml;

    /**
     * Objet pour gérer les zones claims d'un joueur
     * @param playerDataClaim Fichier contenant les claims du joueur
     */
    public PlayerAreas(File playerDataClaim) {
        String name = playerDataClaim.getName();
        this.ownerUUID = name.substring(0, name.lastIndexOf('.'));
        location1 = null;
        location2 = null;
        areas = new ArrayList<>();
        file = playerDataClaim;
        yaml = YamlConfiguration.loadConfiguration(file);
        yaml.addDefault("areas", new ArrayList<>());
        yaml.options().copyDefaults(true);
        save();
        loadAreas();
    }

    private void loadAreas() {
        try {
            for(String area : yaml.getConfigurationSection("areas").getKeys(false)) {
                World w = Bukkit.getWorld(yaml.getString("areas." + area + ".world"));
                areas.add(Area.loadArea(this, area, new Location(w, yaml.getDouble("areas." + area + ".location1.x"), yaml.getDouble("areas." + area + ".location1.y"), yaml.getDouble("areas." + area + ".location1.z")), new Location(w, yaml.getDouble("areas." + area + ".location2.x"), yaml.getDouble("areas." + area + ".location2.y"), yaml.getDouble("areas." + area + ".location2.z"))));
            }
        }catch(NullPointerException ignored) {}
    }

    /**
     * Ajoute une zone claim
     * @param name Nom de la zone
     * @return <code>0</code> si l'ajout s'est effectué avec succès<br>
     * <code>1</code> si une zone du même nom existe<br>
     * <code>2</code> si une des <code>Location</code> ou les deux sont <code>null</code><br>
     * <code>3</code> si les mondes des <code>Location</code> ne sont pas les mêmes<br>
     * <code>4</code> si une zone claim est déjà présente à cette emplacement
     */
    public int addArea(String name) {
        if(!areaExists(name)) {
            if(location1 != null && location2 != null) {
                World one = location1.getWorld();
                if(one == location2.getWorld()) {
                    if(!isAnyClaimInArea(location1, location2)) {
                        yaml.set("areas." + name + ".world", one.getName());
                        yaml.set("areas." + name + ".location1.x", location1.getX());
                        yaml.set("areas." + name + ".location1.y", location1.getY());
                        yaml.set("areas." + name + ".location1.z", location1.getZ());
                        yaml.set("areas." + name + ".location2.x", location2.getX());
                        yaml.set("areas." + name + ".location2.y", location2.getY());
                        yaml.set("areas." + name + ".location2.z", location2.getZ());
                        save();
                        areas.add(Area.loadArea(
                                this,
                                name,
                                new Location(one, location1.getX(), location1.getY(), location1.getZ()),
                                new Location(one, location2.getX(), location2.getY(), location2.getZ()))
                        );
                        return 0;
                    }
                    return 4;
                }
                return 3;
            }
            return 2;
        }
        return 1;
    }


    public static boolean isAnyClaimInArea(Location location1, Location location2) {
        double x1, x2, y1, y2, z1, z2;
        if(location1.getX() > location2.getX()) {
            x1 = location2.getX();
            x2 = location1.getX();
        }else {
            x1 = location1.getX();
            x2 = location2.getX();
        }
        if(location1.getY() > location2.getY()) {
            y1 = location2.getY();
            y2 = location1.getY();
        }else {
            y1 = location1.getY();
            y2 = location2.getY();
        }
        if(location1.getZ() > location2.getZ()) {
            z1 = location2.getZ();
            z2 = location1.getZ();
        }else {
            z1 = location1.getZ();
            z2 = location2.getZ();
        }
        double xx1, xx2, yy1, yy2, zz1, zz2;
        double y, z;
        for(;x1 <= x2; x1++) {
            for(y = y1; y <= y2; y++) {
                for(z = z1; z <= z2; z++) {
                    for(PlayerAreas areas : Main.AREAS) {
                        for(Area area : areas.getAreas()) {
                            if(area.getLocation1().getX() > area.getLocation2().getX()) {
                                xx1 = area.getLocation2().getX();
                                xx2 = area.getLocation1().getX();
                            }else {
                                xx1 = area.getLocation1().getX();
                                xx2 = area.getLocation2().getX();
                            }
                            if(area.getLocation1().getY() > area.getLocation2().getY()) {
                                yy1 = area.getLocation2().getY();
                                yy2 = area.getLocation1().getY();
                            }else {
                                yy1 = area.getLocation1().getY();
                                yy2 = area.getLocation2().getY();
                            }
                            if(area.getLocation1().getZ() > area.getLocation2().getZ()) {
                                zz1 = area.getLocation2().getZ();
                                zz2 = area.getLocation1().getZ();
                            }else {
                                zz1 = area.getLocation1().getZ();
                                zz2 = area.getLocation2().getZ();
                            }
                            if(x1 >= xx1 && x1 <= xx2 && y >= yy1 && y <= yy2 && z >= zz1 && z <= zz2) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param location1
     * @param location2
     * @return Les blocs dans une zone
     */
    public static List<Block> getBlocks(Location location1, Location location2) {
        List<Block> blocks = new ArrayList<>();
        double x1, x2, y1, y2, z1, z2;
        if(location1.getX() > location2.getX()) {
            x1 = location2.getX();
            x2 = location1.getX();
        }else {
            x1 = location1.getX();
            x2 = location2.getX();
        }
        if(location1.getY() > location2.getY()) {
            y1 = location2.getY();
            y2 = location1.getY();
        }else {
            y1 = location1.getY();
            y2 = location2.getY();
        }
        if(location1.getZ() > location2.getZ()) {
            z1 = location2.getZ();
            z2 = location1.getZ();
        }else {
            z1 = location1.getZ();
            z2 = location2.getZ();
        }
        double y, z;
        for(;x1 <= x2; x1++) {
            for(y = y1; y <= y2; y++) {
                for(z = z1; z <= z2; z++) {
                    blocks.add(location1.getWorld().getBlockAt((int) x1, (int) y, (int) z));
                }
            }
        }
        return blocks;
    }

    /**
     * Vérifie si une zone existe
     * @param name Nom de la zone à checker
     * @return <code>true</code> si la zone existe<br>
     *     <code>false</code> si la zone n'existe pas
     */
    public boolean areaExists(String name) {
        for(Area area : areas) {
            if(area.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retire une zone claim si elle existe
     * @param name Nom de la zone à supprimer
     * @return <code>true</code> si la zone a été supprimé<br>
     *     <code>false</code> si la zone n'a pas été supprimée
     */
    public boolean removeArea(String name) {
        for(Area area : areas) {
            if(area.getName().equals(name)) {
                areas.remove(area);
                if(areas.size() == 0) {
                    yaml.set("areas", new ArrayList<>());
                }else {
                    yaml.set("areas." + name, null);
                }
                save();
                return true;
            }
        }
        return false;
    }

    /**
     * Sauvegarde les changements effectués sur le fichier du joueur
     */
    public void save() {
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Définie la position 1 pour faire une claim
     * @param location1
     */
    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    /**
     * Définie la position 2 pour faire une claim
     * @param location2
     */
    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    /**
     * @return La position 1 pour faire une claim
     */
    public Location getLocation1() {
        return location1;
    }

    /**
     * @return La position 2 pour faire une claim
     */
    public Location getLocation2() {
        return location2;
    }

    /**
     * @param name Nom de la zone
     * @return La position 1 de la zone possédant ce nom
     */
    public Location getLocation1Of(String name) {
        for(Area area : areas) {
            if(area.getName().equals(name)) {
                return area.getLocation1();
            }
        }
        return null;
    }

    /**
     * @param name Nom de la zone
     * @return La position 2 de la zone possédant ce nom
     */
    public Location getLocation2Of(String name) {
        for(Area area : areas) {
            if(area.getName().equals(name)) {
                return area.getLocation2();
            }
        }
        return null;
    }

    /**
     * @return La liste des zones claims du joueurs
     */
    public List<Area> getAreas() {
        return areas;
    }

    /**
     * @return L'UUID du propriétaire
     */
    public String getOwnerUUID() {
        return ownerUUID;
    }

    /**
     * @return Le fichier yml
     */
    public YamlConfiguration getYaml() {
        return yaml;
    }


    /**
     * @param player
     * @return Les zones du joueur spécifié
     */
    public static PlayerAreas getAreasOf(Player player) {
        String uuid = player.getUniqueId().toString();
        PlayerAreas pAreas = null;
        for(PlayerAreas areas : Main.AREAS) {
            if(areas.getOwnerUUID().equals(uuid)) {
                pAreas = areas;
                break;
            }
        }
        if(pAreas == null) {
            pAreas = new PlayerAreas(new File(Main.INSTANCE.getDataFolder() + File.separator + "claims" + File.separator + uuid + ".yml"));
            Main.AREAS.add(pAreas);
        }
        return pAreas;
    }

    /**
     * @return Le nombre de zones claims en tout
     */
    public static int size() {
        int number = 0;
        for(PlayerAreas areas : Main.AREAS) {
            number += areas.getAreas().size();
        }
        return number;
    }

    /**
     * Calcule le nombre de blocs entre deux positions
     * @param location1
     * @param location2
     * @return <code>long</code> contenant le nombre de blocs entre les deux positions
     */
    public static long size(Location location1, Location location2) {
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

    /**
     * Vérifie si le joueur se trouve dans une zone
     * @param player
     * @return <code>true</code> si le joueur se trouve dans une zone<br>
     *     <code>false</code> s'il ne se trouve pas dans une zone
     */
    public static boolean isInArea(Player player) {
        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double x1 = 0, y1 = 0, z1 = 0;
        double x2 = 0, y2 = 0, z2 = 0;
        boolean oui = false;
        Location loc1;
        Location loc2;
        for(PlayerAreas areas : Main.AREAS) {
            for(Area area : areas.getAreas()) {
                loc1 = area.getLocation1();
                loc2 = area.getLocation2();
                if(loc1.getX() > loc2.getX()) {
                    x1 = loc2.getX();
                    x2 = loc1.getX() + 1;
                }else {
                    x1 = loc1.getX();
                    x2 = loc2.getX() + 1;
                }
                if(loc1.getY() > loc2.getY()) {
                    y1 = loc2.getY();
                    y2 = loc1.getY() + 0.99;
                }else {
                    y1 = loc1.getY();
                    y2 = loc2.getY() + 0.99;
                }
                if(loc1.getZ() > loc2.getZ()) {
                    z1 = loc2.getZ();
                    z2 = loc1.getZ() + 1;
                }else {
                    z1 = loc1.getZ();
                    z2 = loc2.getZ() + 1;
                }
                if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && ((y >= y1 && y <= y2) || (y + 1.99 >= y1 && y + 1.99 <= y2))) {
                    oui = true;
                    break;
                }
            }
            if(oui) break;
        }
        return oui;
    }

    /**
     * Vérifie si le bloc se trouve dans une zone
     * @param block
     * @return <code>true</code> si le bloc se trouve dans une zone<br>
     *     <code>false</code> s'il ne se trouve pas dans une zone
     */
    public static boolean isInArea(Block block) {
        Location location = block.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double x1 = 0, y1 = 0, z1 = 0;
        double x2 = 0, y2 = 0, z2 = 0;
        boolean oui = false;
        Location loc1;
        Location loc2;
        for(PlayerAreas areas : Main.AREAS) {
            for(Area area : areas.getAreas()) {
                loc1 = area.getLocation1();
                loc2 = area.getLocation2();
                if(loc1.getX() > loc2.getX()) {
                    x1 = loc2.getX();
                    x2 = loc1.getX();
                }else {
                    x1 = loc1.getX();
                    x2 = loc2.getX();
                }
                if(loc1.getY() > loc2.getY()) {
                    y1 = loc2.getY();
                    y2 = loc1.getY();
                }else {
                    y1 = loc1.getY();
                    y2 = loc2.getY();
                }
                if(loc1.getZ() > loc2.getZ()) {
                    z1 = loc2.getZ();
                    z2 = loc1.getZ();
                }else {
                    z1 = loc1.getZ();
                    z2 = loc2.getZ();
                }
                if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && y >= y1 && y <= y2) {
                    oui = true;
                    break;
                }
            }
            if(oui) break;
        }
        return oui;
    }

    /**
     * Renvoie la zone dans laquelle le joueur se trouve
     * @param player Joueur à vérifier
     * @return La zone dans laquelle le joueur se situe, <code>null</code> s'il n'est pas dans une zone
     */
    public static Area getAreaOfLocation(Player player) {
        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double x1 = 0, y1 = 0, z1 = 0;
        double x2 = 0, y2 = 0, z2 = 0;
        Location loc1;
        Location loc2;
        for(PlayerAreas areas : Main.AREAS) {
            for(Area area : areas.getAreas()) {
                loc1 = area.getLocation1();
                loc2 = area.getLocation2();
                if(loc1.getX() > loc2.getX()) {
                    x1 = loc2.getX();
                    x2 = loc1.getX() + 1;
                }else {
                    x1 = loc1.getX();
                    x2 = loc2.getX() + 1;
                }
                if(loc1.getY() > loc2.getY()) {
                    y1 = loc2.getY();
                    y2 = loc1.getY() + 0.99;
                }else {
                    y1 = loc1.getY();
                    y2 = loc2.getY() + 0.99;
                }
                if(loc1.getZ() > loc2.getZ()) {
                    z1 = loc2.getZ();
                    z2 = loc1.getZ() + 1;
                }else {
                    z1 = loc1.getZ();
                    z2 = loc2.getZ() + 1;
                }
                if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && ((y >= y1 && y <= y2) || (y + 1.99 >= y1 && y + 1.99 <= y2))) {
                    return area;
                }
            }
        }
        return null;
    }

    /**
     * Renvoie la zone dans laquelle le bloc se trouve
     * @param block Bloc à vérifier
     * @return La zone dans laquelle le bloc se situe, <code>null</code> s'il n'est pas dans une zone
     */
    public static Area getAreaOfLocation(Block block) {
        Location location = block.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double x1 = 0, y1 = 0, z1 = 0;
        double x2 = 0, y2 = 0, z2 = 0;
        Location loc1;
        Location loc2;
        for(PlayerAreas areas : Main.AREAS) {
            for(Area area : areas.getAreas()) {
                loc1 = area.getLocation1();
                loc2 = area.getLocation2();
                if(loc1.getX() > loc2.getX()) {
                    x1 = loc2.getX();
                    x2 = loc1.getX();
                }else {
                    x1 = loc1.getX();
                    x2 = loc2.getX();
                }
                if(loc1.getY() > loc2.getY()) {
                    y1 = loc2.getY();
                    y2 = loc1.getY();
                }else {
                    y1 = loc1.getY();
                    y2 = loc2.getY();
                }
                if(loc1.getZ() > loc2.getZ()) {
                    z1 = loc2.getZ();
                    z2 = loc1.getZ();
                }else {
                    z1 = loc1.getZ();
                    z2 = loc2.getZ();
                }
                if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && y >= y1 && y <= y2) {
                    return area;
                }
            }
        }
        return null;
    }

    public static Area getAreaOfLocation(Hanging hanging) {
        Location location = hanging.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double x1 = 0, y1 = 0, z1 = 0;
        double x2 = 0, y2 = 0, z2 = 0;
        Location loc1;
        Location loc2;
        for(PlayerAreas areas : Main.AREAS) {
            for(Area area : areas.getAreas()) {
                loc1 = area.getLocation1();
                loc2 = area.getLocation2();
                if(loc1.getX() > loc2.getX()) {
                    x1 = loc2.getX();
                    x2 = loc1.getX() + 1;
                }else {
                    x1 = loc1.getX();
                    x2 = loc2.getX() + 1;
                }
                if(loc1.getY() > loc2.getY()) {
                    y1 = loc2.getY();
                    y2 = loc1.getY() + 1;
                }else {
                    y1 = loc1.getY();
                    y2 = loc2.getY() + 1;
                }
                if(loc1.getZ() > loc2.getZ()) {
                    z1 = loc2.getZ();
                    z2 = loc1.getZ() + 1;
                }else {
                    z1 = loc1.getZ();
                    z2 = loc2.getZ() + 1;
                }
                if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && y >= y1 && y <= y2) {
                    return area;
                }
            }
        }
        return null;
    }

    public static Area getAreaOfLocation(Entity entity) {
        Location location = entity.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double x1 = 0, y1 = 0, z1 = 0;
        double x2 = 0, y2 = 0, z2 = 0;
        Location loc1;
        Location loc2;
        switch(entity.getType()) {
            case PLAYER:
                for(PlayerAreas areas : Main.AREAS) {
                    for(Area area : areas.getAreas()) {
                        loc1 = area.getLocation1();
                        loc2 = area.getLocation2();
                        if(loc1.getX() > loc2.getX()) {
                            x1 = loc2.getX();
                            x2 = loc1.getX() + 1;
                        }else {
                            x1 = loc1.getX();
                            x2 = loc2.getX() + 1;
                        }
                        if(loc1.getY() > loc2.getY()) {
                            y1 = loc2.getY();
                            y2 = loc1.getY() + 0.99;
                        }else {
                            y1 = loc1.getY();
                            y2 = loc2.getY() + 0.99;
                        }
                        if(loc1.getZ() > loc2.getZ()) {
                            z1 = loc2.getZ();
                            z2 = loc1.getZ() + 1;
                        }else {
                            z1 = loc1.getZ();
                            z2 = loc2.getZ() + 1;
                        }
                        if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && ((y >= y1 && y <= y2) || (y + 1.99 >= y1 && y + 1.99 <= y2))) {
                            return area;
                        }
                    }
                }
                break;
            case ITEM_FRAME:
                for(PlayerAreas areas : Main.AREAS) {
                    for(Area area : areas.getAreas()) {
                        loc1 = area.getLocation1();
                        loc2 = area.getLocation2();
                        if(loc1.getX() > loc2.getX()) {
                            x1 = loc2.getX();
                            x2 = loc1.getX() + 1;
                        }else {
                            x1 = loc1.getX();
                            x2 = loc2.getX() + 1;
                        }
                        if(loc1.getY() > loc2.getY()) {
                            y1 = loc2.getY();
                            y2 = loc1.getY() + 1;
                        }else {
                            y1 = loc1.getY();
                            y2 = loc2.getY() + 1;
                        }
                        if(loc1.getZ() > loc2.getZ()) {
                            z1 = loc2.getZ();
                            z2 = loc1.getZ() + 1;
                        }else {
                            z1 = loc1.getZ();
                            z2 = loc2.getZ() + 1;
                        }
                        if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && y >= y1 && y <= y2) {
                            return area;
                        }
                    }
                }
                break;
            default:
                for(PlayerAreas areas : Main.AREAS) {
                    for(Area area : areas.getAreas()) {
                        loc1 = area.getLocation1();
                        loc2 = area.getLocation2();
                        if(loc1.getX() > loc2.getX()) {
                            x1 = loc2.getX();
                            x2 = loc1.getX() + 1;
                        }else {
                            x1 = loc1.getX();
                            x2 = loc2.getX() + 1;
                        }
                        if(loc1.getY() > loc2.getY()) {
                            y1 = loc2.getY();
                            y2 = loc1.getY();
                        }else {
                            y1 = loc1.getY();
                            y2 = loc2.getY();
                        }
                        if(loc1.getZ() > loc2.getZ()) {
                            z1 = loc2.getZ();
                            z2 = loc1.getZ() + 1;
                        }else {
                            z1 = loc1.getZ();
                            z2 = loc2.getZ() + 1;
                        }
                        if(x >= x1 && x <= x2 && z >= z1 && z <= z2 && y >= y1 && y <= y2) {
                            return area;
                        }
                    }
                }
        }
        return null;
    }

    public static Area getArea(PlayerAreas areas, String name) {
        for(Area area : areas.getAreas()) {
            if(area.getName().equals(name)) {
                return area;
            }
        }
        return null;
    }

    public static Area getArea(Player owner, String name) {
        for(Area area : PlayerAreas.getAreasOf(owner).getAreas()) {
            if(area.getName().equals(name)) {
                return area;
            }
        }
        return null;
    }



}
