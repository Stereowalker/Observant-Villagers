package com.stereowalker.obville.dat;

import java.util.Random;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BaseData {
	Random rng;
	public abstract void tick(Player player);
	public abstract void read(CompoundTag compound);
	public abstract void write(CompoundTag compound);
	public abstract boolean shouldTick();
	
	public BaseData() {
		this.rng = new Random();
	}
	
	public void baseTick(Player player) {
		if (shouldTick()) {
			tick(player);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void baseClientTick(AbstractClientPlayer player) {
		if (shouldTick()) {
			clientTick(player);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void clientTick(AbstractClientPlayer player) {
		
	}
}