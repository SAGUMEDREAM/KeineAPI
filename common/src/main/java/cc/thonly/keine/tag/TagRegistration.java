package cc.thonly.keine.tag;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

public record TagRegistration<T>(ResourceKey<Registry<T>> registryKey) {
    public static final TagRegistration<Item> ITEM_TAG;
    public static final TagRegistration<Block> BLOCK_TAG;
    public static final TagRegistration<Biome> BIOME_TAG;
    public static final TagRegistration<Structure> STRUCTURE_TAG;
    public static final TagRegistration<Fluid> FLUID_TAG;
    public static final TagRegistration<EntityType<?>> ENTITY_TYPE_TAG;
    public static final TagRegistration<Enchantment> ENCHANTMENT_TAG;

    public TagKey<T> registerFabric(String tagId) {
        return TagKey.create(this.registryKey, Identifier.fromNamespaceAndPath("fabric", tagId));
    }

    public TagKey<T> registerC(String tagId) {
        return TagKey.create(this.registryKey, Identifier.fromNamespaceAndPath("c", tagId));
    }

    static {
        ITEM_TAG = new TagRegistration<Item>(Registries.ITEM);
        BLOCK_TAG = new TagRegistration<Block>(Registries.BLOCK);
        BIOME_TAG = new TagRegistration<Biome>(Registries.BIOME);
        STRUCTURE_TAG = new TagRegistration<Structure>(Registries.STRUCTURE);
        FLUID_TAG = new TagRegistration<Fluid>(Registries.FLUID);
        ENTITY_TYPE_TAG = new TagRegistration<EntityType<?>>(Registries.ENTITY_TYPE);
        ENCHANTMENT_TAG = new TagRegistration<Enchantment>(Registries.ENCHANTMENT);
    }
}