package com.stereowalker.obville.core.registries;

import com.stereowalker.obville.sounds.ModSounds;
import com.stereowalker.obville.world.entity.ModEntities;
import com.stereowalker.obville.world.entity.VillageLeader;
import com.stereowalker.obville.world.entity.ai.memories.ModMemories;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents
{
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.VILLAGE_CHIEF, VillageLeader.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
		ModSounds.registerAll(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerEntity(final RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().register(ModEntities.VILLAGE_CHIEF);
	}

	@SubscribeEvent
	public static void registerMomories(final RegistryEvent.Register<MemoryModuleType<?>> event) {
		event.getRegistry().register(ModMemories.LEADER_DETECTED_RECENTLY);
	}
}
