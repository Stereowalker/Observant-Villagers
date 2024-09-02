package com.stereowalker.obville.dat;

import com.stereowalker.obville.Law;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;

public class VillagerReputation {
	public Tuple<Law, Integer> recentlyWitnessedCrime = null;
	
	
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		tag.putString("RecentCrimeCommited", recentlyWitnessedCrime.getA().crimeIdentifier);
		tag.putInt("TimeSinceCommitedCrime", recentlyWitnessedCrime.getB());
		return  tag;
	}
	
	
//	public static VillagerReputation load(CompoundTag tag) {
//		VillagerReputation rep = new VillagerReputation();
//		rep.recentlyWitnessedCrime = new Tuple<Law, Integer>(tag.getString("RecentCrimeCommited"), tag.getInt("TimeSinceCommitedCrime"));
//	}
}
