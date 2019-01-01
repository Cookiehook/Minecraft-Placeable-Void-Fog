package com.cookiehook.voidfogblock.util;

import com.cookiehook.voidfogblock.Main;
import com.cookiehook.voidfogblock.init.ModBlocks;
import com.cookiehook.voidfogblock.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegsiter(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.itemList.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegsiter(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.blockList.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onModelRegsiter(ModelRegistryEvent event) {
        for (Item item : ModItems.itemList) {
            Main.proxy.registerItemRenderer(item, 0, "inventory");
        }
        for (Block block : ModBlocks.blockList) {
            Main.proxy.registerItemRenderer(Item.getItemFromBlock(block), 0, "inventory");
        }

    }

}