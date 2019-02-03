package com.cookiehook.voidfogblock.init;

import com.cookiehook.voidfogblock.blocks.BlockFog;
import com.cookiehook.voidfogblock.blocks.BlockFluidSource;
import com.cookiehook.voidfogblock.materials.FogMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static List<Block> blockList = new ArrayList<Block>();
    public static BlockFog fogBlock;
    public static Block fogSourceBlock;

    public static void registerBlocks() {
        fogBlock = new BlockFog("fog_block", new FogMaterial());
        fogSourceBlock = new BlockFluidSource(ModFluids.FOG_SOURCE_FLUID, Material.WATER, fogBlock,"fog_fluid_source");
    }
}
