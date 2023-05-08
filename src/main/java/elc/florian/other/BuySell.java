package elc.florian.other;

import elc.florian.Main;
import elc.florian.commands.CommandMarket;
import elc.florian.db.DbConnection;
import elc.florian.listener.JoinListener;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BuySell {
    public static void buyItem(String item, int amount, Player player) {
        Main main = Main.INSTANCE;

        HashMap<Integer, ItemStack> hashMap = player.getInventory().addItem(new ItemStack(Objects.requireNonNull(Material.matchMaterial(item)), amount));
        if (hashMap.get(0) != null) {
            amount = amount - hashMap.get(0).getAmount();
            player.getInventory().removeItem(new ItemStack(Objects.requireNonNull(Material.matchMaterial(item)), amount));
            player.sendMessage(ChatColor.RED + "Il n'y a pas assez de place dans votre inventaire, " + amount + " " + item + " seront achetés");
        } else {
            player.getInventory().removeItem(new ItemStack(Objects.requireNonNull(Material.matchMaterial(item)), amount));
        }

        UUID uuid = player.getUniqueId();

        InfoPlayer infoPlayer = getInfoPlayerAuto(uuid);
        InfoMarket infoMarket = CommandMarket.getInfoMarketByName(item);

        float toPay;
        float money = infoPlayer.getMoney();
        int product = infoMarket.getProduct();
        float coin = infoMarket.getCoin();
        float c1 = coin;

        for (int i = 0; i < amount; i++) {
            toPay = coin / product;
            product = product - 1;
            coin = coin + toPay;
        }
        toPay = coin / product;
        coin = coin + toPay/2;

        toPay = coin-c1;

        if (money >= toPay) {
            if (0 <= product) {
                money = money - toPay;

                InfoPlayer infoPlayer1 = new InfoPlayer(player.getUniqueId(), infoPlayer.getUsername(), infoPlayer.getGrade(), infoPlayer.getVille(), infoPlayer.getLv(), infoPlayer.getTravail(), money);
                main.getInfoPlayer().put(player.getUniqueId(), infoPlayer1);
                main.getUsernameToUUID().put(infoPlayer1.getUsername(), uuid);

                InfoMarket infoMarket1 = new InfoMarket(item, infoMarket.getType(), toPay, infoMarket.getTaxe(), product, coin);
                main.getInfoMarket().put(item, infoMarket1);

                final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                try {
                    final Connection connection = db1Connection.getConnection();

                    final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE market SET price = ?, product = ?, coin = ?, updated_at = ? WHERE material = ?");
                    final long time = System.currentTimeMillis();

                    preparedStatement2.setFloat(1, toPay);
                    preparedStatement2.setInt(2, product);
                    preparedStatement2.setFloat(3, coin);
                    preparedStatement2.setTimestamp(4, new Timestamp(time));
                    preparedStatement2.setString(5, item);

                    preparedStatement2.executeUpdate();

                    final PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE player SET money = ?, updated_at = ? WHERE uuid = ?");

                    preparedStatement3.setFloat(1, money);
                    preparedStatement3.setTimestamp(2, new Timestamp(time));
                    preparedStatement3.setString(3, uuid.toString());

                    preparedStatement3.executeUpdate();


                    player.getInventory().addItem(new ItemStack(Objects.requireNonNull(Material.matchMaterial(item)), amount));
                    player.updateInventory();
                    DecimalFormat df = new DecimalFormat("0.00");

                    TextComponent message = new TextComponent(ChatColor.GREEN + "Achat effectué " + amount + " " + item + " acheté pour " + df.format(toPay) + " coins");
                    //Item itemJ = new Item(Material.getMaterial(item).getKey().getKey(), amount, ItemTag.ofNbt("JSON"));
                    //message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemJ));
                    player.spigot().sendMessage(message);

                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "ERROR");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Pas assez de produits a vendre, produits disponibles: " + product + ", quantité de votre achat: " + amount);
            }
        } else {
            player.sendMessage(ChatColor.RED + "pas assez d'argent, vous avez: " + money + " coins, et le total de votre achat est de " + toPay + " coins");
        }
    }

    public static void sellItem(String item, int amount, Player player) {
        Main main = Main.INSTANCE;
        UUID uuid = player.getUniqueId();

        InfoPlayer infoPlayer = getInfoPlayerAuto(uuid);
        InfoMarket infoMarket = CommandMarket.getInfoMarketByName(item);
        float toPay;
        float money = infoPlayer.getMoney();
        int product = infoMarket.getProduct();
        float coin = infoMarket.getCoin();
        float c1 = coin;

        for (int i = 0; i < amount; i++) {
            toPay = coin / product;
            product = product + 1;
            coin = coin - toPay;
        }
        toPay = coin / product;
        coin = coin + toPay/2;

        toPay = -(coin-c1);

        if (player.getInventory().contains(Material.valueOf(item), amount)) {
            if (coin >= 0) {
                money = money + toPay;

                InfoPlayer infoPlayer1 = new InfoPlayer(player.getUniqueId(), infoPlayer.getUsername(), infoPlayer.getGrade(), infoPlayer.getVille(), infoPlayer.getLv(), infoPlayer.getTravail(), money);
                main.getInfoPlayer().put(player.getUniqueId(), infoPlayer1);
                main.getUsernameToUUID().put(infoPlayer1.getUsername(), uuid);

                InfoMarket infoMarket1 = new InfoMarket(item, infoMarket.getType(), toPay, infoMarket.getTaxe(), product, coin);
                main.getInfoMarket().put(item, infoMarket1);

                final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                try {
                    final Connection connection = db1Connection.getConnection();

                    final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE market SET price = ?, product = ?, coin = ?, updated_at = ? WHERE material = ?");
                    final long time = System.currentTimeMillis();

                    preparedStatement2.setFloat(1, toPay);
                    preparedStatement2.setInt(2, product);
                    preparedStatement2.setFloat(3, coin);
                    preparedStatement2.setTimestamp(4, new Timestamp(time));
                    preparedStatement2.setString(5, item);

                    preparedStatement2.executeUpdate();

                    final PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE player SET money = ?, updated_at = ? WHERE uuid = ?");

                    preparedStatement3.setFloat(1, money);
                    preparedStatement3.setTimestamp(2, new Timestamp(time));
                    preparedStatement3.setString(3, uuid.toString());

                    preparedStatement3.executeUpdate();


                    player.getInventory().removeItem(new ItemStack(Objects.requireNonNull(Material.matchMaterial(item)), amount));
                    player.updateInventory();
                    DecimalFormat df = new DecimalFormat("0.00");
                    player.sendMessage(ChatColor.GREEN + "Vente effectué " + amount + " " + item + " vendu pour " + df.format(toPay) + " coins");

                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "ERROR");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Pas assez de produits a vendre, produits disponibles: " + product + ", quantité de votre achat: " + amount);
            }
        } else {
            player.sendMessage(ChatColor.RED + "pas assez d'argent, vous avez: " + money + " coins, et le total de votre achat est de " + toPay + " coins");
        }
    }

    public static InfoPlayer getInfoPlayerAuto(UUID uuid) {
        Main main = Main.INSTANCE;
        if (!main.getInfoPlayer().containsKey(uuid)) {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();

            final Connection connection;
            try {
                connection = db1Connection.getConnection();
                InfoPlayer infoPlayer = JoinListener.getPlayer(connection, uuid);
                main.getInfoPlayer().put(uuid, infoPlayer);
                main.getUsernameToUUID().put(infoPlayer.getUsername(), uuid);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return main.getInfoPlayer().get(uuid);
    }

    public static UUID getUUIDwithUsername(String displayName) {
        Main main = Main.INSTANCE;
        if (!main.getUsernameToUUID().containsKey(displayName)) {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();

            final Connection connection;
            try {
                connection = db1Connection.getConnection();
                InfoPlayer info_player = getOfflinePlayerByName(connection, displayName);
                main.getInfoPlayer().put(info_player.getUuid(), info_player);
                main.getUsernameToUUID().put(displayName, info_player.getUuid());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return main.getUsernameToUUID().get(displayName);
    }

    private static InfoPlayer getOfflinePlayerByName(Connection connection, String displayName) {
        String uuid;
        String grade;
        String ville;
        int lv;
        String travail;
        int money;


        InfoPlayer info_player = null;
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid, grade, ville, lv, travail, money FROM player WHERE pseudo = ?");

            preparedStatement.setString(1, displayName);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                uuid = (resultSet.getString(1));
                grade = (resultSet.getString(2));
                ville = (resultSet.getString(3));
                lv = (resultSet.getInt(4));
                travail = (resultSet.getString(5));
                money = (resultSet.getInt(6));

                info_player = new InfoPlayer(UUID.fromString(uuid), displayName, grade, ville, lv, travail, money);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info_player;
    }
}
