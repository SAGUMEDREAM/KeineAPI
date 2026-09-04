package cc.thonly.keine.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryGetter<T> {
   DeferredRegistry<T> get(String modId, ResourceKey<Registry<T>> registryKey);
}
