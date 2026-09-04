package cc.thonly.keine.mixin;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LootPool.class})
public interface LootPoolAccessor {
   @Accessor("rolls")
   NumberProvider keine_getRolls();

   @Accessor("bonusRolls")
   NumberProvider keine_getBonusRolls();

   @Accessor("entries")
   List<LootPoolEntryContainer> keine_getEntries();

   @Accessor("conditions")
   List<LootItemCondition> keine_getConditions();

   @Accessor("functions")
   List<LootItemFunction> keine_getFunctions();
}
