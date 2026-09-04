package cc.thonly.keine.api.registry.impl;

import cc.thonly.keine.api.registry.IEntryRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityTypeAddBlockRegistry implements IEntryRegistry<BlockEntityTypeAddBlockRegistry.Context, BlockEntityTypeAddBlockRegistry.Entry> {
   List<BlockEntityTypeAddBlockRegistry.Entry> entries = new ArrayList<>();

   @Override
   public List<BlockEntityTypeAddBlockRegistry.Entry> getEntries() {
      return this.entries;
   }

   @Override
   public void register(Consumer<BlockEntityTypeAddBlockRegistry.Context> accepter) {
      accepter.accept(new BlockEntityTypeAddBlockRegistry.Context() {
         {
            Objects.requireNonNull(BlockEntityTypeAddBlockRegistry.this);
         }

         @Override
         public void add(Holder<BlockEntityType<?>> blockEntityHolder, Holder<Block> blockHolder) {
            BlockEntityTypeAddBlockRegistry.this.entries.add(new BlockEntityTypeAddBlockRegistry.Entry(blockEntityHolder, blockHolder));
         }
      });
   }

   public interface Context {
      void add(Holder<BlockEntityType<?>> blockEntityHolder, Holder<Block> blockHolder);
   }

   public record Entry(Holder<BlockEntityType<?>> blockEntityTypeHolder, Holder<Block> blockHolder) {
   }
}
