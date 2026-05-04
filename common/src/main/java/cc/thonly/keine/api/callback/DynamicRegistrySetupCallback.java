package cc.thonly.keine.api.callback;

import cc.thonly.keine.registry.DynamicRegistryView;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;

@FunctionalInterface
public interface DynamicRegistrySetupCallback {
    void onRegistrySetup(DynamicRegistryView registryView);

    Event<DynamicRegistrySetupCallback> EVENT = EventFactory.createArrayBacked(
            DynamicRegistrySetupCallback.class,
            callbacks -> registryView -> {
                for (DynamicRegistrySetupCallback callback : callbacks) {
                    callback.onRegistrySetup(registryView);
                }
            }
    );
}