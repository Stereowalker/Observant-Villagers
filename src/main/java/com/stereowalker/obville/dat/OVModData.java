package com.stereowalker.obville.dat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableInt;

import com.stereowalker.obville.Crime;
import com.stereowalker.obville.Law;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

/**
 * 
 */
public class OVModData extends BaseData {
	private Map<Integer,List<Crime>> crimesCommited = new HashMap<>();
	private Map<Integer,Integer> reputation = new HashMap<>();
	public Map<Integer,Integer> prevReputation = new HashMap<>();
	public Map<Integer,Reput> repu = new HashMap<>();
	private boolean hasSetPreviousRep = false;
	private int previousVillage = -1;
	private int currentVillage = -1;
	private int cropsBrokenWarning = 0;
	private int ticker = 0;
	private int committedBanditry = 0;

	public OVModData() {
		this.reputation = new HashMap<>();
	}

	public void tick(Player player) {
		ticker++;
		if (cropsBrokenWarning > 0) cropsBrokenWarning--;
		if (ticker >= 160) {
			ticker = 0;
		}

		if (recentlyCommitedBandirty() && player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARVED_PUMPKIN) {
			committedBanditry--;
		}

		reputation.keySet().forEach(village -> {
			if (reputation.get(village) <= -4 && !reputAtNoSave(village).hasCommitedCrimeBefore) reputAt(village).hasCommitedCrimeBefore = true;
		});
	}

	@Override
	public void clientTick(AbstractClientPlayer player) {
		if (!hasSetPreviousRep) {
			reputation.forEach((village, reputation) -> {
				prevReputation.put(village.intValue(), reputation.intValue());
			});
			hasSetPreviousRep = true;
		}

		for (int village : reputation.keySet()) {
			if (prevReputation.containsKey(village)) {
				if (prevReputation.get(village).intValue() != reputation.get(village).intValue()) {
					ticker++; break;
				}
			} else {
				ticker++; break;
			}
		}
		if (ticker >= 80) {
			reputation.forEach((village, reputation) -> {
				prevReputation.put(village.intValue(), reputation.intValue());
			});
			ticker = 0;
		}
	}
	
	public Set<Integer> vill(){
		return reputation.keySet();
	}

	/**
	 * Reads the water data for the player.
	 */
	public void read(CompoundTag compound) {
		if (compound.contains("cropsBrokenWarning", 99)) {
			ListTag list4 = compound.getList("AllCrimes", 10);
			for (int i = 0; i < list4.size(); i++) {
				CompoundTag tag = list4.getCompound(i);
				ListTag tagList = tag.getList("Crimes", 10);
				List<Crime> crimes = new ArrayList<>();
				for (int j = 0; j < tag.size(); j++) {
					if (tagList.getCompound(i).contains("lawBroken"))
						crimes.add(Crime.read(tagList.getCompound(i)));
				}
				this.crimesCommited.put(tag.getInt("Village"), crimes);
			}

			ListTag list2 = compound.getList("Rep", 10);
			for (int i = 0; i < list2.size(); i++) {
				CompoundTag tag = list2.getCompound(i);
				this.reputation.put(tag.getInt("Village"), tag.getInt("Reputation"));
			}

			ListTag list5 = compound.getList("Repu", 10);
			for (int i = 0; i < list5.size(); i++) {
				CompoundTag tag = list5.getCompound(i);
				this.repu.put(tag.getInt("Village"), Reput.read(tag.getCompound("RepDat")));
			}

			this.currentVillage = compound.getInt("currentVillage");
			this.previousVillage = compound.getInt("previousVillage");
			this.cropsBrokenWarning = compound.getInt("cropsBrokenWarning");
			this.committedBanditry = compound.getInt("committedBanditry");
		}

	}

	/**
	 * Writes the water data for the player.
	 */
	public void write(CompoundTag compound) {
		ListTag list4 = new ListTag();
		crimesCommited.forEach((village, crimes) -> {
			CompoundTag tag = new CompoundTag();
			tag.putInt("Village", village);
			ListTag listTag = new ListTag();
			crimes.forEach(crime -> listTag.add(crime.write()));
			tag.put("Crimes", listTag);
			list4.add(tag);
		});
		compound.put("AllCrimes", list4);

		ListTag list2 = new ListTag();
		reputation.forEach((village, timer) -> {
			CompoundTag tag = new CompoundTag();
			tag.putInt("Village", village);
			tag.putInt("Reputation", timer);
			list2.add(tag);
		});
		compound.put("Rep", list2);

		ListTag list5 = new ListTag();
		repu.forEach((village, dat) -> {
			CompoundTag tag = new CompoundTag();
			tag.putInt("Village", village);
			tag.put("RepDat", dat.write());
			list5.add(tag);
		});
		compound.put("Repu", list5);

		compound.putInt("currentVillage", this.currentVillage);
		compound.putInt("previousVillage", this.previousVillage);
		compound.putInt("cropsBrokenWarning", this.cropsBrokenWarning);
		compound.putInt("committedBanditry", this.committedBanditry);
	}

