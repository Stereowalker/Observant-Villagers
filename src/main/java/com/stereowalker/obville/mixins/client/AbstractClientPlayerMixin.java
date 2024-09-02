package com.stereowalker.obville.mixins.client;

import org.spongepowered.asm.mixin.Mixin;

import com.mojang.authlib.GameProfile;
import com.stereowalker.obville.interfaces.IModdedEntity;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IModdedEntity {

	public AbstractClientPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
		super(pLevel, pPos, pYRot, pGameProfile);
	}

	@Override
	public void tick() {
		super.tick();
		AbstractClientPlayer player = (AbstractClientPlayer)(Object)this;
		getData().baseClientTick(player);
	}
}
