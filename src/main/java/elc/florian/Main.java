package elc.florian;

import elc.florian.commands.CommandAlert;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("CraftTown started !");

        getCommand("alert").setExecutor(new CommandAlert());
    }
}