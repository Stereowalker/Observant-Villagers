package com.stereowalker.obville.compat;

import com.stereowalker.obville.Law;
import com.stereowalker.obville.Laws;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MoreCTCompat {
	public static boolean fromMod(Block block){
		if (block == com.duarte.mctb.blocks.Blocks.ACACIA_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.AZALEA_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.BIRCH_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.BLOSSOM_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.CHERRY_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.CRIMSON_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.DARK_OAK_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.DEAD_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.FIR_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.HELLBARK_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.JACARANDA_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.JUNGLE_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.MAGIC_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.MAHOGANY_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.PALM_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.REDWOOD_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.SPRUCE_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.UMBRAN_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.WARPED_CRAFTING_TABLE) return true;
		else if (block == com.duarte.mctb.blocks.Blocks.WILLOW_CRAFTING_TABLE) return true;
		return false;
	}
	
	public static Item equivalentItem(Block block){
		if (block == com.duarte.mctb.blocks.Blocks.ACACIA_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.ACACIA_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.AZALEA_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.AZALEA_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.BIRCH_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.BIRCH_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.BLOSSOM_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.BLOSSOM_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.CHERRY_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.CHERRY_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.CRIMSON_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.CRIMSON_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.DARK_OAK_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.DARK_OAK_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.DEAD_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.DEAD_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.FIR_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.FIR_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.HELLBARK_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.HELLBARK_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.JACARANDA_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.JACARANDA_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.JUNGLE_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.JUNGLE_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.MAGIC_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.MAGIC_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.MAHOGANY_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.MAHOGANY_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.PALM_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.PALM_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.REDWOOD_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.REDWOOD_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.SPRUCE_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.SPRUCE_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.UMBRAN_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.UMBRAN_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.WARPED_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.WARPED_CRAFTING_TABLE;
		else if (block == com.duarte.mctb.blocks.Blocks.WILLOW_CRAFTING_TABLE) return com.duarte.mctb.blocks.BlockItems.WILLOW_CRAFTING_TABLE;
		return null;
	}
	
	public static Law equivalentLaw(Block block){
		if (block == com.duarte.mctb.blocks.Blocks.ACACIA_CRAFTING_TABLE) return Laws.BREAKING_ACACIA_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.AZALEA_CRAFTING_TABLE) return Laws.BREAKING_AZALEA_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.BIRCH_CRAFTING_TABLE) return Laws.BREAKING_BIRCH_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.BLOSSOM_CRAFTING_TABLE) return Laws.BREAKING_BLOSSOM_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.CHERRY_CRAFTING_TABLE) return Laws.BREAKING_CHERRY_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.CRIMSON_CRAFTING_TABLE) return Laws.BREAKING_CRIMSON_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.DARK_OAK_CRAFTING_TABLE) return Laws.BREAKING_DARK_OAK_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.DEAD_CRAFTING_TABLE) return Laws.BREAKING_DEAD_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.FIR_CRAFTING_TABLE) return Laws.BREAKING_FIR_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.HELLBARK_CRAFTING_TABLE) return Laws.BREAKING_HELLBARK_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.JACARANDA_CRAFTING_TABLE) return Laws.BREAKING_JACARANDA_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.JUNGLE_CRAFTING_TABLE) return Laws.BREAKING_JUNGLE_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.MAGIC_CRAFTING_TABLE) return Laws.BREAKING_MAGIC_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.MAHOGANY_CRAFTING_TABLE) return Laws.BREAKING_MAHOGANY_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.PALM_CRAFTING_TABLE) return Laws.BREAKING_PALM_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.REDWOOD_CRAFTING_TABLE) return Laws.BREAKING_REDWOOD_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.SPRUCE_CRAFTING_TABLE) return Laws.BREAKING_SPRUCE_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.UMBRAN_CRAFTING_TABLE) return Laws.BREAKING_UMBRAN_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.WARPED_CRAFTING_TABLE) return Laws.BREAKING_WARPED_CRAFTING_TABLES;
		else if (block == com.duarte.mctb.blocks.Blocks.WILLOW_CRAFTING_TABLE) return Laws.BREAKING_WILLOW_CRAFTING_TABLES;
		return null;
	}
}
