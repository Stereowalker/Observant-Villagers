package com.stereowalker.obville.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.stereowalker.obville.world.entity.ModEntities;
import com.stereowalker.obville.world.entity.VillageLeader;
import com.stereowalker.obville.world.entity.ai.memories.ModMemories;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public interface ISheep {
	public Villager villager();

	private boolean leaderSpawnConditionsMet(long pGameTime) {
		/*
		Optional<Long> optional = villager().getBrain().getMemory(MemoryModuleType.LAST_SLEPT);
		if (optional.isPresent()) {
			return pGameTime - optional.get() < 24000L;
		} else {
			return false;
		}*/
		return true;
	}

	public default boolean wantsToSpawnLeader(long pGameTime) {
		if (!leaderSpawnConditionsMet(villager().level.getGameTime())) {
			return false;
		} else {
			return !villager().getBrain().hasMemoryValue(ModMemories.LEADER_DETECTED_RECENTLY);
		}
	}

	public default boolean spawnLeaderIfNeeded(ServerLevel pServerLevel, long pGameTime, int pMinVillagerAmount) {
		if (this.wantsToSpawnLeader(pGameTime)) {
			AABB aabb = villager().getBoundingBox().inflate(10.0D, 10.0D, 10.0D);
			List<Villager> list = pServerLevel.getEntitiesOfClass(Villager.class, aabb);
			List<Villager> list1 = list.stream().filter((p_186293_) -> {
				return ((ISheep)p_186293_).wantsToSpawnLeader(pGameTime);
			}).limit(5L).collect(Collectors.toList());
			if (list1.size() >= pMinVillagerAmount) {
				VillageLeader irongolem = trySpawnLeader(pServerLevel);
				if (irongolem != null) {
					return true;
					//list.forEach(GolemSensor::golemDetected);
				}
			}
		}
		return false;
	}

	@Nullable
	private VillageLeader trySpawnLeader(ServerLevel pServerLevel) {
		BlockPos blockpos = villager().blockPosition();

		for(int i = 0; i < 10; ++i) {
			double d0 = (double)(pServerLevel.random.nextInt(16) - 8);
			double d1 = (double)(pServerLevel.random.nextInt(16) - 8);
			BlockPos blockpos1 = this.findSpawnPositionForLeaderInColumn(blockpos, d0, d1);
			if (blockpos1 != null) {
				VillageLeader irongolem = ModEntities.VILLAGE_CHIEF.create(pServerLevel, (CompoundTag)null, (Component)null, (Player)null, blockpos1, MobSpawnType.MOB_SUMMONED, false, false);
				if (irongolem != null) {
					if (irongolem.checkSpawnRules(pServerLevel, MobSpawnType.MOB_SUMMONED) && irongolem.checkSpawnObstruction(pServerLevel)) {
						pServerLevel.addFreshEntityWithPassengers(irongolem);
						return irongolem;
					}

					irongolem.discard();
				}
			}
		}

		return null;
	}

	@Nullable
	private BlockPos findSpawnPositionForLeaderInColumn(BlockPos pVillagerPos, double pOffsetX, double pOffsetZ) {
		int i = 6;
		BlockPos blockpos = pVillagerPos.offset(pOffsetX, 6.0D, pOffsetZ);
		BlockState blockstate = villager().level.getBlockState(blockpos);

		for(int j = 6; j >= -6; --j) {
			BlockPos blockpos1 = blockpos;
			BlockState blockstate1 = blockstate;
			blockpos = blockpos.below();
			blockstate = villager().level.getBlockState(blockpos);
			if ((blockstate1.isAir() || blockstate1.getMaterial().isLiquid()) && blockstate.getMaterial().isSolidBlocking()) {
				return blockpos1;
			}
		}

		return null;
	}
}
