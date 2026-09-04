package cc.thonly.keine.neoforge;

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
import cc.thonly.keine.mixin.FireBlockAccessor;
import cc.thonly.keine.neoforge.mixin.AxeItemAccessor;
import cc.thonly.keine.neoforge.mixin.BlockEntityTypeAccessor;
import cc.thonly.keine.util.DeferredSet;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.registries.BaseMappedRegistry;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"rawtypes", "unchecked", "UnstableApiUsage", "deprecation"})
@Mod("keine")
public class NeoForgeKeine {
    private static final Logger log = LoggerFactory.getLogger(NeoForgeKeine.class);
    public static final Object2FloatMap<ItemLike> COMPOSTABLES = new Object2FloatOpenHashMap();
    public static final Map<Holder<Block>, Holder<Block>> STRIPPABLES = new Object2ObjectOpenHashMap();

    public static void loadApiImpl() {
    }

    public NeoForgeKeine(ModContainer modContainer, IEventBus modEventBus) {
        Keine.initialize();
        loadApiImpl();
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onBlockEntityTypeAddBlock);
        NeoForge.EVENT_BUS.addListener(this::onFuelBurnTime);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            KeineAPI api = KeineAPI.getApi();

            for(KeineRegistries registries : api.values()) {
                CompostingChanceRegistry compostingChanceRegistry = registries.compostingChanceRegistry();

                for(CompostingChanceRegistry.CompostingChanceEntry entry : compostingChanceRegistry.getEntries()) {
                    Holder<Item> item = entry.item();
                    Holder<Block> block = entry.block();
                    Float value = entry.value();
                    if (item != null) {
                        COMPOSTABLES.put(item.value(), value);
                    }

                    if (block != null) {
                        COMPOSTABLES.put(block.value().asItem(), value);
                    }
                }

                FlammableBlockRegistry flammableBlockRegistry = registries.flammableBlockRegistry();
                FireBlockAccessor fire = (FireBlockAccessor)Blocks.FIRE;

                for(FlammableBlockRegistry.Entry entry : flammableBlockRegistry.getEntries()) {
                    Holder<Block> blockHolder = entry.blockHolder();
                    int burn = entry.burn();
                    int spread = entry.spread();
                    if (blockHolder != null) {
                        fire.keineApi$setFlammable((Block)blockHolder.value(), spread, burn);
                    }
                }

                StrippableBlockRegistry strippableBlockRegistry = registries.strippableBlockRegistry();

                for(StrippableBlockRegistry.Entry entry : strippableBlockRegistry.getEntries()) {
                    Holder<Block> input = entry.input();
                    Holder<Block> output = entry.output();
                    STRIPPABLES.put(input, output);
                }

                if (AxeItemAccessor.getStrippables() instanceof ImmutableMap) {
                    AxeItemAccessor.setStrippables(new HashMap(AxeItemAccessor.getStrippables()));
                }

                STRIPPABLES.forEach((inputx, outputx) -> AxeItemAccessor.getStrippables().put((Block)inputx.value(), (Block)outputx.value()));
                LootTableRegistry lootTableRegistry = registries.lootTableRegistry();

                lootTableRegistry.getReplaceMap().values().forEach(
                        LootTableCallback.REPLACE::register
                );

                lootTableRegistry.getModifyMap().values().forEach(
                        LootTableCallback.MODIFY::register
                );

                lootTableRegistry.getLoadedMap().values().forEach(
                        LootTableCallback.ALL_LOADED::register
                );

                lootTableRegistry.getModifyDropsMap().values().forEach(
                        LootTableCallback.MODIFY_DROPS::register
                );
                EntityDataSerializerRegistry entityDataSerializerRegistry = registries.entityDataSerializerRegistry();

                for(EntityDataSerializerRegistry.Entry entry : entityDataSerializerRegistry.getEntries()) {
                    BaseMappedRegistry<EntityDataSerializer<?>> registry = (BaseMappedRegistry)NeoForgeRegistries.ENTITY_DATA_SERIALIZERS;
                    if (registry instanceof DefaultedMappedRegistry mappedRegistry) {
                        mappedRegistry.unfreeze(false);
                    }

                    if (registry instanceof MappedRegistry mappedRegistry) {
                        mappedRegistry.unfreeze(false);
                    }

                    Registry.register(registry, entry.id(), entry.serializer());
                }
            }

        });
    }

    @SubscribeEvent
    public void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        KeineAPI api = KeineAPI.getApi();

        for(KeineRegistries registries : api.values()) {
            FuelRegistry fuelRegistry = registries.fuelRegistry();

            for(FuelRegistry.Entry entry : fuelRegistry.getEntries()) {
                if (entry.itemHolder() != null && entry.itemHolder().value() == item) {
                    event.setBurnTime(entry.value());
                }

                if (entry.blockHolder() != null) {
                    Item blockItem = ((Block)entry.blockHolder().value()).asItem();
                    if (blockItem != Items.AIR && blockItem == item) {
                        event.setBurnTime(entry.value());
                    }
                }

                if (entry.itemTagKey() != null && stack.is(entry.itemTagKey())) {
                    event.setBurnTime(entry.value());
                }
            }
        }

    }

    @SubscribeEvent
    public void onBlockEntityTypeAddBlock(BlockEntityTypeAddBlocksEvent event) {
        KeineAPI api = KeineAPI.getApi();

        for(KeineRegistries registries : api.values()) {
            BlockEntityTypeAddBlockRegistry blockEntityTypeAddBlockRegistry = registries.blockEntityTypeAddBlockRegistry();

            for(BlockEntityTypeAddBlockRegistry.Entry entry : blockEntityTypeAddBlockRegistry.getEntries()) {
                try {
                    BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor)entry.blockEntityTypeHolder().value();
                    Set<Block> oldSet = accessor.api$getBlocks();
                    if (oldSet.getClass().getSimpleName().contains("DeferredSet") && !oldSet.getClass().getName().startsWith("cc.thonly")) {
                        DeferredSet<Block> lazySet = new DeferredSet<>(() -> {
                            Set<Block> set = accessor.api$getBlocks();
                            set.add((Block)entry.blockHolder().value());
                            return set;
                        });
                        accessor.api$setBlocks(lazySet);
                    } else {
                        Set<Block> hashSet = accessor.api$getBlocks();
                        if (!(oldSet instanceof HashSet)) {
                            hashSet = new HashSet<>(oldSet);
                            accessor.api$setBlocks(hashSet);
                        }

                        hashSet.add((Block)entry.blockHolder().value());
                    }
                } catch (Exception e) {
                    log.error("Error in onBlockEntityTypeAddBlock", e);
                }
            }
        }

    }
}
