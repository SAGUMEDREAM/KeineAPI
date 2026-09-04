package cc.thonly.keine.api.resource.impl;

import java.util.ArrayList;
import net.minecraft.server.packs.PackResources;

public class QslProvider {
   public static void addPacks(CompositeResourcePack pack) {
      ArrayList<PackResources> packs = new ArrayList<>();
      packs.forEach(pack::append);
   }
}
