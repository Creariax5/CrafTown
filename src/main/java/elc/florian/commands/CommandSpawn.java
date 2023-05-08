package elc.florian.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CommandSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Debut de la teleportation ...");

            World world = Bukkit.getWorld("world");
            final Location location = new Location(world, 0, 0, 0, 0, 0);
            player.teleport(location);

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "... le voyage est terminé");
        } else {
            CommandCity.spawnCity(args[0], sender);
        }
        return false;
    }
}
