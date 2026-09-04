package cc.thonly.keine.api.registry.impl;

import cc.thonly.keine.api.registry.IEntryRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class FlammableBlockRegistry implements IEntryRegistry<FlammableBlockRegistry.Context, FlammableBlockRegistry.Entry> {
   List<FlammableBlockRegistry.Entry> entries = new ArrayList<>();

   @Override
   public List<FlammableBlockRegistry.Entry> getEntries() {
      return this.entries;
   }

   @Override
   public void register(Consumer<FlammableBlockRegistry.Context> consumer) {
      consumer.accept(new FlammableBlockRegistry.Context() {
         {
            Objects.requireNonNull(FlammableBlockRegistry.this);
         }

         @Override
         public void addBlock(Holder<Block> block, int burn, int spread) {
            FlammableBlockRegistry.this.entries.add(new FlammableBlockRegistry.Entry(block, null, burn, spread));
         }

         @Override
         public void addTag(TagKey<Block> tag, int burn, int spread) {
            FlammableBlockRegistry.this.entries.add(new FlammableBlockRegistry.Entry(null, tag, burn, spread));
         }
      });
   }

   public interface Context {
      void addBlock(Holder<Block> block, int burn, int spread);

      void addTag(TagKey<Block> tag, int burn, int spread);
   }

   public record Entry(@Nullable Holder<Block> blockHolder, @Nullable TagKey<Block> tagKey, int burn, int spread) {
   }
}
