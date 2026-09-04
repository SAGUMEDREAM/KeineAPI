package cc.thonly.keine.mixin;

import cc.thonly.keine.Keine;
import cc.thonly.keine.api.callback.ServerCallback;
import cc.thonly.keine.api.callback.ServerSavingCallback;
import com.mojang.datafixers.DataFixer;

import java.net.Proxy;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MinecraftServer.class})
public class MinecraftServerMixin {
    @Inject(
            method = {"<init>"},
            at = {@At("TAIL")}
    )
    public void init(
            Thread serverThread, LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer fixerUpper, Services services, LevelLoadListener levelLoadListener, CallbackInfo ci
    ) {
        MinecraftServer minecraftServer = (MinecraftServer) (Object) this;
        Keine.setServer(minecraftServer);
    }

    @Inject(
            method = {"saveAllChunks"},
            at = {@At("HEAD")}
    )
    public void startSaveChunks(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ((ServerSavingCallback) ServerSavingCallback.BEFORE.invoker()).handle((MinecraftServer) (Object) this, flush, force);
    }

    @Inject(
            method = {"saveAllChunks"},
            at = {@At("HEAD")}
    )
    public void endSaveChunks(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ServerSavingCallback.AFTER.invoker().handle((MinecraftServer) (Object) this, flush, force);
    }

    @Inject(
            method = {"reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;"},
            at = {@At("RETURN")}
    )
    private void reloadResources(Collection<String> packsToEnable, CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) {
        callbackInfo.getReturnValue()
                .thenAccept(var1 -> ServerCallback.RELOADED.invoker().handle((MinecraftServer) (Object) this));
    }
}
