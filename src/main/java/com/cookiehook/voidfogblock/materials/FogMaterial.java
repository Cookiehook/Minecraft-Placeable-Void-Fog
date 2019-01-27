package com.cookiehook.voidfogblock.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class FogMaterial extends Material {

    public FogMaterial() {
        super(MapColor.GRAY);
        this.setReplaceable();
    }

    public boolean isSolid() {
        return false;
    }

    public boolean blocksMovement() {
        return false;
    }

    public boolean blocksLight() { return true; }
}
