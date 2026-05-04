package cc.thonly.keine.api;

import cc.thonly.keine.api.registry.*;
import cc.thonly.keine.api.callback.ServerSavingCallback;
import net.minecraft.resources.Identifier;

public class KeineRegistries {
    private String modId;
    private final CompostingChanceRegistry compostingChanceRegistry = new CompostingChanceRegistry();
    private final EntityAttributeRegistry entityAttributeRegistry = new EntityAttributeRegistry();
    private final FlammableBlockRegistry flammableBlockRegistry = new FlammableBlockRegistry();
    private final StrippableBlockRegistry strippableBlockRegistry = new StrippableBlockRegistry();
    private final CreativeTabRegistry creativeTabRegistry = new CreativeTabRegistry();
    private final FuelRegistry fuelRegistry = new FuelRegistry();
    private final BlockEntityTypeAddBlockRegistry blockEntityTypeAddBlockRegistry = new BlockEntityTypeAddBlockRegistry();

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

    public EntityAttributeRegistry entityAttributeRegistry() {
        return this.entityAttributeRegistry;
    }

    public FlammableBlockRegistry flammableBlockRegistry() {
        return this.flammableBlockRegistry;
    }

    public StrippableBlockRegistry strippableBlockRegistry() {
        return this.strippableBlockRegistry;
    }

    public CreativeTabRegistry creativeTabRegistry() {
        return this.creativeTabRegistry;
    }

    public FuelRegistry fuelRegistry() {
        return this.fuelRegistry;
    }

    public BlockEntityTypeAddBlockRegistry blockEntityTypeAddBlockRegistry() {
        return this.blockEntityTypeAddBlockRegistry;
    }

    public void onSavingBefore(ServerSavingCallback callback) {
        ServerSavingCallback.BEFORE.register(callback);
    }

    public void onSavingAfter(ServerSavingCallback callback) {
        ServerSavingCallback.AFTER.register(callback);
    }

    public String getModId() {
        return this.modId;
    }
}
