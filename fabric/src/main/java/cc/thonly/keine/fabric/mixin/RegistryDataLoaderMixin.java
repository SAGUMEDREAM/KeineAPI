package cc.thonly.keine.fabric.mixin;

import cc.thonly.keine.api.callback.DynamicRegistrySetupCallback;
import cc.thonly.keine.api.registry.impl.DynamicRegistryViewImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Unique
    private static final ThreadLocal<Boolean> keine$IS_SERVER = ThreadLocal.withInitial(() -> false);

    /**
     * Sets IS_SERVER flag. Note that this must be reset after call, as the render thread
     * invokes this method as well.
     */
    @WrapOperation(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;")
    )
    private static RegistryAccess.Frozen wrapIsServerCall(@Coerce Object registryLoadable, List<HolderLookup.RegistryLookup<?>> baseRegistries, List<RegistryDataLoader.RegistryData<?>> entries, Operation<RegistryAccess.Frozen> original) {
        try {
            keine$IS_SERVER.set(true);
            return original.call(registryLoadable, baseRegistries, entries);
        } finally {
            keine$IS_SERVER.set(false);
        }
    }

    @Inject(
            method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
                    ordinal = 0
            )
    )
    private static void beforeLoad(@Coerce Object registryLoadable,
                                   List<HolderLookup.RegistryLookup<?>> baseRegistries,
                                   List<RegistryDataLoader.RegistryData<?>> entries,
                                   CallbackInfoReturnable<RegistryAccess.Frozen> cir,
                                   @Local(ordinal = 2) List<RegistryDataLoader.Loader<?>> registriesList
    ) {
        if (!keine$IS_SERVER.get()) return;

        Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new IdentityHashMap<>(registriesList.size());

        for (RegistryDataLoader.Loader<?> entry : registriesList) {
            registries.put(entry.registry().key(), entry.registry());
        }

        DynamicRegistrySetupCallback.EVENT.invoker().onRegistrySetup(new DynamicRegistryViewImpl(registries));
    }
}
