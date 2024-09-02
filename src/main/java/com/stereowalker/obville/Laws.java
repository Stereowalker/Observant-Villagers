package com.stereowalker.obville;

import java.util.Map;

import com.google.common.collect.Maps;

public class Laws {

	public static Map<String, Law> lawsToUphold = Maps.newHashMap();
	public static final Law KILLING_GOLEMS = reg(new Law("killing_golem", () -> ObVille.REPUTATION_CONFIG.killing_golems, false));
	public static final Law KILLING_VILLAGERS = reg(new Law("killing_villagers", () -> ObVille.REPUTATION_CONFIG.killing_villagers, false));
	public static final Law KILLING_LEADERS = reg(new Law("killing_leaders", () -> ObVille.REPUTATION_CONFIG.killing_chiefs, false));
	public static final Law KILLING_GUARDS = reg(new Law("killing_guards", () -> ObVille.REPUTATION_CONFIG.killing_guards, false));
	public static final Law BREAKING_TORCHES = reg(new Law("breaking_torches", () -> ObVille.REPUTATION_CONFIG.breaking_torches));
	public static final Law BREAKING_CROPS = reg(new Law("breaking_crops", () -> ObVille.REPUTATION_CONFIG.breaking_crops));
	public static final Law BREAKING_CARROT = reg(new Law("breaking_carrot", () -> ObVille.REPUTATION_CONFIG.breaking_crops));
	public static final Law BREAKING_BEETROOT = reg(new Law("breaking_beetroot", () -> ObVille.REPUTATION_CONFIG.breaking_crops));
	public static final Law BREAKING_POTATO = reg(new Law("breaking_potato", () -> ObVille.REPUTATION_CONFIG.breaking_crops));
	public static final Law BREAKING_HAY = reg(new Law("breaking_hay", () -> ObVille.REPUTATION_CONFIG.breaking_hay));
	public static final Law BREAKING_PUMPKINS = reg(new Law("breaking_pumpkins", () -> ObVille.REPUTATION_CONFIG.breaking_pumpkins));
	public static final Law BREAKING_MELONS = reg(new Law("breaking_melons", () -> ObVille.REPUTATION_CONFIG.breaking_melons));
	public static final Law BREAKING_POT = reg(new Law("breaking_pot", () -> ObVille.REPUTATION_CONFIG.breaking_pot));
	public static final Law BREAKING_COMPOSTERS = reg(new Law("breaking_composters", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_BREWING_STAND = reg(new Law("breaking_brewing_stand", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_CARTOGRAPHY_TABLE = reg(new Law("breaking_cartography_table", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_GRINDSTONES = reg(new Law("breaking_grindstone", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_FURNACES = reg(new Law("breaking_furnace", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_CRAFTING_TABLES = reg(new Law("breaking_crafting_table", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_FLETCHING_TABLES = reg(new Law("breaking_fletching_table", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_SMITHING_TABLE = reg(new Law("breaking_smithing_table", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_STONECUTTTER = reg(new Law("breaking_stonecutter", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_ANVILS = reg(new Law("breaking_anvil", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_LECTERN = reg(new Law("breaking_lectern", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_BELLS = reg(new Law("breaking_bell", () -> ObVille.REPUTATION_CONFIG.breaking_bells));
	public static final Law BREAKING_OBSERVER = reg(new Law("breaking_observer", () -> ObVille.REPUTATION_CONFIG.breaking_bells));
	public static final Law BREAKING_LOOM = reg(new Law("breaking_loom", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_TARGET = reg(new Law("breaking_target", () -> ObVille.REPUTATION_CONFIG.breaking_bells));
	public static final Law BREAKING_JUKEBOX = reg(new Law("breaking_jukebox", () -> ObVille.REPUTATION_CONFIG.breaking_bells));
	public static final Law BREAKING_SMOKER = reg(new Law("breaking_smoker", () -> ObVille.REPUTATION_CONFIG.breaking_bells));
	public static final Law BREAKING_CAULDRON = reg(new Law("breaking_cauldron", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_CAMPFIRE = reg(new Law("breaking_campfire", () -> ObVille.REPUTATION_CONFIG.breaking_bells));
	public static final Law BREAKING_BOOKSHELF = reg(new Law("breaking_bookshelf", () -> ObVille.REPUTATION_CONFIG.breaking_bells));
	public static final Law BREAKING_LANTERN = reg(new Law("breaking_lantern", () -> ObVille.REPUTATION_CONFIG.breaking_torches));
	public static final Law BREAKING_BLAST_FURNACE = reg(new Law("breaking_blast_furnace", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_HOPPER = reg(new Law("breaking_hopper", () -> ObVille.REPUTATION_CONFIG.opening_containers));
	public static final Law BREAKING_BED = reg(new Law("breaking_bed", () -> ObVille.REPUTATION_CONFIG.breaking_beds));

	public static final Law BREAKING_CONTAINER = reg(new Law("breaking_container", () -> ObVille.REPUTATION_CONFIG.opening_containers));

	//More Crafting tables
	public static final Law BREAKING_ACACIA_CRAFTING_TABLES = reg(new Law("breaking_acacia_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_AZALEA_CRAFTING_TABLES = reg(new Law("breaking_azalea_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_BIRCH_CRAFTING_TABLES = reg(new Law("breaking_birch_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_BLOSSOM_CRAFTING_TABLES = reg(new Law("breaking_blossom_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_CHERRY_CRAFTING_TABLES = reg(new Law("breaking_cherry_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_CRIMSON_CRAFTING_TABLES = reg(new Law("breaking_crimson_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_DARK_OAK_CRAFTING_TABLES = reg(new Law("breaking_dark_oak_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_DEAD_CRAFTING_TABLES = reg(new Law("breaking_dead_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_FIR_CRAFTING_TABLES = reg(new Law("breaking_fir_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_HELLBARK_CRAFTING_TABLES = reg(new Law("breaking_hellbark_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_JACARANDA_CRAFTING_TABLES = reg(new Law("breaking_jacaranda_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_JUNGLE_CRAFTING_TABLES = reg(new Law("breaking_jungle_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_MAGIC_CRAFTING_TABLES = reg(new Law("breaking_magic_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_MAHOGANY_CRAFTING_TABLES = reg(new Law("breaking_mahogany_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_PALM_CRAFTING_TABLES = reg(new Law("breaking_palm_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_REDWOOD_CRAFTING_TABLES = reg(new Law("breaking_redwood_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_SPRUCE_CRAFTING_TABLES = reg(new Law("breaking_spruce_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_UMBRAN_CRAFTING_TABLES = reg(new Law("breaking_umbran_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_WARPED_CRAFTING_TABLES = reg(new Law("breaking_warped_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));
	public static final Law BREAKING_WILLOW_CRAFTING_TABLES = reg(new Law("breaking_willow_crafting_tables", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));

	//Quark
	public static final Law BREAKING_QUARK_BOOKSHELVES = reg(new Law("breaking_quark_bookshelves", () -> ObVille.REPUTATION_CONFIG.breaking_job_sites));

	//Farmers Delight
	public static final Law BREAKING_TOMATO = reg(new Law("breaking_tomato", () -> ObVille.REPUTATION_CONFIG.breaking_crops));
	public static final Law BREAKING_RICH_SOIL = reg(new Law("breaking_rich_soil", () -> ObVille.REPUTATION_CONFIG.break_rich_soil));
	public static final Law BREAKING_ORGANIC_COMPOST = reg(new Law("breaking_organic_compost", () -> ObVille.REPUTATION_CONFIG.break_organic_compost));

	//Waystones
	public static final Law BREAKING_WAYSTONES = reg(new Law("breaking_waystone", () -> ObVille.REPUTATION_CONFIG.breaking_waystones));

	public static Law reg(Law law) {
		lawsToUphold.put(law.crimeIdentifier, law);
		return law;
	}

	public static void bootstrap() {
	}
}
