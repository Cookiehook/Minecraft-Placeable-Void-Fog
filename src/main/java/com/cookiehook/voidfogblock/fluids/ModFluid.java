package com.cookiehook.voidfogblock.fluids;


import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

// TODO: Auto-generated Javadoc
public class ModFluid extends Fluid {
    protected float overlayAlpha = 0.2F;
    protected boolean bucketEnabled = false;


    public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);
        this.setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY);
        this.setFillSound(SoundEvents.ITEM_BUCKET_FILL);
        this.setColor(0xFFFFFFFF);
    }


    public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor) {
        this(fluidName, still, flowing);
        setColor(mapColor);
    }

    public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor, float overlayAlpha) {
        this(fluidName, still, flowing, mapColor);
        setAlpha(overlayAlpha);
    }

    public float getAlpha() {
        return overlayAlpha;
    }


    public ModFluid setAlpha(float parOverlayAlpha) {
        overlayAlpha = parOverlayAlpha;
        return this;
    }


    public ModFluid setHasBucket(boolean parEnableBucket) {
        bucketEnabled = parEnableBucket;
        return this;
    }

    public boolean isBucketEnabled() {
        return bucketEnabled;
    }
}

