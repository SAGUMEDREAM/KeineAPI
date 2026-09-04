package cc.thonly.keine.resource;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;
import org.jspecify.annotations.NonNull;

public record BuiltinModResourcePackSource(String modId) implements PackSource {
   public boolean shouldAddAutomatically() {
      return true;
   }

   public @NonNull Component decorate(Component packName) {
      return Component.translatable("pack.nameAndSource", new Object[]{packName, Component.translatable("pack.source.builtinMod", new Object[]{this.modId})})
         .withStyle(ChatFormatting.GRAY);
   }
}
