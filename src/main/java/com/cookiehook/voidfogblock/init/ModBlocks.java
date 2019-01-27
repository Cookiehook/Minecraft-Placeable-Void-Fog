package com.cookiehook.voidfogblock.init;

import com.cookiehook.voidfogblock.blocks.FogBlock;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static List<Block> blockList = new ArrayList<Block>();
    public static Block fogBlock;

    public static void registerBlocks() {
        fogBlock = new FogBlock("fog_block");
    }
}
