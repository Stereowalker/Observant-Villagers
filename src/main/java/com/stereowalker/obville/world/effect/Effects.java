package com.stereowalker.obville.world.effect;

import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.interfaces.IInvestigator;
import com.stereowalker.unionlib.core.registries.RegistryHolder;
import com.stereowalker.unionlib.core.registries.RegistryObject;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@RegistryHolder(registry = MobEffect.class)
public class Effects {
	@RegistryObject("investigate")
	public static final MobEffect INVESTIGATE = new InvestigativeEffect(MobEffectCategory.NEUTRAL, 4393481);

	public static class InvestigativeEffect extends InstantenousMobEffect{

		public InvestigativeEffect(MobEffectCategory p_19440_, int p_19441_) {
			super(p_19440_, p_19441_);
		}

		public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
			if (pLivingEntity.hasEffect(MobEffects.INVISIBILITY)) {
				pLivingEntity.removeEffect(MobEffects.INVISIBILITY);
			} else {
				if(pLivingEntity.level instanceof ServerLevel server) {
					Player player = server.getNearestPlayer(pLivingEntity, 10);
					if (player != null)
						player.addEffect(new MobEffectInstance(INVESTIGATE));
				}
			}
		}
		
		@Override
		public void applyInstantenousEffect(Entity pSource, Entity pIndirectSource, LivingEntity pLivingEntity,
				int pAmplifier, double pHealth) {
			if (pLivingEntity.hasEffect(MobEffects.INVISIBILITY)) {
				pLivingEntity.removeEffect(MobEffects.INVISIBILITY);
			} else {
				if(pLivingEntity.level instanceof ServerLevel server) {
					Player player = server.getNearestPlayer(pLivingEntity, 10);
					if (player != null)
						player.addEffect(new MobEffectInstance(INVESTIGATE));
				}
			}
			if (pIndirectSource instanceof IInvestigator leader && leader.crimeToInvestigate() != null && pLivingEntity instanceof Player player) {
				ObVille.upsetNearby(player, false, () -> leader.crimeToInvestigate());
				leader.crimeToInvestigate(null);
			}
		}

	}
}
