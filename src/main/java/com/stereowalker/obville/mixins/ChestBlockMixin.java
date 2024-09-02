package com.stereowalker.obville.mixins;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.interfaces.ICombinedBlock;
import com.stereowalker.obville.interfaces.ILootableBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends AbstractChestBlock<ChestBlockEntity> {

	protected ChestBlockMixin(Properties pProperties,
			Supplier<BlockEntityType<? extends ChestBlockEntity>> pBlockEntityFactory) {
		super(pProperties, pBlockEntityFactory);
	}
	
	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;angerNearbyPiglins(Lnet/minecraft/world/entity/player/Player;Z)V"))
	public void useInject(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, CallbackInfoReturnable<InteractionResult> ci) {
		if (pPlayer instanceof ServerPlayer sPlayer) {
			MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
			if (menuprovider instanceof ChestBlockEntity chest) {
				ObVille.upsetOnOpen(chest, sPlayer, pPos, null);
			} else {
				AbstractContainerMenu menu = menuprovider.createMenu(0, pPlayer.getInventory(), pPlayer);
				if (menu instanceof ChestMenu chestmenu && chestmenu.getContainer() instanceof CompoundContainer compound) {
					System.out.println("C1 "+((ICombinedBlock)compound).getContainer1()+" C2 "+((ICombinedBlock)compound).getContainer2());
					if (((ICombinedBlock)compound).getContainer1() instanceof ChestBlockEntity chest) {
						BlockPos p1 = chest.getBlockPos();
						BlockPos p2 = null;
						if (((ICombinedBlock)compound).getContainer2() instanceof ChestBlockEntity chest2) {
							p2 = chest2.getBlockPos();
						}
						if (!ObVille.upsetOnOpen(chest, sPlayer, pPos, p2)) {
							if (((ICombinedBlock)compound).getContainer2() instanceof ChestBlockEntity chest2) {
								ObVille.upsetOnOpen(chest2, sPlayer, chest.getBlockPos(), chest2.getBlockPos());
							}
						}
					} else if (((ICombinedBlock)compound).getContainer2() instanceof ChestBlockEntity chest) {
						ObVille.upsetOnOpen(chest, sPlayer, pPos, null);
					}
				}
			}
		}
	}

}