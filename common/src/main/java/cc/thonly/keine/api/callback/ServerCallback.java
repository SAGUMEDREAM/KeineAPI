package cc.thonly.keine.api.callback;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;

public interface ServerCallback {
    Event<ServerCallback.Reloading> RELOADING = EventFactory.createArrayBacked(ServerCallback.Reloading.class, listeners -> (server, resources) -> {
        for (Reloading listener : listeners) {
            listener.handle(server, resources);
        }
    });
    Event<ServerCallback.Reloaded> RELOADED = EventFactory.createArrayBacked(ServerCallback.Reloaded.class, listeners-> server -> {
        for (Reloaded listener : listeners) {
            listener.handle(server);
        }
    });

    @FunctionalInterface
    public interface Reloaded {
        void handle(MinecraftServer server);
    }

    @FunctionalInterface
    public interface Reloading {
        void handle(MinecraftServer server, ReloadableServerResources resources);
    }
}
