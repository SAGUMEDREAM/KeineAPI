package cc.thonly.keine.api;

import cc.thonly.keine.api.proxy.PlatformProxy;
import cc.thonly.keine.api.registry.EntityTypeAttributeRegistry;
import cc.thonly.keine.api.registry.impl.BlockEntityTypeAddBlockRegistry;
import cc.thonly.keine.api.registry.impl.CompostingChanceRegistry;
import cc.thonly.keine.api.registry.impl.EntityDataSerializerRegistry;
import cc.thonly.keine.api.registry.impl.FlammableBlockRegistry;
import cc.thonly.keine.api.registry.impl.FuelRegistry;
import cc.thonly.keine.api.registry.impl.LootTableRegistry;
import cc.thonly.keine.api.registry.impl.StrippableBlockRegistry;
import net.minecraft.resources.Identifier;

public class KeineRegistries {
   private final String modId;
   private final CompostingChanceRegistry compostingChanceRegistry = new CompostingChanceRegistry();
   private final FlammableBlockRegistry flammableBlockRegistry = new FlammableBlockRegistry();
   private final StrippableBlockRegistry strippableBlockRegistry = new StrippableBlockRegistry();
   private final FuelRegistry fuelRegistry = new FuelRegistry();
   private final BlockEntityTypeAddBlockRegistry blockEntityTypeAddBlockRegistry = new BlockEntityTypeAddBlockRegistry();
   private final LootTableRegistry lootTableRegistry = new LootTableRegistry();
   private final EntityDataSerializerRegistry entityDataSerializerRegistry = new EntityDataSerializerRegistry();
   private final EntityTypeAttributeRegistry entityTypeAttributeRegistry = PlatformProxy.<EntityTypeAttributeRegistry>builder()
      .withFabric("cc.thonly.keine.fabric.impl.registry.EntityTypeAttributeRegistryImpl")
      .withNeoForge("cc.thonly.keine.neoforge.impl.registry.EntityTypeAttributeRegistryImpl")
      .build();

   public KeineRegistries(String modId) {
      this.modId = modId;
   }

   public String modId() {
      return this.modId;
   }

   public Identifier id(String name) {
      return Identifier.fromNamespaceAndPath(this.modId, name);
   }

   public CompostingChanceRegistry compostingChanceRegistry() {
      return this.compostingChanceRegistry;
   }

   public FlammableBlockRegistry flammableBlockRegistry() {
      return this.flammableBlockRegistry;
   }

   public StrippableBlockRegistry strippableBlockRegistry() {
      return this.strippableBlockRegistry;
   }

   public FuelRegistry fuelRegistry() {
      return this.fuelRegistry;
   }

   public BlockEntityTypeAddBlockRegistry blockEntityTypeAddBlockRegistry() {
      return this.blockEntityTypeAddBlockRegistry;
   }

   public LootTableRegistry lootTableRegistry() {
      return this.lootTableRegistry;
   }

   public EntityDataSerializerRegistry entityDataSerializerRegistry() {
      return this.entityDataSerializerRegistry;
   }

   public EntityTypeAttributeRegistry entityTypeAttributeRegistry() {
      return this.entityTypeAttributeRegistry;
   }

   public String getModId() {
      return this.modId;
   }
}
