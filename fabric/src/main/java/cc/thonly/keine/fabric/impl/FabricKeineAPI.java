package cc.thonly.keine.fabric.impl;

import cc.thonly.keine.api.KeineAPI;
import net.minecraft.core.Registry;

public class FabricKeineAPI implements KeineAPI {
    public <T> void unfreeze(Registry<T> registry) {
    }

    public boolean directRegister() {
        return true;
    }
}
