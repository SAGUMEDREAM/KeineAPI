package cc.thonly.keine.fabric;

import cc.thonly.keine.Keine;
import cc.thonly.keine.api.KeineAPI;
import cc.thonly.keine.api.KeineRegistries;
import cc.thonly.keine.api.callback.LootTableCallback;
import cc.thonly.keine.api.registry.impl.BlockEntityTypeAddBlockRegistry;
import cc.thonly.keine.api.registry.impl.CompostingChanceRegistry;
import cc.thonly.keine.api.registry.impl.EntityDataSerializerRegistry;
import cc.thonly.keine.api.registry.impl.FlammableBlockRegistry;
import cc.thonly.keine.api.registry.impl.FuelRegistry;
import cc.thonly.keine.api.registry.impl.LootTableRegistry;
import cc.thonly.keine.api.registry.impl.StrippableBlockRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.fabric.impl.object.builder.FabricTrackedDataRegistryImpl;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricKeine implements ModInitializer {
    public static boolean SERVER_SIDE_ONLY = false;

    public static void serverSideOnly() {
        SERVER_SIDE_ONLY = true;
    }

    public static boolean isServerSideOnly() {
        return SERVER_SIDE_ONLY;
    }

    public static void loadApiImpl() {
    }

    public void onInitialize() {
        Keine.initialize();
    }

    public static void freeze() {
        KeineAPI api = KeineAPI.getApi();

        for(KeineRegistries registries : api.values()) {
            CompostingChanceRegistry compostingChanceRegistry = registries.compostingChanceRegistry();
            net.fabricmc.fabric.api.registry.CompostingChanceRegistry fabricCompostingChanceRegistry = net.fabricmc.fabric.api.registry.CompostingChanceRegistry.INSTANCE;

            for(CompostingChanceRegistry.CompostingChanceEntry entry : compostingChanceRegistry.getEntries()) {
                Holder<Item> item = entry.item();
                Holder<Block> block = entry.block();
                TagKey<Item> itemTagKey = entry.tagKey();
                Float value = entry.value();
                if (item != null) {
                    fabricCompostingChanceRegistry.add(item.value(), value);
                }

                if (block != null) {
                    fabricCompostingChanceRegistry.add(block.value(), value);
                }

                if (itemTagKey != null) {
                    fabricCompostingChanceRegistry.add(itemTagKey, value);
                }
            }

            FlammableBlockRegistry flammableBlockRegistry = registries.flammableBlockRegistry();
            net.fabricmc.fabric.api.registry.FlammableBlockRegistry fabricFlammableBlockRegistry = net.fabricmc.fabric.api.registry.FlammableBlockRegistry.getDefaultInstance();

            for(FlammableBlockRegistry.Entry entry : flammableBlockRegistry.getEntries()) {
                Holder<Block> blockHolder = entry.blockHolder();
                TagKey<Block> blockTagKey = entry.tagKey();
                int burn = entry.burn();
                int spread = entry.spread();
                if (blockHolder != null) {
                    fabricFlammableBlockRegistry.add(blockHolder.value(), burn, spread);
                }

                if (blockTagKey != null) {
                    fabricFlammableBlockRegistry.add(blockTagKey, burn, spread);
                }
            }

            StrippableBlockRegistry strippableBlockRegistry = registries.strippableBlockRegistry();

            for(StrippableBlockRegistry.Entry entry : strippableBlockRegistry.getEntries()) {
                Holder<Block> input = entry.input();
                Holder<Block> output = entry.output();
                net.fabricmc.fabric.api.registry.StrippableBlockRegistry.register(input.value(), output.value());
            }

            for(FuelRegistry.Entry entry : registries.fuelRegistry().getEntries()) {
                FuelRegistryEvents.BUILD.register((builder, context) -> {
                    Holder<Item> itemHolder = entry.itemHolder();
                    Holder<Block> blockHolder = entry.blockHolder();
                    TagKey<Item> itemTagKey = entry.itemTagKey();
                    int value = entry.value();
                    if (itemHolder != null) {
                        builder.add(itemHolder.value(), value);
                    }

                    if (blockHolder != null) {
                        builder.add(blockHolder.value(), value);
                    }

                    if (itemTagKey != null) {
                        builder.add(itemTagKey, value);
                    }

                });
            }

            BlockEntityTypeAddBlockRegistry blockEntityTypeAddBlockRegistry = registries.blockEntityTypeAddBlockRegistry();

            for(BlockEntityTypeAddBlockRegistry.Entry entry : blockEntityTypeAddBlockRegistry.getEntries()) {
                Holder<BlockEntityType<?>> blockEntityTypeHolder = entry.blockEntityTypeHolder();
                BlockEntityType<?> blockEntityType = blockEntityTypeHolder.value();
                Holder<Block> blockHolder = entry.blockHolder();
                blockEntityType.addSupportedBlock(blockHolder.value());
            }

            LootTableRegistry lootTableRegistry = registries.lootTableRegistry();
            lootTableRegistry.getModifyMap().values().forEach(LootTableCallback.MODIFY::register);
            lootTableRegistry.getLoadedMap().values().forEach(LootTableCallback.ALL_LOADED::register);
            lootTableRegistry.getModifyDropsMap().values().forEach(LootTableCallback.MODIFY_DROPS::register);
            EntityDataSerializerRegistry entityDataSerializerRegistry = registries.entityDataSerializerRegistry();

            for(EntityDataSerializerRegistry.Entry entry : entityDataSerializerRegistry.getEntries()) {
                FabricTrackedDataRegistryImpl.register(entry.id(), entry.serializer());
            }
        }

    }
}
