package cc.thonly.keine.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface LazySupplier<T> extends Supplier<T> {
   @Override
   T get();

   default void unbound() {
   }

   static <T> LazySupplier<T> of(Supplier<T> getter) {
      return new LazySupplier.Impl<>(getter);
   }

   static <T> LazySupplier<T> of(T value) {
      return new LazySupplier.ImmediateImpl<>(value);
   }

   static <T> LazySupplier<T> defineByName(String name, Supplier<T> getter) {
      return (LazySupplier<T>)LazySupplier.NamedImpl.IMPLS.computeIfAbsent(name, var2 -> new LazySupplier.NamedImpl<>(name, getter));
   }

   static <T> LazySupplier<T> byName(String name) {
      return (LazySupplier<T>)LazySupplier.NamedImpl.IMPLS.get(name);
   }

   static <T> LazySupplier<T> byName(String name, Class<T> tClass) {
      return (LazySupplier<T>)LazySupplier.NamedImpl.IMPLS.get(name);
   }

   public static class ImmediateImpl<T> implements LazySupplier<T> {
      private final T value;

      public ImmediateImpl(T value) {
         this.value = Objects.requireNonNull(value);
      }

      @Override
      public T get() {
         return this.value;
      }

      @Override
      public String toString() {
         return this.value.toString();
      }
   }

   public static class Impl<T> implements LazySupplier<T> {
      private final Supplier<T> getter;
      private T value;
      private boolean initialized = false;

      public Impl(Supplier<T> getter) {
         this.getter = Objects.requireNonNull(getter);
      }

      @Override
      public synchronized T get() {
         if (!this.initialized) {
            this.value = this.getter.get();
            this.initialized = true;
         }

         return this.value;
      }

      @Override
      public void unbound() {
         this.initialized = false;
      }

      @Override
      public String toString() {
         return this.initialized ? this.value.toString() : super.toString();
      }
   }

   public static class NamedImpl<T> implements LazySupplier<T> {
      protected static final Map<String, LazySupplier.NamedImpl<?>> IMPLS = new Object2ObjectOpenHashMap<>(64);
      protected final String name;
      private final Supplier<T> getter;
      private T value;

      protected NamedImpl(String name, Supplier<T> getter) {
         this.name = name;
         this.getter = getter;
      }

      @Override
      public T get() {
         return this.value == null ? (this.value = this.getter.get()) : this.value;
      }

      @Override
      public String toString() {
         return this.value != null ? this.value.toString() : super.toString();
      }
   }
}
