package io.github.meeples10.lastseen;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSeen implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("lastseen.use")) {
            if(args.length == 0) return false;
            Player p = Main.getOnlinePlayer(args[0]);
            if(p != null) {
                sender.sendMessage(String.format(Main.playerOnlineMessage, args[0]));
                if(sender.hasPermission("lastseen.location")) {
                    sender.sendMessage(String.format(Main.playerLocationMessage, p.getLocation().x(),
                            p.getLocation().y(), p.getLocation().z(), p.getLocation().getWorld().getName()));
                }
                return true;
            }
            PlayerData pd = Main.getPlayerData(args[0]);
            if(pd != null) {
                sender.sendMessage(String.format(Main.lastSeenMessage, pd.name, Main.dateFormat.format(pd.lastSeen)));
                if(sender.hasPermission("lastseen.location")) {
                    sender.sendMessage(String.format(Main.playerLocationMessage, pd.x, pd.y, pd.z, pd.world));
                }
            } else {
                sender.sendMessage(String.format(Main.playerNotFoundMessage, args[0]));
            }
        } else {
            sender.sendMessage(Main.noPermissionMessage);
        }
        return true;
    }
}
