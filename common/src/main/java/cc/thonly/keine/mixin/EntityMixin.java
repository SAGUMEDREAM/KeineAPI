package cc.thonly.keine.mixin;

import cc.thonly.keine.api.entity.EntityDataProviderGetter;
import cc.thonly.keine.api.entity.EntityDataProviders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public class EntityMixin implements EntityDataProviderGetter {
    @Unique
    EntityDataProviders reverie_dreams$dataProviders;

    @Inject(
            method = {"<init>"},
            at = {@At("RETURN")}
    )
    public void keine$InitEntity(EntityType<?> type, Level level, CallbackInfo ci) {
        this.reverie_dreams$dataProviders = new EntityDataProviders((Entity) (Object) this);
    }

    @Inject(
            method = {"saveWithoutId"},
            at = {@At("RETURN")}
    )
    private void keine$toTag(ValueOutput view, CallbackInfo ci) {
        this.reverie_dreams$dataProviders.write(view);
    }

    @Inject(
            method = {"load"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V",
                    shift = Shift.AFTER
            )}
    )
    private void keine$fromTag(ValueInput view, CallbackInfo ci) {
        this.reverie_dreams$dataProviders.read(view);
    }

    @Override
    public EntityDataProviders keine$getProviders() {
        return this.reverie_dreams$dataProviders;
    }
}
