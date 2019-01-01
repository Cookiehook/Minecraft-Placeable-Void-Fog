package com.cookiehook.voidfogblock.util;

import com.cookiehook.voidfogblock.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FogEvent {

    @SubscribeEvent
    public void render(RenderFogEvent e) {
        Entity entity = e.getEntity();

        BlockPos playerHeadPosition = entity.getPosition().up();
        WorldClient worldclient = Minecraft.getMinecraft().world;
        Block headBlock = worldclient.getBlockState(playerHeadPosition).getBlock();

        if (headBlock == ModBlocks.fogBlock) {

            float f1 = 10;
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

            if (e.getFogMode() < 0) {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(f1);
            } else {
                GlStateManager.setFogStart(f1 * 0.25F);
                GlStateManager.setFogEnd(f1);
            }
        }
    }
}