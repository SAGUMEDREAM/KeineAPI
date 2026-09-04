package cc.thonly.keine;

import cc.thonly.keine.api.KeineAPI;
import cc.thonly.keine.api.KeineLogger;
import cc.thonly.keine.api.KeineRegistries;
import cc.thonly.keine.api.entity.EntityDataProviders;
import cc.thonly.keine.api.proxy.PlatformProxy;
import cc.thonly.keine.client.ClientNetworkingHandlers;
import cc.thonly.keine.network.DataSyncPacket;
import cc.thonly.keine.network.DataSyncRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.network.BalmNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter.ScopedCollector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"resource", "ConstantValue"})
public class Keine {
   public static final Logger logger = LoggerFactory.getLogger(Keine.class);
   public static final String MOD_ID = "keine";
   public static final Map<String, KeineRegistries> id2Registries = new ConcurrentHashMap<>();
   private static MinecraftServer server;
   private static final KeineAPI __API = PlatformProxy.<KeineAPI>builder()
      .withFabric("cc.thonly.keine.fabric.impl.FabricKeineAPI")
      .withNeoForge("cc.thonly.keine.neoforge.impl.NeoForgeKeineAPI")
      .buildOrThrow();

   public static KeineAPI api() {
      return __API;
   }

   public static Identifier id(String path) {
      return Identifier.fromNamespaceAndPath("keine", path);
   }

   public static void setServer(MinecraftServer server) {
      Keine.server = server;
   }

   public static MinecraftServer getServer() {
      return server;
   }

   public static void initialize() {
      BalmNetworking networking = Balm.networking();

      // S2C
      networking.registerClientboundPacket(
              DataSyncPacket.PACKET_ID,
              DataSyncPacket.class,
              DataSyncPacket.CODEC,
              (player, packet) ->
                      ClientNetworkingHandlers.safeHandleClient(() ->
                              ClientNetworkingHandlers.onReceiveDataSyncPacket(packet)
                      )
      );

      // C2S
      networking.registerServerboundPacket(
              DataSyncRequest.PACKET_ID,
              DataSyncRequest.class,
              DataSyncRequest.CODEC,
              (serverPlayer, packet) -> {
                 MinecraftServer server = serverPlayer.level().getServer();
                 if (server == null) {
                    return;
                 }

                 Identifier dimensionId = packet.dimensionId();

                 ServerLevel level = server.getLevel(
                         ResourceKey.create(
                                 Registries.DIMENSION,
                                 dimensionId
                         )
                 );

                 if (level == null) {
                    return;
                 }

                 int entityId = packet.entityId();

                 Entity entity = level.getEntity(entityId);
                 if (entity == null) {
                    return;
                 }

                 List<CompoundTag> tags = new ArrayList<>();

                 EntityDataProviders providers =
                         EntityDataProviders.getProviders(entity);

                 if (providers.isEmpty()) {
                    return;
                 }

                 try {
                    ScopedCollector scopedCollector =
                            new ScopedCollector(KeineLogger.log());

                    try {
                       TagValueOutput valueOutput =
                               TagValueOutput.createWithContext(
                                       scopedCollector,
                                       entity.registryAccess()
                               );

                       tags.addAll(providers.write(valueOutput));
                    } catch (Throwable throwable) {
                       try {
                          scopedCollector.close();
                       } catch (Throwable closeException) {
                          throwable.addSuppressed(closeException);
                       }

                       throw throwable;
                    }

                    scopedCollector.close();
                 } catch (Exception exception) {
                    logger.error("Error writing entity data", exception);
                 }

                 networking.sendTo(
                         serverPlayer,
                         new DataSyncPacket(
                                 entityId,
                                 tags,
                                 dimensionId
                         )
                 );
              }
      );
   }
}
