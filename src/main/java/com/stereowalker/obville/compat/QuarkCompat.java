package com.stereowalker.obville.compat;

import com.stereowalker.obville.Law;
import com.stereowalker.obville.Laws;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class QuarkCompat {
	public static boolean isTable(Block block){
		return block instanceof vazkii.quark.content.building.block.VariantBookshelfBlock;
	}
	
	public static Item equivalentItem(Block block){
		if (block instanceof vazkii.quark.content.building.block.VariantBookshelfBlock vBlock)
		return vBlock.asItem();
		return Items.AIR;
	}
	
	public static Law equivalentLaw(Block block){
		return Laws.BREAKING_QUARK_BOOKSHELVES;
	}
}
