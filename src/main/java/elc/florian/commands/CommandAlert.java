package elc.florian.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandAlert implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas précisé de message");
            return false;
        }

        String message = String.join(" ", args);
        message = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Dieu:" + message);

        return false;
    }
}
