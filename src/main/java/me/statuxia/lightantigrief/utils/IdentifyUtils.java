package me.statuxia.lightantigrief.utils;

import net.coreprotect.CoreProtectAPI;
import me.statuxia.lightantigrief.LAG;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IdentifyUtils {

    private static final HashMap<Block, String> cachedBlocksOwners = new HashMap<>();

    public static boolean isOwner(Block block, Player player) {
        if (cachedBlocksOwners.getOrDefault(block, "").equals(player.getName())) {
            return true;
        }

        CoreProtectAPI coreProtect = LAG.getCoreProtectAPI();
        if (coreProtect == null) {
            return true;
        }
        List<String[]> strings = coreProtect.blockLookup(block, 13150080 * 4);

        boolean isGenerated = true;
        for (String[] string : strings) {
            CoreProtectAPI.ParseResult parseResult = coreProtect.parseResult(string);
            if (parseResult.getActionId() == 1 && parseResult.getType() == block.getType()) {
                if (parseResult.getPlayer().equals(player.getName())) {
                    return true;
                }
                isGenerated = false;
            }
        }
        return isGenerated;
    }

    static {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new RunnableTask(), 1, 60, TimeUnit.SECONDS);
    }

    static class RunnableTask implements Runnable {
        @Override
        public void run() {
            cachedBlocksOwners.clear();
        }
    }

}
