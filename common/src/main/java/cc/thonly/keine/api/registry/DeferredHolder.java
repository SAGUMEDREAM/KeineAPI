package cc.thonly.keine.api.registry;

import com.mojang.datafixers.util.Either;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder.Kind;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T> {
   protected final ResourceKey<R> key;
   @Nullable
   private Holder<R> holder = null;

   public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<? extends Registry<R>> registryKey, Identifier valueName) {
      return create(ResourceKey.create(registryKey, valueName));
   }

   public static <R, T extends R> DeferredHolder<R, T> create(Identifier registryName, Identifier valueName) {
      return create(ResourceKey.createRegistryKey(registryName), valueName);
   }

   public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<R> key) {
      return new DeferredHolder<>(key);
   }

   protected DeferredHolder(ResourceKey<R> key) {
      this.key = Objects.requireNonNull(key);
      this.bind(false);
   }

   public T value() {
      this.bind(true);
      if (this.holder == null) {
         throw new NullPointerException("Trying to access unbound value: " + this.key);
      } else {
         return (T)this.holder.value();
      }
   }

   @Override
   public T get() {
      return this.value();
   }

   public Optional<T> asOptional() {
      return this.isBound() ? Optional.of(this.value()) : Optional.empty();
   }

   @Nullable
   protected Registry<R> getRegistry() {
      return (Registry<R>)BuiltInRegistries.REGISTRY.getValue(this.key.registry());
   }

   protected final void bind(boolean throwOnMissingRegistry) {
      if (this.holder == null) {
         Registry<R> registry = this.getRegistry();
         if (registry != null) {
            this.holder = (Holder<R>)registry.get(this.key).orElse(null);
         } else if (throwOnMissingRegistry) {
            throw new IllegalStateException("Registry not present for " + this + ": " + this.key.registry());
         }
      }
   }

   public Identifier getId() {
      return this.key.identifier();
   }

   public ResourceKey<R> getKey() {
      return this.key;
   }

   @Override
   public boolean equals(Object obj) {
      return this == obj ? true : obj instanceof Holder<?> h && h.kind() == Kind.REFERENCE && h.unwrapKey().orElse(null) == this.key;
   }

   @Override
   public int hashCode() {
      return this.key.hashCode();
   }

   @Override
   public String toString() {
      return String.format(Locale.ENGLISH, "DeferredHolder{%s}", this.key);
   }

   public boolean isBound() {
      this.bind(false);
      return this.holder != null && this.holder.isBound();
   }

//   public boolean areComponentsBound() {
//      this.bind(false);
//      return this.holder != null && this.holder.areComponentsBound();
//   }
//
//   @NonNull
//   public DataComponentMap components() {
//      this.bind(true);
//      return this.holder != null ? this.holder.components() : DataComponentMap.EMPTY;
//   }

   public boolean is(Identifier id) {
      return id.equals(this.key.identifier());
   }

   public boolean is(ResourceKey<R> key) {
      return key == this.key;
   }

   public boolean is(Predicate<ResourceKey<R>> filter) {
      return filter.test(this.key);
   }

   public boolean is(@NonNull TagKey<R> tag) {
      this.bind(false);
      return this.holder != null && this.holder.is(tag);
   }

   @Deprecated
   public boolean is(@NonNull Holder<R> holder) {
      this.bind(false);
      return this.holder != null && this.holder.is(holder);
   }

   public Stream<TagKey<R>> tags() {
      this.bind(false);
      return this.holder != null ? this.holder.tags() : Stream.empty();
   }

   public Either<ResourceKey<R>, R> unwrap() {
      return Either.left(this.key);
   }

   @NonNull
   public Optional<ResourceKey<R>> unwrapKey() {
      return Optional.of(this.key);
   }

   public Kind kind() {
      return Kind.REFERENCE;
   }

   public boolean canSerializeIn(@NonNull HolderOwner<R> owner) {
      this.bind(false);
      return this.holder != null && this.holder.canSerializeIn(owner);
   }
}
