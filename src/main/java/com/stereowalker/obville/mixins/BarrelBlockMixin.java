package com.stereowalker.obville.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.stereowalker.obville.ObVille;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BaseEntityBlock {

	protected BarrelBlockMixin(Properties p_49224_) {
		super(p_49224_);
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;angerNearbyPiglins(Lnet/minecraft/world/entity/player/Player;Z)V"))
	public void useInject(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, CallbackInfoReturnable<InteractionResult> ci) {
		if (pPlayer instanceof ServerPlayer sPlayer) {
			MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
			if (menuprovider instanceof BarrelBlockEntity barrel) {
				ObVille.upsetOnOpen(barrel, sPlayer, pPos, null);
			}
		}
	}

}