package elc.florian.listener;

import elc.florian.commands.CommandCity;
import elc.florian.commands.CommandMarket;
import elc.florian.commands.CommandMenu;
import elc.florian.other.InfoCity;
import elc.florian.other.InfoMarket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        if (event.getView().getTitle().equals("§5market")) {
            player.closeInventory();
            Inventory invMarcket = CommandMarket.baseMarket();

            invMarcket = marketByType(invMarcket, current.getItemMeta().getDisplayName());
            player.openInventory(invMarcket);
        }
    }

    private Inventory marketByType(Inventory invMarcket, String type) {
        List<InfoMarket> listInfoMarket = CommandMarket.getInfoMarketByType(type);

        int i = 2+9;
        for (InfoMarket infoMarket : listInfoMarket) {
            invMarcket.setItem(i, CommandMenu.getAdvancedItem(infoMarket.getMaterial(), 0, infoMarket.getPrice(), infoMarket.getTaxe()));
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
