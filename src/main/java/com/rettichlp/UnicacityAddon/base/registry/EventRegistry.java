package com.rettichlp.UnicacityAddon.base.registry;

import com.rettichlp.UnicacityAddon.base.registry.annotation.UCEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import static com.rettichlp.UnicacityAddon.base.utils.DebugUtils.Debug;

public class EventRegistry {

    public static void register(ASMDataTable asmDataTable) {
        asmDataTable.getAll(UCEvent.class.getCanonicalName()).forEach(asmData -> {
            try {
                Class<?> clazz = Class.forName(asmData.getClassName());
                MinecraftForge.EVENT_BUS.register(clazz.newInstance());
                Debug(EventRegistry.class, "UCEvent: " + clazz.getSimpleName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                // TODO: 14.08.2022
                throw new RuntimeException(e);
            }
        });
    }
}