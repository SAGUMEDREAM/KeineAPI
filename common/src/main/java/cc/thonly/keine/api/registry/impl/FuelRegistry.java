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

public class FuelRegistry implements IEntryRegistry<FuelRegistry.Context, FuelRegistry.Entry> {
   List<FuelRegistry.Entry> entries = new ArrayList<>();

   @Override
   public List<FuelRegistry.Entry> getEntries() {
      return this.entries;
   }

   @Override
   public void register(Consumer<FuelRegistry.Context> accepter) {
      accepter.accept(new FuelRegistry.Context() {
         {
            Objects.requireNonNull(FuelRegistry.this);
         }

         @Override
         public void addItem(Holder<Item> itemHolder, int value) {
            FuelRegistry.this.entries.add(new FuelRegistry.Entry(itemHolder, null, null, value));
         }

         @Override
         public void addBlock(Holder<Block> blockHolder, int value) {
            FuelRegistry.this.entries.add(new FuelRegistry.Entry(null, blockHolder, null, value));
         }

         @Override
         public void addTag(TagKey<Item> tagKey, int value) {
            FuelRegistry.this.entries.add(new FuelRegistry.Entry(null, null, tagKey, value));
         }
      });
   }

   public interface Context {
      void addItem(Holder<Item> itemHolder, int value);

      void addBlock(Holder<Block> blockHolder, int value);

      void addTag(TagKey<Item> tagKey, int value);
   }

   public record Entry(@Nullable Holder<Item> itemHolder, @Nullable Holder<Block> blockHolder, @Nullable TagKey<Item> itemTagKey, int value) {
   }
}
