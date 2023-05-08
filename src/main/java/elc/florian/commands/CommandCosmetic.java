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

public class CommandCosmetic implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        Inventory inv = baseCosmetic();

        player.openInventory(inv);
        return false;
    }

    private Inventory baseCosmetic() {
        Inventory inv = Bukkit.createInventory(null, 54, "§5cosmetic");

        inv.setItem(0, ItemStackCustomData(Material.GOLDEN_SWORD, "cheated", 1));
        inv.setItem(1, ItemStackCustomData(Material.ELYTRA, "demon", 1));

        return inv;
    }

    public static ItemStack ItemStackCustomData(Material material, String displayName, int data) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemStackM = itemStack.getItemMeta();
        assert itemStackM != null;
        itemStackM.setDisplayName(displayName);
        itemStackM.setCustomModelData(data);
        itemStack.setItemMeta(itemStackM);
        return itemStack;
    }
}
