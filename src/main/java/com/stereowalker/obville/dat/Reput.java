package com.stereowalker.obville.dat;

import net.minecraft.nbt.CompoundTag;

public class Reput {
	public boolean generatedBounty = false;
	public boolean droppedBounty = false;
	public boolean hasSpokenToLeader = false;
	public boolean hasCommitedCrimeBefore = false;
	
	public static Reput read(CompoundTag tag) {
		Reput rep = new Reput();
		rep.droppedBounty = tag.getBoolean("generatedBounty");
		rep.droppedBounty = tag.getBoolean("droppedBounty");
		rep.hasSpokenToLeader = tag.getBoolean("hasSpokenToLeader");
		rep.hasSpokenToLeader = tag.getBoolean("hasCommitedCrimeBefore");
		return rep;
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("generatedBounty", droppedBounty);
		tag.putBoolean("droppedBounty", droppedBounty);
		tag.putBoolean("hasSpokenToLeader", hasSpokenToLeader);
		tag.putBoolean("hasCommitedCrimeBefore", hasSpokenToLeader);
		return tag;
	}
}
