package cc.thonly.keine.api.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ItemStackTemplateModifier {
   void keine$setItem(Holder<Item> item);

   void keine$setCount(int count);

   void keine$setComponents(DataComponentPatch patch);

   static ItemStackTemplateModifier of(ItemStack template) {
      return (ItemStackTemplateModifier)(Object)template;
   }
}
