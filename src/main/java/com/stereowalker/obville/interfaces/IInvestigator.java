package com.stereowalker.obville.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.stereowalker.obville.Crime;
import com.stereowalker.obville.world.effect.Effects;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.phys.Vec3;

public interface IInvestigator {
	public BlockPos investigatePos();
	public Crime crimeToInvestigate();
	

	public void setInvestigatePos(BlockPos pos);
	public void crimeToInvestigate(Crime crime);
	
	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public default void performRangedAttack(LivingEntity owner, BlockPos pos, float pDistanceFactor) {
		Vec3 vec3 = Vec3.ZERO;
		double d0 = pos.getX() + vec3.x - owner.getX();
		double d1 = pos.getY() - (double)1.1F - owner.getY();
		double d2 = pos.getZ() + vec3.z - owner.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);

		ThrownPotion thrownpotion = new ThrownPotion(owner.level, owner);
		List<MobEffectInstance> effects = new ArrayList<>();
		effects.add(new MobEffectInstance(Effects.INVESTIGATE));
		thrownpotion.setItem(PotionUtils.setCustomEffects(new ItemStack(Items.LINGERING_POTION), effects));
		thrownpotion.setXRot(thrownpotion.getXRot() - -20.0F);
		thrownpotion.shoot(d0, d1 + d3 * 0.2D, d2, 0.75F, 8.0F);
		if (!owner.isSilent()) {
			owner.level.playSound((Player)null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.WITCH_THROW, owner.getSoundSource(), 1.0F, 0.8F + owner.getRandom().nextFloat() * 0.4F);
		}

		owner.level.addFreshEntity(thrownpotion);
	}
}
