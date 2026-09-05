package cc.thonly.keine.mixin;

import net.minecraft.resources.FileToIdConverter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({FileToIdConverter.class})
public interface FileToIdConverterAccessor {
    @Accessor("prefix")
    String api$getPrefix();
}
