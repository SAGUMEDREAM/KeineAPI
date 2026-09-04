package cc.thonly.keine.network;

import cc.thonly.keine.Keine;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record DataSyncPacket(int entityId, int listSize, List<CompoundTag> tags, Identifier dimensionId) implements CustomPacketPayload {
   public static final Identifier payload = Keine.id("data_sync");
   public static final Type<DataSyncPacket> PACKET_ID = new Type<>(payload);
   public static final StreamCodec<RegistryFriendlyByteBuf, DataSyncPacket> CODEC = StreamCodec.ofMember(DataSyncPacket::write, DataSyncPacket::read);

   public DataSyncPacket(int entityId, List<CompoundTag> tags, Identifier dimensionId) {
      this(entityId, tags.size(), tags, dimensionId);
   }

   private void write(RegistryFriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeInt(this.listSize);

      for (CompoundTag tag : this.tags) {
         buf.writeNbt(tag);
      }

      buf.writeIdentifier(this.dimensionId);
   }

   private static DataSyncPacket read(RegistryFriendlyByteBuf buf) {
      int entityId = buf.readInt();
      int listSize = buf.readInt();
      List<CompoundTag> tags = new ArrayList<>();

      for (int i = 0; i < listSize; i++) {
         tags.add(buf.readNbt());
      }

      Identifier dimensionId = buf.readIdentifier();
      return new DataSyncPacket(entityId, listSize, tags, dimensionId);
   }

   @NonNull
   public Type<? extends CustomPacketPayload> type() {
      return PACKET_ID;
   }
}
