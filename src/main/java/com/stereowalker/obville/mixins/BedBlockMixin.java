package com.stereowalker.obville.mixins;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.interfaces.IGeneratableBlockEntity;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.world.PlacedBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin extends HorizontalDirectionalBlock implements EntityBlock {

	protected BedBlockMixin(Properties p_54120_) {
		super(p_54120_);
	}

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BedBlock;kickVillagerOutOfBed(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
	public boolean kickVillagerOutOfBedRedirect(BedBlock bed, Level pLevel, BlockPos pPos, BlockState pState, Level pLevel2, BlockPos pPos2, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		List<Villager> list = pLevel.getEntitiesOfClass(Villager.class, new AABB(pPos), LivingEntity::isSleeping);
		if (list.isEmpty()) {
			return false;
		} else {
			list.get(0).stopSleeping();
			if (!pLevel.isClientSide) {
				ObVille.upsetNearby(pPlayer, pPos, true, -ObVille.REPUTATION_CONFIG.kick_out_of_bed, null);					
			}
			return true;
		}
	}

	@Inject(method = "setPlacedBy", at = @At("TAIL"))
	public void set(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack, CallbackInfo ci) {
		if (pLevel instanceof ServerLevel server && pPlacer instanceof IModdedEntity mocx) {
			if (mocx.getData().currentVillage() >= 0) {
				PlacedBlocks pb = PlacedBlocks.getInstance(server);
				BlockPos blockpos = pPos.relative(pState.getValue(FACING));
				pb.blockChanges.put(pPos, 1);
				pb.blockChanges.put(blockpos, 1);
				pb.setDirty();
				if (server.getBlockEntity(pPos) instanceof BedBlockEntity bed) ((IGeneratableBlockEntity)bed).notGenerated();
				if (server.getBlockEntity(blockpos) instanceof BedBlockEntity bed) ((IGeneratableBlockEntity)bed).notGenerated();
			}
		}
	}

	@Inject(method = "playerWillDestroy", at = @At(value = "TAIL"))
	public void set(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, CallbackInfo ci) {
		if (pLevel instanceof ServerLevel server) {
			PlacedBlocks pb = PlacedBlocks.getInstance(server);
			BlockPos blockpos = pPos.relative(pState.getValue(FACING));
			switch(pb.blockChanges.getOrDefault(pPos, 0)) {
			case 1:
				pb.blockChanges.remove(pPos);
				System.out.println("Removed My Bed @ "+pPos);
				break;
			default: 
				pb.blockChanges.put(pPos, 2);
				System.out.println("Removed A Bed @ "+pPos);
				break;
			}
			switch(pb.blockChanges.getOrDefault(blockpos, 0)) {
			case 1:
				pb.blockChanges.remove(blockpos);
				System.out.println("Removed My Bed @ "+pPos);
				break;
			default: 
				pb.blockChanges.put(blockpos, 2);
				System.out.println("Removed A Bed @ "+pPos);
				break;
			}
			if (pb.blockChanges.getOrDefault(pPos, 0) == 1) {
			}
			pb.setDirty();
		}
	}
}
