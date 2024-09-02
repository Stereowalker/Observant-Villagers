package com.stereowalker.obville;

import com.stereowalker.obville.dat.OVModData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public class Crime {
	public Law lawBroken;
	public ItemStack reparation1, reparation2;
	
	public Crime(Law lawBroken, ItemStack reparation1, ItemStack reparation2) {
		this.lawBroken = lawBroken;
		this.reparation1 = reparation1;
		this.reparation2 = reparation2;
	}
	
	public Crime(Law lawBroken, ItemStack reparation1) {
		this(lawBroken, reparation1, ItemStack.EMPTY);
	}
	
	public Crime(Law lawBroken, Item reparation) {
		this(lawBroken, new ItemStack(reparation, 2));
	}
	
	public static Crime read(CompoundTag tag) {
		return new Crime(Laws.lawsToUphold.get(tag.getString("lawBroken")), 
				ItemStack.of(tag.getCompound("reparation1")), 
				ItemStack.of(tag.getCompound("reparation2")));
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		if (this.lawBroken != null) tag.putString("lawBroken", lawBroken.crimeIdentifier);
		tag.put("reparation1", reparation1.save(new CompoundTag()));
		tag.put("reparation2", reparation2.save(new CompoundTag()));
		return tag;
	}

	public static ItemStack forgive(int amount, String forWhat, int whichVillage) {
		ItemStack stack = new ItemStack(Items.PAPER);
		stack.setHoverName(new TranslatableComponent("crime."+forWhat));
		CompoundTag give = new CompoundTag();
		give.putInt("amount", amount);
		give.putString("forWhat", forWhat);
		give.putInt("whichVillage", whichVillage);
		stack.getOrCreateTag().put("obville:to_forgive", give);
		return stack;
	}
	
	public MerchantOffer frogive(OVModData data, int village) {
		return new MerchantOffer(reparation1.copy(), reparation2.copy(), 
				forgive(-lawBroken.getRepHit(), lawBroken.crimeIdentifier, village), data.crimesCommitedOfType(village, lawBroken), 0, 1);
	}
}
