package com.cookiehook.voidfogblock.init;

import com.cookiehook.voidfogblock.blocks.FogBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static List<Block> blockList = new ArrayList<Block>();

    public static void registerBlocks() {
        Block fogBlock = new FogBlock("fog_block");
    }
}
