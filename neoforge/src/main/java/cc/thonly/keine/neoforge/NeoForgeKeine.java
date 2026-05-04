package cc.thonly.keine.neoforge;

import cc.thonly.keine.api.KeineAPI;
import cc.thonly.keine.api.KeineRegistries;
import cc.thonly.keine.api.registry.*;
import cc.thonly.keine.neoforge.mixin.BlockEntityTypeAccessor;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import cc.thonly.keine.Keine;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

import java.util.Map;

@SuppressWarnings("deprecation")
@Mod(Keine.MOD_ID)
public class NeoForgeKeine {
    public static final Object2FloatMap<ItemLike> COMPOSTABLES = new Object2FloatOpenHashMap<>();
    public static final Map<Holder<Block>, Holder<Block>> STRIPPABLES = new Object2ObjectOpenHashMap<>();

    public static void loadApiImpl() {
        Keine.enable(new KeineAPI() {
            @Override
            public <T> void unfreeze(Registry<T> registry) {
                if (registry instanceof MappedRegistry<T> mappedRegistry) {
                    mappedRegistry.unfreeze(false);
                }
            }
        });
    }

    public NeoForgeKeine(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        Balm.initializeMod(Keine.MOD_ID, context, Keine::initialize);
        loadApiImpl();

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onBlockEntityTypeAddBlock);
        NeoForge.EVENT_BUS.addListener(this::onFuelBurnTime);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            KeineAPI api = KeineAPI.getApi();
            for (KeineRegistries registries : api.values()) {
                CompostingChanceRegistry compostingChanceRegistry = registries.compostingChanceRegistry();
                for (CompostingChanceRegistry.CompostingChanceEntry entry : compostingChanceRegistry.getEntries()) {
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
                FireBlock fire = (FireBlock) Blocks.FIRE;
                for (FlammableBlockRegistry.Entry entry : flammableBlockRegistry.getEntries()) {
                    Holder<Block> blockHolder = entry.blockHolder();
                    int burn = entry.burn();
                    int spread = entry.spread();
                    if (blockHolder != null) {
                        fire.setFlammable(blockHolder.value(), spread, burn);
                    }
                }
                StrippableBlockRegistry strippableBlockRegistry = registries.strippableBlockRegistry();
                for (StrippableBlockRegistry.Entry entry : strippableBlockRegistry.getEntries()) {
                    Holder<Block> input = entry.input();
                    Holder<Block> output = entry.output();
                    STRIPPABLES.put(input, output);
                }
            }
        });
    }

    @SubscribeEvent
    public void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        KeineAPI api = KeineAPI.getApi();

        for (KeineRegistries registries : api.values()) {
            FuelRegistry fuelRegistry = registries.fuelRegistry();

            for (FuelRegistry.Entry entry : fuelRegistry.getEntries()) {
                if (entry.itemHolder() != null) {
                    if (entry.itemHolder().value() == item) {
                        event.setBurnTime(entry.value());
                    }
                }

                if (entry.blockHolder() != null) {
                    Item blockItem = entry.blockHolder().value().asItem();
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

        for (KeineRegistries registries : api.values()) {
            BlockEntityTypeAddBlockRegistry blockEntityTypeAddBlockRegistry = registries.blockEntityTypeAddBlockRegistry();
            for (BlockEntityTypeAddBlockRegistry.Entry entry : blockEntityTypeAddBlockRegistry.getEntries()) {
                BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) entry.blockEntityTypeHolder().value();
                accessor.api$getBlocks().add(entry.blockHolder().value());
            }
        }

    }
}
