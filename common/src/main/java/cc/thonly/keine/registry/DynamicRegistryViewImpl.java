package cc.thonly.keine.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class DynamicRegistryViewImpl implements DynamicRegistryView {
    private final Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries;

    public DynamicRegistryViewImpl(Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries) {
        this.registries = registries;
    }

    @Override
    public RegistryAccess asDynamicRegistryManager() {
        return new RegistryAccess.Frozen() {
            @SuppressWarnings("unchecked")
            public <T> @NonNull Optional<Registry<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                return Optional.ofNullable((Registry<T>) DynamicRegistryViewImpl.this.registries.get(key));
            }

            public @NonNull Stream<RegistryEntry<?>> registries() {
                return DynamicRegistryViewImpl.this.stream()
                        .map(this::entry);
            }

            private <T> RegistryEntry<T> entry(Registry<T> registry) {
                return new RegistryEntry<>(registry.key(), registry);
            }

            public @NonNull Frozen freeze() {
                return this;
            }
        };
    }

    @Override
    public Stream<Registry<?>> stream() {
        return this.registries.values().stream();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<Registry<T>> getOptional(ResourceKey<? extends Registry<? extends T>> registryRef) {
        return Optional.ofNullable((Registry<T>) this.registries.get(registryRef));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void registerEntryAdded(ResourceKey<? extends Registry<? extends T>> registryRef, RegistryEntryAddedCallback<T> callback) {
        Registry<T> registry = (Registry<T>) this.registries.get(registryRef);

        if (registry != null) {
            RegistryEntryAddedCallback.event(registry).register(callback);
        }
    }
}
