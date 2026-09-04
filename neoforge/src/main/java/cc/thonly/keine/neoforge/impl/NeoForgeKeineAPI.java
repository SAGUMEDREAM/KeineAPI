package cc.thonly.keine.neoforge.impl;

import cc.thonly.keine.api.KeineAPI;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;

@SuppressWarnings("deprecation")
public class NeoForgeKeineAPI implements KeineAPI {
    public <T> void unfreeze(Registry<T> registry) {
        if (registry instanceof MappedRegistry<T> mappedRegistry) {
            mappedRegistry.unfreeze(false);
        }

    }

    public boolean directRegister() {
        return false;
    }
}
