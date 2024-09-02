package com.stereowalker.obville.network.protocol.game;

import java.util.UUID;

import com.stereowalker.obville.ObVille;
import com.stereowalker.unionlib.network.protocol.game.ClientboundUnionPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

public class ClientboundOverlayOverridePacket extends ClientboundUnionPacket {
	private UUID uuid;

	public ClientboundOverlayOverridePacket(final UUID uuid) {
		super(ObVille.getInstance().channel);
		this.uuid = uuid;
	}
	
	public ClientboundOverlayOverridePacket(FriendlyByteBuf packetBuffer) {
		super(ObVille.getInstance().channel);
		this.uuid = new UUID(packetBuffer.readLong(), packetBuffer.readLong());
	}

	@Override
	public void encode(FriendlyByteBuf packetBuffer) {
		packetBuffer.writeLong(uuid.getMostSignificantBits());
		packetBuffer.writeLong(uuid.getLeastSignificantBits());
	}

	@Override
	public boolean handleOnClient(LocalPlayer player) {
		if (uuid.equals(Player.createPlayerUUID(player.getGameProfile()))) {
			Minecraft mc = Minecraft.getInstance();
			mc.gui.setOverlayMessage(new TranslatableComponent("obville.messages.bribe_opportunity"), false);
			mc.gui.overlayMessageTime = 120;
		}
		return true;
	}
}
