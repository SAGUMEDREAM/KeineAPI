package cc.thonly.keine.neoforge.mixin;

import cc.thonly.keine.neoforge.NeoForgeKeine;
import net.minecraft.core.Holder;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Mutable
    @Shadow
    @Final
    @Deprecated
    protected static Map<Block, Block> STRIPPABLES;

    @Inject(method = "<clinit>", at = @At("TAIL"), cancellable = true)
    private static void api$modifyClinit(CallbackInfo ci) {
        if (!(STRIPPABLES instanceof HashMap<Block, Block>)) {
            STRIPPABLES = new HashMap<>(STRIPPABLES);
        }
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "getAxeStrippingState", at = @At("RETURN"), cancellable = true)
    private static void modifyValue(BlockState originalState, CallbackInfoReturnable<BlockState> cir) {
        if (cir.getReturnValue() != null) return;

        Map<Holder<Block>, Holder<Block>> strippables = NeoForgeKeine.STRIPPABLES;

        Holder<Block> inputHolder = originalState.getBlock().builtInRegistryHolder();
        Holder<Block> outputHolder = strippables.get(inputHolder);

        if (outputHolder == null) return;

        Block outputBlock = outputHolder.value();

        BlockState result = outputBlock.withPropertiesOf(originalState);

        cir.setReturnValue(result);
    }
}