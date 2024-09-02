package com.stereowalker.obville.world.entity.ai.goal;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.stereowalker.obville.interfaces.IInvestigator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class InvestigateCrimeGoal extends Goal {
	protected final Mob mob;
	private final double speedModifier;
	private boolean hasThrownPotion;
	@Nullable
	protected BlockPos pos = BlockPos.ZERO;
	private int calmDown;
	private boolean isRunning;

	public InvestigateCrimeGoal(Mob pMob, double pSpeedModifier) {
		this.mob = pMob;
		this.speedModifier = pSpeedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
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
			if (this.mob instanceof IInvestigator investigator) {
				this.pos = investigator.investigatePos();
			}
			if (this.pos == BlockPos.ZERO) return false;
			return this.mob.blockPosition().distSqr(this.pos) < 1000D;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		if (this.mob.blockPosition().distSqr(this.pos) < 5D && this.hasThrownPotion) {
			return false;
		}

		return this.canUse();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		if (this.mob instanceof IInvestigator investigator) {
			this.pos = investigator.investigatePos();
			if (this.mob.level.getBlockState(pos).isAir()) {
				this.pos = pos.below();
			}
		}
		this.hasThrownPotion = false;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		this.pos = BlockPos.ZERO;
		if (this.mob instanceof IInvestigator investigator) {
			investigator.setInvestigatePos(BlockPos.ZERO);;
		}
		this.mob.getNavigation().stop();
		this.calmDown = reducedTickDelay(100);
		this.isRunning = false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		if (this.mob.blockPosition().distSqr(this.pos) < 5D) {
			this.mob.getNavigation().stop();
			if (this.mob instanceof IInvestigator investigator) {
				investigator.performRangedAttack(this.mob, pos, .5f);
			}
			this.hasThrownPotion = true;
		} else {
			this.mob.getNavigation().moveTo(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.speedModifier);
		}
	}

	/**
	 * @see #isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
}