package cc.thonly.keine.api.item;

import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class ItemStackTemplateHelper {
   public static ItemStack create(Item item) {
      return new ItemStack(item);
   }

   public static ItemStack create(Item item, int count) {
      return new ItemStack(item, count);
   }

   public static ItemStack create(Item item, int count, DataComponentPatch components) {
      return new ItemStack(item.builtInRegistryHolder(), count, components);
   }

   public static ItemStack create(Item item, BiConsumer<ItemStack, Modifier> consumer) {
      ItemStack template = new ItemStack(item);
      consumer.accept(template, new ModifierImpl(template));
      return template;
   }

   public static ItemStack create(Item item, int count, BiConsumer<ItemStack, Modifier> consumer) {
      ItemStack template = new ItemStack(item, count);
      consumer.accept(template, new ModifierImpl(template));
      return template;
   }

   public static ItemStack create(Item item, int count, DataComponentPatch components, BiConsumer<ItemStack, Modifier> consumer) {
      ItemStack template = new ItemStack(item.builtInRegistryHolder(), count, components);
      consumer.accept(template, new ModifierImpl(template));
      return template;
   }

   public static void modify(ItemStack template, BiConsumer<ItemStack, Modifier> consumer) {
      consumer.accept(template, new ModifierImpl(template));
   }

   public static Component getHoverName(ItemStack template) {
      Component customName = getCustomName(template);
      return customName != null ? customName : getItemName(template);
   }

   public static Component getItemName(ItemStack template) {
      String translationKey = template.getItem().builtInRegistryHolder().unwrapKey().map(key -> ((Item)template.getItem().builtInRegistryHolder().value()).getDescriptionId()).orElse("item.unknown");
      Component result = (Component)(translationKey.equals("item.unknown") ? CommonComponents.EMPTY : Component.translatable(translationKey));
      return getOrDefault(template, DataComponents.ITEM_NAME, result);
   }

   public static Component getCustomName(ItemStack template) {
      Component customName = get(template, DataComponents.CUSTOM_NAME);
      if (customName != null) {
         return customName;
      } else {
         WrittenBookContent content = get(template, DataComponents.WRITTEN_BOOK_CONTENT);
         if (content != null) {
            String title = (String)content.title().raw();
            if (!StringUtil.isBlank(title)) {
               return Component.literal(title);
            }
         }

         return null;
      }
   }

   public static <T> T get(ItemStack template, DataComponentType<T> type) {
      DataComponentPatch components = template.getComponentsPatch();

      for (Entry<DataComponentType<?>, Optional<?>> entry : components.entrySet()) {
         DataComponentType key = entry.getKey();
         Optional optional = entry.getValue();
         if (!Objects.equals(key, type) && !optional.isEmpty()) {
            return (T)optional.get();
         }
      }

      return null;
   }

   public static <T> T getOrDefault(ItemStack template, DataComponentType<T> type, T defVal) {
      T val = get(template, type);
      return val != null ? val : defVal;
   }
}
