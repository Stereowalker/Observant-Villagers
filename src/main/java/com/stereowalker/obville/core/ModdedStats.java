package com.stereowalker.obville.core;

import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.dat.OVModData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;

public class ModdedStats {
	public static String getModDataString() {
		return ObVille.MOD_ID+":PlayerData";
	}

	public static CompoundTag getModNBT(Entity entity) {
		return entity.getPersistentData().getCompound(getModDataString());
	}

	public static CompoundTag getOrCreateModNBT(Entity entity) {
		if (!entity.getPersistentData().contains(getModDataString(), 10)) {
			entity.getPersistentData().put(getModDataString(), new CompoundTag());
		}
		return entity.getPersistentData().getCompound(getModDataString());
	}

	public static void setModNBT(CompoundTag nbt, Entity entity) {
		entity.getPersistentData().put(getModDataString(), nbt);
	}
}
