package elc.florian.commands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Material.*;

public class CommandAutoBridge implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // get location and location fragments
            Location loc = player.getLocation();

            loc.setY(loc.getY() - 1);
            Block block = loc.getBlock();
            block.setType(TNT);
        }

        return false;
    }
}
