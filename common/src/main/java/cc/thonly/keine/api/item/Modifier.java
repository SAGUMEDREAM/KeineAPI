package cc.thonly.keine.api.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentPatch.Builder;
import net.minecraft.world.item.enchantment.Enchantment;

public interface Modifier {
   <T> void set(DataComponentType<T> type, T object);

   void setCount(int count);

   void enchant(Holder<Enchantment> enchantment, int level);

   void replace(DataComponentPatch builder);

   void replace(Builder builder);
}
