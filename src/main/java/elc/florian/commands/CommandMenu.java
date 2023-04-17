package elc.florian.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandMenu implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        Player player = (Player) sender;
        openMenu(player);
        return false;
    }

    public static void openMenu(Player player) {

        Inventory inv = Bukkit.createInventory(null, 27, "§5menu");

        inv.setItem(11, getPlayerHead("§6city", "Dipicrylamine"));
        inv.setItem(13, getPlayerHead("§6market", "HotdogParmesan"));
        inv.setItem(15, getPlayerHead("§6mes information", player.getName()));

        player.openInventory(inv);
    }

    public static ItemStack getItem(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName(displayName);
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }

    public static ItemStack getAdvancedItem(String material, int product, float coin, float taxe) {

        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)));
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName("§b" + material);
        float price = coin / product;
        itemStackM.setLore(Arrays.asList("§8" + product + " units", "", "§7buy: §6" + price + " coins", "§7sell: §6" + price + " coins", "", "§eClic to view product"));
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }

    public static ItemStack getPlayerHead(String displayName, String playerName) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        assert skullMeta != null;
        skullMeta.setOwner(playerName);
        itemStack.setItemMeta(skullMeta);
        ItemMeta itemStackM = itemStack.getItemMeta();
        itemStackM.setDisplayName(displayName);
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }

    public static ItemStack getBuyGui(String material, String name, int amount, float price) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)));
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName(name);
        itemStackM.setLore(List.of("§7buy for: §6" + price * amount + " coins"));
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }

    public static ItemStack getSellGui(String material, String name, int amount, float price) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)));
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName(name);
        itemStackM.setLore(List.of("§7sell for: §6" + price * amount + " coins"));
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }
}
