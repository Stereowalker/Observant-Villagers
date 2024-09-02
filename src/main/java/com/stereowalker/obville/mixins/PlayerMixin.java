package com.stereowalker.obville.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.obville.core.ModdedStats;
import com.stereowalker.obville.dat.OVModData;
import com.stereowalker.obville.interfaces.IModdedEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IModdedEntity {
	@Shadow private int sleepCounter;
	private OVModData modData = new OVModData();

	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void initInject(CallbackInfo ci) {
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/world/entity/player/Player;updateIsUnderwater()Z"))
	public void tickInject(CallbackInfo ci) {
		Player player = (Player)(Object)this;
		if (player != null) {
			CompoundTag compound = ModdedStats.getOrCreateModNBT(player);
			if(player.isAlive()) {
				if (!compound.contains("obvData")) {
					CompoundTag compound2 = new CompoundTag();
					modData.write(compound2);
					ModdedStats.getModNBT(player).put("obvData", compound2);
				}
			}
		}
		
		//
		if (!this.level.isClientSide && (Player)(Object)this instanceof ServerPlayer serverplayer) {
			getData().baseTick(serverplayer);
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	public void readInject(CompoundTag pCompound, CallbackInfo ci) {
		if (!level.isClientSide) {
			OVModData stats = new OVModData();
			Player entity = (Player)(Object)this;
			if(entity != null) {
				if (ModdedStats.getModNBT(entity) != null && ModdedStats.getModNBT(entity).contains("obvData", 10)) {
					stats.read(ModdedStats.getModNBT(entity).getCompound("obvData"));
				}
			}
			modData = stats;
		}
	}
	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
	public void writeInject(CompoundTag pCompound, CallbackInfo ci) {
		if (!level.isClientSide) {
			Player entity = (Player)(Object)this;
			CompoundTag compound2 = new CompoundTag();
			modData.write(compound2);
			ModdedStats.getModNBT(entity).put("obvData", compound2);
		}
	}

	public OVModData getData(){
		return modData;
	}
	
	@Override
	public void setData(OVModData data) {
		modData = data;
	}

}
