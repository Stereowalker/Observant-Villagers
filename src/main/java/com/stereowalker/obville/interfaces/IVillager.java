package com.stereowalker.obville.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.stereowalker.obville.Crime;
import com.stereowalker.obville.Law;
import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.dat.VillageData;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface IVillager<T extends PathfinderMob> {
	public void blacklist(ServerPlayer player);
	public Map<UUID, Tuple<Law, Integer>> recentlyWitnessedCrime();
	public Map<UUID, Integer> recentlyTakenBribe();
	public void witnessCrime(Player player, Crime crime);
	public int invisibleLineCooldown();
	public void setInvisibleLineCooldown(int v);
	public Map<UUID, Integer> crimesWitnessed();
	
	public T me();
	
	public default Component fromVillager(Component original) {
		if (me().getTags().contains("villagernames.named"))
			return me().getCustomName().copy().append(": ").append(original);
		else
			return me().getName().copy().append(": ").append(original);
	}
	
	public default Component fromVillager(List<String> lines) {
		Component line = new TextComponent(lines.get(me().getRandom().nextInt(lines.size())));
		
		if (me().getTags().contains("villagernames.named"))
			return me().getCustomName().copy().append(": ").append(line);
		else
			return me().getName().copy().append(": ").append(line);
	}
	
	public enum BribeStatus{ Accepted, Rejected, NotInitiated}
	
	public default BribeStatus acceptBribe(Player player, ItemStack stack, Random rng) {
		IModdedEntity modded = (IModdedEntity)player;
		if (recentlyWitnessedCrime().containsKey(player.getUUID()) && stack.getItem() == Items.EMERALD && !recentlyTakenBribe().containsKey(player.getUUID())) {
			boolean bribeTaken = rng.nextInt(650) < stack.getCount() * 10;
			if (!bribeTaken) {
				recentlyWitnessedCrime().remove(player.getUUID());
				modded.getData().incrementReputation(ObVille.REPUTATION_CONFIG.bribe_fail);
				if (player instanceof ServerPlayer)
					VillageData.invalidateGounty((ServerPlayer)player);
				return BribeStatus.Rejected;
			}
			modded.getData().resolveCrime(modded.getData().currentVillage(), recentlyWitnessedCrime().get(player.getUUID()).getA());
			modded.getData().incrementReputation(-recentlyWitnessedCrime().get(player.getUUID()).getA().getRepHit());
			recentlyWitnessedCrime().remove(player.getUUID());
			recentlyTakenBribe().put(player.getUUID(), 4800);
			stack.shrink(stack.getCount());
			return BribeStatus.Accepted;
		}
		return BribeStatus.NotInitiated;
	}
}
