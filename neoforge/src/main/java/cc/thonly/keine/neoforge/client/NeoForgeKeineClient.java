package cc.thonly.keine.neoforge.client;

import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import cc.thonly.keine.Keine;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;

@Mod(value = Keine.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeKeineClient {

    public NeoForgeKeineClient(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);

    }


}
