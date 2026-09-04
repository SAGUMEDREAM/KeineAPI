package cc.thonly.keine.mixin;

import cc.thonly.keine.api.entity.EntityDataProviders;
import cc.thonly.keine.network.DataSyncPacket;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.blay09.mods.balm.Balm;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.level.ServerEntity.Synchronizer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerEntity.class})
public class ServerEntityMixin {
   @Shadow
   @Final
   private Entity entity;
   @Shadow
   @Final
   private Synchronizer synchronizer;

   @Inject(
      method = {"addPairing"},
      at = {@At("RETURN")}
   )
   private void keine$onStartedTracking(ServerPlayer player, CallbackInfo ci) {
      EntityDataProviders.sync(player, this.entity);
   }

   @Inject(
      method = {"sendPairingData"},
      at = {@At("RETURN")}
   )
   private void keine$sendPairingData(ServerPlayer player, Consumer<Packet<? super ClientGamePacketListener>> broadcast, CallbackInfo ci) {
      DataSyncPacket packet = EntityDataProviders.getPacket(this.entity);
      if (packet != null) {
         Balm.networking().sendTo(player, packet);
      }
   }

   @Inject(
      method = {"sendDirtyEntityData"},
      at = {@At("RETURN")}
   )
   private void keine$sendDirtyEntityData(CallbackInfo ci) {
      DataSyncPacket packet = EntityDataProviders.getPacket(this.entity);
      if (packet != null) {
         if (this.synchronizer instanceof TrackedEntity trackedEntity) {
            EntityTrackerAccessor accessor = (EntityTrackerAccessor)trackedEntity;
            Set<ServerPlayerConnection> serverPlayerConnections = accessor.keine$getSeenBy();
            Set<ServerPlayer> players = serverPlayerConnections.stream().<ServerPlayer>map(ServerPlayerConnection::getPlayer).collect(Collectors.toSet());
            players.forEach(player -> Balm.networking().sendTo(player, packet));
         }
      }
   }
}
