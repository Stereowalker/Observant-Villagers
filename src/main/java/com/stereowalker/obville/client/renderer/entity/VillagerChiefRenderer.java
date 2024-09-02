package com.stereowalker.obville.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.obville.world.entity.VillageLeader;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VillagerChiefRenderer extends MobRenderer<VillageLeader, VillageChiefModel> {
	private static final ResourceLocation VILLAGER_BASE_SKIN = new ResourceLocation("obville:textures/entity/villager_texture.png");
	private static final ResourceLocation VILLAGER_BASE_2 = new ResourceLocation("obville:textures/entity/villager_texture_2.png");
	private static final ResourceLocation VILLAGER_BASE_3 = new ResourceLocation("obville:textures/entity/villager_texture_3.png");

	public VillagerChiefRenderer(EntityRendererProvider.Context p_174437_) {
		super(p_174437_, new VillageChiefModel(p_174437_.bakeLayer(VillageChiefModel.LAYER_LOCATION)), 0.5F);
		this.addLayer(new CustomHeadLayer<>(this, p_174437_.getModelSet()));
		this.addLayer(new CrossedArmsItemLayer<>(this));
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(VillageLeader pEntity) {
		if (pEntity.getHatType() == 2) return VILLAGER_BASE_2;
		if (pEntity.getHatType() == 3) return VILLAGER_BASE_3;
		return VILLAGER_BASE_SKIN;
	}

	protected void scale(VillageLeader pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
		float f = 0.9375F;
		if (pLivingEntity.isBaby()) {
			f *= 0.5F;
			this.shadowRadius = 0.25F;
		} else {
			this.shadowRadius = 0.5F;
		}

		pMatrixStack.scale(f, f, f);
	}
}