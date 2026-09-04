package cc.thonly.keine.network;

import cc.thonly.keine.Keine;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record DataSyncRequest(int entityId, Identifier dimensionId) implements CustomPacketPayload {
   public static final Identifier payload = Keine.id("data_sync_request");
   public static final Type<DataSyncRequest> PACKET_ID = new Type<>(payload);
   public static final StreamCodec<RegistryFriendlyByteBuf, DataSyncRequest> CODEC = StreamCodec.ofMember(DataSyncRequest::write, DataSyncRequest::read);

   private void write(RegistryFriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeIdentifier(this.dimensionId);
   }

   private static DataSyncRequest read(RegistryFriendlyByteBuf buf) {
      int entityId = buf.readInt();
      Identifier dimensionId = buf.readIdentifier();
      return new DataSyncRequest(entityId, dimensionId);
   }

   @NonNull
   public Type<? extends CustomPacketPayload> type() {
      return PACKET_ID;
   }
}
