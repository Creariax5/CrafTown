package elc.florian.db;

import elc.florian.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.SQLException;

public class DbManager {
    private DbConnection db1Connection;
    Plugin plugin = Bukkit.getPluginManager().getPlugin("CraftTown");

    public DbManager() {
        String host = "eu02-sql.pebblehost.com";
        String user = "customer_456749_craftown";
        String password = "84@6hvS~tbK$t4JVgMeO";
        String name = "customer_456749_craftown";
        int port = 3306;

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
