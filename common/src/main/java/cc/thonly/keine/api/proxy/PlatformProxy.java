package cc.thonly.keine.api.proxy;

import cc.thonly.keine.api.proxy.impl.PlatformProxyImpl;
import cc.thonly.keine.util.PlatformContext;
import net.blay09.mods.balm.Balm;

import java.util.Optional;
import java.util.function.Supplier;

public interface PlatformProxy<T> {
   String FABRIC = "fabric";
   String FORGE = "forge";
   String POLYMER = "polymer";
   String NEOFORGE = "neoforge";

   static <T> PlatformProxy<T> builder() {
      String type = "fabric";
      if (Balm.platform().name().equals("neoforge")) {
         type = "neoforge";
      } else if (Balm.platform().name().equals("fabric")) {
         type = "fabric";
      } else if (Balm.platform().isModLoaded("polymer")) {
         type = "polymer";
      }

      return new PlatformProxyImpl<>(type);
   }

   default PlatformProxy<T> with(String platform, String clazzName) {
      return this.with(platform, () -> true, clazzName);
   }

   PlatformProxy<T> with(String platform, Supplier<Boolean> condition, String clazzName);

   T build();

   T buildOrThrow();

   Optional<T> buildOrNull();

   default PlatformProxy<T> withFabric(String clazzName) {
      return this.with("fabric", clazzName);
   }

   default PlatformProxy<T> withForge(String clazzName) {
      return this.with("forge", clazzName);
   }

   default PlatformProxy<T> withPolymer(String clazzName) {
      return this.with("fabric", PlatformContext::hasPolymer, clazzName);
   }

   default PlatformProxy<T> withNeoForge(String clazzName) {
      return this.with("neoforge", clazzName);
   }
}
