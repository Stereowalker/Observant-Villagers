package com.stereowalker.obville.mixins.guard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.obville.Crime;
import com.stereowalker.obville.interfaces.IInvestigator;
import com.stereowalker.obville.interfaces.IPlayerFollower;
import com.stereowalker.obville.interfaces.IVillager;
import com.stereowalker.obville.world.entity.ai.goal.FollowCriminalGoal;
import com.stereowalker.obville.world.entity.ai.goal.InvestigateCrimeGoal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tallestegg.guardvillagers.entities.Guard;

@Mixin(Guard.class)
public abstract class GuardMixin extends PathfinderMob implements IVillager<Guard>, IPlayerFollower, IInvestigator {
	
	Map<UUID, Integer> crimesWitnessed = new HashMap<>();
	public BlockPos investigatePos = BlockPos.ZERO;
	public Crime crimeToInvestigate;

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveDataInject(CompoundTag pCompound, CallbackInfo ci) {
		ListTag list1 = pCompound.getList("CrimesWitnessed", 10);
		this.crimesWitnessed = new HashMap<>();
		for (int i = 0; i < list1.size(); i++) {
			CompoundTag tag = list1.getCompound(i);
			this.crimesWitnessed.put(NbtUtils.loadUUID(tag.get("UUID")), tag.getInt("Crimes"));
		}
		investigatePos = new BlockPos(pCompound.getInt("InvX"), pCompound.getInt("InvY"), pCompound.getInt("InvZ"));
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveDataInject(CompoundTag pCompound, CallbackInfo ci) {
		ListTag list4 = new ListTag();
		crimesWitnessed.forEach((player, timer) -> {
			CompoundTag tag = new CompoundTag();
			tag.put("UUID", NbtUtils.createUUID(player));
			tag.putInt("Crimes", timer);
			list4.add(tag);
		});
		pCompound.put("CrimesWitnessed", list4);
		pCompound.putInt("InvX", investigatePos.getX());
		pCompound.putInt("InvY", investigatePos.getY());
		pCompound.putInt("InvZ", investigatePos.getZ());
	}
	
	@Override
	public Map<UUID, Integer> crimesWitnessed() {
		return crimesWitnessed;
	}
	
	protected GuardMixin(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
		super(p_21683_, p_21684_);
	}

	@Inject(method = "registerGoals", at = @At("HEAD"))
	public void h(CallbackInfo ci) {
		this.goalSelector.addGoal(3, new FollowCriminalGoal(this, 1D));
		this.goalSelector.addGoal(0, new InvestigateCrimeGoal(this, .5D));
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void t(CallbackInfo ci) {
		if (followedtime >= 300 && followedCriminal != null) {
			follow(null);
		}
	}

	public Player followedCriminal = null;
	public int followedtime = 0;
	
	@Override
	public Player followedCriminal() {
		return followedCriminal;
	}
	
	@Override
	public int followedtime() {
		return followedtime;
	}
	
	@Override
	public void setFollowedCriminal(Player player) {
		followedCriminal = player;
	}
	
	@Override
	public void setFollowedtime(int time) {
		followedtime = time;
	}

	@Override
	public Guard me() {
		return (Guard)(Object)this;
	}

	@Override
	public BlockPos investigatePos() {
		return investigatePos;
	}

	@Override
	public Crime crimeToInvestigate() {
		return crimeToInvestigate;
	}

	@Override
	public void setInvestigatePos(BlockPos pos) {
		this.investigatePos = pos;
	}

	@Override
	public void crimeToInvestigate(Crime crime) {
		this.crimeToInvestigate = crime;
	}
}
