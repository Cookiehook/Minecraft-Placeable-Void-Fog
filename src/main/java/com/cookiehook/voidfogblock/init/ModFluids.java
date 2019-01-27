package com.cookiehook.voidfogblock.init;

import com.cookiehook.voidfogblock.fluids.ModFluid;
import com.cookiehook.voidfogblock.util.Reference;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Set;

public class ModFluids {


    public static final ModFluid SLIME = (ModFluid) new ModFluid(
            "slime",
            new ResourceLocation(Reference.MOD_ID, "slime_still"),
            new ResourceLocation(Reference.MOD_ID, "slime_flow"))
            .setHasBucket(true)
            .setDensity(1100)
            .setGaseous(false)
            .setLuminosity(9)
            .setViscosity(25000)
            .setTemperature(300);

    public static final Set<ModFluid> SET_FLUIDS = ImmutableSet.of(
            SLIME);

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
