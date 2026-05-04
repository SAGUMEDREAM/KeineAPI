package cc.thonly.keine.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface DynamicRegistryView {
    RegistryAccess asDynamicRegistryManager();

    Stream<Registry<?>> stream();

    <T> Optional<Registry<T>> getOptional(ResourceKey<? extends Registry<? extends T>> var1);

    <T> void registerEntryAdded(ResourceKey<? extends Registry<? extends T>> var1, RegistryEntryAddedCallback<T> var2);
}
