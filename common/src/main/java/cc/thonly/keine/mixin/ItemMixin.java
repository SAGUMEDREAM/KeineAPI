package cc.thonly.keine.mixin;

import cc.thonly.keine.api.callback.ItemAttackHitCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Item.class})
public abstract class ItemMixin implements ItemLike, FeatureElement {
   @Inject(
      method = {"hurtEnemy"},
      at = {@At("TAIL")}
   )
   public void keine_api$postHitCallback(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
      ((ItemAttackHitCallback)ItemAttackHitCallback.EVENT.invoker()).postHit(stack, target, attacker);
   }
}
