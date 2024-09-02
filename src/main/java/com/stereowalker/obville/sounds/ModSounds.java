package com.stereowalker.obville.sounds;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {
	public static final List<SoundEvent> SOUNDEVENTS = new ArrayList<SoundEvent>();
	
	public static final SoundEvent POSITIVE = register("positive");
	public static final SoundEvent NEGATIVE = register("negative");
	
	public static SoundEvent register(String name) {
		SoundEvent soundEvent = new SoundEvent(new ResourceLocation("obville:"+name));
		soundEvent.setRegistryName(new ResourceLocation("obville:"+name));
		ModSounds.SOUNDEVENTS.add(soundEvent);
		return soundEvent;
	}
	
	public static void registerAll(IForgeRegistry<SoundEvent> registry) {
		for(SoundEvent soundEvent : SOUNDEVENTS) {
			registry.register(soundEvent);
		}
	}
}