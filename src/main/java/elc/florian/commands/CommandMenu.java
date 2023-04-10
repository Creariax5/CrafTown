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

public class CommandMenu implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        Player player = (Player) sender;
        Inventory inv = Bukkit.createInventory(null, 27, "§5menu");

        inv.setItem(11, getPlayerHead("§6city", "Dipicrylamine"));
        inv.setItem(15, getPlayerHead("§6mes information", sender.getName()));

        player.openInventory(inv);

        return false;
    }

    public ItemStack getItem(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemStackM = itemStack.getItemMeta();
        itemStackM.setDisplayName(displayName);
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }

    public static ItemStack getPlayerHead(String displayName, String playerName) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(playerName);
        itemStack.setItemMeta(skullMeta);
        ItemMeta itemStackM = itemStack.getItemMeta();
        itemStackM.setDisplayName(displayName);
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }
}
