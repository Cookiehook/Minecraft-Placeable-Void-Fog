package com.cookiehook.voidfogblock.blocks;

import java.awt.Color;
import java.util.Random;

import javax.annotation.Nonnull;

import com.cookiehook.voidfogblock.init.ModBlocks;
import com.cookiehook.voidfogblock.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class FogSourceBlock extends BlockFluidClassic {

    private FogBlock fogblock;

    public FogSourceBlock(Fluid parFluid, Material parMaterial, FogBlock fogBlock, String name) {
        super(parFluid, parMaterial);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.fogblock = fogBlock;
        ModBlocks.blockList.add(this);
        ModItems.itemList.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos blockpos, @Nonnull IBlockState state, @Nonnull Random rand) {
        super.updateTick(worldIn, blockpos, state, rand);
        BlockPos[] neighbourBlocks = {blockpos.up(), blockpos.down(), blockpos.north(), blockpos.east(), blockpos.south(), blockpos.west()};

        for (BlockPos neighbourPos : neighbourBlocks) {
            if (worldIn.getBlockState(neighbourPos).getBlock() == Blocks.AIR) {
                worldIn.setBlockState(neighbourPos, this.fogblock.getDefaultState());
            }
        }

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        int upperDecay = this.fogblock.maxDistanceToSource;
        int lowerDecay = upperDecay * -1;

        for (int xdist = lowerDecay; xdist <= upperDecay; ++xdist) {
            for (int ydist = lowerDecay; ydist <= upperDecay; ++ydist) {
                for (int zdist = lowerDecay; zdist <= upperDecay; ++zdist) {

                    double distance = Math.sqrt((xdist * xdist + zdist * zdist) + ydist * ydist);
                    if (distance <= (double) upperDecay) {
                        BlockPos fogPos = new BlockPos(pos.getX() + xdist, pos.getY() + ydist, pos.getZ() + zdist);
                        Block potentialFog = worldIn.getBlockState(fogPos).getBlock();
                        if (potentialFog instanceof FogBlock) {
                            worldIn.setBlockState(fogPos, this.fogblock.getStateFromMeta(14));
                        }
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks) {
        return new Vec3d(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue());
    }

}
