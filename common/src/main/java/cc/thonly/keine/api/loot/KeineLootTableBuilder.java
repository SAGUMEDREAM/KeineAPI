package cc.thonly.keine.api.loot;

import cc.thonly.keine.mixin.LootTableAccessor;
import java.util.Collection;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootPool.Builder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface KeineLootTableBuilder {
   default net.minecraft.world.level.storage.loot.LootTable.Builder pool(LootPool pool) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default net.minecraft.world.level.storage.loot.LootTable.Builder apply(LootItemFunction function) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default net.minecraft.world.level.storage.loot.LootTable.Builder pools(Collection<? extends LootPool> pools) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default net.minecraft.world.level.storage.loot.LootTable.Builder apply(Collection<? extends LootItemFunction> functions) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default net.minecraft.world.level.storage.loot.LootTable.Builder modifyPools(Consumer<? super Builder> modifier) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   static net.minecraft.world.level.storage.loot.LootTable.Builder copyOf(LootTable table) {
      net.minecraft.world.level.storage.loot.LootTable.Builder builder = LootTable.lootTable();
      LootTableAccessor accessor = (LootTableAccessor)table;
      KeineLootTableBuilder keineLootTableBuilder = (KeineLootTableBuilder)builder;
      builder.setParamSet(table.getParamSet());
      keineLootTableBuilder.pools(accessor.keine_getPools());
      keineLootTableBuilder.apply(accessor.keine_getFunctions());
      accessor.keine_getRandomSequenceId().ifPresent(builder::setRandomSequence);
      return builder;
   }
}
