package elc.florian;

import elc.florian.commands.CommandAlert;
import elc.florian.commands.CommandAutoBridge;
import elc.florian.commands.CommandCity;
import elc.florian.db.DbManager;
import elc.florian.other.MyListener;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
    public static Main INSTANCE;
    public DbManager dbManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        dbManager = new DbManager();
        System.out.println("CraftTown started !");

        getCommand("alert").setExecutor(new CommandAlert());
        getCommand("city").setExecutor(new CommandCity(INSTANCE));
        getServer().getPluginManager().registerEvents(new MyListener(), this);
        getCommand("bridge").setExecutor(new CommandAutoBridge());
    }

    @Override
    public void onDisable() {
        this.dbManager.close();
        System.out.println("CraftTown stopped !");
    }

    public DbManager getDbManager() {
        return dbManager;
    }
}