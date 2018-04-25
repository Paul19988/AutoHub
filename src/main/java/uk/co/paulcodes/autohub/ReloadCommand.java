package uk.co.paulcodes.autohub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by paulb on 25/04/2018.
 */
public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("autohub")) {
            if(sender.hasPermission("autohub.reload")) {
                if(args[0].equalsIgnoreCase("reload")) {
                    Core.getInstance().reloadConfig();
                    sender.sendMessage(Core.prefix + "You have successfully reloaded the config");
                }else{
                    sender.sendMessage(Core.prefix + "Please use the correct arguments. /autohub reload");
                }
            }else{

            }
        }
        return false;
    }

}
