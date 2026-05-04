package cc.thonly.keine.api;

import cc.thonly.keine.Keine;
import net.minecraft.core.Registry;

import java.util.Collection;
import java.util.function.Consumer;

public interface KeineAPI {
    default KeineRegistries get(String modId) {
        return Keine.id2Api.computeIfAbsent(modId, KeineRegistries::new);
    }

    default KeineRegistries global() {
        return Keine.id2Api.computeIfAbsent("global", KeineRegistries::new);
    }

    default Collection<KeineRegistries> values() {
        return Keine.id2Api.values();
    }

    <T> void unfreeze(Registry<T> registry);

    static KeineAPI getApi() {
        return Keine.api();
    }
}
