package cc.thonly.keine.api.callback;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface AttackBlockCallback {
    Event<AttackBlockCallback> EVENT = EventFactory.createArrayBacked(AttackBlockCallback.class, listeners -> (player, world, hand, pos, direction) -> {
        for (AttackBlockCallback event : listeners) {
            InteractionResult result = event.interact(player, world, hand, pos, direction);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction);
}
