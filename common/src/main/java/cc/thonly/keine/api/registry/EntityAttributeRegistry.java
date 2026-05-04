package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.IEntryRegistry;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityAttributeRegistry implements IEntryRegistry<EntityAttributeRegistry.Context, EntityAttributeRegistry.Entry<?>> {
    List<Entry<?>> entries = new ArrayList<>();

    public List<Entry<?>> getEntries() {
        return entries;
    }

    public void register(Consumer<Context> consumer) {
        consumer.accept(new Context() {
            @Override
            public <T extends Entity> void add(BalmEntityTypeRegistration<T> type, AttributeSupplier supplier) {
                entries.add(new Entry<>(type, supplier));
            }
        });
    }

    public interface Context {
        <T extends Entity> void add(BalmEntityTypeRegistration<T> type, AttributeSupplier supplier);
    }


    public record Entry<T extends Entity>(
            BalmEntityTypeRegistration<T> type,
            AttributeSupplier supplier
    ) {
    }

}
