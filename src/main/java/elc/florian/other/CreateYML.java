package elc.florian.other;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CreateYML {
    public static void setFields() {
        heads();
        dataBase();
        lang();
    }

    private static void lang() {
        final File file = new File("plugins/CrafTown/lang.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = "fr.";
        final ConfigurationSection configurationSection = config.getConfigurationSection(key);
    }

    private static void dataBase() {
        final File file = new File("plugins/CrafTown/dataBase.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = "DbManager.";
        final ConfigurationSection configurationSection = config.getConfigurationSection(key);

        if (configurationSection == null) {

            config.set(key + "host", "eu02-sql.pebblehost.com");
            config.set(key + "user", "customer_456749_craftown");
            config.set(key + "password", "84@6hvS~tbK$t4JVgMeO");
            config.set(key + "name", "customer_456749_craftown");
            config.set(key + "port", 3306);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void heads() {
        String tmpURL = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0=";

        final File file = new File("plugins/CrafTown/heads.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = "invPerso.";
        final ConfigurationSection configurationSection = config.getConfigurationSection(key);

        if (configurationSection == null) {

            config.set(key + "levelURL", tmpURL);
            config.set(key + "gradeURL", tmpURL);
            config.set(key + "cityURL", tmpURL);
            config.set(key + "workURL", tmpURL);
            config.set(key + "moneyURL", tmpURL);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
