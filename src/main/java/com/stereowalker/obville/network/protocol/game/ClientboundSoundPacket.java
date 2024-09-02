package com.stereowalker.obville.network.protocol.game;

import java.util.UUID;
import java.util.function.Supplier;

import com.stereowalker.obville.sounds.ModSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSoundPacket {
	private boolean positive;
	private UUID uuid;

	public ClientboundSoundPacket(
			final boolean positive,
			final UUID uuid) {
		this.uuid = uuid;
		this.positive = positive;
	}

	public static void encode(final ClientboundSoundPacket msg, final FriendlyByteBuf packetBuffer) {
		packetBuffer.writeBoolean(msg.positive);
		packetBuffer.writeLong(msg.uuid.getMostSignificantBits());
		packetBuffer.writeLong(msg.uuid.getLeastSignificantBits());
	}

	public static ClientboundSoundPacket decode(final FriendlyByteBuf packetBuffer) {
		return new ClientboundSoundPacket(packetBuffer.readBoolean(), new UUID(packetBuffer.readLong(), packetBuffer.readLong()));
	}

	@SuppressWarnings("deprecation")
	public static void handle(final ClientboundSoundPacket msg, final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final UUID uuid = msg.uuid;
			final boolean message = msg.positive;
			update(message, uuid);
		}));
		context.setPacketHandled(true);
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static void update(final boolean message, final UUID uuid) {
		if (uuid.equals(Player.createPlayerUUID(Minecraft.getInstance().player.getGameProfile()))) {
			Minecraft.getInstance().player.level.playLocalSound(Minecraft.getInstance().player.getX(), Minecraft.getInstance().player.getY(), Minecraft.getInstance().player.getZ(), message ? ModSounds.POSITIVE : ModSounds.NEGATIVE, SoundSource.PLAYERS, 1, 1, false);
		}
	}
}
