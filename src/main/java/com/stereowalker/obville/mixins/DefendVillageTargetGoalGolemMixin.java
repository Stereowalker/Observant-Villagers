package com.stereowalker.obville.mixins;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.stereowalker.obville.interfaces.IModdedEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

@Mixin(DefendVillageTargetGoal.class)
public abstract class DefendVillageTargetGoalGolemMixin {
	@Shadow @Nullable
	private LivingEntity potentialTarget;
	@Shadow @Final private IronGolem golem;
	@Shadow @Final private TargetingConditions attackTargeting;

	@Redirect(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;getPlayerReputation(Lnet/minecraft/world/entity/player/Player;)I"))
	public int getPlayerReputationRedirect(Villager villager, Player pPlayer) {
		IModdedEntity ent = (IModdedEntity)pPlayer;
		if (ent.getData().IsExiled())
			return -1000 + villager.getPlayerReputation(pPlayer);
		else
			return villager.getPlayerReputation(pPlayer);
	}



	@Inject(method = "canUse", at = @At("HEAD"))
	protected void canUseInject(CallbackInfoReturnable<Boolean> cir) {
		AABB aabb = this.golem.getBoundingBox().inflate(20.0D, 16.0D, 20.0D);
		List<Player> list1 = this.golem.level.getNearbyPlayers(this.attackTargeting, this.golem, aabb);
		
		for (Player player : list1) {
			IModdedEntity ent = (IModdedEntity)player;
			if (ent.getData().IsExiled() && ent.getData().isInAnyVillage()) {
				potentialTarget = player;
				break;
			}
		}
	}
}