package cc.thonly.keine.neoforge.client;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import cc.thonly.keine.Keine;

@Mod(value = Keine.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeKeineClient {

    public NeoForgeKeineClient(ModContainer modContainer, IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modContainer, modEventBus);

    }


}
