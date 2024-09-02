package com.stereowalker.obville.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.interfaces.ILootableBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Block.class)
public abstract class BlockMixin {
	
	@Inject(method = "playerWillDestroy", at = @At(value = "TAIL"))
	public void useInject(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, CallbackInfo ci) {
		if (!pLevel.isClientSide) {
			BlockEntity pContainer = pLevel.getBlockEntity(pPos);
			ResourceLocation loot = null;
			if (pContainer instanceof ChestBlockEntity chest)
				loot = ((ILootableBlock)chest).getLoot();
			else if (pContainer instanceof BarrelBlockEntity barrel)
				loot = ((ILootableBlock)barrel).getLoot();
			if (loot != null) {
				if (ObVille.MOD_CONFIG.COnts().contains(loot)) {
					ObVille.upsetNearby(pPlayer, pPos, true, -ObVille.REPUTATION_CONFIG.opening_containers, null);					
				}
			}
		}
	}
}
