package elc.florian.listener;

import elc.florian.commands.CommandCity;
import elc.florian.commands.CommandMarket;
import elc.florian.commands.CommandMenu;
import elc.florian.other.InfoCity;
import elc.florian.other.InfoMarket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static elc.florian.commands.CommandCity.getCity;
import static org.apache.commons.lang3.RegExUtils.replaceAll;

public class InvListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        cityGui(event);
        marcketGui(event);
    }

    private void marcketGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) {
            return;
        }

        List<String> types = Arrays.asList("block", "item", "food",  "tool");
        List<String> params = Arrays.asList("search", "trier", "add item");
        if (event.getView().getTitle().equals("§5market")) {
            if (types.contains(current.getItemMeta().getDisplayName())) {
                player.closeInventory();
                Inventory invMarcket = CommandMarket.baseMarket();

                marketByType(invMarcket, current.getItemMeta().getDisplayName());
                player.openInventory(invMarcket);
            } else if (params.contains(current.getItemMeta().getDisplayName())) {
                player.closeInventory();
            } else {
                player.closeInventory();
                Inventory invItem = itemActionGui(current.getItemMeta().getDisplayName());
                player.openInventory(invItem);
            }
        }
        if (event.getView().getTitle().substring(0, 6).equals("§5item")) {
            if (current.getItemMeta().getDisplayName().equals("§abuy x64")){
                player.closeInventory();
                CommandMarket.buyItem(event.getView().getTitle().substring(10), 64, player);

            } else if (current.getItemMeta().getDisplayName().equals("§abuy x1")) {
                player.closeInventory();
                CommandMarket.buyItem(event.getView().getTitle().substring(10), 1, player);

            } else if (current.getItemMeta().getDisplayName().equals("§csell x64")) {
                player.closeInventory();
                CommandMarket.sellItem(event.getView().getTitle().substring(10), 64, player);

            } else if (current.getItemMeta().getDisplayName().equals("§csell x1")) {
                player.closeInventory();
                CommandMarket.sellItem(event.getView().getTitle().substring(10), 1, player);

            } else {
                player.closeInventory();
                Inventory invItem = itemActionGui(current.getItemMeta().getDisplayName());
                player.openInventory(invItem);
            }
        }

    }

    private Inventory itemActionGui(String displayName) {
        Inventory inv = Bukkit.createInventory(null, 36, "§5item -> " + replaceAll(displayName, "§b", ""));

        inv.setItem(10, CommandMenu.getItem64(Material.LIME_CONCRETE, "§abuy x64"));
        inv.setItem(11, CommandMenu.getItem(Material.LIME_CONCRETE_POWDER, "§abuy x1"));
        inv.setItem(13, CommandMenu.getItem(Material.matchMaterial(replaceAll(displayName, "§b", "")), displayName));
        inv.setItem(15, CommandMenu.getItem(Material.RED_CONCRETE_POWDER, "§csell x1"));
        inv.setItem(16, CommandMenu.getItem64(Material.RED_CONCRETE, "§csell x64"));

        return inv;
    }

    public static Inventory marketByType(Inventory invMarcket, String type) {
        List<InfoMarket> listInfoMarket = CommandMarket.getInfoMarketByType(type);

        int i = 2+9;
        for (InfoMarket infoMarket : listInfoMarket) {
            invMarcket.setItem(i, CommandMenu.getAdvancedItem(infoMarket.getMaterial(), infoMarket.getProduct(), infoMarket.getCoin(), infoMarket.getTaxe()));
            i++;
        }
        return invMarcket;
    }

    private void cityGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) {
            return;
        }

        List<String> cityList = new ArrayList<>();
        if (event.getView().getTitle().equals("§5menu")) {
            if (current.getItemMeta().getDisplayName().equals("§6city")) {
                player.closeInventory();
                getCity(cityList);
                Inventory invCity = Bukkit.createInventory(null, 54, "§5city");

                for (int i = 0; i < cityList.size(); i++) {
                    invCity.setItem(i, CommandMenu.getPlayerHead(cityList.get(i), "cake"));
                }

                player.openInventory(invCity);

            } else if (current.getItemMeta().getDisplayName().equals("§6mes information")) {
                player.closeInventory();

                player.sendMessage(ChatColor.RED + "En dev");
            }
        } else if (event.getView().getTitle().equals("§5city")) {
            player.closeInventory();
            InfoCity infoCity = CommandCity.infoCity(current.getItemMeta().getDisplayName(), player);

            Inventory invCity = Bukkit.createInventory(null, 27, "§4habitants");

            invCity.setItem(11, CommandMenu.getPlayerHead("Nombre d'habitants: " + infoCity.getHabs_nb(), "cake"));
            invCity.setItem(12, CommandMenu.getPlayerHead("Liste des habitants de " + current.getItemMeta().getDisplayName(), "cake"));
            invCity.setItem(14, CommandMenu.getPlayerHead("Maire: " + infoCity.getMaire(), "king"));
            invCity.setItem(15, CommandMenu.getPlayerHead("Niveau: " + infoCity.getLv(), "cake"));

            player.openInventory(invCity);
        } else if (event.getView().getTitle().equals("§4habitants")) {
            player.closeInventory();
            String city = replaceAll(current.getItemMeta().getDisplayName(), "Liste des habitants de ", "");
            InfoCity infoCity = CommandCity.infoCity(city, player);
            List<String> habsList = new ArrayList<>(Arrays.asList(infoCity.getHabs().split(" ")));

            Inventory invHabs = Bukkit.createInventory(null, 54, "§4ville");

            for (int i = 1; i < habsList.size(); i++) {
                invHabs.setItem(i-1, CommandMenu.getPlayerHead(habsList.get(i), habsList.get(i)));
            }

            player.openInventory(invHabs);
        }
    }
}
