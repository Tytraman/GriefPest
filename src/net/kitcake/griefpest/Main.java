package net.kitcake.griefpest;

import net.kitcake.griefpest.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin implements CommandExecutor, TabCompleter {
    public static Main INSTANCE;
    public static List<PlayerAreas> AREAS;
    public static List<String> BLOCKED_COMMANDS;
    public static int MAX_CLAIMS;
    public static int MAX_BLOCKS;

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        File claims = new File(getDataFolder() + File.separator + "claims");
        claims.mkdir();
        AREAS = new ArrayList<>();
        BLOCKED_COMMANDS = new ArrayList<>();
        boolean ignoreCase = getConfig().getBoolean("ignore-case");
        getCommand("pest").setExecutor(this);
        getCommand("pest").setTabCompleter(this);
        getServer().getPluginManager().registerEvents(new OnPlace(), this);
        getServer().getPluginManager().registerEvents(new OnBreak(), this);
        getServer().getPluginManager().registerEvents(new OnInteract(), this);
        getServer().getPluginManager().registerEvents(new OnDamage(), this);
        getServer().getPluginManager().registerEvents(new OnExplosion(), this);
        getServer().getPluginManager().registerEvents(new OnEntityMount(), this);
        getServer().getPluginManager().registerEvents(new PreprocessCommandEvent(ignoreCase), this);
        getServer().getPluginManager().registerEvents(new OnSpawn(), this);
        getServer().getPluginManager().registerEvents(new OnLightningStrike(), this);
        for(File file : claims.listFiles()) {
            AREAS.add(new PlayerAreas(file));
        }
        if(ignoreCase) {
            String current;
            for(String str : getConfig().getStringList("blocked-commands")) {
                current = str.toLowerCase();
                if(!BLOCKED_COMMANDS.contains(current)) {
                    BLOCKED_COMMANDS.add(current);
                }
            }
        }else {
            for(String str : getConfig().getStringList("blocked-commands")) {
                BLOCKED_COMMANDS.add(str);
            }
        }
        MAX_CLAIMS = getConfig().getInt("max-claims");
        MAX_BLOCKS = getConfig().getInt("max-blocks");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0) {
            switch(args[0].toLowerCase()) {
                case "pos1":
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        PlayerAreas pAreas = PlayerAreas.getAreasOf(player);
                        pAreas.setLocation1(player.getTargetBlock(null, 10).getLocation());
                        player.sendMessage(ChatColor.DARK_AQUA + "Position 1 : " + ChatColor.LIGHT_PURPLE + pAreas.getLocation1().getX() + " " + pAreas.getLocation1().getY() + " " + pAreas.getLocation1().getZ());
                    }
                    break;
                case "pos2":
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        PlayerAreas pAreas = PlayerAreas.getAreasOf(player);
                        pAreas.setLocation2(player.getTargetBlock(null, 10).getLocation());
                        player.sendMessage(ChatColor.DARK_AQUA + "Position 2 : " + ChatColor.LIGHT_PURPLE + pAreas.getLocation2().getX() + " " + pAreas.getLocation2().getY() + " " + pAreas.getLocation2().getZ());
                    }
                    break;
                case "claim":
                    if(sender instanceof Player) {
                        if(args.length > 1) {
                            if(checkName(args[1])) {
                                Player player = (Player) sender;
                                PlayerAreas pAreas = PlayerAreas.getAreasOf(player);
                                if(pAreas.getAreas().size() < MAX_CLAIMS) {
                                    long blocks = PlayerAreas.size(pAreas.getLocation1(), pAreas.getLocation2());
                                    if(blocks <= MAX_BLOCKS) {
                                        switch(pAreas.addArea(args[1])) {
                                            case 0:
                                                player.sendMessage(ChatColor.GREEN + "Zone claim avec succès ! " + ChatColor.DARK_AQUA + "[" + args[1] + "]");
                                                break;
                                            case 1:
                                                player.sendMessage(ChatColor.RED + "Tu possèdes déjà une zone claim avec ce nom.");
                                                break;
                                            case 2:
                                                player.sendMessage(ChatColor.RED + "Tu dois définir la position 1 et 2 de la zone pour pouvoir la claim.");
                                                break;
                                            case 3:
                                                player.sendMessage(ChatColor.RED + "Les 2 positions doivent se situer dans le même monde.");
                                                break;
                                            case 4:
                                                player.sendMessage(ChatColor.RED + "Une zone claim est déjà présente dans cette zone.");
                                                break;
                                            default:
                                                player.sendMessage(ChatColor.RED + "Erreur inconnue.");
                                        }
                                    }else {
                                        sender.sendMessage(ChatColor.RED + "La zone ne peut pas contenir plus de " + ChatColor.GOLD + MAX_BLOCKS + " blocs" + ChatColor.AQUA + " [" + blocks + "]");
                                    }
                                }else {
                                    player.sendMessage(ChatColor.RED + "Tu ne peux pas avoir plus de zones claims " + ChatColor.DARK_AQUA + "[" + MAX_CLAIMS + "]");
                                }
                            }else {
                                sender.sendMessage(ChatColor.RED + "Le nom ne doit contenir que des lettres ou des chiffres et avoir un maximum de 20 caractères.");
                            }
                        }else {
                            sender.sendMessage(ChatColor.RED + "/pest claim <nom de la zone>");
                        }
                    }
                    break;
                case "number":
                    sender.sendMessage(ChatColor.DARK_AQUA + "Il y a " + ChatColor.RED + PlayerAreas.size() + ChatColor.DARK_AQUA + " zones claims.");
                    break;
                case "amiinanarea":
                    if(sender instanceof Player) {
                        sender.sendMessage(PlayerAreas.isInArea((Player) sender) ? ChatColor.RED + "Tu es dans une zone claim." : ChatColor.GREEN + "Tu n'es pas dans une zone claim.");
                    }
                    break;
                case "remove":
                    if(sender instanceof Player) {
                        if(args.length > 1) {
                            sender.sendMessage(PlayerAreas.getAreasOf((Player) sender).removeArea(args[1]) ? ChatColor.GREEN + "La zone ne t'appartient plus " + ChatColor.DARK_AQUA + "[" + args[1] + "]" : ChatColor.RED + "Tu n'as pas de zone claim possédant ce nom.");
                        }else {
                            sender.sendMessage(ChatColor.RED + "/pest remove <nom de la zone>");
                        }
                    }
                    break;
                case "edit":
                    if(sender instanceof Player) {
                        if(args.length > 3) {
                            switch(args[2].toLowerCase()) {
                                case "options":
                                    if(args.length > 4) {
                                        try {
                                            Options options = Options.valueOf(args[3].toUpperCase());
                                            byte yes = 2;
                                            if(args[4].equalsIgnoreCase("yes")) {
                                                yes = 1;
                                            }else if(args[4].equalsIgnoreCase("no")) {
                                                yes = 0;
                                            }
                                            if(yes != 2) {
                                                PlayerAreas areas = PlayerAreas.getAreasOf((Player) sender);
                                                Area area = PlayerAreas.getArea(areas, args[1]);
                                                if(area != null) {
                                                    switch(options) {
                                                        case CAN_PLACE:
                                                            area.setCanPlace(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "poser dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "poser dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_BREAK_BLOCKS:
                                                            area.setCanBreakBlocks(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "casser de blocs dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "casser les blocs de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_PVP:
                                                            area.setCanPvP(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "se taper dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "se taper dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_PVE_ANIMALS:
                                                            area.setCanPvE_Animals(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "taper les animaux dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "taper les animaux dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_PVE_MONSTERS:
                                                            area.setCanPvE_Monsters(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "taper les monstres dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "taper les monstres dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_OPEN_CHESTS:
                                                            area.setCanOpenChests(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "ouvrir les coffres de cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "ouvrir les coffres de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_INTERACT:
                                                            area.setCanInteract(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "interagir dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "interagir dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_PVE_NPC:
                                                            area.setCanPvE_NPC(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "taper les PNJ dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "taper les PNJ dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CREEPERS_EXPLOSIONS:
                                                            area.setCreepersExplosions(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les creepers " + ChatColor.RED + "ne pèteront plus " + ChatColor.DARK_AQUA + "les blocs de cette zone " : "Les creepers " + ChatColor.GREEN + "pèteront " + ChatColor.DARK_AQUA + "les blocs de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case TNT_EXPLOSIONS:
                                                            area.setTNTExplosions(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les TNT " + ChatColor.RED + "ne pèteront plus " + ChatColor.DARK_AQUA + "les blocs de cette zone " : "Les TNT " + ChatColor.GREEN + "pèteront " + ChatColor.DARK_AQUA + "les blocs de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case OTHERS_EXPLOSIONS:
                                                            area.setOthersExplosions(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les autres explosions " + ChatColor.RED + "ne pèteront plus " + ChatColor.DARK_AQUA + "les blocs de cette zone " : "Les autres explosions " + ChatColor.GREEN + "pèteront " + ChatColor.DARK_AQUA + "les blocs de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_BREAK_ITEM_FRAMES:
                                                            area.setCanBreakItemFrames(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les item frames " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "être cassés cette zone " : "Les item frames " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "être cassés dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_MOUNT_ENTITIES:
                                                            area.setCanMountEntities(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "monter sur les entités de cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "monter sur les entités de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_ENTER_VEHICLES:
                                                            area.setCanEnterVehicles(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "monter dans les véhicules de cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "monter dans les véhicules de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_BREAK_VEHICLES:
                                                            area.setCanBreakVehicles(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "casser les véhicules de cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "casser les véhicules de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case FALL_DAMAGE:
                                                            area.setFallDamage(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne subissent plus " + ChatColor.DARK_AQUA + "de dégâts de chutes dans cette zone " : "Les joueurs " + ChatColor.GREEN + "subissent " + ChatColor.DARK_AQUA + "les dégâts de chutes dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case NATURAL_ANIMALS_SPAWN:
                                                            area.setNaturalAnimalsSpawn(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les animaux " + ChatColor.RED + "ne spawneront plus " + ChatColor.DARK_AQUA + "naturellement dans cette zone " : "Les animaux " + ChatColor.GREEN + "spawneront " + ChatColor.DARK_AQUA + "naturellement dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case NATURAL_MONSTERS_SPAWN:
                                                            area.setNaturalMonstersSpawn(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les monstres " + ChatColor.RED + "ne spawneront plus " + ChatColor.DARK_AQUA + "naturellement dans cette zone " : "Les monstres " + ChatColor.GREEN + "spawneront " + ChatColor.DARK_AQUA + "naturellement dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_INTERACT_WITH_NPC:
                                                            area.setCanInteractWithNPC(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "interagir avec les PNJ de cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "interagir avec les PNJ de cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_LIGHTNING_STRIKE:
                                                            area.setCanLightningStrike(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "La foudre " + ChatColor.RED + "ne tombera plus " + ChatColor.DARK_AQUA + "dans cette zone " : "La foudre " + ChatColor.GREEN + "tombera " + ChatColor.DARK_AQUA + "dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        case CAN_BURN_BLOCKS:
                                                            area.setCanBurnBlocks(areas, yes == 1);
                                                            sender.sendMessage(ChatColor.DARK_AQUA + (yes != 1 ? "Les joueurs " + ChatColor.RED + "ne peuvent plus " + ChatColor.DARK_AQUA + "mettre le feu dans cette zone " : "Les joueurs " + ChatColor.GREEN + "peuvent " + ChatColor.DARK_AQUA + "mettre le feu dans cette zone ") + ChatColor.AQUA + "[" + args[1] + "]");
                                                            break;
                                                        default:
                                                            sender.sendMessage(ChatColor.RED + "Option pas encore configurée.");
                                                    }
                                                }else {
                                                    sender.sendMessage(ChatColor.RED + "Tu n'as pas de zone claim possédant ce nom.");
                                                }
                                            }else {
                                                sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> options <OPTIONS> <yes|no>");
                                            }
                                        }catch(IllegalArgumentException e) {
                                            sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> options <OPTION> <yes|no>");
                                        }
                                    }else {
                                        sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> options <OPTIONS> <yes|no>");
                                    }
                                    break;
                                case "group":
                                    switch(args[3].toLowerCase()) {
                                        case "add":
                                            if(args.length > 4) {
                                                Player target0 = Bukkit.getPlayer(args[4]);
                                                if(target0 != null) {
                                                    PlayerAreas areas0 = PlayerAreas.getAreasOf((Player) sender);
                                                    Area area0 = PlayerAreas.getArea(areas0, args[1]);
                                                    if(area0 != null) {
                                                        sender.sendMessage((area0.addAllowedPlayer(areas0, target0) ? ChatColor.DARK_AQUA + target0.getName() + ChatColor.GREEN + " a été ajouté(e) aux personnes de confiances dans cette zone" : ChatColor.DARK_AQUA + target0.getName() + ChatColor.RED + " est déjà dans la liste des personnes de confiances de cette zone") + ChatColor.AQUA + " [" + args[1] + "]");
                                                    }else {
                                                        sender.sendMessage(ChatColor.RED + "Tu n'as pas de zone claim possédant ce nom.");
                                                    }
                                                }else {
                                                    sender.sendMessage(ChatColor.RED + args[4] + " n'est pas connecté(e).");
                                                }
                                            }else {
                                                sender.sendMessage(ChatColor.RED + "/pest edit <nom du groupe> group add <pseudo>");
                                            }
                                            break;
                                        case "remove":
                                            if(args.length > 4) {
                                                Player player1 = (Player) sender;
                                                PlayerAreas areas1 = PlayerAreas.getAreasOf(player1);
                                                Area area1 = PlayerAreas.getArea(areas1, args[1]);
                                                if(area1 != null) {
                                                    Player target1 = Bukkit.getPlayer(args[4]);
                                                    if(player1 != target1) {
                                                        if(target1 != null) {
                                                            sender.sendMessage((area1.removeAllowedPlayer(areas1, target1) ? ChatColor.DARK_AQUA + target1.getName() + ChatColor.GREEN + " a été enlevé(e) des personnes de confiances dans cette zone" : ChatColor.DARK_AQUA + target1.getName() + ChatColor.RED + " ne fait pas partie des personnes de confiances de cette zone") + ChatColor.AQUA + " [" + args[1] + "]");
                                                        }else {
                                                            sender.sendMessage((area1.removeAllowedPlayer(areas1, args[4]) ? ChatColor.DARK_AQUA + args[4] + ChatColor.GREEN + " a été enlevé(e) des personnes de confiances dans cette zone" : ChatColor.DARK_AQUA + args[4] + ChatColor.RED + " ne fait pas partie des personnes de confiances de cette zone") + ChatColor.AQUA + " [" + args[1] + "]");
                                                        }
                                                    }else {
                                                        sender.sendMessage(ChatColor.RED + "Tu ne peux pas t'enlever de ta propre zone.");
                                                    }
                                                }else {
                                                    sender.sendMessage(ChatColor.RED + "Tu n'as pas de zone claim possédant ce nom.");
                                                }
                                            }else {
                                                sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> group remove <pseudo|UUID>");
                                            }
                                            break;
                                        case "reset":
                                            PlayerAreas areas2 = PlayerAreas.getAreasOf((Player) sender);
                                            Area area2 = PlayerAreas.getArea(areas2, args[1]);
                                            if(area2 != null) {
                                                area2.resetAllowedPlayers(areas2);
                                                sender.sendMessage(ChatColor.GREEN + "Liste des personnes de confiances de cette zone réinitialisé" + ChatColor.AQUA + " [" + args[1] +" ]");
                                            }else {
                                                sender.sendMessage(ChatColor.RED + "Tu n'as pas de zone claim possédant ce nom.");
                                            }
                                            break;
                                        default:
                                            sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> group <add|remove|reset>");
                                        }
                                    break;
                                default:
                                    sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> <options|group>");
                            }
                        }else {
                            if(args.length > 2) {
                                switch(args[2].toLowerCase()) {
                                    case "options":
                                        sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> options <OPTIONS> <yes|no>");
                                        break;
                                    case "group":
                                        sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> group <add|remove|reset>");
                                        break;
                                    default:
                                        sender.sendMessage(ChatColor.RED + "/pest edit <nom de la zone> <options|group>");
                                }
                            }
                        }
                    }
                    break;
                case "zonename":                            // Affiche le nom de la zone claim dans laquelle le joueur se situe
                    if(sender instanceof Player) {
                        Area area = PlayerAreas.getAreaOfLocation((Player) sender);
                        sender.sendMessage(area != null ? ChatColor.DARK_AQUA + "La zone claim dans laquelle tu te trouves s'appelle " + ChatColor.RED + area.getName() + ChatColor.DARK_AQUA + "." : ChatColor.RED + "Tu n'es pas dans une zone claim.");
                    }
                    break;
                case "blockedcommands":
                    for(String str : BLOCKED_COMMANDS) {
                        sender.sendMessage(str);
                    }
                    break;
                case "size":
                    if(sender instanceof Player) {
                        Area area = PlayerAreas.getAreaOfLocation((Player) sender);
                        sender.sendMessage(area != null ? ChatColor.DARK_AQUA + "La zone contient " + ChatColor.RED + area.size() + " blocs" + ChatColor.DARK_AQUA + "." : ChatColor.RED + "Tu n'es pas dans une zone claim.");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Argument invalide.");
            }
        }else {
            sender.sendMessage(ChatColor.RED + "Argument manquant.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        switch(args.length) {
            case 1:
                for(String str : new String[]{"pos1", "pos2", "claim", "number", "amiinanarea", "remove", "edit", "zonename", "blockedcommands", "size"}) {
                    if(str.startsWith(args[0].toLowerCase())) {
                        list.add(str);
                    }
                }
                break;
            case 2:
                switch(args[0].toLowerCase()) {
                    case "remove":
                    case "edit":
                        if(sender instanceof Player) {
                            for(PlayerAreas areas : AREAS) {
                                if(areas.getOwnerUUID().equals(((Player) sender).getUniqueId().toString())) {
                                    for(Area area : areas.getAreas()) {
                                        if(area.getName().startsWith(args[1])) {
                                            list.add(area.getName());
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                }
                break;
            case 3:
                switch(args[0].toLowerCase()) {
                    case "edit":
                        for(String str : new String[] {"options", "group"}) {
                            if(str.startsWith(args[2].toLowerCase())) {
                                list.add(str);
                            }
                        }
                        break;
                }
                break;
            case 4:
                if("edit".equalsIgnoreCase(args[0])) {
                    switch(args[2].toLowerCase()) {
                        case "options":
                            String val;
                            for(Options options : Options.values()) {
                                val = options.toString();
                                if(val.startsWith(args[3].toUpperCase())) {
                                    list.add(val);
                                }
                            }
                            break;
                        case "group":
                            for(String str : new String[] {"add", "remove", "reset"}) {
                                if(str.startsWith(args[3].toLowerCase())) {
                                    list.add(str);
                                }
                            }
                            break;
                    }
                }
                break;
            case 5:
                if("edit".equalsIgnoreCase(args[0])) {
                    switch(args[2].toLowerCase()) {
                        case "options":
                            for(String str : new String[]{"yes", "no"}) {
                                if(str.startsWith(args[4].toLowerCase())) {
                                    list.add(str);
                                }
                            }
                            break;
                        case "group":
                            if(sender instanceof Player) {
                                switch(args[3].toLowerCase()) {
                                    case "add":
                                        String name;
                                        for(Player player : Bukkit.getOnlinePlayers()) {
                                            name = player.getName();
                                            if(name.startsWith(args[4].toLowerCase())) {
                                                list.add(name);
                                            }
                                        }
                                        break;
                                    case "remove":
                                        Area area = PlayerAreas.getArea((Player) sender, args[1]);
                                        if(area != null) {
                                            Player target;
                                            for(String uuid : area.getAllowedPlayers()) {
                                                target = Bukkit.getPlayer(UUID.fromString(uuid));
                                                if(target != null) {
                                                    list.add(target.getName());
                                                }else {
                                                    list.add(uuid);
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                            break;
                    }
                }
                break;
        }
        Collections.sort(list);
        return list;
    }

    /**
     * Vérifie si le nom donné est correct pour ne pas corrompre les fichiers YAML
     * @param value La chaîne à vérifier
     * @return <code>true</code> si cela respecte <code>[a-zA-Z1-9]</code> sinon <code>false</code>
     */
    public static boolean checkName(String value) {
        if(value.length() > 20) {
            return false;
        }
        for(char c : value.toCharArray()) {
            if(!((c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c >= 48 && c <= 57))) {
                return false;
            }
        }
        return true;
    }
}
