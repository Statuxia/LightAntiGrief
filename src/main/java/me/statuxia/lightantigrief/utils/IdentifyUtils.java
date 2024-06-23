package me.statuxia.lightantigrief.utils;

import me.statuxia.lightantigrief.LAG;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.block.Block;

import java.util.List;


public class IdentifyUtils {

    public static String getOwner(Block block) {
        CoreProtectAPI coreProtect = LAG.getCoreProtectAPI();
        if (coreProtect == null) {
            return "";
        }
        List<String[]> strings = coreProtect.blockLookup(block, 13150080 * 4);

        for (String[] string : strings) {
            CoreProtectAPI.ParseResult parseResult = coreProtect.parseResult(string);
            if (parseResult.getActionId() == 1 && parseResult.getType() == block.getType()) {
                return parseResult.getPlayer();
            }
        }
        return "";
    }
}
