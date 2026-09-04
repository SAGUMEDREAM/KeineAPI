package cc.thonly.keine.api.registry.impl;

import cc.thonly.keine.api.registry.IEntryRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class CompostingChanceRegistry implements IEntryRegistry<CompostingChanceRegistry.Context, CompostingChanceRegistry.CompostingChanceEntry> {
   List<CompostingChanceRegistry.CompostingChanceEntry> entries = new ArrayList<>();

   @Override
   public List<CompostingChanceRegistry.CompostingChanceEntry> getEntries() {
      return this.entries;
   }

   @Override
   public void register(Consumer<CompostingChanceRegistry.Context> consumer) {
      consumer.accept(new CompostingChanceRegistry.Context() {
         {
            Objects.requireNonNull(CompostingChanceRegistry.this);
         }

         @Override
         public void addItem(Holder<Item> item, Float value) {
            CompostingChanceRegistry.this.entries.add(new CompostingChanceRegistry.CompostingChanceEntry(item, null, null, value));
         }

         @Override
         public void addBlock(Holder<Block> block, Float value) {
            CompostingChanceRegistry.this.entries.add(new CompostingChanceRegistry.CompostingChanceEntry(null, block, null, value));
         }

         @Override
         public void addItemTag(TagKey<Item> tagKey, Float value) {
            CompostingChanceRegistry.this.entries.add(new CompostingChanceRegistry.CompostingChanceEntry(null, null, tagKey, value));
         }
      });
   }

   public record CompostingChanceEntry(@Nullable Holder<Item> item, @Nullable Holder<Block> block, @Nullable TagKey<Item> tagKey, Float value) {
   }

   public interface Context {
      void addItem(Holder<Item> item, Float value);

      void addBlock(Holder<Block> block, Float value);

      void addItemTag(TagKey<Item> tagKey, Float value);
   }
}
