package com.cookiehook.voidfogblock.init;

import com.cookiehook.voidfogblock.util.Reference;
import com.google.common.collect.ImmutableSet;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Set;

public class ModFluids {


    public static final Fluid FOG_SOURCE_FLUID = new Fluid(
            "fog_source",
            new ResourceLocation(Reference.MOD_ID, "fog_source_still"),
            new ResourceLocation(Reference.MOD_ID, "fog_source_flow"))
            .setViscosity(5000)
            .setTemperature(300)
            .setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY)
            .setFillSound(SoundEvents.ITEM_BUCKET_FILL);

    public static final Set<Fluid> SET_FLUIDS = ImmutableSet.of(
            FOG_SOURCE_FLUID);

    public static void registerFluids() {
        for (final Fluid fluid : SET_FLUIDS) {
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
        }
    }
}
