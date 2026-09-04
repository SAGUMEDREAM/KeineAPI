package cc.thonly.keine.mixin;

import java.util.List;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LootTable.class})
public interface LootTableAccessor {
   @Accessor("pools")
   List<LootPool> keine_getPools();

   @Accessor("functions")
   List<LootItemFunction> keine_getFunctions();

   @Accessor("randomSequence")
   Optional<Identifier> keine_getRandomSequenceId();
}
