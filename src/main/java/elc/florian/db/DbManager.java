package elc.florian.db;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.SQLException;

public class DbManager {
    private DbConnection db1Connection;
    Plugin plugin = Bukkit.getPluginManager().getPlugin("CraftTown");

    public DbManager() {
        final File file = new File("plugins/CrafTown/dataBase.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = "DbManager.";
        final ConfigurationSection configurationSection = config.getConfigurationSection(key);

        assert configurationSection != null;
        String host = configurationSection.getString("host");
        String user = configurationSection.getString("user");
        String password = configurationSection.getString("password");
        String name = configurationSection.getString("name");
        int port = configurationSection.getInt("port");

        this.db1Connection = new DbConnection(new DbCred(host, user, password, name, port));
    }

    public DbConnection getDb1Connection() {
        return db1Connection;
    }

    public void close() {
        try {
            this.db1Connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
