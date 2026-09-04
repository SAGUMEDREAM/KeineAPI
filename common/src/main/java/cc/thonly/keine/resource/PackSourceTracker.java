package cc.thonly.keine.resource;

import java.util.WeakHashMap;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.PackSource;

public class PackSourceTracker {
   private static final WeakHashMap<PackResources, PackSource> SOURCES = new WeakHashMap<>();

   public static PackSource getSource(PackResources pack) {
      return SOURCES.getOrDefault(pack, PackSource.DEFAULT);
   }

   public static void setSource(PackResources pack, PackSource source) {
      SOURCES.put(pack, source);
   }
}
