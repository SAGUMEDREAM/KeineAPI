package cc.thonly.keine;

import cc.thonly.keine.api.KeineAPI;
import cc.thonly.keine.api.KeineRegistries;
import net.blay09.mods.balm.Balm;
import net.minecraft.resources.Identifier;
import net.blay09.mods.balm.core.BalmRegistrars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Keine {
    public static final Logger logger = LoggerFactory.getLogger(Keine.class);
    public static final String MOD_ID = "keine";
    public static final Map<String, KeineRegistries> id2Api = new ConcurrentHashMap<>();
    private static boolean __LOADED = false;
    private static KeineAPI __API = null;

    public static void enable(KeineAPI impl) {
        if (__LOADED) {
            return;
        }
        synchronized (Keine.class) {
            __API = impl;
            __LOADED = true;
        }
    }

    public static KeineAPI api() {
        return __API;
    }

    public static boolean isLoaded() {
        return __LOADED;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static KeineConfig config() {
        return Balm.config().getActiveConfig(KeineConfig.class);
    }

    public static void initialize(BalmRegistrars registrars) {
        if (__LOADED) {
            return;
        }
        Balm.config().registerConfig(KeineConfig.class);

    }

}
