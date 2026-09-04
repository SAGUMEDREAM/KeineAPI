package cc.thonly.keine.api.registry.impl;

import cc.thonly.keine.api.callback.LootTableCallback;
import cc.thonly.keine.api.registry.IEntryRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
public class LootTableRegistry implements IEntryRegistry<LootTableRegistry.Context, LootTableRegistry.Entry> {
   final Map<Integer, LootTableCallback.Replace> replaceMap = new Object2ObjectLinkedOpenHashMap<>();
   final Map<Integer, LootTableCallback.Modify> modifyMap = new Object2ObjectLinkedOpenHashMap<>();
   final Map<Integer, LootTableCallback.Loaded> loadedMap = new Object2ObjectLinkedOpenHashMap<>();
   final Map<Integer, LootTableCallback.ModifyDrops> modifyDropsMap = new Object2ObjectLinkedOpenHashMap<>();

   @Override
   public List<LootTableRegistry.Entry> getEntries() {
      return List.of();
   }

   @Override
   public void register(Consumer<LootTableRegistry.Context> consumer) {
      consumer.accept(new LootTableRegistry.Context() {
         @Override
         public synchronized int replaceLootTable(LootTableCallback.Replace event) {
            int id = LootTableRegistry.this.replaceMap.size();
            LootTableRegistry.this.replaceMap.put(id, event);
            return id;
         }

         @Override
         public synchronized int modifyLootTable(LootTableCallback.Modify event) {
            int id = LootTableRegistry.this.modifyMap.size();
            LootTableRegistry.this.modifyMap.put(id, event);
            return id;
         }

         @Override
         public synchronized int onLootTablesLoaded(LootTableCallback.Loaded event) {
            int id = LootTableRegistry.this.loadedMap.size();
            LootTableRegistry.this.loadedMap.put(id, event);
            return id;
         }

         @Override
         public synchronized int modifyLootTableDrops(LootTableCallback.ModifyDrops event) {
            int id = LootTableRegistry.this.modifyDropsMap.size();
            LootTableRegistry.this.modifyDropsMap.put(id, event);
            return id;
         }
      });
   }

   public Map<Integer, LootTableCallback.Replace> getReplaceMap() {
      return this.replaceMap;
   }

   public Map<Integer, LootTableCallback.Modify> getModifyMap() {
      return this.modifyMap;
   }

   public Map<Integer, LootTableCallback.Loaded> getLoadedMap() {
      return this.loadedMap;
   }

   public Map<Integer, LootTableCallback.ModifyDrops> getModifyDropsMap() {
      return this.modifyDropsMap;
   }

   public interface Context {
      int replaceLootTable(LootTableCallback.Replace event);

      int modifyLootTable(LootTableCallback.Modify event);

      int onLootTablesLoaded(LootTableCallback.Loaded event);

      int modifyLootTableDrops(LootTableCallback.ModifyDrops event);
   }

   public record Entry() {
   }
}
