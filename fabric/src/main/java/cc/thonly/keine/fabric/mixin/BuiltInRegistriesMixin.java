package cc.thonly.keine.fabric.mixin;

import cc.thonly.keine.fabric.FabricKeine;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "freeze", at = @At("RETURN"))
    private static void freezeEvent(CallbackInfo ci) {
        FabricKeine.freeze();
    }
}
