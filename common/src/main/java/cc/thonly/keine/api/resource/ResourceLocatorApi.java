package cc.thonly.keine.api.resource;

import cc.thonly.keine.api.resource.impl.ArrpProvider;
import cc.thonly.keine.api.resource.impl.CompositeResourcePack;
import cc.thonly.keine.api.resource.impl.QslProvider;
import cc.thonly.keine.api.resource.impl.RawFileProvider;
import java.lang.reflect.Method;

import net.blay09.mods.balm.Balm;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLocatorApi {
   public static final Logger LOGGER = LoggerFactory.getLogger("resource-locator-api");
   private static Method addPacksMethod = null;

   public static AssetContainer createGlobalAssetContainer() {
      CompositeResourcePack compositePack = new CompositeResourcePack(PackType.CLIENT_RESOURCES);
      if (Balm.platform().isModLoaded("advanced_runtime_resource_pack")) {
         ArrpProvider.addPacksBeforeVanilla(compositePack);
      }

      if (Balm.platform().isModLoaded("quilt_resource_loader")) {
         QslProvider.addPacks(compositePack);
      } else if (addPacksMethod != null) {
         try {
            addPacksMethod.invoke(compositePack);
         } catch (Exception var2) {
         }
      }

      if (Balm.platform().isModLoaded("advanced_runtime_resource_pack")) {
         ArrpProvider.addPacksAfterVanilla(compositePack);
      }

      RawFileProvider.addPacks(compositePack);
      return compositePack;
   }

   static {
      try {
         Class<?> clazz = Class.forName("cc.thonly.keine.fabric.resource.impl.FapiProvider");
         Method addPacks = clazz.getDeclaredMethod("addPacks", CompositeResourcePack.class);
         addPacks.setAccessible(true);
         addPacksMethod = addPacks;
      } catch (Exception var2) {
      }
   }
}
