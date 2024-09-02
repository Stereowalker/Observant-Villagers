package com.stereowalker.obville.mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.stereowalker.obville.interfaces.ILootableBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(RandomizableContainerBlockEntity.class)
public abstract class RandomizableMixin extends BaseContainerBlockEntity implements ILootableBlock {

	protected RandomizableMixin(BlockEntityType<?> p_155076_, BlockPos p_155077_, BlockState p_155078_) {
		super(p_155076_, p_155077_, p_155078_);
	}

	@Shadow protected ResourceLocation lootTable;
	protected ResourceLocation lootTable1;
	protected List<UUID> playersOpened;

	@Override
	public ResourceLocation getLoot() {
		return lootTable1;
	}
	
	@Override
	public List<UUID> playersOpened() {
		return playersOpened;
	}
	
	@Override
	public void addPlayer(Player player) {
		if (playersOpened == null)
			playersOpened = new ArrayList<>();
		playersOpened.add(player.getUUID());
		
	}

	@Inject(method = "tryLoadLootTable", at = @At("HEAD"))
	protected void tryLoadLootTableInject(CompoundTag pTag, CallbackInfoReturnable<Boolean> cir) {
		if (pTag.contains("ContainerLootTable", 8)) {				
			this.lootTable1 = new ResourceLocation(pTag.getString("ContainerLootTable"));
		}
		else if (pTag.contains("LootTable", 8)) {
			this.lootTable1 = new ResourceLocation(pTag.getString("LootTable"));
		}
		
		if (pTag.contains("PlayersOpened")) {		
			ListTag list = pTag.getList("PlayersOpened", 11);
			this.playersOpened = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				this.playersOpened.add(NbtUtils.loadUUID(list.get(i)));
			}
		}
	}

	@Inject(method = "trySaveLootTable", at = @At("HEAD"))
	protected void trySaveLootTableInject(CompoundTag pTag, CallbackInfoReturnable<Boolean> cir) {
		if (lootTable1 != null) {
			pTag.putString("ContainerLootTable", this.lootTable1.toString());
		}
		
		if (playersOpened != null && playersOpened.size() > 0) {
			ListTag list = new ListTag();
			playersOpened.forEach(player -> list.add(NbtUtils.createUUID(player)));
			pTag.put("PlayersOpened", list);
		}
	}

	@Inject(method = "setLootTable(Lnet/minecraft/resources/ResourceLocation;J)V", at = @At("HEAD"))
	public void setLootTableInject(ResourceLocation pLootTable, long pLootTableSeed, CallbackInfo ci) {
		this.lootTable1 = pLootTable;
	}
	
	@Inject(method = "unpackLootTable", at = @At("HEAD"))
	public void unpackLootTableInject(@Nullable Player pPlayer, CallbackInfo ci) {
		if (this.lootTable != null && this.level.getServer() != null) {
			System.out.println("Generating "+lootTable);
			System.out.println("Saving "+lootTable);
			this.lootTable1 = lootTable;
		}
	}

}