package com.cookiehook.voidfogblock.init;

import com.cookiehook.voidfogblock.fluids.ModFluid;
import com.cookiehook.voidfogblock.util.Reference;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Set;

public class ModFluids {


    public static final ModFluid FOG_SOURCE_FLUID = (ModFluid) new ModFluid(
            "fog_source",
            new ResourceLocation(Reference.MOD_ID, "fog_source_still"),
            new ResourceLocation(Reference.MOD_ID, "fog_source_flow"))
            .setHasBucket(true)
            .setGaseous(false)
            .setLuminosity(9)
            .setViscosity(5000)
            .setTemperature(300);

    public static final Set<ModFluid> SET_FLUIDS = ImmutableSet.of(
            FOG_SOURCE_FLUID);

    public static void registerFluids() {
        // DEBUG
        System.out.println("Registering fluids");
        for (final ModFluid fluid : SET_FLUIDS) {
            FluidRegistry.registerFluid(fluid);
            if (fluid.isBucketEnabled()) {
                FluidRegistry.addBucketForFluid(fluid);
            }
            // DEBUG
            System.out.println("Registering fluid: " + fluid.getName() + " with bucketF = " + fluid.isBucketEnabled());
        }
    }
}
