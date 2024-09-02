package com.stereowalker.obville.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.obville.world.entity.VillageLeader;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BellBlockEntity.class)
public class BellBlockEntityMixin {

	@Inject(method = "serverTick", at = @At("HEAD"))
	private static void makeLeaderGlow(Level pLevel, BlockPos pPos, BlockState pState, BellBlockEntity pBlockEntity, CallbackInfo ci) {
		if (pBlockEntity.nearbyEntities != null && pBlockEntity.shaking && !BellBlockEntity.areRaidersNearby(pPos, pBlockEntity.nearbyEntities)) {
			pBlockEntity.nearbyEntities.stream().filter((entity) -> {
				return entity instanceof VillageLeader && !entity.hasEffect(MobEffects.GLOWING);
			}).forEach((entity) -> entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, false, false)));
		}
	}
}
