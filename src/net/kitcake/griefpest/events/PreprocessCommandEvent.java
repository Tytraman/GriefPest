package net.kitcake.griefpest.events;

import net.kitcake.griefpest.Area;
import net.kitcake.griefpest.Main;
import net.kitcake.griefpest.PlayerAreas;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreprocessCommandEvent implements Listener {
    private final boolean ignoreCase;

    public PreprocessCommandEvent(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void preprocess(PlayerCommandPreprocessEvent e) {
        Area area = PlayerAreas.getAreaOfLocation(e.getPlayer());
        if(area != null && !area.isAllowed(e.getPlayer())) {
            String message = e.getMessage().substring(1);
            if(message.contains(" ")) {
                message = message.substring(0, message.indexOf(" "));
            }
            if(ignoreCase) {
                message = message.toLowerCase();
            }
            if(Main.BLOCKED_COMMANDS.contains(message)) {
                e.getPlayer().sendMessage(ChatColor.RED + "Tu ne peux pas faire cette commande quand tu te situes dans une zone claim.");
                e.setCancelled(true);
            }
        }
    }
}
