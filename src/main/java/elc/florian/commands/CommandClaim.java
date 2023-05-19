package elc.florian.commands;

import elc.florian.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static elc.florian.commands.CommandMenu.getItemWithLore;

public class CommandClaim implements CommandExecutor {
    private static Main main;

    public CommandClaim(Main main) {
        CommandClaim.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            Inventory inv = Bukkit.createInventory(null, 27, "claim");
            inv.setItem(11, getItemWithLore(Material.LIME_CONCRETE, "§aoui", "§2Acheter et claim le chunk sur le quel vous vous situez"));
            inv.setItem(13, getItemWithLore(Material.LIGHT_BLUE_CONCRETE, "§bvisualiser", "§9Je veux juste visualiser et avoir des infos sur ce terrain"));
            inv.setItem(15, getItemWithLore(Material.RED_CONCRETE, "§cnon", "§4Abandonner l'achat"));

            player.openInventory(inv);
        }
        return false;
    }

    public static void claimInv(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        assert current != null;
        if (event.getView().getTitle().equals("claim")) {
            if (Objects.requireNonNull(current.getItemMeta()).getDisplayName().equals("§aoui")) {
                player.closeInventory();
                player.sendMessage("claimed");

                Location location = player.getLocation();
                CommandTerrain.getChunk(location);
                CommandTerrain.getChunkToLoc(location);
                double locY = location.getY();

                chunkParticles(location, player, Color.LIME, locY);


            } else if (Objects.requireNonNull(current.getItemMeta()).getDisplayName().equals("§bvisualiser")) {
                player.closeInventory();
                player.sendMessage("§bInfos:");

                Location location = player.getLocation();
                CommandTerrain.getChunk(location);
                CommandTerrain.getChunkToLoc(location);
                double locY = location.getY();

                chunkParticles(location, player, Color.AQUA, locY);
            } else {
                player.closeInventory();
                player.sendMessage("canceled");
            }
        }
    }

    private static void chunkParticles(Location location, Player player, Color color, double locY) {
        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                Particle.DustOptions dustOptions = new Particle.DustOptions(color, 4.0F);

                for (int i = 0; i < 16; i++) {
                    location.setX(location.getX() + 1);
                    location.setY(getYParticle(location, locY));

                    player.spawnParticle(Particle.REDSTONE, location, 1, dustOptions);

                }
                for (int i = 0; i < 16; i++) {
                    location.setZ(location.getZ() + 1);
                    location.setY(getYParticle(location, locY));

                    player.spawnParticle(Particle.REDSTONE, location, 1, dustOptions);
                }
                for (int i = 0; i < 16; i++) {
                    location.setX(location.getX() - 1);
                    location.setY(getYParticle(location, locY));

                    player.spawnParticle(Particle.REDSTONE, location, 1, dustOptions);

                }
                for (int i = 0; i < 16; i++) {
                    location.setZ(location.getZ() - 1);
                    location.setY(getYParticle(location, locY));

                    player.spawnParticle(Particle.REDSTONE, location, 1, dustOptions);
                }
                i[0]++;
                if(i[0] == 50) {
                    this.cancel();
                }
            }

            private double getYParticle(Location location, double locY) {
                int i = 0;
                location.setY(locY);
                Material block = location.getBlock().getType();
                while (block != Material.AIR) {
                    location.setY(location.getY() + 1);
                    block = location.getBlock().getType();
                    i++;
                }
                return location.getY();
            }


        }.runTaskTimerAsynchronously(Main.getINSTANCE(), 0,15);
    }
}
