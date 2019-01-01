package com.cookiehook.voidfogblock;

import com.cookiehook.voidfogblock.init.ModBlocks;
import com.cookiehook.voidfogblock.proxy.CommonProxy;
import com.cookiehook.voidfogblock.util.Reference;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class Main
{
    @Mod.Instance
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        ModBlocks.registerBlocks();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.error("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
