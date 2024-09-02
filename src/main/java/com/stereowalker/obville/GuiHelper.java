package com.stereowalker.obville;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.obville.dat.OVModData;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.unionlib.util.ScreenHelper;
import com.stereowalker.unionlib.util.ScreenHelper.ScreenOffset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.OverlayRegistry;

@OnlyIn(Dist.CLIENT)
public class GuiHelper {
	static int visibleTicks;
	
	public static final ResourceLocation GUI_ICONS = new ResourceLocation(ObVille.MOD_ID, "textures/gui/icons.png");
	@OnlyIn(Dist.CLIENT)
	public static void registerOverlays() {
		OverlayRegistry.registerOverlayTop("Reputation", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
			if (!gui.minecraft.options.hideGui) {
				gui.setupOverlayRenderState(true, false, GUI_ICONS);
				GuiHelper.renderTemperature(gui, ObVille.CLIENT_CONFIG.reputationPosition, gui.getCameraPlayer(), mStack, true);
			}
		});
	}

	public static int getXOffset(ScreenOffset pos, Minecraft mc) {
		if (pos.equals((Object) ScreenOffset.TOP_LEFT) || pos.equals((Object) ScreenOffset.LEFT)
				|| pos.equals((Object) ScreenOffset.BOTTOM_LEFT)) {
			return 0;
		}
		if (pos.equals((Object) ScreenOffset.TOP) || pos.equals((Object) ScreenOffset.CENTER)
				|| pos.equals((Object) ScreenOffset.BOTTOM)) {
			return mc.getWindow().getGuiScaledWidth() / 2;
		}
		if (pos.equals((Object) ScreenOffset.TOP_RIGHT) || pos.equals((Object) ScreenOffset.RIGHT)
				|| pos.equals((Object) ScreenOffset.BOTTOM_RIGHT)) {
			return mc.getWindow().getGuiScaledWidth();
		}
		return 0;
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static void renderTemperature(Gui gui, ScreenOffset position, Player playerentity, PoseStack matrixStack, boolean forgeOverlay) {
		if (position == ScreenOffset.TOP && gui.getBossOverlay().events.size() > 0) position = ScreenOffset.TOP_LEFT;
		
		int x = getXOffset(position, gui.minecraft);
		int y = ScreenHelper.getYOffset(position, gui.minecraft);
		Minecraft.getInstance().getProfiler().push("temperature");
		OVModData ent = ((IModdedEntity)playerentity).getData();

		int repu = ent.getPrevReputation();
		int col = 0;
		String rep = "";
		if (ent.IsWelcomeAt(ent.previousVillage())) {
			rep = "Welcomed";
			col = ObVille.CLIENT_CONFIG.welcome;
		}
		else if (ent.IsWearyAt(ent.previousVillage())) {
			rep = "Weary";
			col = ObVille.CLIENT_CONFIG.weary;
		}
		else if (ent.IsDistrustedAt(ent.previousVillage())) {
			rep = "Distrusting";
			col = ObVille.CLIENT_CONFIG.distrusted;
		}
		else if (ent.IsExiledAt(ent.previousVillage())) {
			rep = "Exiled";
			col = ObVille.CLIENT_CONFIG.exiled;
		}
		else {
			rep = "Neutral";
			col = ObVille.CLIENT_CONFIG.neutral;
		}
		Component rep2 = new TextComponent("Reputation - ")
				.append(new TextComponent(rep).setStyle(Style.EMPTY.withColor(col)));

		int repDiff = repu - ent.prevReputation.getOrDefault(ent.previousVillage(), 0); 
		if (repDiff != 0) {
			rep2 = rep2.copy().append(new TextComponent(" "+(repDiff > 0 ? "+"+repDiff : repDiff)).setStyle(Style.EMPTY.withColor(repDiff > 0 ? 0x00AA00 : repDiff < 0 ? 0xAA0000 : 0xBBBBBB)));
		}
		
		int w = Minecraft.getInstance().font.width(rep2);
		matrixStack.pushPose();
		if (position == ScreenOffset.BOTTOM_RIGHT || position == ScreenOffset.RIGHT ||  position == ScreenOffset.TOP_RIGHT)
			x -= w;
		else if (position == ScreenOffset.BOTTOM || position == ScreenOffset.CENTER ||  position == ScreenOffset.TOP)
			x -= w / 2;
		
		if (position == ScreenOffset.BOTTOM_LEFT || position == ScreenOffset.BOTTOM ||  position == ScreenOffset.BOTTOM_RIGHT)
			y -= Minecraft.getInstance().font.lineHeight;
		else if (position == ScreenOffset.LEFT || position == ScreenOffset.CENTER ||  position == ScreenOffset.RIGHT)
			y -= Minecraft.getInstance().font.lineHeight / 2;
		if (ent.isInAnyVillage()) {
			if (visibleTicks < 10) visibleTicks++;
		}
		else {
			if (visibleTicks > 0) visibleTicks--;
		}
		if (visibleTicks != 0) {
			float time = (float)visibleTicks / 10.0f;
			int alpha = (int)(time * 255f);
			Minecraft.getInstance().font.drawShadow(matrixStack, rep2, x, y, (alpha << 24) + 0xFFFFFF);
		}
		if (Minecraft.getInstance().gameMode.hasExperience()) {
		}
		Minecraft.getInstance().getProfiler().pop();
	}
}
