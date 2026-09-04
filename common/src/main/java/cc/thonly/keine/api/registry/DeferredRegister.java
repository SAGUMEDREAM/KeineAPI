package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.KeineAPI;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public class DeferredRegister<T> {
   private final ResourceKey<? extends Registry<T>> registryKey;
   private final String namespace;
   private final Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
   private final Set<DeferredHolder<T, ? extends T>> entriesView = Collections.unmodifiableSet(this.entries.keySet());
   private final Map<Identifier, Identifier> aliases = new HashMap<>();
   @Nullable
   private Registry<T> customRegistry;
   private DeferredRegister.@Nullable RegistryHolder<T> registryHolder;

   public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
      return new DeferredRegister<>(registry.key(), namespace);
   }

   public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
      return new DeferredRegister<>(key, namespace);
   }

   public static <T> DeferredRegister<T> create(Identifier registryName, String modid) {
      return new DeferredRegister<>(ResourceKey.createRegistryKey(registryName), modid);
   }

   public DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
      this.registryKey = Objects.requireNonNull(registryKey);
      this.namespace = Objects.requireNonNull(namespace);
   }

   public <I extends T> DeferredHolder<T, I> register(final String name, final Supplier<? extends I> sup) {
      return this.register(name, key -> sup.get());
   }

   public <I extends T> DeferredHolder<T, I> register(final String name, final Function<Identifier, ? extends I> func) {
      Objects.requireNonNull(name);
      Objects.requireNonNull(func);
      final Identifier key = Identifier.fromNamespaceAndPath(this.namespace, name);
      DeferredHolder<T, I> ret = this.createHolder(this.registryKey, key);
      if (this.entries.putIfAbsent(ret, new Supplier<T>() {
         private I cached;

         {
            Objects.requireNonNull(DeferredRegister.this);
         }

         @Override
         public I get() {
            if (this.cached == null) {
               this.cached = (I)func.apply(key);
            }

            return this.cached;
         }
      }) != null) {
         throw new IllegalArgumentException("Duplicate registration " + name);
      } else {
         if (KeineAPI.getApi().directRegister()) {
            Registry registry = BuiltInRegistries.REGISTRY.getValue(this.registryKey.identifier());
            Supplier<? extends T> sup = this.entries.get(ret);
            I value = (I)sup.get();
            if (registry != null) {
               Registry.register(registry, key, value);
            }
         }

         return ret;
      }
   }

   protected <I extends T> DeferredHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, Identifier key) {
      return DeferredHolder.create(registryKey, key);
   }

   @SuppressWarnings({"unchecked", "rawtypes"})
   private static class RegistryHolder<V> implements Supplier<Registry<V>> {
      private final ResourceKey<? extends Registry<V>> registryKey;
      private Registry<V> registry = null;

      private RegistryHolder(ResourceKey<? extends Registry<V>> registryKey) {
         this.registryKey = registryKey;
      }

      @Nullable
      public Registry<V> get() {
         if (this.registry == null) {
            Registry valueOrThrow = BuiltInRegistries.REGISTRY.getValueOrThrow((ResourceKey) this.registryKey);
            this.registry = (Registry<V>)valueOrThrow;
         }

         return this.registry;
      }
   }
}
