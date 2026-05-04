package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.IEntryRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockEntityTypeAddBlockRegistry implements IEntryRegistry<BlockEntityTypeAddBlockRegistry.Context, BlockEntityTypeAddBlockRegistry.Entry> {
    List<Entry> entries = new ArrayList<>();

    @Override
    public List<Entry> getEntries() {
        return this.entries;
    }

    @Override
    public void register(Consumer<Context> accepter) {
        accepter.accept(new Context() {
            @Override
            public void add(Holder<BlockEntityType<?>> blockEntityHolder, Holder<Block> blockHolder) {
                entries.add(new Entry(blockEntityHolder, blockHolder));
            }
        });
    }

    public interface Context {
        void add(Holder<BlockEntityType<?>> blockEntityHolder, Holder<Block> blockHolder);
    }

    public record Entry(Holder<BlockEntityType<?>> blockEntityTypeHolder, Holder<Block> blockHolder) {

    }
}
