package cc.thonly.keine.fabric.mixin;

import cc.thonly.keine.fabric.FabricKeine;
import net.blay09.mods.balm.network.internal.NetworkVersions;
import net.blay09.mods.balm.network.internal.RemotePlayerModList;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RemotePlayerModList.class)
@Pseudo
public class RemotePlayerModListMixin {
    @Inject(method = "validateRemoteMods", at = @At("HEAD"), cancellable = true)
    private static void fabric$validateRemoteMods(ServerPlayer player, Map<String, NetworkVersions> modList, CallbackInfo ci) {
        if (!FabricKeine.isServerSideOnly()) {
            return;
        }
        ci.cancel();
    }
}