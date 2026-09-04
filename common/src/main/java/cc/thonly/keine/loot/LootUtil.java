package cc.thonly.keine.loot;

import cc.thonly.keine.api.loot.LootTableSource;
import cc.thonly.keine.resource.BuiltinModResourcePackSource;
import cc.thonly.keine.resource.PackSourceTracker;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.storage.loot.LootTable;

public final class LootUtil {
   public static final PackSource RESOURCE_PACK_SOURCE = new PackSource() {
      public Component decorate(Component packName) {
         return Component.translatable("pack.nameAndSource", new Object[]{packName, Component.translatable("pack.source.fabricmod")});
      }

      public boolean shouldAddAutomatically() {
         return true;
      }
   };
   public static final ThreadLocal<Map<Identifier, LootTableSource>> SOURCES = ThreadLocal.withInitial(HashMap::new);

   public static LootTableSource determineSource(Resource resource) {
      if (resource != null) {
         PackSource packSource = PackSourceTracker.getSource(resource.source());
         if (packSource == PackSource.BUILT_IN) {
            return LootTableSource.VANILLA;
         }

         if (packSource == RESOURCE_PACK_SOURCE || packSource instanceof BuiltinModResourcePackSource) {
            return LootTableSource.MOD;
         }
      }

      return LootTableSource.DATA_PACK;
   }

   public static Holder<LootTable> getEntryOrDirect(ServerLevel world, LootTable table) {
      HolderLookup.Provider wrapperLookup = world
              .getServer()
              .reloadableRegistries()
              .lookup();

      HolderLookup<LootTable> lootTableRegistryWrapper = wrapperLookup
              .lookup(Registries.LOOT_TABLE)
              .orElseThrow(() -> new IllegalStateException("Failed to fetch LootTable wrapper from WrapperLookup"));

      return lootTableRegistryWrapper
              .listElements()
              .filter(it -> it.value().equals(table))
              .findFirst()
              .map(Function.<Holder<LootTable>>identity())
              .orElseGet(() -> Holder.direct(table));
   }
}
