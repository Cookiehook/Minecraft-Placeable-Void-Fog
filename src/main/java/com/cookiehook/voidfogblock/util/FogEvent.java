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

    private float fogDistance = 0F;

    @SubscribeEvent
    public void render(RenderFogEvent event) {
        float fogStartPoint = 50.0F;
        float fogEndPoint = 5.0F;
        float fogIncrement = 0.1F;

        Entity entity = event.getEntity();
        BlockPos playerHeadPosition = entity.getPosition().up();
        WorldClient worldclient = Minecraft.getMinecraft().world;
        Block headBlock = worldclient.getBlockState(playerHeadPosition).getBlock();

        if (headBlock == ModBlocks.fogBlock) {

            if (fogDistance < fogStartPoint - fogEndPoint) {
                fogDistance += fogIncrement;
                fogStartPoint -= fogDistance;
            } else {
                fogStartPoint = fogEndPoint;
            }

            setFog(fogStartPoint);
        } else if (fogDistance > 0) {
            fogDistance -= fogIncrement;

            if (fogDistance < fogStartPoint)
                fogStartPoint -= fogDistance;

            setFog(fogStartPoint);
        } else {
            fogDistance = 0;
        }
    }

    private void setFog(float threshold) {
        GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
        GlStateManager.setFogStart(threshold * 0.25F);
        GlStateManager.setFogEnd(threshold);
    }
}
