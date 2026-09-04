package cc.thonly.keine.api.loot;

import cc.thonly.keine.mixin.LootPoolAccessor;
import java.util.Collection;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootPool.Builder;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface KeineLootPoolBuilder {
   default Builder with(LootPoolEntryContainer entry) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default Builder with(Collection<? extends LootPoolEntryContainer> entries) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default Builder conditionally(LootItemCondition condition) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default Builder conditionally(Collection<? extends LootItemCondition> conditions) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default Builder apply(LootItemFunction function) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   default Builder apply(Collection<? extends LootItemFunction> functions) {
      throw new UnsupportedOperationException("Implemented via mixin");
   }

   static Builder copyOf(LootPool pool) {
      LootPoolAccessor accessor = (LootPoolAccessor)pool;
      Builder builder = LootPool.lootPool();
      KeineLootPoolBuilder keineLootPoolBuilder = (KeineLootPoolBuilder)builder;
      builder.setRolls(accessor.keine_getRolls());
      builder.setBonusRolls(accessor.keine_getBonusRolls());
      keineLootPoolBuilder.with(accessor.keine_getEntries());
      keineLootPoolBuilder.conditionally(accessor.keine_getConditions());
      keineLootPoolBuilder.apply(accessor.keine_getFunctions());
      return builder;
   }
}
