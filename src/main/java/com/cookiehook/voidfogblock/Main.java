package com.cookiehook.voidfogblock;

import com.cookiehook.voidfogblock.init.ModBlocks;
import com.cookiehook.voidfogblock.init.ModFluids;
import com.cookiehook.voidfogblock.proxy.CommonProxy;
import com.cookiehook.voidfogblock.util.FogEvent;
import com.cookiehook.voidfogblock.util.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class Main {

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.Instance
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModFluids.registerFluids();
        ModBlocks.registerBlocks();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new FogEvent());
    }
}
