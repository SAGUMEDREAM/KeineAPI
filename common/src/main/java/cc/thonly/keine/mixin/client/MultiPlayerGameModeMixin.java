package cc.thonly.keine.mixin.client;

import cc.thonly.keine.api.callback.AttackBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MultiPlayerGameMode.class})
public abstract class MultiPlayerGameModeMixin {
   @Shadow
   @Final
   private Minecraft minecraft;

   @Shadow
   protected abstract void startPrediction(ClientLevel clientLevel, PredictiveAction predictiveAction);

   @Inject(
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;getAbilities()Lnet/minecraft/world/entity/player/Abilities;",
         ordinal = 0
      )},
      method = {"startDestroyBlock"},
      cancellable = true
   )
   public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
      this.api_fireAttackBlockCallback(pos, direction, info);
   }

   @Unique
   private void api_fireAttackBlockCallback(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
      InteractionResult result = ((AttackBlockCallback)AttackBlockCallback.EVENT.invoker())
         .interact(this.minecraft.player, this.minecraft.level, InteractionHand.MAIN_HAND, pos, direction);
      if (result != InteractionResult.PASS) {
         info.setReturnValue(result == InteractionResult.SUCCESS);
         if (result.consumesAction()) {
            this.startPrediction(this.minecraft.level, id -> new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, pos, direction, id));
         }
      }
   }
}
