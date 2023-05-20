package elc.florian;

import elc.florian.commands.*;
import elc.florian.db.DbManager;
import elc.florian.listener.ChatListener;
import elc.florian.listener.InvListener;
import elc.florian.listener.JoinListener;
import elc.florian.listener.MoveListener;
import elc.florian.other.CreateYML;
import elc.florian.other.InfoCity;
import elc.florian.other.InfoMarket;
import elc.florian.other.InfoPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class Main extends JavaPlugin {
    public static Main INSTANCE;
    private DbManager dbManager;
    private HashMap<UUID, InfoPlayer> infoPlayer;
    private HashMap<String, UUID> usernameToUUID;
    private HashMap<String, InfoCity> infoCity;
    private HashMap<String, InfoMarket> infoMarket;
    private HashMap<String, List<String>> infoMarketByType;
    private List<String> cityList;

    @Override
    public void onLoad() {
        CreateYML.setFields();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        dbManager = new DbManager();
        infoPlayer = new HashMap<>();
        usernameToUUID = new HashMap<>();
        infoCity = new HashMap<>();
        infoMarket  = new HashMap<>();
        infoMarketByType  = new HashMap<>();
        cityList = new ArrayList<>();
        System.out.println("CraftTown started !");

        getCommand("alert").setExecutor(new CommandAlert());
        getCommand("city").setExecutor(new CommandCity(INSTANCE));
        getCommand("money").setExecutor(new CommandMoney(INSTANCE));
        getCommand("spawn").setExecutor(new CommandSpawn());
        getCommand("menu").setExecutor(new CommandMenu());
        getCommand("market").setExecutor(new CommandMarket(INSTANCE));
        getCommand("bridge").setExecutor(new CommandAutoBridge());
        getCommand("cosmetic").setExecutor(new CommandCosmetic());
        getCommand("terrain").setExecutor(new CommandTerrain(INSTANCE));
        getCommand("claim").setExecutor(new CommandClaim(INSTANCE));
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InvListener(), this);
    }

    @Override
    public void onDisable() {
        this.dbManager.close();
        System.out.println("CraftTown stopped !");
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public HashMap<UUID, InfoPlayer> getInfoPlayer() {
        return infoPlayer;
    }
    public HashMap<String, UUID> getUsernameToUUID() {
        return usernameToUUID;
    }
    public HashMap<String, InfoCity> getInfoCity() {
        return infoCity;
    }
    public HashMap<String, InfoMarket> getInfoMarket() {
        return infoMarket;
    }
    public HashMap<String, List<String>> getInfoMarketByType() {
        return infoMarketByType;
    }

    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }
    public static Main getINSTANCE() {
        return INSTANCE;
    }
}