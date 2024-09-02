package com.stereowalker.obville.network.protocol.game;

import com.stereowalker.obville.ObVille;
import com.stereowalker.unionlib.network.protocol.game.ServerboundUnionPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ServerboundRelaxPacket extends ServerboundUnionPacket {
	int amount;

	public ServerboundRelaxPacket(int amount) {
		super(ObVille.getInstance().channel);
		this.amount = amount;
	}

	public ServerboundRelaxPacket(FriendlyByteBuf packetBuffer) {
		super(packetBuffer, ObVille.getInstance().channel);
		this.amount = packetBuffer.readVarInt();
	}

	@Override
	public void encode(final FriendlyByteBuf packetBuffer) {
		packetBuffer.writeVarInt(this.amount);
	}

	@Override
	public boolean handleOnServer(ServerPlayer sender) {
//		if (ObVille.STAMINA_CONFIG.enabled) {
//			StaminaData stats = SurviveEntityStats.getEnergyStats(sender);
//			stats.relax(this.amount, sender.getAttributeValue(SAttributes.MAX_STAMINA));
//			stats.save(sender);
//		}
		return true;
	}
}
