package cc.thonly.keine.client;

import cc.thonly.keine.api.KeineLogger;
import cc.thonly.keine.api.entity.EntityDataProviders;
import cc.thonly.keine.network.DataSyncPacket;
import cc.thonly.keine.util.PlatformContext;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter.ScopedCollector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueInput;

public class ClientNetworkingHandlers {
   public static void safeHandleClient(Runnable task) {
      if (PlatformContext.isClientSide()) {
         clientThreadBySync(task);
      }
   }

   private static void clientThreadBySync(Runnable action) {
      Minecraft.getInstance().execute(action);
   }

   public static void onReceiveDataSyncPacket(DataSyncPacket packet) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel level = mc.level;
      if (level != null) {
         int entityId = packet.entityId();
         Entity entity = level.getEntity(entityId);
         if (entity != null) {
            List<CompoundTag> tags = packet.tags();
            EntityDataProviders providers = EntityDataProviders.getProviders(entity);
            ScopedCollector scopedCollector = new ScopedCollector(KeineLogger.log());

            try {
               for (CompoundTag tag : tags) {
                  TagValueInput valueInput = (TagValueInput)TagValueInput.create(scopedCollector, entity.registryAccess(), tag);
                  providers.read(valueInput);
               }
            } catch (Throwable var13) {
               try {
                  scopedCollector.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }

               throw var13;
            }

            scopedCollector.close();
         }
      }
   }
}
