package com.stereowalker.obville.dat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.network.protocol.game.ClientboundSoundPacket;
import com.stereowalker.obville.world.PlacedBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkDirection;

public class VillageData {
	public Pair<BlockPos, BlockPos> bounds = Pair.of(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0));
	public List<ItemStack> generatedBounties = new ArrayList<>();;

	public static VillageData read(CompoundTag tag) {
		VillageData village = new VillageData();
		village.bounds = Pair.of(
				new BlockPos(tag.getInt("X1"), tag.getInt("Y1"), tag.getInt("Z1")), 
				new BlockPos(tag.getInt("X2"), tag.getInt("Y2"), tag.getInt("Z2")));
		ListTag list1 = tag.getList("GeneratedBounties", 10);
		for (int i = 0; i < list1.size(); i++) {
			village.generatedBounties.add(ItemStack.of(list1.getCompound(i)));
		}
		return village;
	}

	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("X1", bounds.getLeft().getX());
		tag.putInt("Y1", bounds.getLeft().getY());
		tag.putInt("Z1", bounds.getLeft().getZ());
		tag.putInt("X2", bounds.getRight().getX());
		tag.putInt("Y2", bounds.getRight().getY());
		tag.putInt("Z2", bounds.getRight().getZ());

		ListTag listTag = new ListTag();
		generatedBounties.forEach(crime -> listTag.add(crime.save(new CompoundTag())));
		tag.put("GeneratedBounties", listTag);

		return tag;
	}

	public static boolean isBounty(ItemStack stack) {
		return stack.hasTag() && stack.getTag().getBoolean("is_bounty");
	}

	public static ItemStack bounty(ServerPlayer victim, int village) {
		ItemStack stack = new ItemStack(Items.PAPER);
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean("is_bounty", true);
		if (village == 0)
			stack.setHoverName(new TextComponent("Bounty On ")
					.append(victim.getDisplayName()));
		else
			stack.setHoverName(new TextComponent("Bounty On ")
					.append(victim.getDisplayName())
					.append(new TextComponent(" In Village "+village)));
		return stack;
	}

	public static void invalidateGounty(ServerPlayer serverplayer) {
		PlacedBlocks pb = PlacedBlocks.getInstance(serverplayer.getLevel());
		OVModData modEn = ((IModdedEntity)serverplayer).getData();
		if (!modEn.IsExiled() && modEn.reput().generatedBounty && modEn.reput().droppedBounty) {
			modEn.reput().generatedBounty = false;
			pb.villages.get(modEn.currentVillage()).generatedBounties.removeIf((stack) -> ItemStack.matches(stack, VillageData.bounty(serverplayer, modEn.currentVillage())));
		}
	}
}
