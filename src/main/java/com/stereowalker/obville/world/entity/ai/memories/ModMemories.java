package com.stereowalker.obville.world.entity.ai.memories;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class ModMemories {
	public static final MemoryModuleType<Boolean> LEADER_DETECTED_RECENTLY = register("obville:leader_detected_recently", Codec.BOOL);

	private static <U> MemoryModuleType<U> register(String pIdentifier, Codec<U> pCodec) {
		MemoryModuleType<U> type = new MemoryModuleType<>(Optional.of(pCodec));
		type.setRegistryName(new ResourceLocation(pIdentifier));
		return type;
	}	   
}
