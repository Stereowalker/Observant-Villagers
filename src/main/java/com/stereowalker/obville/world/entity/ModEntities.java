package com.stereowalker.obville.world.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
	public static final EntityType<VillageLeader> VILLAGE_CHIEF = register("obville:village_leader", EntityType.Builder.<VillageLeader>of(VillageLeader::new, MobCategory.CREATURE).sized(0.6F, 1.95F));
	
	public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder){
		EntityType<T> type = builder.build(name);
		type.setRegistryName(new ResourceLocation(name));
		return type;
	}
}
