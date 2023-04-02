package elc.florian.commands;

import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCity implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            List<String> cityList = new ArrayList<String>();
            cityList.add("Nice");
            cityList.add("Cannes");

            sender.sendMessage(ChatColor.BLUE + "Voici la liste des villes du pays: " + ChatColor.GOLD + cityList.toString());
            return false;
        }

        String message = String.join(" ", args);
        message = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Dieu:" + message);

        return false;
    }
}
