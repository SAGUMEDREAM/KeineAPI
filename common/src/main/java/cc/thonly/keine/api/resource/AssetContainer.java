package cc.thonly.keine.api.resource;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AssetContainer extends AutoCloseable {
   @Nullable
   IoSupplier<InputStream> getAsset(String namespace, String path);

   @NotNull
   List<IoSupplier<InputStream>> getAllAssets(String namespace, String path);

   @NotNull
   Set<String> getNamespaces();

   boolean containsAsset(String namespace, String path);

   @NotNull
   default Set<Tuple<Identifier, IoSupplier<InputStream>>> locateLanguageFiles() {
      return this.locateFiles("lang");
   }

   @NotNull
   Set<Tuple<Identifier, IoSupplier<InputStream>>> locateFiles(String prefix);
}
