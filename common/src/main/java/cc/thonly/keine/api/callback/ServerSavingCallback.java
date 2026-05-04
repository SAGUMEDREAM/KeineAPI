package cc.thonly.keine.api.callback;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface ServerSavingCallback {
    void handle(MinecraftServer server, boolean flush, boolean force);

    Event<ServerSavingCallback> BEFORE = EventFactory.createArrayBacked(
            ServerSavingCallback.class,
            (listeners) -> (server, flush, force) -> {
                for (ServerSavingCallback listener : listeners) {
                    listener.handle(server, flush, force);
                }
            }
    );
    Event<ServerSavingCallback> AFTER = EventFactory.createArrayBacked(
            ServerSavingCallback.class,
            (listeners) -> (server, flush, force) -> {
                for (ServerSavingCallback listener : listeners) {
                    listener.handle(server, flush, force);
                }
            }
    );
}
