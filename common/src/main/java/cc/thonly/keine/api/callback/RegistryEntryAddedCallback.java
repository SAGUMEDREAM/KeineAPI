package cc.thonly.keine.api.callback;

import cc.thonly.keine.api.registry.ListenableRegistry;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import java.util.function.Consumer;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface RegistryEntryAddedCallback<T> {
   void onEntryAdded(int var1, Identifier var2, T var3);

   static <T> Event<RegistryEntryAddedCallback<T>> event(Registry<T> registry) {
      return ListenableRegistry.get(registry).keine_getAddObjectEvent();
   }

   static <T> void allEntries(Registry<T> registry, Consumer<Holder.Reference<T>> consumer) {
      event(registry).register((rawId, id, object) -> consumer.accept(registry.get(id).orElseThrow()));
      registry.listElements().toList().forEach(consumer);
   }
}