package com.stereowalker.obville.network.protocol.game;

import java.util.UUID;
import java.util.function.Supplier;

import com.stereowalker.obville.interfaces.IModdedEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundNBTPacket {
	private CompoundTag stats;
	private UUID uuid;
	
	public ClientboundNBTPacket(final CompoundTag statsIn, final UUID uuid) {
		this.stats = statsIn;
		this.uuid = uuid;
	}
	
	public ClientboundNBTPacket(final ServerPlayer player){
		this(new CompoundTag(), player.getUUID());
		((IModdedEntity)player).getData().write(stats);
	}
	
	public static void encode(final ClientboundNBTPacket msg, final FriendlyByteBuf packetBuffer) {
		packetBuffer.writeNbt(msg.stats);
		packetBuffer.writeLong(msg.uuid.getMostSignificantBits());
        packetBuffer.writeLong(msg.uuid.getLeastSignificantBits());
	}
	
	public static ClientboundNBTPacket decode(final FriendlyByteBuf packetBuffer) {
		return new ClientboundNBTPacket(packetBuffer.readNbt(), new UUID(packetBuffer.readLong(), packetBuffer.readLong()));
	}
	
	@SuppressWarnings("deprecation")
	public static void handle(final ClientboundNBTPacket msg, final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final CompoundTag stats = msg.stats;
			final UUID uuid = msg.uuid;
			update(stats, uuid);
		}));
		context.setPacketHandled(true);
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static void update(final CompoundTag stats, final UUID uuid) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (uuid.equals(Player.createPlayerUUID(player.getGameProfile()))) {
//			ModdedStats.setModNBT(stats, player);
		}
		((IModdedEntity)player).getData().read(stats);
	}
}
