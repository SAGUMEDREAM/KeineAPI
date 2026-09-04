package cc.thonly.keine.api.callback;

import java.util.function.Consumer;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;

public interface ItemStackTooltipCallback {
   Event<ItemStackTooltipCallback> EVENT = EventFactory.createArrayBacked(ItemStackTooltipCallback.class, listeners -> (stack, context, player, component, consumer, type) -> {
      for (ItemStackTooltipCallback listener : listeners) {
         listener.appendTooltip(stack, context, player, component, consumer, type);
      }
   });

   void appendTooltip(
      ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Player player, Consumer<Component> textConsumer, TooltipFlag type
   );
}
