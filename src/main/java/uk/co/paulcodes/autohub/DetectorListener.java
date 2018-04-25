package uk.co.paulcodes.autohub;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

/**
 * Created by paulb on 24/04/2018.
 */
public class DetectorListener implements Listener {

    public static HashMap<String, Integer> taskid = new HashMap<>();
    private static int id = 0;

    @EventHandler
    private static void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Core.isAfk.put(p.getUniqueId().toString(), false);
        Core.startTimer(p);
    }

    @EventHandler
    private static void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
        }else{
            Core.cancelTimer(p);
            if(taskid.containsKey(p.getUniqueId().toString())) {
            }else {
                if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {

                }else{
                    taskid.remove(p.getUniqueId().toString());
                }
                id = Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Core.startTimer(p);
                        Bukkit.getScheduler().cancelTask(taskid.get(p.getUniqueId().toString()));
                        taskid.remove(p.getUniqueId().toString());
                    }
                }, 400);
                taskid.put(p.getUniqueId().toString(), id);
            }
        }
    }

    @EventHandler
    private static void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Core.cancelTimer(p);
        if(taskid.containsKey(p.getUniqueId().toString())) {
        }else {
            id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Core.startTimer(p);
                    p.sendMessage("You are now AFK");
                }
            }, 400);
            taskid.put(p.getUniqueId().toString(), id);
        }
    }

}
