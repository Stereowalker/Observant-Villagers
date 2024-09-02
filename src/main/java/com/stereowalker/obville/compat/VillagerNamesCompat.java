package com.stereowalker.obville.compat;

import com.mojang.datafixers.util.Pair;
import com.stereowalker.obville.world.entity.VillageLeader;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class VillagerNamesCompat {
	public static void overrideMerchantScreen(Component title, VillageLeader leader){
		com.natamus.villagernames_common_forge.data.Variables.tradedVillagerPair = new Pair<Component, Component>(leader.getName(), title);
	}
}
