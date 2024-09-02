package com.stereowalker.obville.mixins;

import java.util.Iterator;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.dat.VillageData;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.network.protocol.game.ClientboundSoundPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.network.NetworkDirection;

@Mixin(Raid.class)
public abstract class RaidMixin {
	
	@Inject(method = "tick", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value= "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;)V"))
	private void tickInject(CallbackInfo ci,  boolean flag, int i, boolean flag3, int k, Iterator var5, UUID uuid, Entity entity, LivingEntity livingentity, ServerPlayer serverplayer) {			
		ObVille.getInstance().channel.sendTo(new ClientboundSoundPacket(true, serverplayer.getUUID()), serverplayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    	((IModdedEntity)serverplayer).getData().incrementReputation(ObVille.REPUTATION_CONFIG.raid_defence);
    	VillageData.invalidateGounty(serverplayer);
	}
	

}
