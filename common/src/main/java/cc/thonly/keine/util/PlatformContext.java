package cc.thonly.keine.util;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.BalmEnvironment;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;

public class PlatformContext {
    public static final LazySupplier<BalmEnvironment> ENV_TYPE = LazySupplier.of(() -> Balm.getRuntime().platform().physicalSide());
    private static final LazySupplier<Boolean> DEV_ENV = LazySupplier.of(() -> Balm.getRuntime().platform().isDevelopmentEnvironment());
    private static final LazySupplier<Boolean> DEV_MODE = LazySupplier.of(DEV_ENV);

    public static boolean isClientSide() {
        BalmRuntime<? extends BalmRuntimeLoadContext> runtime = Balm.getRuntime();
        BalmPlatform platform = runtime.platform();
        BalmEnvironment environment = platform.physicalSide();
        return environment == BalmEnvironment.CLIENT;
    }

    public static boolean isModLoaded(String id) {
        BalmRuntime<? extends BalmRuntimeLoadContext> runtime = Balm.getRuntime();
        BalmPlatform platform = runtime.platform();
        return platform.isModLoaded(id);
    }

    public static boolean hasPolymer() {
        return isModLoaded("polymer-core");
    }
}
