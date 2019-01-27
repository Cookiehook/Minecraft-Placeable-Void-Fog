package com.cookiehook.voidfogblock.init;

import com.cookiehook.voidfogblock.blocks.FogBlock;
import com.cookiehook.voidfogblock.blocks.ModBlockFluidClassic;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static List<Block> blockList = new ArrayList<Block>();
    public static Block fogBlock;
    public static Block fogSourceBlock;

    public static void registerBlocks() {
        fogBlock = new FogBlock("fog_block");
        fogSourceBlock = new ModBlockFluidClassic(ModFluids.SLIME, Material.WATER, "slime");
    }
}
