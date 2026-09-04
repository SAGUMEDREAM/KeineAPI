package cc.thonly.keine.api.registry;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements.SpawnPredicate;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public interface EntityTypeAttributeRegistry {
   <T extends LivingEntity> void withDefaultAttributes(Holder<EntityType<T>> entry, Supplier<Builder> builderFunction);

   <T extends LivingEntity> void withDefaultAttributes(Holder<EntityType<T>> entry, Function<Builder, Builder> builderFunction);

   <T extends Entity> void withSpawnPlacement(
      Holder<EntityType<T>> entry, SpawnPlacementType placementType, Types heightMapType, Supplier<SpawnPredicate<T>> placement
   );
}
