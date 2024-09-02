package com.stereowalker.obville.world.entity.ai.goal;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.stereowalker.obville.dat.OVModData;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.interfaces.IPlayerFollower;
import com.stereowalker.obville.world.entity.VillageLeader;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class FollowCriminalGoal extends Goal {
	private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(100.0D).ignoreLineOfSight();
	private final TargetingConditions targetingConditions;
	protected final Mob mob;
	private final double speedModifier;
	@Nullable
	protected Player player;
	private int calmDown;
	private boolean isRunning;

	public FollowCriminalGoal(Mob pMob, double pSpeedModifier) {
		this.mob = pMob;
		this.speedModifier = pSpeedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		if (this.calmDown > 0) {
			--this.calmDown;
			return false;
		} else {
			this.player = this.mob.level.getNearestPlayer(this.targetingConditions, this.mob);
			return this.player != null;
		}
	}

	private boolean shouldFollow(LivingEntity p_148139_) {
		if (this.mob instanceof VillageLeader) {
			if (p_148139_ instanceof ServerPlayer player) {
				OVModData data = ((IModdedEntity)player).getData();
				return data.isInAnyVillage() && data.reput().hasCommitedCrimeBefore && 
						!data.reput().hasSpokenToLeader;
			}
		}
		else {
			if (this.mob instanceof IPlayerFollower follower) {
				return follower.followedCriminal() == p_148139_;
			}
		}
		return false;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		return this.canUse();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		this.isRunning = true;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		this.player = null;
		this.mob.getNavigation().stop();
		this.calmDown = reducedTickDelay(100);
		this.isRunning = false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		if (this.mob instanceof IPlayerFollower follower) {
			this.mob.getLookControl().setLookAt(this.player, (float)(this.mob.getMaxHeadYRot() + 20), (float)this.mob.getMaxHeadXRot());
			if (this.mob.distanceToSqr(this.player) < 6.25D) {
				this.mob.getNavigation().stop();
			} else {
				this.mob.getNavigation().moveTo(this.player, this.speedModifier);
			}
			if (this.mob.distanceToSqr(this.player) > 60.0D) {
				follower.follow(null);
			} else {
				follower.follow(this.player);
			}
		}
	}

	/**
	 * @see #isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
}