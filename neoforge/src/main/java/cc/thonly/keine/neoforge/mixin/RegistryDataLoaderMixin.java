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
import cc.thonly.keine.registry.DynamicRegistryViewImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.*;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Unique
    private static final ThreadLocal<Boolean> keine$IS_SERVER = ThreadLocal.withInitial(() -> false);

    /**
     * Sets IS_SERVER flag. Note that this must be reset after call, as the render thread
     * invokes this method as well.
     */
    @WrapOperation(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Ljava/util/List;Ljava/util/List;Z)Lnet/minecraft/core/RegistryAccess$Frozen;"))
    private static RegistryAccess.Frozen wrapIsServerCall(@Coerce Object registryLoadable, List<HolderLookup.RegistryLookup<?>> baseRegistries, List<RegistryDataLoader.RegistryData<?>> entries, boolean b, Operation<RegistryAccess.Frozen> original) {
        try {
            keine$IS_SERVER.set(true);
//            System.out.println(1);
            return original.call(registryLoadable, baseRegistries, entries, b);
        } finally {
            keine$IS_SERVER.set(false);
//            System.out.println(2);
        }
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Ljava/util/List;Ljava/util/List;Z)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
                    ordinal = 0
            )
    )
    private static void beforeLoad(@Coerce Object registryLoadable,
                                   List<HolderLookup.RegistryLookup<?>> baseRegistries,
                                   List<RegistryDataLoader.RegistryData<?>> entries,
                                   boolean fromResources,
                                   CallbackInfoReturnable<RegistryAccess.Frozen> cir,
                                   @Local(ordinal = 0, argsOnly = true) List<?> registriesList
    ) {
        if (!keine$IS_SERVER.get()) return;

        Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new IdentityHashMap<>();

        for (Object obj : registriesList) {
            if (obj instanceof Registry<?> registry) {
                registries.put(registry.key(), registry);
            }
//            Class<?> clazz = obj.getClass();
//            Class<?>[] interfaces = clazz.getInterfaces();
//            System.out.println(obj);
//            System.out.println(clazz);
//            System.out.println(Arrays.toString(interfaces));
        }

        DynamicRegistrySetupCallback.EVENT.invoker().onRegistrySetup(new DynamicRegistryViewImpl(registries));
    }
}
