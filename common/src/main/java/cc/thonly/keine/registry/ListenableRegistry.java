package cc.thonly.keine.registry;

import net.blay09.mods.balm.platform.event.Event;
import net.minecraft.core.Registry;

public interface ListenableRegistry<T> {
    Event<RegistryEntryAddedCallback<T>> keine_getAddObjectEvent();

    Event<RegistryIdRemapCallback<T>> keine_getRemapEvent();

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T> ListenableRegistry<T> get(Registry<T> registry) {
        if (!(registry instanceof ListenableRegistry)) {
            throw new IllegalArgumentException("Unsupported registry: " + String.valueOf(registry.key().identifier()));
        } else {
            return (ListenableRegistry) registry;
        }
    }
}