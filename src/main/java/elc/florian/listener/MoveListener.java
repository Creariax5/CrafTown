package elc.florian.listener;

import elc.florian.Main;
import elc.florian.commands.CommandCity;
import elc.florian.other.InfoCity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;

public class MoveListener implements WebSocket.Listener, Listener {
    private final Main main;

    public MoveListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockY() == event.getTo().getBlockY()) {
            return;
        }
        Player player = event.getPlayer();
        int distance = 8;

        int x = (int) player.getLocation().getX();
        int z = (int) player.getLocation().getZ();

        if (event.getFrom().getBlockX() != event.getTo().getBlockX()) {
            if (x%distance != 0) {
                return;
            }
        } else if (event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            if (z%distance != 0) {
                return;
            }
        } else {
            return;
        }


        List<String> cityList = new ArrayList<>();
        cityList = CommandCity.getCity(cityList);

        assert cityList != null;
        for (String city : cityList) {
            InfoCity infoCity = CommandCity.getInfoCityAuto(city);
            assert infoCity != null;
            int x1 = (int) (infoCity.getSpawn_x() - 50);
            int x2 = (int) (infoCity.getSpawn_x() + 50);
            int z1 = (int) (infoCity.getSpawn_z() - 50);
            int z2 = (int) (infoCity.getSpawn_z() + 50);

            if (x1 < x && x < x2) {
                if (z1 < z && z < z2) {
                    if (event.getFrom().getBlockX() != event.getTo().getBlockX()) {
                        if (event.getFrom().getBlockX() < event.getTo().getBlockX()) {

                            x = x - distance;
                            if (x1 < x) {
                                return;
                            } else {
                                player.sendTitle("§6You join " + city, "§4Bienvenue dans la ville", 20, 20, 20);
                            }
                        } else {
                            x = x + distance;
                            if (x2 > x) {
                                return;
                            } else {
                                player.sendTitle("§6You join " + city, "§4Bienvenue dans la ville", 20, 20, 20);
                            }
                        }

                    } else {
                        if (event.getFrom().getBlockZ() < event.getTo().getBlockZ()) {

                            z = z - distance;
                            if (z1 <= z) {
                                return;
                            } else {
                                player.sendTitle("§6You join " + city, "§4Bienvenue dans la ville", 20, 20, 20);
                            }
                        } else {
                            z = z + distance;
                            if (z2 >= z) {
                                return;
                            } else {
                                player.sendTitle("§6You join " + city, "§4Bienvenue dans la ville", 20, 20, 20);
                            }
                        }
                    }
                    return;
                }
            }
        }
    }
}