package cc.thonly.keine.mixin;


import cc.thonly.keine.api.callback.ServerSavingCallback;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Thread thread, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, Services services, LevelLoadListener levelLoadListener, CallbackInfo ci) {
        MinecraftServer minecraftServer = (MinecraftServer) (Object) this;
    }

    @Inject(method = "saveAllChunks", at = @At("HEAD"))
    public void startSaveChunks(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ServerSavingCallback.BEFORE.invoker().handle((MinecraftServer) (Object) this, flush, force);
    }

    @Inject(method = "saveAllChunks", at = @At("HEAD"))
    public void endSaveChunks(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ServerSavingCallback.AFTER.invoker().handle((MinecraftServer) (Object) this, flush, force);
    }
}
