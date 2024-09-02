package com.stereowalker.obville.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.stereowalker.unionlib.config.ConfigObject;
import com.stereowalker.unionlib.config.UnionConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig.Type;

@UnionConfig(folder = "Obville Configs", name = "world", translatableName = "config.obville.main.file", autoReload = true)
public class ModConfig implements ConfigObject {	
	
	@UnionConfig.Entry(name = "Follow Player Odds", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What are the odds a guard will follow the player if they commit a crime. 0 means never, 1 means always. Anything inbetween are the odds of it occuring"})
	public float guardFollow = .7f;
	
	@UnionConfig.Entry(name = "One Liners", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"Villagers say their lines once when they dont trust you and never say anything again"})
	public boolean one_liners = true;
	
	@UnionConfig.Entry(name = "Reputaion Recovery Window", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"If you speak to a villager when you were distrusted and interact with them again, how much time should pass before they're willing to trade again","This is counted in ticks and 20 ticks is 1 second"})
	@UnionConfig.Range(min = 0.0D, max = 240000.0D)
	public int recovery = 12000;
	
	@UnionConfig.Entry(name = "Blacklisted Time", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"If you speak to a villager when you were distrusted and interact with them again, how much time should pass before they're willing to trade again","This is counted in ticks and 20 ticks is 1 second"})
	@UnionConfig.Range(min = 0.0D, max = 240000.0D)
	public int blacklisted = 24000;
	
	@UnionConfig.Entry(name = "Bribe Window", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"The amount of time after which a crime is commited and was witnessed by a single villager that the villager can be bribed","This is counted in ticks and 20 ticks is 1 second"})
	@UnionConfig.Range(min = 0.0D, max = 4000.0D)
	public int bribe_window = 400;
	
	@UnionConfig.Entry(name = "Invisible Line Cooldown", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"How much time in ticks will a villager wait until they say something again about an invisible player commiting crime",
			"This is counted in ticks and 20 ticks is 1 second"})
	@UnionConfig.Range(min = 0.0D, max = 4000.0D)
	public int invi = 1440;
	
	@UnionConfig.Entry(name = "Reset Reputation After Death", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"Should the player's reputation reset after they die?"})
	public boolean reset_reputation_on_death = false;
	
	@UnionConfig.Entry(name = "Global Reputation", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"Should the player's reputation be restricted to a single village or should it affect all villages.","Changing this will make your reputation what it was before this was changed.",
			"If you were exiled when it was global and you made it local to each village, changing this back will make you exiled globally and vice versa"})
	public boolean global_rep = true;
	
//	@UnionConfig.Entry(name = "Village Chests/Barrels", type = Type.COMMON)
//	@UnionConfig.Comment(comment = {"Opening chests or barrels with this loot table will cause a loss in reputation","If you want to modify this, you'll need to first restart the world then kill all the players in your world to have the changes applied to all the players"})
	public List<String> village_containers = Lists.newArrayList("minecraft:chests/village/village_armorer", "minecraft:chests/village/village_butcher", "minecraft:chests/village/village_cartographer", "minecraft:chests/village/village_desert_house", "minecraft:chests/village/village_fisher", "minecraft:chests/village/village_fletcher", "minecraft:chests/village/village_mason", "minecraft:chests/village/village_plains_house", "minecraft:chests/village/village_savanna_house", "minecraft:chests/village/village_shepherd", "minecraft:chests/village/village_snowy_house", "minecraft:chests/village/village_taiga_house", "minecraft:chests/village/village_tannery", "minecraft:chests/village/village_temple", 
			"minecraft:chests/village/village_toolsmith", 
			"minecraft:chests/village/village_weaponsmith");

	public List<ResourceLocation> COnts(){
		List<ResourceLocation> c = Lists.newArrayList();
		village_containers.forEach(x -> c.add(new ResourceLocation(x)));
		return c;
	}
	
}
