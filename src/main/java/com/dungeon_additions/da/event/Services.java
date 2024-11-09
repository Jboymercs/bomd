package com.dungeon_additions.da.event;

import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.interfaces.IModUtilsHandler;

import java.util.ServiceLoader;


public class Services {
    public static final IModUtilsHandler PLATFORM_CAPABILITY = load(IModUtilsHandler.class);

    public static <T> T load(Class<T> clazz) {
        //This has to be correct right?
        final T loadedService = ServiceLoader.load(clazz).iterator().next();


        ModReference.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }


}
