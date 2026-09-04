package cc.thonly.keine.api.registry.impl;

import cc.thonly.keine.api.callback.RegistryEntryAddedCallback;
import cc.thonly.keine.api.registry.DynamicRegistryView;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.RegistryAccess.RegistryEntry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

public final class DynamicRegistryViewImpl implements DynamicRegistryView {
   private final Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries;

   public DynamicRegistryViewImpl(Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries) {
      this.registries = registries;
   }

   @Override
   public RegistryAccess asDynamicRegistryManager() {
      return new Frozen() {
         {
            Objects.requireNonNull(DynamicRegistryViewImpl.this);
         }

         @NonNull
         public <T> Optional<Registry<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
            return Optional.ofNullable((Registry<T>)DynamicRegistryViewImpl.this.registries.get(key));
         }

         @NonNull
         public Stream<RegistryEntry<?>> registries() {
            return DynamicRegistryViewImpl.this.stream().map(this::entry);
         }

         private <T> RegistryEntry<T> entry(Registry<T> registry) {
            return new RegistryEntry(registry.key(), registry);
         }

         @NonNull
         public Frozen freeze() {
            return this;
         }
      };
   }

   @Override
   public Stream<Registry<?>> stream() {
      return this.registries.values().stream();
   }

   @Override
   public <T> Optional<Registry<T>> getOptional(ResourceKey<? extends Registry<? extends T>> registryRef) {
      return Optional.ofNullable((Registry<T>)this.registries.get(registryRef));
   }

   @Override
   public <T> void registerEntryAdded(ResourceKey<? extends Registry<? extends T>> registryRef, RegistryEntryAddedCallback<T> callback) {
      Registry<T> registry = (Registry<T>)this.registries.get(registryRef);
      if (registry != null) {
         RegistryEntryAddedCallback.event(registry).register(callback);
      }
   }
}
