package cc.thonly.keine.api.registry;

import cc.thonly.keine.api.IEntryRegistry;
import net.blay09.mods.balm.platform.event.callback.CreativeModeTabCallback;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CreativeTabRegistry implements IEntryRegistry<CreativeTabRegistry.Context, CreativeTabRegistry.Entry> {
    List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return this.entries;
    }

    @Override
    public void register(Consumer<Context> accepter) {
        accepter.accept(new Context() {
            @Override
            public void addContentsFor(Identifier id, CreativeModeTabCallback.BuildContents output) {
                this.addContentsFor(ResourceKey.create(Registries.CREATIVE_MODE_TAB, id), output);
            }

            @Override
            public void addContentsFor(ResourceKey<CreativeModeTab> key, CreativeModeTabCallback.BuildContents output) {
                CreativeModeTabCallback.BuildContents.forTab(key.identifier()).register(output);
                entries.add(new Entry(key, output));
            }
        });
    }

    public interface Context {
        void addContentsFor(Identifier id, CreativeModeTabCallback.BuildContents output);
        void addContentsFor(ResourceKey<CreativeModeTab> key, CreativeModeTabCallback.BuildContents output);
    }

    public record Entry(ResourceKey<CreativeModeTab> key, CreativeModeTabCallback.BuildContents consumer) {

    }
}
