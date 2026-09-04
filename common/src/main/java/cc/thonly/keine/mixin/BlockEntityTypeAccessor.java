package cc.thonly.keine.mixin;

import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({BlockEntityType.class})
public interface BlockEntityTypeAccessor<T> {
   @Invoker("<init>")
   static <T extends BlockEntity> BlockEntityType<T> invokeConstructor(BlockEntitySupplier<? extends T> factory, Set<Block> validBlocks) {
      throw new AssertionError();
   }
}
