package com.cookiehook.voidfogblock.blocks;

import com.cookiehook.voidfogblock.init.ModBlocks;
import com.cookiehook.voidfogblock.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

// TODO: Decide on whether to allow mobs to spawn inside the fog
// TODO: Allow configuration of light level for fog to spread through.
// TODO: Allow toggling of growth / retreat (for debug / world recovery)
public class FogBlock extends Block {

    private static int burnLevel = 7;
    private static int growLevel = 4;

    public FogBlock(String name) {
        super(Material.AIR);

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

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;

        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        //worldIn.spawnParticle(EnumParticleTypes.BARRIER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        //worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);

    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading


            if (worldIn.getLight(pos, true) >= burnLevel) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());

            } else if (worldIn.getLight(pos, true) <= growLevel) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                    if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
                        return;
                    }

                    IBlockState iblockstate1 = worldIn.getBlockState(blockpos);

                    if (iblockstate1.getBlock() == Blocks.AIR && worldIn.getLight(blockpos, true) <= growLevel) {
                        worldIn.setBlockState(blockpos, this.getDefaultState());
                    }
                }

            }
        }
    }
}

