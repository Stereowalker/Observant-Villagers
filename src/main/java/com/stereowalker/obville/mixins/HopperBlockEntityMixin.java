package com.stereowalker.obville.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.interfaces.ICombinedBlock;
import com.stereowalker.obville.interfaces.ILootableBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.AABB;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@Shadow private static Container getSourceContainer(Level p_155597_, Hopper p_155598_) {return null;}

	@Inject(method = "suckInItems", at = @At("HEAD"), cancellable = true)
	private static void tryTakeInItemFromSlotInject(Level p_155553_, Hopper p_155554_, CallbackInfoReturnable<Boolean> cir) {
		Boolean ret = net.minecraftforge.items.VanillaInventoryCodeHooks.extractHook(p_155553_, p_155554_);
		if (ret != null) {
			if (ret) {
				Container pContainer = getSourceContainer(p_155553_, p_155554_);
				ServerLevel level = null;
				ILootableBlock loot = null;
				BlockPos pos = null;
				if (pContainer instanceof ChestBlockEntity chest && chest.getLevel() instanceof ServerLevel) {
					level = (ServerLevel) chest.getLevel();
					loot = ((ILootableBlock)chest);
					pos = chest.getBlockPos();
				}
				else if (pContainer instanceof CompoundContainer compound) {
					if (((ICombinedBlock)compound).getContainer1() instanceof ChestBlockEntity chest1) {
						level = (ServerLevel) chest1.getLevel();
						loot = ((ILootableBlock)chest1);
						pos = chest1.getBlockPos();
					}
					if (loot == null && ((ICombinedBlock)compound).getContainer2() instanceof ChestBlockEntity chest2) {
						level = (ServerLevel) chest2.getLevel();
						loot = ((ILootableBlock)chest2);
						pos = chest2.getBlockPos();
					}
				}
				else if (pContainer instanceof BarrelBlockEntity barrel && barrel.getLevel() instanceof ServerLevel) {
					level = (ServerLevel) barrel.getLevel();
					loot = ((ILootableBlock)barrel);
					pos = barrel.getBlockPos();
				}
				System.out.println(loot+" "+pContainer);
				if (loot != null && level != null) {
					if (ObVille.MOD_CONFIG.COnts().contains(loot.getLoot())) {
						List<Player> list = Lists.newArrayList();
						AABB bounds = new AABB(pos).inflate(10);
						for(Player player : level.players()) {
							if (bounds.contains(player.getX(), player.getY(), player.getZ())) {
								list.add(player);
							}
						}
						final BlockPos pos2 = pos;
						final ILootableBlock loot2 = loot;
						list.forEach((pPlayer) -> {
							if (!loot2.hasPlayerOpened(pPlayer) && ObVille.upsetNearby(pPlayer, pos2, true, -ObVille.REPUTATION_CONFIG.opening_containers, null)) {
								loot2.addPlayer(pPlayer);
							}
						});
					}
				}
			}
			cir.setReturnValue(ret);
		}
	}

}