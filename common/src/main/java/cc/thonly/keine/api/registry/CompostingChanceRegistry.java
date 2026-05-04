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

public class CompostingChanceRegistry implements IEntryRegistry<CompostingChanceRegistry.Context, CompostingChanceRegistry.CompostingChanceEntry> {

    List<CompostingChanceEntry> entries = new ArrayList<>();

    @Override
    public List<CompostingChanceEntry> getEntries() {
        return this.entries;
    }

    public void register(Consumer<Context> consumer) {
        consumer.accept(new Context() {

            @Override
            public void addItem(Holder<Item> item, Float value) {
                entries.add(
                        new CompostingChanceEntry(item, null, null, value)
                );
            }

            @Override
            public void addBlock(Holder<Block> block, Float value) {
                entries.add(
                        new CompostingChanceEntry(null, block, null, value)
                );
            }

            @Override
            public void addItemTag(TagKey<Item> tagKey, Float value) {
                entries.add(
                        new CompostingChanceEntry(null, null, tagKey, value)
                );
            }
        });
    }

    public interface Context {
        void addItem(Holder<Item> item, Float value);

        void addBlock(Holder<Block> block, Float value);

        void addItemTag(TagKey<Item> tagKey, Float value);
    }

    public record CompostingChanceEntry(
            @Nullable Holder<Item> item,
            @Nullable Holder<Block> block,
            @Nullable TagKey<Item> tagKey,
            Float value
    ) {
    }
}