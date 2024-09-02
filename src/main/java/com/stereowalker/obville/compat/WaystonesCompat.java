package com.stereowalker.obville.compat;

import com.stereowalker.obville.Law;
import com.stereowalker.obville.Laws;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WaystonesCompat {
	public static boolean isTable(BlockEntity block){
		return block instanceof net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase way && way.getWaystone().wasGenerated();
	}
	
	public static Item equivalentItem(Block block){
		return block.asItem();
	}
	
	public static Law equivalentLaw(Block block){
		return Laws.BREAKING_WAYSTONES;
	}
}
