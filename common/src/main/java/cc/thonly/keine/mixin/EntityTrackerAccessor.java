package cc.thonly.keine.mixin;

import java.util.Set;
import net.minecraft.server.network.ServerPlayerConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
   targets = {"net.minecraft.server.level.ChunkMap$TrackedEntity"}
)
public interface EntityTrackerAccessor {
   @Accessor("seenBy")
   Set<ServerPlayerConnection> keine$getSeenBy();
}
