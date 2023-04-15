package elc.florian.commands;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import elc.florian.other.InfoMarket;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandMarket implements CommandExecutor {
    static Main main;
    public CommandMarket(Main main) {
        CommandMarket.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            Inventory inv = baseMarket();

            player.openInventory(inv);
        } else {
            if (Objects.equals(args[0], "additem")) {
                final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                try {
                    final Connection connection = db1Connection.getConnection();
                    createItem(connection, args[1], args[2]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void createItem(Connection connection, String material, String type) {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO market VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            final Long time = System.currentTimeMillis();

            preparedStatement.setString(1, material);
            preparedStatement.setString(2, type);
            preparedStatement.setFloat(3, 0);
            preparedStatement.setFloat(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.setTimestamp(7, new Timestamp(time));
            preparedStatement.setTimestamp(8, new Timestamp(time));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Inventory baseMarket() {
        Inventory inv = Bukkit.createInventory(null, 54, "§5market");

        inv.setItem(0, CommandMenu.getItem(Material.OAK_LOG, "block"));
        inv.setItem(9, CommandMenu.getItem(Material.COAL, "item"));
        inv.setItem(18, CommandMenu.getItem(Material.APPLE, "food"));
        inv.setItem(27, CommandMenu.getItem(Material.WOODEN_SHOVEL, "tool"));
        inv.setItem(45, CommandMenu.getItem(Material.OAK_SIGN, "search"));
        inv.setItem(50, CommandMenu.getItem(Material.HOPPER, "trier"));
        inv.setItem(52, CommandMenu.getItem(Material.WRITABLE_BOOK, "add item"));

        return inv;
    }

    public static List<InfoMarket> getInfoMarketByType(String type) {
        List<InfoMarket> listInfoMarket;
        if (!main.getInfoMarketByType().containsKey(type)) {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();

            final Connection connection;
            try {
                connection = db1Connection.getConnection();

                String material;
                float price;
                float taxe;
                int buy;
                int sell;


                InfoMarket infoMarket;
                listInfoMarket = null;
                try {
                    final PreparedStatement preparedStatement = connection.prepareStatement("SELECT material, price, taxe, buy, sell FROM market WHERE type = ?");

                    preparedStatement.setString(1, type);
                    final ResultSet resultSet = preparedStatement.executeQuery();

                    listInfoMarket = new ArrayList<>();

                    while (resultSet.next()) {
                        material = (resultSet.getString(1));
                        price = (resultSet.getFloat(2));
                        taxe = (resultSet.getFloat(3));
                        buy = (resultSet.getInt(4));
                        sell = (resultSet.getInt(5));

                        infoMarket = new InfoMarket(material, type, price, taxe, buy, sell);
                        listInfoMarket.add(infoMarket);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                List<String> myList = new ArrayList<>();
                assert listInfoMarket != null;
                for (InfoMarket marketItem : listInfoMarket) {
                    main.getInfoMarket().put(marketItem.getMaterial(), marketItem);
                    myList.add(marketItem.getMaterial());
                }
                main.getInfoMarketByType().put(type, myList);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        List<String> myList = main.getInfoMarketByType().get(type);
        listInfoMarket = new ArrayList<InfoMarket>();
        for (String material : myList) {
            listInfoMarket.add(main.getInfoMarket().get(material));
        }

        return listInfoMarket;
    }

    public InfoMarket getInfoMarketByName(String material) {
        if (!main.getInfoMarket().containsKey(material)) {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();

            final Connection connection;
            try {
                connection = db1Connection.getConnection();

                String type;
                float price;
                float taxe;
                int buy;
                int sell;


                InfoMarket infoMarket = null;
                try {
                    final PreparedStatement preparedStatement = connection.prepareStatement("SELECT type, price, taxe, buy, sell FROM market WHERE material = ?");

                    preparedStatement.setString(1, material);
                    final ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        type = (resultSet.getString(1));
                        price = (resultSet.getFloat(2));
                        taxe = (resultSet.getFloat(3));
                        buy = (resultSet.getInt(4));
                        sell = (resultSet.getInt(5));

                        infoMarket = new InfoMarket(material, type, price, taxe, buy, sell);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                main.getInfoMarket().put(material, infoMarket);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return main.getInfoMarket().get(material);
    }
}
