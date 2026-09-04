package cc.thonly.keine.api.registry.impl;

import cc.thonly.keine.api.registry.IEntryRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public class EntityDataSerializerRegistry implements IEntryRegistry<EntityDataSerializerRegistry.Context, EntityDataSerializerRegistry.Entry> {
   List<EntityDataSerializerRegistry.Entry> entries = new ArrayList<>();

   @Override
   public List<EntityDataSerializerRegistry.Entry> getEntries() {
      return this.entries;
   }

   @Override
   public void register(Consumer<EntityDataSerializerRegistry.Context> accepter) {
      accepter.accept((id, serializer) -> this.entries.add(new EntityDataSerializerRegistry.Entry(id, serializer)));
   }

   public interface Context {
      void register(Identifier id, EntityDataSerializer<?> serializer);
   }

   public record Entry(Identifier id, EntityDataSerializer<?> serializer) {
   }
}
