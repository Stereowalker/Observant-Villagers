package com.stereowalker.obville.config;

import com.stereowalker.unionlib.config.ConfigObject;
import com.stereowalker.unionlib.config.UnionConfig;

import net.minecraftforge.fml.config.ModConfig.Type;

@UnionConfig(folder = "Obville Configs", name = "reputation", translatableName = "config.obville.reputation.file", autoReload = true)
public class ReputationAmountConfig implements ConfigObject {	
	
	@UnionConfig.Entry(name = "Defend From Raid", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for defending the village in a raid"})
	public int raid_defence = 5;

	@UnionConfig.Entry(name = "Bounties", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for completing a bounty"})
	public int bounty = 5;
	
	@UnionConfig.Entry(name = "Max Out Trade", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for maxing out a villagers trades"})
	public int max_trade = 2;
	
	@UnionConfig.Entry(name = "Opening Chests or Barrels", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for opening barrels or chests generated in the village in the presence of another villager"})
	public int opening_containers = -2;
	
	@UnionConfig.Entry(name = "Bribing Villagers", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for failing to bribe a villager"})
	public int bribe_fail = -1;
	
	@UnionConfig.Entry(name = "Sleeping In Village Beds", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for sleeping in beds generated in the village in the presence of another villager"})
	public int sleeping_in_bed = -1;
	
	@UnionConfig.Entry(name = "Kick Villager Out Of Bed", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for kicking a villager out of bed"})
	public int kick_out_of_bed = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Waystones", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking waystones generated in villages"})
	public int breaking_waystones = -3;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Melons", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking melons in villages"})
	public int breaking_melons = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Pumpkins", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking pumpkins in villages"})
	public int breaking_pumpkins = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Bells", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking bells found in villages"})
	public int breaking_bells = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Job Sites", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking blocks that give a villager a profession in the village"})
	public int breaking_job_sites = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Rich Soil", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking rich soil from farmers delight"})
	public int break_rich_soil = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Organic Compost", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking organic compost from farmers delight"})
	public int break_organic_compost = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Flower Pots", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking flower pots generated in a village"})
	public int breaking_pot = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Torches And Lanterns", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking torches and lanterns generated in the village"})
	public int breaking_torches = -1;
	
	@UnionConfig.Entry(group = "Breaking Stuff", name = "Hay Bales", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking any haybale in a village"})
	public int breaking_hay = -1;
	
	@UnionConfig.Entry(name = "Breaking Crops", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for uprooting a crop in a village"})
	public int breaking_crops = -1;
	
	@UnionConfig.Entry(name = "Breaking Beds", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for breaking beds generated in a village"})
	public int breaking_beds = -1;
	
	@UnionConfig.Entry(name = "Killing Villagers", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for killing villagers"})
	public int killing_villagers = -8;
	
	@UnionConfig.Entry(name = "Killing Iron Golems", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for killing iron golems"})
	public int killing_golems = -8;
	
	@UnionConfig.Entry(name = "Killing Guard Villagers", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for killing guard villagers"})
	public int killing_guards = -8;
	
	@UnionConfig.Entry(name = "Killing Village Leaders", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much reputation is gained or lost for killing village leaders"})
	public int killing_chiefs = -10;
}
