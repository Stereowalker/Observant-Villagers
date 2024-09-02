package com.stereowalker.obville.compat;

import com.stereowalker.obville.Crime;
import com.stereowalker.obville.Law;
import com.stereowalker.obville.Laws;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FarmersDelightCompat {
	
	public static boolean plantFromMod(Block block){
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.TOMATO_CROP.get()) return true;
		return false;
	}
	
	public static Crime equivalentCrime(Block block){
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.TOMATO_CROP.get()) return new Crime(Laws.BREAKING_TOMATO, 
				new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.TOMATO.get(), 2), 
				new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.TOMATO_SEEDS.get(), 1));
		return null;
	}
	
	
	public static boolean fromMod(Block block){
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.RICH_SOIL.get()) return true;
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.RICH_SOIL_FARMLAND.get()) return true;
		else if (block == vectorwing.farmersdelight.common.registry.ModBlocks.ORGANIC_COMPOST.get()) return true;
		return false;
	}
	
	
	public static Item equivalentItem(Block block){
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.RICH_SOIL.get()) return vectorwing.farmersdelight.common.registry.ModItems.RICH_SOIL.get();
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.RICH_SOIL_FARMLAND.get()) return vectorwing.farmersdelight.common.registry.ModItems.RICH_SOIL.get();
		else if (block == vectorwing.farmersdelight.common.registry.ModBlocks.ORGANIC_COMPOST.get()) return vectorwing.farmersdelight.common.registry.ModItems.ORGANIC_COMPOST.get();
		return null;
	}
	
	public static Law equivalentLaw(Block block){
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.RICH_SOIL.get()) return Laws.BREAKING_RICH_SOIL;
		if (block == vectorwing.farmersdelight.common.registry.ModBlocks.RICH_SOIL_FARMLAND.get()) return Laws.BREAKING_RICH_SOIL;
		else if (block == vectorwing.farmersdelight.common.registry.ModBlocks.ORGANIC_COMPOST.get()) return Laws.BREAKING_ORGANIC_COMPOST;
		return null;
	}
}
