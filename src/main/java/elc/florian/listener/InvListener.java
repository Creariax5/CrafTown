package elc.florian.listener;

import elc.florian.commands.CommandMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static elc.florian.commands.CommandCity.getCity;

public class InvListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) {
            return;
        }

        if (event.getView().getTitle().equals("§5menu")){
            if (current.getItemMeta().getDisplayName().equals("§6city")) {
                player.closeInventory();
                List<String> cityList = new ArrayList<>();
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
        } else if (event.getView().getTitle().equals("§6city")){
            player.closeInventory();
        }
    }
}
