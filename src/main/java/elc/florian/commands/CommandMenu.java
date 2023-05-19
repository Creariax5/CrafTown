package elc.florian.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;

public class CommandMenu implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        Player player = (Player) sender;
        openMenu(player);
        return false;
    }

    public static void openMenu(Player player) {
        String shopURL = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFiZjZhODM4NjYxZTE1ZGY2NjE5OTA5NDE2NWI3NTM4MzJjNzIzNDcxZDJiMjA0ZDRkNTA3NGFhNjA4Yzc4NCJ9fX0=";

        Inventory inv = Bukkit.createInventory(null, 27, "§5menu");

        inv.setItem(11, getPlayerHead("§6city", "Dipicrylamine"));
        inv.setItem(13, getSkull(shopURL, "§6market"));
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

    public static ItemStack getItemWithLore(Material material, String displayName, String lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName(displayName);
        itemStackM.setLore(Collections.singletonList(lore));
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }

    public static ItemStack getAdvancedItem(String material, int product, float coin, float taxe) {

        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)));
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName("§b" + material);
        float price = coin / product;
        DecimalFormat df = new DecimalFormat("0.00");
        itemStackM.setLore(Arrays.asList("§8" + product + " units", "", "§7buy: §6" + df.format(price) + " coins", "§7sell: §6" + df.format(price) + " coins", "", "§eClic to view product"));
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

    public static ItemStack getSkull(String url, String displayName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) {
            return head;
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {

        }
        head.setItemMeta(headMeta);

        ItemMeta itemStackM = head.getItemMeta();
        itemStackM.setDisplayName(displayName);
        head.setItemMeta(itemStackM);
        return head;
    }


    public static ItemStack getBuyGui(String material, String name, int amount, float product, float coin) {
        DecimalFormat df = new DecimalFormat("0.00");
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)));
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName(name);
        float price;
        float c1 = coin;

        for (int i = 0; i < amount; i++) {
            price = coin / product;
            product = product - 1;
            coin = coin + price;
        }
        price = coin / product;
        coin = coin + price/2;

        price = coin-c1;

        itemStackM.setLore(List.of("§7buy for: §6" + df.format(price) + " coins"));
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }

    public static ItemStack getSellGui(String material, String name, int amount, float product, float coin) {
        DecimalFormat df = new DecimalFormat("0.00");
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)));
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName(name);
        float price;
        float c1 = coin;
        for (int i = 0; i < amount; i++) {
            price = coin / product;
            product = product + 1;
            coin = coin - price;
        }
        price = coin / product;
        coin = coin + price/2;
        price = -(coin-c1);

        itemStackM.setLore(List.of("§7sell for: §6" + df.format(price) + " coins"));
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }
}
