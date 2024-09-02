package com.stereowalker.obville;

import java.util.function.Supplier;

public class Law {
	public String crimeIdentifier;
	private Supplier<Integer> reputationHit;
	private boolean pardonable;
	
	public Law(String crimeIdentifier, Supplier<Integer> reputationHit, boolean pardonable) {
		this.crimeIdentifier = crimeIdentifier;
		this.reputationHit = reputationHit;
		this.pardonable = pardonable;
	}
	
	public Law(String crimeIdentifier, Supplier<Integer> reputationHit) {
		this(crimeIdentifier, reputationHit, true);
	}
	
	public int getRepHit() {
		return reputationHit.get();
	}
	
	public boolean isPardonable() {
		return pardonable;
	}
}
