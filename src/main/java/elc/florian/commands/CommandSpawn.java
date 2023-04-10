package elc.florian.commands;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandSpawn implements CommandExecutor {
    private final Main main;

    public CommandSpawn(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Debut de la teleportation ...");

            Player player = (Player) sender;
            World world = Bukkit.getWorld("world");
            final Location location = new Location(world, 0, 0, 0, 0, 0);
            player.teleport(location);

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "... le voyage est terminé");
        } else {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();
            try {
                final Connection connection = db1Connection.getConnection();
                CommandCity.spawnCity(connection, args[0], sender);

            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "ERROR");
            }
        }
        return false;
    }
}
