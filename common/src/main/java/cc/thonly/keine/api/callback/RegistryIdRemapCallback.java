package cc.thonly.keine.api.callback;

import cc.thonly.keine.api.registry.ListenableRegistry;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface RegistryIdRemapCallback<T> {
   void onRemap(RegistryIdRemapCallback.RemapState<T> var1);

   static <T> Event<RegistryIdRemapCallback<T>> event(Registry<T> registry) {
      return ListenableRegistry.<T>get(registry).keine_getRemapEvent();
   }

   public interface RemapState<T> {
      Int2IntMap getRawIdChangeMap();

      Identifier getIdFromOld(int var1);

      Identifier getIdFromNew(int var1);
   }
}
