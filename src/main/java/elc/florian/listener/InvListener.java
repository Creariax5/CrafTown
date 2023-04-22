package elc.florian.listener;

import elc.florian.commands.CommandCity;
import elc.florian.commands.CommandMarket;
import elc.florian.commands.CommandMenu;
import elc.florian.other.InfoCity;
import elc.florian.other.InfoMarket;
import elc.florian.other.InfoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static elc.florian.commands.CommandMenu.getItem;
import static elc.florian.commands.CommandMenu.getPlayerHead;
import static org.apache.commons.lang3.RegExUtils.replaceAll;

public class InvListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        menuGui(event);
        cityGui(event);
        marcketGui(event);
        playerGui(event);
    }

    private void playerGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        assert current != null;
        if (Objects.requireNonNull(current.getItemMeta()).getDisplayName().equals("back to menu")){
            player.closeInventory();
            CommandMenu.openMenu(player);
        } else if (event.getView().getTitle().equals("§4info perso")) {
            String[] prefix = Objects.requireNonNull(current.getItemMeta()).getDisplayName().split(": ");
            String invOwner = Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(13)).getItemMeta()).getDisplayName();
            if (invOwner.equals(player.getDisplayName())) {
                if (prefix[0].equals("Ville")) {
                    player.closeInventory();
                    manageMyCityGui(prefix[1], player);
                } else {
                    event.setCancelled(true);
                    return;
                }
            } else {
                event.setCancelled(true);
                return;
            }
        } else if (event.getView().getTitle().equals("§5gerer ma ville")) {
            String[] prefix = Objects.requireNonNull(current.getItemMeta()).getDisplayName().split(" ");
            if (prefix[0].equals("Quitter")) {
                player.closeInventory();
                CommandCity.leaveCity(player);



            } else if (prefix[0].equals("Rejoindre")) {
                cityToJoinGui(player);
            } else {
                event.setCancelled(true);
                return;
            }
        } else if (event.getView().getTitle().equals("§5Choisir une ville a rejoindre")) {
            String item = Objects.requireNonNull(current.getItemMeta()).getDisplayName();
            if (item.equals(" ")) {
                event.setCancelled(true);
                return;

            } else if (item.equals("back to menu")) {
                player.closeInventory();
                CommandMenu.openMenu(player);

            } else {
                player.closeInventory();
                String city = current.getItemMeta().getDisplayName();
                CommandCity.joinCity(city, player);

                openCityInfoGui(player, city);
            }
        }
    }

    private void cityToJoinGui(Player player) {
        player.closeInventory();

        List<String> cityList = new ArrayList<>();
        CommandCity.getCity(cityList);
        Inventory invCity = Bukkit.createInventory(null, 54, "§5Choisir une ville a rejoindre");
        String color = "GREEN";
        roundGui(invCity, color);

        for (int i = 10; i < cityList.size() + 10; i++) {
            invCity.setItem(i, getPlayerHead(cityList.get(i - 10), "cake"));
        }

        invCity.setItem(49, CommandMenu.getItem(Material.ARROW, "back"));

        player.openInventory(invCity);
    }

    private void manageMyCityGui(String city, Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§5gerer ma ville");

        if (city.equals("rural")) {
            inv.setItem(13, getItem(Material.LIME_CONCRETE, "Rejoindre une ville"));

        } else {

            inv.setItem(11, getItem(Material.RED_CONCRETE, "Quitter " + city));
            inv.setItem(13, getPlayerHead("Ma ville: " + city, "cake"));
            inv.setItem(15, getPlayerHead("§6ma reputation a " + city, player.getName()));
        }


        player.openInventory(inv);
    }

    private void menuGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) {
            return;
        }

        if (event.getView().getTitle().equals("§5menu")) {
            if (Objects.requireNonNull(current.getItemMeta()).getDisplayName().equals("§6city")) {
                listCityGui(player);

            } else if (current.getItemMeta().getDisplayName().equals("§6market")) {
                player.closeInventory();
                Inventory inv = CommandMarket.baseMarket();
                InvListener.marketByType(inv, "block");
                player.openInventory(inv);

            } else if (current.getItemMeta().getDisplayName().equals("§6mes information")) {
                player.closeInventory();

                player.openInventory(invPerso(player.getUniqueId(), player.getDisplayName()));
            }
        }
    }

    private void listCityGui(Player player) {
        player.closeInventory();
        List<String> cityList = new ArrayList<>();

        CommandCity.getCity(cityList);
        Inventory invCity = Bukkit.createInventory(null, 54, "§5city");

        String color = "GREEN";

        roundGui(invCity, color);

        for (int i = 10; i < cityList.size() + 10; i++) {
            invCity.setItem(i, getPlayerHead(cityList.get(i - 10), "cake"));
        }

        invCity.setItem(49, CommandMenu.getItem(Material.ARROW, "back to menu"));

        player.openInventory(invCity);
    }

    private Inventory invPerso(UUID uuid, String name) {
        InfoPlayer infoPlayer = CommandMarket.getInfoPlayerAuto(uuid);

        Inventory invPerso = Bukkit.createInventory(null, 27, "§4info perso");


        invPerso.setItem(10, getPlayerHead("Grade: " + infoPlayer.getGrade(), "cake"));
        invPerso.setItem(11, getPlayerHead("Ville: " + infoPlayer.getVille(), "cake"));
        invPerso.setItem(4, getPlayerHead("Niveau: " + infoPlayer.getLv(), "cake"));
        invPerso.setItem(13, getPlayerHead(name, name));
        invPerso.setItem(22, CommandMenu.getItem(Material.ARROW, "back to menu"));
        invPerso.setItem(15, getPlayerHead("Travail: " + infoPlayer.getTravail(), "cake"));
        invPerso.setItem(16, getPlayerHead("Money: " + infoPlayer.getMoney(), "cake"));
        return invPerso;
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
            if (types.contains(Objects.requireNonNull(current.getItemMeta()).getDisplayName())) {
                player.closeInventory();
                Inventory invMarcket = CommandMarket.baseMarket();

                marketByType(invMarcket, current.getItemMeta().getDisplayName());
                player.openInventory(invMarcket);

            } else if (params.contains(current.getItemMeta().getDisplayName())) {
                player.closeInventory();

            } else if (current.getItemMeta().getDisplayName().equals("back to menu")) {
                player.closeInventory();
                CommandMenu.openMenu(player);

            } else if (current.getItemMeta().getDisplayName().equals(" ")) {
                event.setCancelled(true);
                return;

            } else {
                player.closeInventory();
                Inventory invItem = itemActionGui(current.getItemMeta().getDisplayName());
                player.openInventory(invItem);
            }
        }
        if (event.getView().getTitle().startsWith("§5item")) {
            String item = event.getView().getTitle().substring(10);
            if (Objects.requireNonNull(current.getItemMeta()).getDisplayName().equals("§abuy x64")){
                CommandMarket.buyItem(item, 64, player);
                Inventory invItem = itemActionGui(item);
                player.closeInventory();
                player.openInventory(invItem);

            } else if (current.getItemMeta().getDisplayName().equals("§abuy x1")) {
                CommandMarket.buyItem(item, 1, player);
                Inventory invItem = itemActionGui(item);
                player.closeInventory();
                player.openInventory(invItem);

            } else if (current.getItemMeta().getDisplayName().equals("§csell x64")) {
                CommandMarket.sellItem(item, 64, player);
                Inventory invItem = itemActionGui(item);
                player.closeInventory();
                player.openInventory(invItem);

            } else if (current.getItemMeta().getDisplayName().equals("§csell x1")) {
                CommandMarket.sellItem(item, 1, player);
                Inventory invItem = itemActionGui(item);
                player.closeInventory();
                player.openInventory(invItem);

            } else if (current.getItemMeta().getDisplayName().equals("back")) {
                player.closeInventory();
                Inventory inv = CommandMarket.baseMarket();
                InvListener.marketByType(inv, "block");
                player.openInventory(inv);
            } else {
                Inventory invItem = itemActionGui(current.getItemMeta().getDisplayName());
                player.closeInventory();
                player.openInventory(invItem);
            }
        }

    }

    private Inventory itemActionGui(String displayName) {
        String name = replaceAll(displayName, "§b", "");
        InfoMarket infoMarket = CommandMarket.getInfoMarketByName(name);
        Inventory inv = Bukkit.createInventory(null, 36, "§5item -> " + name);
        int product = infoMarket.getProduct();
        float coin = infoMarket.getCoin();
        float price = coin / product;

        inv.setItem(10, CommandMenu.getBuyGui("LIME_CONCRETE", "§abuy x64", 64, price));
        inv.setItem(11, CommandMenu.getBuyGui("LIME_CONCRETE_POWDER", "§abuy x1", 1, price));
        inv.setItem(13, CommandMenu.getAdvancedItem(infoMarket.getMaterial(), product, coin, infoMarket.getTaxe()));
        inv.setItem(15, CommandMenu.getSellGui("RED_CONCRETE_POWDER", "§csell x1", 1, price));
        inv.setItem(16, CommandMenu.getSellGui("RED_CONCRETE", "§csell x64", 64, price));
        inv.setItem(31, CommandMenu.getItem(Material.ARROW, "back"));

        return inv;
    }

    public static Inventory marketByType(Inventory invMarcket, String type) {
        List<InfoMarket> listInfoMarket = CommandMarket.getInfoMarketByType(type);

        int j = 2+9;
        for (InfoMarket infoMarket : listInfoMarket) {
            invMarcket.setItem(j, CommandMenu.getAdvancedItem(infoMarket.getMaterial(), infoMarket.getProduct(), infoMarket.getCoin(), infoMarket.getTaxe()));
            j++;
        }

        String color = null;
        if (Objects.equals(type, "block")) {
            color = "YELLOW";
        } else if (Objects.equals(type, "item")) {
            color = "LIME";
        } else if (Objects.equals(type, "food")) {
            color = "RED";
        } else if (Objects.equals(type, "tool")) {
            color = "BLUE";
        }

        for (int i = 1; i < 9; i++) {
            invMarcket.setItem(i, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
        }
        for (int i = 10; i < 54; i = i+9) {
            invMarcket.setItem(i, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
            invMarcket.setItem(i + 7, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
        }
        invMarcket.setItem(47, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
        invMarcket.setItem(48, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
        invMarcket.setItem(51, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));

        return invMarcket;
    }

    private void cityGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) {
            return;
        }

        if (event.getView().getTitle().equals("§5city")) {
            if (Objects.requireNonNull(current.getItemMeta()).getDisplayName().equals(" ")) {
                event.setCancelled(true);
                return;

            } else if (current.getItemMeta().getDisplayName().equals("back to menu")) {
                player.closeInventory();
                CommandMenu.openMenu(player);

            } else {
                player.closeInventory();
                String city = current.getItemMeta().getDisplayName();
                openCityInfoGui(player, city);
            }

        } else if (event.getView().getTitle().equals("§4habitants")) {
            if (current.getItemMeta().getDisplayName().equals("retourner a la liste des villes")) {
                player.closeInventory();
                listCityGui(player);

            } else {

                try {
                    String prefix = current.getItemMeta().getDisplayName().substring(0,23);
                    if (prefix.equals("Liste des habitants de ")) {

                        player.closeInventory();

                        String city = replaceAll(Objects.requireNonNull(current.getItemMeta()).getDisplayName(), "Liste des habitants de ", "");
                        InfoCity infoCity = CommandCity.getInfoCityAuto(city);
                        player.sendMessage(infoCity.getHabs());
                        assert infoCity != null;
                        List<String> habsList = new ArrayList<>(Arrays.asList(infoCity.getHabs().split(" ")));

                        Inventory invHabs = Bukkit.createInventory(null, 54, "§4ville");

                        String color = "RED";

                        roundGui(invHabs, color);

                        for (int i = 1; i < habsList.size(); i++) {
                            invHabs.setItem(i+9, getPlayerHead(habsList.get(i), habsList.get(i)));
                        }

                        invHabs.setItem(49, CommandMenu.getItem(Material.ARROW, "retourner a info de " + city));

                        player.openInventory(invHabs);
                    } else {
                        event.setCancelled(true);
                        return;
                    }
                } catch (Exception e) {
                    event.setCancelled(true);
                    return;
                }
            }

        } else if (event.getView().getTitle().equals("§4ville")) {
            try {
                String prefix = current.getItemMeta().getDisplayName().substring(0,20);
                String city = current.getItemMeta().getDisplayName().substring(20);
                System.out.println(prefix +"_________"+city);
                if (prefix.equals("retourner a info de ")) {
                    player.closeInventory();
                    openCityInfoGui(player, city);

                }
            } catch (Exception e) {
                player.closeInventory();

                UUID uuid = CommandMarket.getUUIDwithUsername(Objects.requireNonNull(current.getItemMeta()).getDisplayName());
                player.openInventory(invPerso(uuid, current.getItemMeta().getDisplayName()));
            }
        }
    }

    private void openCityInfoGui(Player player, String city) {
        InfoCity infoCity = CommandCity.getInfoCityAuto(city);

        Inventory invCity = Bukkit.createInventory(null, 27, "§4habitants");

        assert infoCity != null;
        String maire = infoCity.getMaire();
        if (maire.equals("")) {
            maire = "Pas de maire";
        }


        invCity.setItem(11, getPlayerHead("Nombre d'habitants: " + infoCity.getHabs_nb(), "cake"));
        invCity.setItem(12, getPlayerHead("Liste des habitants de " + city, "cake"));
        invCity.setItem(14, getPlayerHead("Maire: " + maire, "king"));
        invCity.setItem(15, getPlayerHead("Niveau: " + infoCity.getLv(), "cake"));

        invCity.setItem(22, CommandMenu.getItem(Material.ARROW, "retourner a la liste des villes"));

        player.openInventory(invCity);
    }

    private Inventory roundGui(Inventory inv, String color) {
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
        }
        for (int i = 9; i < 54; i = i+9) {
            inv.setItem(i, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
            inv.setItem(i + 8, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
        }
        for (int i = 53-9; i < 53; i++) {
            inv.setItem(i, CommandMenu.getItem(Objects.requireNonNull(Material.matchMaterial(color + "_STAINED_GLASS_PANE")), " "));
        }
        return inv;
    }
}
