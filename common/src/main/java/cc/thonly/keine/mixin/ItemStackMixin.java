package cc.thonly.keine.mixin;

import cc.thonly.keine.api.callback.ItemStackTooltipCallback;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemStack.class})
public abstract class ItemStackMixin {
   @Inject(
      method = {"addDetailsToTooltip"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;",
         ordinal = 0
      )}
   )
   public void keine_api$appendTooltipCallback(
      TooltipContext context, TooltipDisplay displayComponent, Player player, TooltipFlag type, Consumer<Component> textConsumer, CallbackInfo ci
   ) {
      ItemStack itemStack = (ItemStack)(Object)this;
      ItemStackTooltipCallback.EVENT.invoker().appendTooltip(itemStack, context, displayComponent, player, textConsumer, type);
   }
}
