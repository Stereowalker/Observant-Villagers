package com.stereowalker.obville.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.stereowalker.obville.dat.OVModData;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.world.entity.VillageLeader;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends AgeableMob implements Npc, Merchant{

	protected AbstractVillagerMixin(EntityType<? extends AgeableMob> type, Level worldIn) {
		super(type, worldIn);
	}

	//	@Redirect(method = "<init>", at = @At(value = "HEAD"))
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AgeableMob;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"))
	private static Level redirect(Level p_35268_) {
//		System.out.println("Accessing Abstract Villager");
		return p_35268_;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, (p_200828_0_) -> {
			if ((AbstractVillager)(Object)this instanceof VillageLeader)
				return false;
			else {
				OVModData data = ((IModdedEntity)p_200828_0_).getData();
				return data.IsExiled() || data.recentlyCommitedBandirty();
			}
		}, 7.0F, 1.0D, 1D, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
	}

}
