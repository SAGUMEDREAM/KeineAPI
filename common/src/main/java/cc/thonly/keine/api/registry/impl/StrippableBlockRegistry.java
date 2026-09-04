package cc.thonly.keine.api.registry.impl;

import cc.thonly.keine.api.registry.IEntryRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public class StrippableBlockRegistry implements IEntryRegistry<StrippableBlockRegistry.Context, StrippableBlockRegistry.Entry> {
   List<StrippableBlockRegistry.Entry> entries = new ArrayList<>();

   @Override
   public List<StrippableBlockRegistry.Entry> getEntries() {
      return this.entries;
   }

   @Override
   public void register(Consumer<StrippableBlockRegistry.Context> consumer) {
      consumer.accept(new StrippableBlockRegistry.Context() {
         {
            Objects.requireNonNull(StrippableBlockRegistry.this);
         }

         @Override
         public void add(Holder<Block> input, Holder<Block> output) {
            StrippableBlockRegistry.this.entries.add(new StrippableBlockRegistry.Entry(input, output));
         }
      });
   }

   public interface Context {
      void add(Holder<Block> input, Holder<Block> output);
   }

   public record Entry(Holder<Block> input, Holder<Block> output) {
   }
}
