package elc.florian.commands;

import elc.florian.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static java.lang.Math.ceil;

public class CommandTerrain implements CommandExecutor {
    private static Main main;
    public CommandTerrain(Main main) {
        CommandTerrain.main = main;
    }

    public static int getChunkPoint(double x) {
        x += 1;

        x = ceil(x/ 16)-1;
        return (int) x;
    }

    public static Location getChunk(Location location) {
        location.setX(getChunkPoint(location.getX()));
        location.setZ(getChunkPoint(location.getZ()));
        return location;
    }

    public static Location getChunkToLoc(Location location) {
        location.setX(location.getX()*16);
        location.setZ(location.getZ()*16);
        return location;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        return false;
    }
}
