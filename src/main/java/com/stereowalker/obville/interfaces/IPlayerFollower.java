package com.stereowalker.obville.interfaces;

import net.minecraft.world.entity.player.Player;

public interface IPlayerFollower {
	public Player followedCriminal();
	public int followedtime();
	public void setFollowedCriminal(Player player);
	public void setFollowedtime(int time);
	
	public default void follow(Player player) {
		setFollowedCriminal(player);
		if (player == null) {
			setFollowedtime(0);
		} else {
			setFollowedtime(followedtime()+1);
		}
	}
}
