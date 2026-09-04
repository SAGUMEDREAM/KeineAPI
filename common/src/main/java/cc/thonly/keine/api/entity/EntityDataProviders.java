package cc.thonly.keine.api.entity;

import cc.thonly.keine.api.KeineLogger;
import cc.thonly.keine.network.DataSyncPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.blay09.mods.balm.Balm;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter.ScopedCollector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityDataProviders {
   private static final Logger log = LoggerFactory.getLogger(EntityDataProviders.class);
   private final Map<Identifier, EntityDataProviderContainer> providers = new Object2ObjectLinkedOpenHashMap<>(8);
   private final Entity entity;

   public EntityDataProviders(Entity entity) {
      this.entity = entity;
   }

   public void register(Identifier key, EntityDataProviderContainer container) {
      if (this.providers.containsKey(key)) {
         throw new IllegalStateException("Entity data provider already registered: " + key);
      } else if (this.providers.containsValue(container)) {
         throw new IllegalStateException("Entity data provider container already registered: " + key);
      } else {
         this.providers.put(key, container);
      }
   }

   public boolean isEmpty() {
      return this.providers.isEmpty();
   }

   public EntityDataProviderContainer get(Identifier key) {
      return this.providers.get(key);
   }

   public boolean contains(Identifier key) {
      return this.providers.containsKey(key);
   }

   public List<CompoundTag> read(ValueInput view) {
      List<CompoundTag> tags = new ArrayList<>();

      for (Entry<Identifier, EntityDataProviderContainer> entry : this.providers.entrySet()) {
         Identifier key = entry.getKey();
         ValueInput providerView = view.childOrEmpty(key.toString());
         CompoundTag compoundTag = entry.getValue().readSyncData(providerView);
         if (compoundTag != null) {
            tags.add(compoundTag);
         }
      }

      return tags;
   }

   public List<CompoundTag> write(ValueOutput view) {
      List<CompoundTag> tags = new ArrayList<>();

      for (Entry<Identifier, EntityDataProviderContainer> entry : this.providers.entrySet()) {
         Identifier key = entry.getKey();
         ValueOutput providerView = view.child(key.toString());
         CompoundTag compoundTag = entry.getValue().writeSyncData(providerView);
         if (compoundTag != null) {
            tags.add(compoundTag);
         }
      }

      return tags;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public static DataSyncPacket getPacket(Entity entity) {
      Level level = entity.level();
      List<CompoundTag> tags = new ArrayList<>();
      EntityDataProviders providers = getProviders(entity);
      if (providers.isEmpty()) {
         return null;
      } else {
         try {
            ScopedCollector scopedCollector = new ScopedCollector(KeineLogger.log());

            try {
               TagValueOutput valueOutput = TagValueOutput.createWithContext(scopedCollector, entity.registryAccess());
               tags.addAll(providers.write(valueOutput));
            } catch (Throwable var8) {
               try {
                  scopedCollector.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            scopedCollector.close();
         } catch (Exception var9) {
            log.error("Error: ", var9);
         }

         return new DataSyncPacket(entity.getId(), tags, level.dimension().identifier());
      }
   }

   public static void sync(ServerPlayer player, Entity entity) {
      DataSyncPacket packet = getPacket(entity);
      if (packet != null) {
         Balm.networking().sendTo(player, packet);
      }
   }

   public static EntityDataProviders getProviders(Entity entity) {
      return ((EntityDataProviderGetter)entity).keine$getProviders();
   }
}
