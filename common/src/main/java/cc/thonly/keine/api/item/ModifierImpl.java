package cc.thonly.keine.api.item;

import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentPatch.Builder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments.Mutable;

public class ModifierImpl implements Modifier {
   private final ItemStack template;

   public ModifierImpl(ItemStack template) {
      this.template = template;
   }

   @Override
   public <T> void set(DataComponentType<T> type, T object) {
      ItemStackTemplateModifier accessor = ItemStackTemplateModifier.of(this.template);
      DataComponentPatch components = this.template.getComponentsPatch();
      Builder builder = DataComponentPatch.builder();

      for (Entry<DataComponentType<?>, Optional<?>> entry : components.entrySet()) {
         DataComponentType key = entry.getKey();
         Optional optional = entry.getValue();
         if (!optional.isEmpty()) {
            builder.set(key, optional.get());
         }
      }

      builder.set(type, object);
      accessor.keine$setComponents(builder.build());
   }

   @Override
   public void setCount(int count) {
      ItemStackTemplateModifier accessor = ItemStackTemplateModifier.of(this.template);
      if (accessor != null) {
         accessor.keine$setCount(count);
      }
   }

   @Override
   public void replace(Builder builder) {
      ItemStackTemplateModifier accessor = ItemStackTemplateModifier.of(this.template);
      if (accessor != null) {
         accessor.keine$setComponents(builder.build());
      }
   }

   @Override
   public void replace(DataComponentPatch patch) {
      ItemStackTemplateModifier accessor = ItemStackTemplateModifier.of(this.template);
      if (accessor != null) {
         accessor.keine$setComponents(patch);
      }
   }

   @Override
   public void enchant(Holder<Enchantment> enchantment, int level) {
      this.updateEnchantments(enchantments -> enchantments.upgrade(enchantment, level));
   }

   public ItemEnchantments updateEnchantments(Consumer<Mutable> consumer) {
      DataComponentType<ItemEnchantments> componentType = getComponentType(this.template);
      ItemEnchantments oldEnchantments = ItemStackTemplateHelper.get(this.template, componentType);
      if (oldEnchantments == null) {
         return ItemEnchantments.EMPTY;
      } else {
         Mutable mutableEnchantments = new Mutable(oldEnchantments);
         consumer.accept(mutableEnchantments);
         ItemEnchantments newEnchantments = mutableEnchantments.toImmutable();
         this.set(componentType, newEnchantments);
         return newEnchantments;
      }
   }

   public static DataComponentType<ItemEnchantments> getComponentType(ItemStack itemStack) {
      return itemStack.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
   }
}
