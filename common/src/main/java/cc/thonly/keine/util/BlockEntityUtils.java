package cc.thonly.keine.util;

import java.util.function.Consumer;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;

public class BlockEntityUtils {
   public static void sync(BlockEntity blockEntity) {
      if (blockEntity.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.getChunkSource().blockChanged(blockEntity.getBlockPos());
      }
   }

   public static Packet<ClientGamePacketListener> createUpdatePacket(BlockEntity blockEntity) {
      return ClientboundBlockEntityDataPacket.create(blockEntity, BlockEntity::getUpdateTag);
   }

   public static CompoundTag createUpdateTag(Provider registries, Consumer<ValueOutput> outputConsumer) {
      TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
      outputConsumer.accept(output);
      return output.buildResult();
   }
}