	public void caughtBanditry() {
		committedBanditry = 20;
	}

	public boolean recentlyCommitedBandirty() {
		return committedBanditry > 0;
	}

	public int getReputation() {
		if (currentVillage >= 0)
			return getReputationInCurrentVillage();
		return 0;
	}

	public int getPrevReputation() {
		if (previousVillage >= 0)
			return reputation.getOrDefault(previousVillage, 0);
		return 0;
	}

	public boolean IsWelcomeAt(int village) {
		if (village >= 0)
			return this.getReputationIn(village) >= 10;
			return false;
	}

	public boolean IsWelcome() {
		return IsWelcomeAt(currentVillage);
	}

	public boolean IsNeutralAt(int village) {
		if (village >= 0)
			return this.getReputationIn(village) <= 9 && this.getReputationIn(village) > -4;
			return false;
	}

	public boolean IsNeutral() {
		return IsNeutralAt(currentVillage);
	}

	public boolean IsWearyAt(int village) {
		if (village >= 0)
			return this.getReputationIn(village) <= -4 && this.getReputationIn(village) > -8;
			return false;
	}

	public boolean IsWeary() {
		return IsWearyAt(currentVillage);
	}

	public boolean IsDistrustedAt(int village) {
		if (village >= 0)
			return this.getReputationIn(village) <= -8 && this.reputation.get(village) > -14;
			return false;
	}

	public boolean IsDistrusted() {
		return IsDistrustedAt(currentVillage);
	}

	public boolean IsExiledAt(int village) {
		if (village >= 0)
			return this.getReputationIn(village) <= -14;
		return false;
	}

	public boolean IsExiled() {
		return IsExiledAt(currentVillage);
	}

	public Reput reputAtNoSave(int village) {
		if (village >= 0)
			return repu.getOrDefault(village, new Reput());
		return null;
	}

	public Reput reputAt(int village) {
		if (!repu.containsKey(village))
			repu.put(village, new Reput());
		return reputAtNoSave(village);
	}

	public Reput reput() {
		return reputAt(currentVillage);
	}

	public boolean isWatchedForBreakingCrops() {
		return this.cropsBrokenWarning > 0;
	}


	/**
	 * -1 Represents Not In Village
	 * 0 Represents Global Village
	 * 
	 * @return
	 */
	public int currentVillage() {
		return this.currentVillage;
	}
	public int previousVillage() {
		return this.previousVillage;
	}

	private int getReputationIn(int village) {
		return reputation.getOrDefault(village, 0);
	}

	private int getReputationInCurrentVillage() {
		return getReputationIn(currentVillage);
	}

	public boolean isInAnyVillage() {
		return currentVillage() >= 0;
	}

	public void resolveCrime(int village, Law law) {
		for(int i = 0; i < crimesCommited.get(village).size(); i++) {
			if (crimesCommited.get(village).get(i).lawBroken == law) {
				crimesCommited.get(village).remove(i);
				break;
			}
		}
	}

	public int crimesCommitedOfType(int village, Law law) {
		MutableInt i = new MutableInt();
		crimesCommitedAt(village).forEach((crime) -> {
			if (crime.lawBroken == law) i.increment();
		});
		return i.getValue();
	}

	public List<Crime> crimesCommitedAt(int village) {
		if (village >= 0)
			return this.crimesCommited.getOrDefault(village, new ArrayList<>());
		return new ArrayList<>();
	}

	public void watchForBreakingCrops() {
		this.cropsBrokenWarning = 1200;
	}

	public void setHasSpokenToLeader() {
		this.reput().hasSpokenToLeader = true;
	}
	public void setReputation(int rep) {
		if (currentVillage >= 0)
			this.reputation.put(currentVillage, rep);
	}

	public void incrementReputation(int rep) {
		if (currentVillage >= 0)
			this.reputation.put(currentVillage, this.reputation.getOrDefault(currentVillage, 0)+rep);
	}

	public void setInVillage(int current) {
		this.currentVillage = current;
		if (current >= 0)
			this.previousVillage = current;
	}
	
	public void commitCrime(Crime crime) {
		if (!crimesCommited.containsKey(currentVillage()))
			crimesCommited.put(currentVillage(), new ArrayList<>());
		crimesCommited.get(currentVillage()).add(crime);
	}

	@Override
	public boolean shouldTick() {
		return true;
	}
}
