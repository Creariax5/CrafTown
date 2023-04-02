package elc.florian;

import elc.florian.commands.CommandAlert;
import elc.florian.commands.CommandAutoBridge;
import elc.florian.commands.CommandCity;
import elc.florian.other.MyListener;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("CraftTown started !");

        getCommand("alert").setExecutor(new CommandAlert());
        getCommand("city").setExecutor(new CommandCity());
        getServer().getPluginManager().registerEvents(new MyListener(), this);
        getCommand("bridge").setExecutor(new CommandAutoBridge());
    }

    @Override
    public void onDisable() {
        System.out.println("CraftTown stopped !");
    }
}