package com.stereowalker.obville.network.protocol.game;

import java.util.UUID;

import com.stereowalker.obville.ObVille;
import com.stereowalker.unionlib.network.protocol.game.ClientboundUnionPacket;

import net.minecraft.Util;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class ClientboundVillagerMessagePacket extends ClientboundUnionPacket {
	private Component message;
	private UUID uuid;

	public ClientboundVillagerMessagePacket(final Component message, final UUID uuid) {
		super(ObVille.getInstance().channel);
		this.uuid = uuid;
		this.message = message;
	}
	
	public ClientboundVillagerMessagePacket(FriendlyByteBuf packetBuffer) {
		super(ObVille.getInstance().channel);
		this.message = packetBuffer.readComponent();
		this.uuid = new UUID(packetBuffer.readLong(), packetBuffer.readLong());
	}

	@Override
	public void encode(FriendlyByteBuf packetBuffer) {
		packetBuffer.writeComponent(message);
		packetBuffer.writeLong(uuid.getMostSignificantBits());
		packetBuffer.writeLong(uuid.getLeastSignificantBits());
	}

	@Override
	public boolean handleOnClient(LocalPlayer player) {
		if (uuid.equals(Player.createPlayerUUID(player.getGameProfile()))) {
			player.sendMessage(message, Util.NIL_UUID);
		}
		return true;
	}
}
