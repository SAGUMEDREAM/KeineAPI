package cc.thonly.keine.fabric.resource.impl;

import cc.thonly.keine.api.resource.impl.CompositeResourcePack;
import java.util.Objects;
import net.fabricmc.fabric.impl.resource.pack.ModResourcePackCreator;

public class FapiProvider {
    public static void addPacks(CompositeResourcePack pack) {
        ModResourcePackCreator var10000 = ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER;
        Objects.requireNonNull(pack);
        var10000.loadPacks(pack::append);
    }
}
