package me.statuxia.lightantigrief.config;

import me.statuxia.lightantigrief.LightAntiGrief;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Path;

public class LAGConfig {

    private static ConfigManager manager;

    public static void getConfig() {
        try {
            if (manager != null) {
                return;
            }
            Path path = Path.of(Path.of(System.getProperty("user.dir"), "plugins").toString(), LightAntiGrief.class.getSimpleName());
            manager = ConfigManager.of(Path.of(path.toString(), "config.json").toString());

            if (!manager.isCreated()) {
                return;
            }

            JSONObject config = new JSONObject();
            config.put("trustedTime", 21600);
            config.put("fireCharge", 7);
            config.put("getItem", 12);
            config.put("putItem", 12);
            config.put("breakBlock", 5);
            config.put("placeBlock", 6);
            config.put("minecart", 3);
            config.put("explode", 4);
            config.put("triggerRandomBonus", true);
            config.put("banReason", "You have been banned for suspected griefing");

            manager.updateFile(config, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getTrustedTime() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("trustedTime", 21600);
    }

    public static int getFireCharge() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("fireCharge", 7);
    }

    public static int getGetItem() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("getItem", 12);
    }

    public static int getPutItem() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("putItem", 12);
    }

    public static int getBreakBlock() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("breakBlock", 5);
    }

    public static int getPlaceBlock() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("placeBlock", 6);
    }

    public static int getMinecart() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("minecart", 3);
    }

    public static int getExplode() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optInt("explode", 4);
    }

    public static String getBanReason() {
        if (manager == null) {
            getConfig();
        }
        return manager.getJsonObject().optString("banReason", "You have been banned for suspected griefing");
    }
}
