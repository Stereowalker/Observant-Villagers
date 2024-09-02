package com.stereowalker.obville.interfaces;

import java.util.List;
import java.util.UUID;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface ILootableBlock {
	public ResourceLocation getLoot();
	public List<UUID> playersOpened();
	
	public void addPlayer(Player player);
	
	public default boolean hasPlayerOpened(Player player) {
		if (playersOpened() == null) return false;
		return playersOpened().contains(player.getUUID());
	}
}
