package elc.florian.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbConnection {
    private DbCred dbCred;
    private Connection connection;
    public DbConnection(DbCred dbcred) {
        this.dbCred = dbcred;
        this.connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.dbCred.toURL(), this.dbCred.getUser(), this.dbCred.getPassword());

            Logger.getLogger("Minecraft").info("db ON");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException {
        if (this.connection != null) {
            if (!this.connection.isClosed()) {
                this.connection.close();
            }
        }
    }
    public Connection getConnection() throws SQLException {
        if (this.connection != null) {
            if (!this.connection.isClosed()) {
                return this.connection;
            }
        }

        connect();
        return this.connection;
    }
}
