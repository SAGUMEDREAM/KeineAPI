package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.callback.RegistryEntryAddedCallback;
import cc.thonly.keine.api.callback.RegistryIdRemapCallback;
import net.blay09.mods.balm.platform.event.Event;
import net.minecraft.core.Registry;

@SuppressWarnings("unchecked")
public interface ListenableRegistry<T> {
   Event<RegistryEntryAddedCallback<T>> keine_getAddObjectEvent();

   Event<RegistryIdRemapCallback<T>> keine_getRemapEvent();

   static <T> ListenableRegistry<T> get(Registry<T> registry) {
      if (!(registry instanceof ListenableRegistry)) {
         throw new IllegalArgumentException("Unsupported blocks: " + registry.key().identifier());
      } else {
         return (ListenableRegistry<T>)registry;
      }
   }
}
