package cc.thonly.keine.api.item;

import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings({"unchecked", "deprecation", "ConstantValue"})
public interface ItemLikeEntry extends ItemLike {
   <T extends ItemLike> Holder<T> asHolder();

   default ItemStack createStack() {
      return this.createStack(1, DataComponentPatch.builder().build());
   }

   default ItemStack createStack(int count) {
      return this.createStack(count, DataComponentPatch.builder().build());
   }

   default ItemStack createStack(int count, DataComponentPatch componentPatch) {
      return new ItemStack(this.asHolder(), count, componentPatch);
   }

   default ItemStack createTemplate() {
      return this.createTemplate(1, DataComponentPatch.builder().build());
   }

   default ItemStack createTemplate(int count) {
      return this.createTemplate(count, DataComponentPatch.builder().build());
   }

   default ItemStack createTemplate(int count, DataComponentPatch componentPatch) {
      return new ItemStack(this.asHolder(), count, componentPatch);
   }

   default Component getHoverName(ItemStack template) {
      Component customName = this.getCustomName(template);
      return customName != null ? customName : this.getItemName(template);
   }

   default Component getItemName(ItemStack template) {
      String translationKey = template.getItem().builtInRegistryHolder().unwrapKey().map(var1 -> template.getItem().builtInRegistryHolder().value().getDescriptionId()).orElse("item.unknown");
      Component result = translationKey.equals("item.unknown") ? CommonComponents.EMPTY : Component.translatable(translationKey);
      return this.getOrDefault(template, DataComponents.ITEM_NAME, result);
   }

   default Component getCustomName(ItemStack template) {
      Component customName = this.get(template, DataComponents.CUSTOM_NAME);
      if (customName != null) {
         return customName;
      } else {
         WrittenBookContent content = this.get(template, DataComponents.WRITTEN_BOOK_CONTENT);
         if (content != null) {
            String title = (String)content.title().raw();
            if (!StringUtil.isBlank(title)) {
               return Component.literal(title);
            }
         }

         return null;
      }
   }

   default <T> T get(ItemStack template, DataComponentType<T> type) {
      DataComponentPatch components = template.getComponentsPatch();

      for (Entry<DataComponentType<?>, Optional<?>> entry : components.entrySet()) {
         DataComponentType<?> key = entry.getKey();
         Optional<?> optional = entry.getValue();
         if (!Objects.equals(key, type) && !optional.isEmpty()) {
            return (T)optional.get();
         }
      }

      return null;
   }

   default <T> T getOrDefault(ItemStack template, DataComponentType<T> type, T defVal) {
      T val = this.get(template, type);
      return val != null ? val : defVal;
   }
}
