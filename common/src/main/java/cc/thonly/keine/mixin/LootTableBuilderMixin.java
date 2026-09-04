package cc.thonly.keine.mixin;

import cc.thonly.keine.api.loot.KeineLootPoolBuilder;
import cc.thonly.keine.api.loot.KeineLootTableBuilder;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.function.Consumer;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootPool.Builder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({net.minecraft.world.level.storage.loot.LootTable.Builder.class})
abstract class LootTableBuilderMixin implements KeineLootTableBuilder {
    @Shadow
    @Final
    @Mutable
    private com.google.common.collect.ImmutableList.Builder<LootPool> pools;
    @Shadow
    @Final
    private com.google.common.collect.ImmutableList.Builder<LootItemFunction> functions;

    @Unique
    private net.minecraft.world.level.storage.loot.LootTable.Builder self() {
        return (net.minecraft.world.level.storage.loot.LootTable.Builder) (Object) this;
    }

    @Override
    public net.minecraft.world.level.storage.loot.LootTable.Builder pool(LootPool pool) {
        this.pools.add(pool);
        return this.self();
    }

    @Override
    public net.minecraft.world.level.storage.loot.LootTable.Builder apply(LootItemFunction function) {
        this.functions.add(function);
        return this.self();
    }

    @Override
    public net.minecraft.world.level.storage.loot.LootTable.Builder pools(Collection<? extends LootPool> pools) {
        this.pools.addAll(pools);
        return this.self();
    }

    @Override
    public net.minecraft.world.level.storage.loot.LootTable.Builder apply(Collection<? extends LootItemFunction> functions) {
        this.functions.addAll(functions);
        return this.self();
    }

    @Override
    public net.minecraft.world.level.storage.loot.LootTable.Builder modifyPools(Consumer<? super Builder> modifier) {
        ArrayList<LootPool> list = new ArrayList<>(this.pools.build());
        ListIterator<LootPool> iterator = list.listIterator();

        while (iterator.hasNext()) {
            Builder poolBuilder = KeineLootPoolBuilder.copyOf(iterator.next());
            modifier.accept(poolBuilder);
            iterator.set(poolBuilder.build());
        }

        this.pools = ImmutableList.builder();
        this.pools.addAll(list);
        return this.self();
    }
}
