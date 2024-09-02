package com.stereowalker.obville.mixins;

import org.spongepowered.asm.mixin.Mixin;

import com.stereowalker.obville.interfaces.IGeneratableBlockEntity;
import com.stereowalker.obville.world.PlacedBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BedBlockEntity.class)
public abstract class BedBlockEntityMixin extends BlockEntity implements IGeneratableBlockEntity {
	public BedBlockEntityMixin(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
		super(pType, pWorldPosition, pBlockState);
	}

	public boolean notGenerated;
	
	@Override
	public void setLevel(Level pLevel) {
		super.setLevel(pLevel);

		if (this.level instanceof ServerLevel server) {
			PlacedBlocks pb = PlacedBlocks.getInstance(server);
			if (pb.didPlayerPlaceBlock(this.getBlockPos())) notGenerated();
		}
	}
	
	@Override
	public boolean wasGenerated() {
		return !notGenerated;
	}
	
	@Override
	public void notGenerated() {
		notGenerated = true;
	}
}