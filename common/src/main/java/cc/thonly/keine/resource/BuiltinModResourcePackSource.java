package cc.thonly.keine.resource;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;

public record BuiltinModResourcePackSource(String modId) implements PackSource {
    @Override
    public boolean shouldAddAutomatically() {
        return true;
    }

    @Override
    public Component decorate(Component packName) {
        return Component.translatable("pack.nameAndSource", packName, Component.translatable("pack.source.builtinMod", this.modId))
                .withStyle(ChatFormatting.GRAY);
    }
}
