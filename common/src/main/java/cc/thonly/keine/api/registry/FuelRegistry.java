package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.IEntryRegistry;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FuelRegistry implements IEntryRegistry<FuelRegistry.Context, FuelRegistry.Entry> {
    List<Entry> entries = new ArrayList<>();

    @Override
    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public void register(Consumer<Context> accepter) {
        accepter.accept(new Context() {

            @Override
            public void addItem(Holder<Item> itemHolder, int value) {
                entries.add(new Entry(itemHolder, null, null, value));
            }

            @Override
            public void addBlock(Holder<Block> blockHolder, int value) {
                entries.add(new Entry(null, blockHolder, null, value));
            }

            @Override
            public void addTag(TagKey<Item> tagKey, int value) {
                entries.add(new Entry(null, null, tagKey, value));
            }
        });
    }

    public interface Context {
        void addItem(Holder<Item> itemHolder, int value);

        void addBlock(Holder<Block> blockHolder, int value);

        void addTag(TagKey<Item> tagKey, int value);
    }

    public record Entry(@Nullable Holder<Item> itemHolder, @Nullable Holder<Block> blockHolder,
                        @Nullable TagKey<Item> itemTagKey, int value) {

    }
}
