package cc.thonly.keine.api.entity;

import cc.thonly.keine.api.KeineLogger;
import cc.thonly.keine.mixin.TagValueInputAccessor;
import cc.thonly.keine.mixin.TagValueOutputAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface EntityDataProviderContainer {
   Identifier getId();

   default CompoundTag readSyncData(ValueInput view) {
      CompoundTag compoundTag = null;

      try {
         if (view instanceof TagValueInput valueInput) {
            TagValueInputAccessor accessor = (TagValueInputAccessor)valueInput;
            compoundTag = accessor.keine$getTag();
         }
      } catch (Exception var5) {
         KeineLogger.log().error("Error: ", var5);
      }

      return compoundTag;
   }

   default CompoundTag writeSyncData(ValueOutput view) {
      CompoundTag compoundTag = null;

      try {
         if (view instanceof TagValueOutput valueOutput) {
            TagValueOutputAccessor accessor = (TagValueOutputAccessor)valueOutput;
            compoundTag = accessor.keine$getTag();
         }
      } catch (Exception var5) {
         KeineLogger.log().error("Error: ", var5);
      }

      return compoundTag;
   }

   void readAdditionalSaveData(ValueInput view);

   void addAdditionalSaveData(ValueOutput view);
}
