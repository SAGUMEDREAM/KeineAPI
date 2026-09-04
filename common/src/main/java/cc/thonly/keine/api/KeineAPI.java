package cc.thonly.keine.api;

import cc.thonly.keine.Keine;
import cc.thonly.keine.network.DataSyncRequest;
import java.util.Collection;

import net.blay09.mods.balm.Balm;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface KeineAPI {
   default KeineRegistries getRegistries(String modId) {
      return Keine.id2Registries.computeIfAbsent(modId, KeineRegistries::new);
   }

   default KeineRegistries globalRegistries() {
      return Keine.id2Registries.computeIfAbsent("global", KeineRegistries::new);
   }

   default Collection<KeineRegistries> values() {
      return Keine.id2Registries.values();
   }

   <T> void unfreeze(Registry<T> registry);

   boolean directRegister();

   static KeineAPI getApi() {
      return Keine.api();
   }

   static void requestUpdateEntity(Player player, Entity entity) {
      Level level = entity.level();
      if (level.isClientSide()) {
         Balm.networking().sendToServer(new DataSyncRequest(entity.getId(), level.dimension().identifier()));
      }
   }
}
