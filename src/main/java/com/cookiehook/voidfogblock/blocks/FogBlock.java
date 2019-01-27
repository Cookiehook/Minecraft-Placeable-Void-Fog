package com.cookiehook.voidfogblock.blocks;

import com.cookiehook.voidfogblock.init.ModBlocks;
import com.cookiehook.voidfogblock.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;
import java.lang.Math;

// TODO: Decide on whether to allow mobs to spawn inside the fog
// TODO: Allow configuration of light level for fog to spread through.
// TODO: Allow toggling of growth / retreat (for debug / world recovery)
public class FogBlock extends Block {

    public FogBlock(String name, Material material) {
        super(material);

        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setTickRandomly(true);
        this.useNeighborBrightness = true;

        ModBlocks.blockList.add(this);
        ModItems.itemList.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }


    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        return (blockState != iblockstate);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    // Required to render the blocks behind this.
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    // Lets the player click through to blocks behind. Does not allow players to walk through the block.
    public boolean isCollidable() {
        return false;
    }


    // Lets the player walk through the block unimpeded. Does not block interaction with this block.
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    // Adds the gray fog particles at random
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {

        double posX = (double) pos.getX() + worldIn.rand.nextDouble();
        double posY = (double) pos.getY() + worldIn.rand.nextDouble();
        double posZ = (double) pos.getZ() + worldIn.rand.nextDouble();

        worldIn.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    }


    public void updateTick(World worldIn, BlockPos currentPos, IBlockState state, Random rand) {
        spreadFog(worldIn, currentPos, rand, 3, 5);
    }

    // Controls spread and retreat, based on light levels. Cannibalised from grass block code.
    public static void spreadFog (World worldIn, BlockPos currentPos, Random rand, int boundH, int boundV) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(currentPos, 3))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading

            if (!validDistanceToSource(worldIn, currentPos)) {
                worldIn.setBlockState(currentPos, Blocks.AIR.getDefaultState());
                return;  // If this block was too far from a source, don't move on to spreading.
            }

            if (!validLightLevel(worldIn, currentPos) && worldIn.getBlockState(currentPos).getBlock() == ModBlocks.fogBlock) {
                worldIn.setBlockState(currentPos, Blocks.AIR.getDefaultState());

            } else {
                for (int i = 0; i < 4; ++i) {
                    BlockPos newPos = currentPos.add(rand.nextInt(boundH) - 1, rand.nextInt(boundV) - 3, rand.nextInt(boundH) - 1);

                    if (newPos.getY() >= 0 && newPos.getY() < 256 && !worldIn.isBlockLoaded(newPos)) {
                        return;
                    }

                    IBlockState iblockstate1 = worldIn.getBlockState(newPos);
                    if (iblockstate1.getBlock() == Blocks.AIR
                            && validLightLevel(worldIn, newPos)
                            && validDistanceToSource(worldIn, newPos)) {
                        worldIn.setBlockState(newPos, ModBlocks.fogBlock.getDefaultState());
                    }
                }
            }
        }
    }

    private static boolean validDistanceToSource(World worldIn, BlockPos pos) {
        int upperDecay = 6;
        int lowerDecay = upperDecay * -1;

        for (int xdist = lowerDecay; xdist <= upperDecay; ++xdist) {
            for (int ydist = lowerDecay; ydist <= upperDecay; ++ydist) {
                for (int zdist = lowerDecay; zdist <= upperDecay; ++zdist) {

                    double distance = Math.sqrt((xdist * xdist + zdist * zdist) + ydist * ydist);
                    if (distance <= (double) upperDecay) {
                        BlockPos sourceCheck = new BlockPos(pos.getX() + xdist, pos.getY() + ydist, pos.getZ() + zdist);
                        Block potentialSource = worldIn.getBlockState(sourceCheck).getBlock();
                        if (potentialSource == ModBlocks.fogSourceBlock) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean validLightLevel(World worldIn, BlockPos pos) {
        int retreatLevel = 8;
        int lightLevel = worldIn.getLight(pos, true);
        return lightLevel <= retreatLevel; // If lightLevel lower than our retreat, then we can grow.
    }

}

