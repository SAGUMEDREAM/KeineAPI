package cc.thonly.keine.neoforge.mixin;

import cc.thonly.keine.util.DeferredSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> {
    @Mutable
    @Shadow
    @Final
    private Set<Block> validBlocks;

    @Inject(
            method = {"<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType$BlockEntitySupplier;Ljava/util/Set;)V"},
            at = {@At("RETURN")}
    )
    private void mutableBlocks(BlockEntityType.BlockEntitySupplier<? extends T> factory, Set<Block> blocks, CallbackInfo ci) {
        if (!(this.validBlocks instanceof HashSet)) {
            this.validBlocks = new DeferredSet<>(this.validBlocks);
        }

    }
}
