package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.IEntryRegistry;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FlammableBlockRegistry implements IEntryRegistry<FlammableBlockRegistry.Context, FlammableBlockRegistry.Entry> {
    List<Entry> entries = new ArrayList<>();

    @Override
    public List<Entry> getEntries() {
        return entries;
    }

    public void register(Consumer<Context> consumer) {
        consumer.accept(new Context() {

            @Override
            public void addBlock(Holder<Block> block, int burn, int spread) {
                entries.add(
                        new Entry(block, null, burn, spread)
                );
            }

            @Override
            public void addTag(TagKey<Block> tag, int burn, int spread) {
                entries.add(
                        new Entry(null, tag, burn, spread)
                );
            }
        });
    }

    public interface Context {
        void addBlock(Holder<Block> block, int burn, int spread);

        void addTag(TagKey<Block> tag, int burn, int spread);
    }

    public record Entry(@Nullable Holder<Block> blockHolder, @Nullable TagKey<Block> tagKey, int burn,
                        int spread) {
    }

}
