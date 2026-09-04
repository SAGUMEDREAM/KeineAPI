package cc.thonly.keine.api.callback;

import cc.thonly.keine.api.loot.LootTableSource;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;

import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import org.jspecify.annotations.Nullable;

public interface LootTableCallback {
    Event<LootTableCallback.Replace> REPLACE = EventFactory.createArrayBacked(LootTableCallback.Replace.class, listeners -> (key, original, source, registries) -> {
        for (LootTableCallback.Replace listener : listeners) {
            LootTable replaced = listener.replaceLootTable(key, original, source, registries);
            if (replaced != null) {
                return replaced;
            }
        }

        return null;
    });
    Event<LootTableCallback.Modify> MODIFY = EventFactory.createArrayBacked(LootTableCallback.Modify.class, listeners -> (key, tableBuilder, source, registries) -> {
        for (LootTableCallback.Modify listener : listeners) {
            listener.modifyLootTable(key, tableBuilder, source, registries);
        }
    });
    Event<LootTableCallback.Loaded> ALL_LOADED = EventFactory.createArrayBacked(LootTableCallback.Loaded.class, listeners -> (resourceManager, lootManager) -> {
        for (LootTableCallback.Loaded listener : listeners) {
            listener.onLootTablesLoaded(resourceManager, lootManager);
        }
    });
    Event<LootTableCallback.ModifyDrops> MODIFY_DROPS = EventFactory.createArrayBacked(LootTableCallback.ModifyDrops.class, listeners -> (entry, context, drops) -> {
        for (LootTableCallback.ModifyDrops listener : listeners) {
            listener.modifyLootTableDrops(entry, context, drops);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onLootTablesLoaded(ResourceManager resourceManager, Registry<LootTable> lootRegistry);
    }

    @FunctionalInterface
    public interface Modify {
        void modifyLootTable(ResourceKey<LootTable> key, Builder tableBuilder, LootTableSource source, Provider registries);
    }

    @FunctionalInterface
    public interface ModifyDrops {
        void modifyLootTableDrops(Holder<LootTable> entry, LootContext context, List<ItemStack> drops);
    }

    @FunctionalInterface
    public interface Replace {
        @Nullable
        LootTable replaceLootTable(ResourceKey<LootTable> key, LootTable original, LootTableSource source, Provider registries);
    }
}
