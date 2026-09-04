package cc.thonly.keine.mixin;

import cc.thonly.keine.api.callback.AttackBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerPlayerGameMode.class})
public class ServerPlayerGameModeMixin {
   @Final
   @Shadow
   protected ServerPlayer player;
   @Shadow
   protected ServerLevel level;

   @Inject(
      at = {@At("HEAD")},
      method = {"handleBlockBreakAction"},
      cancellable = true
   )
   public void startBlockBreak(BlockPos pos, Action playerAction, Direction direction, int worldHeight, int i, CallbackInfo info) {
      if (playerAction == Action.START_DESTROY_BLOCK) {
         InteractionResult result = ((AttackBlockCallback)AttackBlockCallback.EVENT.invoker())
            .interact(this.player, this.level, InteractionHand.MAIN_HAND, pos, direction);
         if (result != InteractionResult.PASS) {
            this.player.connection.send(new ClientboundBlockUpdatePacket(this.level, pos));
            if (this.level.getBlockState(pos).hasBlockEntity()) {
               BlockEntity blockEntity = this.level.getBlockEntity(pos);
               if (blockEntity != null) {
                  Packet<ClientGamePacketListener> updatePacket = blockEntity.getUpdatePacket();
                  if (updatePacket != null) {
                     this.player.connection.send(updatePacket);
                  }
               }
            }

            info.cancel();
         }
      }
   }
}
