package com.cookiehook.voidfogblock.blocks;

import com.cookiehook.voidfogblock.init.ModBlocks;
import com.cookiehook.voidfogblock.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import java.lang.Math;

// TODO: Decide on whether to allow mobs to spawn inside the fog
// TODO: Allow configuration of light level for fog to spread through.
public class BlockFog extends Block {

    static int maxDistanceToSource = 6;
    private static PropertyInteger distanceToSource = PropertyInteger.create("distance", 0, maxDistanceToSource);
    private static PropertyBool decay = PropertyBool.create("decay");

    public BlockFog(String name, Material material) {
        super(material);

        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setTickRandomly(true);
        this.useNeighborBrightness = true;

        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(distanceToSource, 0)
                .withProperty(decay, false));

        ModBlocks.blockList.add(this);
    }


    /*************************************************
     *             BLOCKSTATE HANDLING               *
     *************************************************/

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, distanceToSource, decay);
    }

    public IBlockState getStateFromMeta(int meta) {
        // Currently, meta == 8 should not be used. 0-6 for distance and no decay. 8-14 for distance and decay
        if (meta < 7) {
            return this.getDefaultState().withProperty(distanceToSource, meta).withProperty(decay, false);
        } else {
            return this.getDefaultState().withProperty(distanceToSource, meta - 8).withProperty(decay, true);
        }
    }

    public int getMetaFromState(IBlockState state) {
        // Currently, meta == 8 should not be used. 0-6 for distance and no decay. 8-14 for distance and decay
        if (!state.getValue(decay)) {
            return state.getValue(distanceToSource);
        } else {
            return state.getValue(distanceToSource) + 8;
        }
    }


    /*************************************************
     *              RENDERING HANDLING               *
     *************************************************/

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

    // Adds the gray fog particles at random
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {

        double posX = (double) pos.getX() + worldIn.rand.nextDouble();
        double posY = (double) pos.getY() + worldIn.rand.nextDouble();
        double posZ = (double) pos.getZ() + worldIn.rand.nextDouble();

        worldIn.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
    }


    /*************************************************
     *             INTERACTION HANDLING              *
     *************************************************/

    // Lets the player click through to blocks behind. Does not allow players to walk through the block.
    public boolean isCollidable() {
        return false;
    }

    // Lets the player walk through the block unimpeded. Does not block interaction with this block.
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    // Make sure nothing gets dropped when broken. Whilst the player can't break the block, liquids can
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    public void updateTick(World worldIn, BlockPos currentPos, IBlockState state, Random rand) {
        // Controls spread and retreat, based on light levels. Cannibalised from grass block code.
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(currentPos, 3))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading

            if (worldIn.getBlockState(currentPos).getValue(decay) || !validDistanceToSource(worldIn, currentPos)) {
                worldIn.setBlockState(currentPos, Blocks.AIR.getDefaultState());
                return;  // If this block was too far from a source, don't move on to spreading.
            }

            if (!validLightLevel(worldIn, currentPos)) {
                worldIn.setBlockState(currentPos, Blocks.AIR.getDefaultState());
                return; // If the light here is too high, don't move on to spreading.
            }

            for (int i = 0; i < 4; ++i) {
                BlockPos newPos = currentPos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                if (newPos.getY() >= 0 && newPos.getY() < 256 && !worldIn.isBlockLoaded(newPos)) {
                    return;
                }

                IBlockState iblockstate1 = worldIn.getBlockState(newPos);
                if (iblockstate1.getBlock() == Blocks.AIR
                        && validLightLevel(worldIn, newPos)
                        && validDistanceToSource(worldIn, newPos)) {
                    int distanceMeta = getNeighbourDistance(worldIn, newPos) + 1;
                    worldIn.setBlockState(newPos, this.getDefaultState().withProperty(distanceToSource, distanceMeta).withProperty(decay, false));
                }
            }
        }
    }

    private boolean validDistanceToSource(World worldIn, BlockPos pos) {
        int upperDecay = maxDistanceToSource;
        int lowerDecay = upperDecay * -1;

        for (int xdist = lowerDecay; xdist <= upperDecay; ++xdist) {
            for (int ydist = lowerDecay; ydist <= upperDecay; ++ydist) {
                for (int zdist = lowerDecay; zdist <= upperDecay; ++zdist) {

                    double distance = Math.sqrt((xdist * xdist + zdist * zdist) + ydist * ydist);
                    if (distance <= (double) upperDecay) {
                        BlockPos sourceCheck = new BlockPos(pos.getX() + xdist, pos.getY() + ydist, pos.getZ() + zdist);
                        Block potentialSource = worldIn.getBlockState(sourceCheck).getBlock();
                        if (potentialSource instanceof BlockFluidSource && getNeighbourDistance(worldIn, pos) < maxDistanceToSource) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getNeighbourDistance(World worldIn, BlockPos pos) {
        int newDistance = maxDistanceToSource;
        BlockPos[] neighbourBlocks = {pos.up(), pos.down(), pos.north(), pos.east(), pos.south(), pos.west()};

        for (BlockPos neighbourPos : neighbourBlocks) {
            if (worldIn.getBlockState(neighbourPos).getBlock() == this) {
                int neighbourDistance = worldIn.getBlockState(neighbourPos).getValue(distanceToSource);
                newDistance = neighbourDistance < newDistance ? neighbourDistance : newDistance;
            } else if (worldIn.getBlockState(neighbourPos).getBlock() instanceof BlockFluidSource) {
                return 0;
            }
        }
        return newDistance;
    }

    private boolean validLightLevel(World worldIn, BlockPos pos) {
        int retreatLevel = 8;
        int lightLevel = worldIn.getLight(pos, true);
        return lightLevel <= retreatLevel; // If lightLevel lower than our retreat, then we can grow.
    }
}

