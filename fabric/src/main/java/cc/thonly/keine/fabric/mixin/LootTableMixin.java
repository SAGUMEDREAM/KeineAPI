/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.thonly.keine.fabric.mixin;

import cc.thonly.keine.api.callback.LootTableCallback;
import cc.thonly.keine.loot.KeineLootTable;
import cc.thonly.keine.loot.LootUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = LootTable.class, priority = 3000 /* arbitrary, but requires mods to explicit set a priority to wrap the fabric event.*/)
class LootTableMixin implements KeineLootTable {
	/*
	 * the key of this loot table, if initialized.
	 */
	@Unique
	@Nullable
	Holder<LootTable> entry = null;

	@WrapMethod(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V")
	private void keine$modifyDrops(LootContext context, Consumer<ItemStack> lootConsumer, Operation<Void> original) {
		if (entry == null) {
			this.entry = LootUtil.getEntryOrDirect(context.getLevel(), (LootTable) (Object) this);
		}

		List<ItemStack> list = new ObjectArrayList<>();
		original.call(context, (Consumer<ItemStack>) list::add);
		LootTableCallback.MODIFY_DROPS.invoker().modifyLootTableDrops(
				this.entry,
				context,
				list
		);
		list.forEach(lootConsumer);
	}

	@Override
	public void keine$setHolder(Holder<LootTable> key) {
		this.entry = key;
	}
}
