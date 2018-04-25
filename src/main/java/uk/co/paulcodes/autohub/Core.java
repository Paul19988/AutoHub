package uk.co.paulcodes.autohub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by paulb on 24/04/2018.
 */
public class Core extends JavaPlugin {

    public static HashMap<String, Boolean> isAfk = new HashMap<>();

    private static String command = "";
    private static String server = "";
    private static int time = 0;
    public static String prefix = "";
    private static String sendingservermsg = "";

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new DetectorListener(), this);
        if(getConfig().getString("command") != null) {
            command = getInstance().getConfig().getString("command");
            server = getInstance().getConfig().getString("server");
            time = getInstance().getConfig().getInt("time");
            prefix = ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("prefix"));
            sendingservermsg = ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("sendingservermsg"));
        }else {
            getConfig().set("command", "");
            getConfig().set("server", "hub");
            getConfig().set("time", 500);
            getConfig().set("prefix", "&aAutoHub &8> &e");
            getConfig().set("sendingservermsg", "You are being sent to %server% for being AFK");
            saveConfig();
        }

        this.getCommand("autohub").setExecutor(new ReloadCommand());
    }

    @Override
    public void onDisable() {

    }

    private static HashMap<String, Integer> taskid = new HashMap<>();
    private static int id = 0;

    public static void startTimer(Player p) {
        id = Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable() {
            @Override
            public void run() {
                if(!getInstance().getConfig().getString("command").equalsIgnoreCase("")) {
                    p.performCommand(command);
                }else if(!getInstance().getConfig().getString("server").equalsIgnoreCase("")) {
                    p.sendMessage(prefix + sendSvrMsg(server));
                    sendPlayer(server, p);
                }else{
                    p.sendMessage("Your server admin has not configured the plugin correctly. (AutoHub)");
                }
            }
        }, time * 20);
        taskid.put(p.getUniqueId().toString(), id);
    }

    public static void cancelTimer(Player p) {
        Bukkit.getScheduler().cancelTask(taskid.get(p.getUniqueId().toString()));
    }

    public static Core getInstance() {
        return Core.getPlugin(Core.class);
    }

    public static void sendPlayer(String server, Player p) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static String sendSvrMsg(String server) {
        String response = sendingservermsg.replace("%server%", server);
        return response;
    }
}
