//package cc.thonly.keine.mixin;
//
//import cc.thonly.keine.api.item.ItemStackTemplateModifier;
//import net.minecraft.core.Holder;
//import net.minecraft.core.component.DataComponentPatch;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStackTemplate;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Mutable;
//import org.spongepowered.asm.mixin.Shadow;
//
//@Mixin({ItemStackTemplate.class})
//public class ItemStackTemplateMixin implements ItemStackTemplateModifier {
//   @Mutable
//   @Shadow
//   @Final
//   private Holder<Item> item;
//   @Mutable
//   @Shadow
//   @Final
//   private int count;
//   @Mutable
//   @Shadow
//   @Final
//   private DataComponentPatch components;
//
//   @Override
//   public void keine$setItem(Holder<Item> item) {
//      this.item = item;
//   }
//
//   @Override
//   public void keine$setCount(int count) {
//      this.count = count;
//   }
//
//   @Override
//   public void keine$setComponents(DataComponentPatch patch) {
//      this.components = patch;
//   }
//}
