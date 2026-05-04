package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.IEntryRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StrippableBlockRegistry implements IEntryRegistry<StrippableBlockRegistry.Context, StrippableBlockRegistry.Entry> {
    List<Entry> entries = new ArrayList<>();

    @Override
    public List<Entry> getEntries() {
        return entries;
    }

    public void register(Consumer<Context> consumer) {
        consumer.accept(new Context() {
            @Override
            public void add(Holder<Block> input, Holder<Block> output) {
                entries.add(new Entry(input, output));
            }
        });
    }

    public interface Context {
        void add(Holder<Block> input, Holder<Block> output);
    }

    public record Entry(Holder<Block> input, Holder<Block> output) {

    }
}
