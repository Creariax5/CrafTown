package elc.florian;

import elc.florian.commands.*;
import elc.florian.db.DbManager;
import elc.florian.listener.ChatListener;
import elc.florian.other.InfoPlayer;
import elc.florian.listener.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public class Main extends JavaPlugin {
    public static Main INSTANCE;
    private DbManager dbManager;
    private HashMap<UUID, InfoPlayer> info_player;

    @Override
    public void onEnable() {
        INSTANCE = this;
        dbManager = new DbManager();
        info_player = new HashMap<>();
        System.out.println("CraftTown started !");

        getCommand("alert").setExecutor(new CommandAlert());
        getCommand("city").setExecutor(new CommandCity(INSTANCE));
        getCommand("money").setExecutor(new CommandMoney(INSTANCE));
        getCommand("spawn").setExecutor(new CommandSpawn(INSTANCE));
        getCommand("menu").setExecutor(new CommandMoney(INSTANCE));
        getCommand("bridge").setExecutor(new CommandAutoBridge());
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        this.dbManager.close();
        System.out.println("CraftTown stopped !");
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public HashMap<UUID, InfoPlayer> getInfo_player() {
        return info_player;
    }
}