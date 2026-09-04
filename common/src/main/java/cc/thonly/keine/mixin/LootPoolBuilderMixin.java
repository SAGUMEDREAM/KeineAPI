package cc.thonly.keine.mixin;

import cc.thonly.keine.api.loot.KeineLootPoolBuilder;
import java.util.Collection;
import net.minecraft.world.level.storage.loot.LootPool.Builder;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Builder.class})
abstract class LootPoolBuilderMixin implements KeineLootPoolBuilder {
   @Shadow
   @Final
   private com.google.common.collect.ImmutableList.Builder<LootPoolEntryContainer> entries;
   @Shadow
   @Final
   private com.google.common.collect.ImmutableList.Builder<LootItemCondition> conditions;
   @Shadow
   @Final
   private com.google.common.collect.ImmutableList.Builder<LootItemFunction> functions;

   @Unique
   private Builder self() {
      return (Builder)(Object)this;
   }

   @Override
   public Builder with(LootPoolEntryContainer entry) {
      this.entries.add(entry);
      return this.self();
   }

   @Override
   public Builder with(Collection<? extends LootPoolEntryContainer> entries) {
      this.entries.addAll(entries);
      return this.self();
   }

   @Override
   public Builder conditionally(LootItemCondition condition) {
      this.conditions.add(condition);
      return this.self();
   }

   @Override
   public Builder conditionally(Collection<? extends LootItemCondition> conditions) {
      this.conditions.addAll(conditions);
      return this.self();
   }

   @Override
   public Builder apply(LootItemFunction function) {
      this.functions.add(function);
      return this.self();
   }

   @Override
   public Builder apply(Collection<? extends LootItemFunction> functions) {
      this.functions.addAll(functions);
      return this.self();
   }
}
