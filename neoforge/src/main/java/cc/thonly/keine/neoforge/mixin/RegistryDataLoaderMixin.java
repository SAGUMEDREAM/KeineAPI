/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.thonly.keine.neoforge.mixin;

import cc.thonly.keine.api.callback.DynamicRegistrySetupCallback;
import cc.thonly.keine.mixin.RegistryLoadTaskAccessor;
import cc.thonly.keine.registry.DynamicRegistryViewImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.*;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Unique
    private static final ScopedValue<Boolean> keine$IS_SERVER = ScopedValue.newInstance();

    @WrapOperation(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/List;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;load(Lnet/minecraft/resources/RegistryDataLoader$LoaderFactory;Ljava/util/List;Ljava/util/List;Ljava/util/concurrent/Executor;Z)Ljava/util/concurrent/CompletableFuture;"))

    private static CompletableFuture<RegistryAccess.Frozen> wrapIsServerCall(@Coerce Object loaderFactory,
                                                                             List<HolderLookup.RegistryLookup<?>> contextRegistries,
                                                                             List<RegistryDataLoader.RegistryData<?>> registriesToLoad,
                                                                             Executor executor,
                                                                             boolean fromResources,
                                                                             Operation<CompletableFuture<RegistryAccess.Frozen>> original) {
        return ScopedValue.where(keine$IS_SERVER, true).call(() -> original.call(loaderFactory, contextRegistries, registriesToLoad, executor, fromResources));
    }

    @ModifyArg(method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoaderFactory;Ljava/util/List;Ljava/util/List;Ljava/util/concurrent/Executor;Z)Ljava/util/concurrent/CompletableFuture;",
            at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private static Supplier<CompletableFuture<RegistryAccess.Frozen>> supplyAsync(Supplier<CompletableFuture<RegistryAccess.Frozen>> supplier) {
        final boolean isServer = keine$IS_SERVER.orElse(false);
        return () -> ScopedValue.where(keine$IS_SERVER, isServer).call(supplier::get);
    }

    @ModifyArg(method = "lambda$load$0", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;thenApplyAsync(Ljava/util/function/Function;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private static Function<Void, RegistryAccess.Frozen> thenApplyAsync(Function<Void, RegistryAccess.Frozen> function) {
        final boolean isServer = keine$IS_SERVER.get();
        return (arg1) -> ScopedValue.where(keine$IS_SERVER, isServer).call(() -> function.apply(arg1));
    }

    @WrapOperation(
            method = "lambda$load$0",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;createContext(Ljava/util/List;Ljava/util/List;)Lnet/minecraft/resources/RegistryOps$RegistryInfoLookup;")
    )
    private static RegistryOps.RegistryInfoLookup beforeLoad(
            List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryLoadTask<?>> loadTasks, Operation<RegistryOps.RegistryInfoLookup> original) {
        if (keine$IS_SERVER.get()) {
            Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new IdentityHashMap<>(loadTasks.size());

            for (RegistryLoadTask<?> entry : loadTasks) {
                RegistryLoadTaskAccessor<?> loadTaskAccessor = (RegistryLoadTaskAccessor<?>) entry;
                registries.put(loadTaskAccessor.api$getRegistry().key(), loadTaskAccessor.api$getRegistry());
            }

            DynamicRegistrySetupCallback.EVENT.invoker().onRegistrySetup(new DynamicRegistryViewImpl(registries));
        }

        return original.call(contextRegistries, loadTasks);
    }
}
