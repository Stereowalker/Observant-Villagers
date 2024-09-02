package com.stereowalker.obville.mixins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.stereowalker.obville.Crime;
import com.stereowalker.obville.Law;
import com.stereowalker.obville.Laws;
import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.dat.VillageData;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.interfaces.ISheep;
import com.stereowalker.obville.interfaces.IVillager;
import com.stereowalker.obville.network.protocol.game.ClientboundSoundPacket;
import com.stereowalker.obville.network.protocol.game.ClientboundVillagerMessagePacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager implements VillagerDataHolder, IVillager<Villager>, ISheep {
	@Shadow private Player lastTradedPlayer;
	@Shadow private void setUnhappy() {}
	@Shadow private boolean shouldIncreaseLevel() {return false;}

	private Map<UUID, Integer> blacklisted = new HashMap<>();
	private List<UUID> untrustworthy = new ArrayList<>();
	private List<UUID> playersSaidLineTo = new ArrayList<>();
	private List<UUID> playersSaidUntrustworthyLineTo = new ArrayList<>();
	private List<UUID> playersSaidRecoverLineTo = new ArrayList<>();
	private Map<UUID, Tuple<Law, Integer>> recentlyWitnessedCrime = new HashMap<>();
	private Map<UUID, Integer> trustTimer = new HashMap<>();
	private Map<UUID, Integer> recentlyTakenBribe = new HashMap<>();
	private String tradesWithDistrustedPlayer = "";
	private boolean decidedOnTradingWithDistrusted = false;
	private boolean hasRewardedCustomer = false;
	private int affectedByWeary = 0;

	public VillagerMixin(EntityType<? extends AbstractVillager> p_35267_, Level p_35268_) {
		super(p_35267_, p_35268_);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void ticker(CallbackInfo ci) {
		List<UUID> timersToRemove1 = new ArrayList<>();
		List<UUID> timersToRemove2 = new ArrayList<>();
		List<UUID> timersToRemove3 = new ArrayList<>();

		trustTimer.replaceAll((key,value) -> {
			if (value <= 0) {
				timersToRemove1.add(key);
				untrustworthy.add(key);
			}
			return value-1;
		});
		blacklisted.replaceAll((key,value) -> {
			if (value <= 0) timersToRemove2.add(key);
			return value-1;
		});

		recentlyTakenBribe.replaceAll((key,value) -> {
			if (value <= 0) timersToRemove3.add(key);
			return value-1;
		});

		if (invi > 0) invi--;

		timersToRemove1.forEach(trustTimer::remove);
		timersToRemove2.forEach(blacklisted::remove);
		timersToRemove3.forEach(recentlyTakenBribe::remove);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveDataInject(CompoundTag pCompound, CallbackInfo ci) {
		ListTag list1 = pCompound.getList("BlacklistedTimer", 10);
		this.trustTimer = new HashMap<>();
		for (int i = 0; i < list1.size(); i++) {
			CompoundTag tag = list1.getCompound(i);
			this.trustTimer.put(NbtUtils.loadUUID(tag.get("UUID")), tag.getInt("Timer"));
		}

		this.playersSaidLineTo = new ArrayList<>();
		pCompound.getList("SaidLineTo", 11).forEach(nbt -> this.playersSaidLineTo.add(NbtUtils.loadUUID(nbt)));

		this.playersSaidRecoverLineTo = new ArrayList<>();
		pCompound.getList("RecoverSaidLineTo", 11).forEach(nbt -> this.playersSaidRecoverLineTo.add(NbtUtils.loadUUID(nbt)));

		this.playersSaidUntrustworthyLineTo = new ArrayList<>();
		pCompound.getList("UntrustworthySaidLineTo", 11).forEach(nbt -> this.playersSaidUntrustworthyLineTo.add(NbtUtils.loadUUID(nbt)));

		ListTag list3 = pCompound.getList("RecentlyWitnessedCrime", 10);
		this.recentlyWitnessedCrime = new HashMap<>();
		for (int i = 0; i < list3.size(); i++) {
			CompoundTag tag = list3.getCompound(i);
			this.recentlyWitnessedCrime.put(NbtUtils.loadUUID(tag.get("UUID")), new Tuple<Law, Integer>(Laws.lawsToUphold.get(tag.getString("CrimeCommited")), tag.getInt("TimeSince")));
		} 

		ListTag list4 = pCompound.getList("TrustTimer", 10);
		this.trustTimer = new HashMap<>();
		for (int i = 0; i < list4.size(); i++) {
			CompoundTag tag = list4.getCompound(i);
			this.trustTimer.put(NbtUtils.loadUUID(tag.get("UUID")), tag.getInt("Timer"));
		}

		ListTag list5 = pCompound.getList("Untrustworthy", 11);
		this.untrustworthy = new ArrayList<>();
		for (int i = 0; i < list5.size(); i++) {
			this.untrustworthy.add(NbtUtils.loadUUID(list5.get(i)));
		}

		ListTag list6 = pCompound.getList("RecentlyTakenBribe", 10);
		this.recentlyTakenBribe = new HashMap<>();
		for (int i = 0; i < list6.size(); i++) {
			CompoundTag tag = list6.getCompound(i);
			this.recentlyTakenBribe.put(NbtUtils.loadUUID(tag.get("UUID")), tag.getInt("Timer"));
		}

		this.decidedOnTradingWithDistrusted = pCompound.getBoolean("DecidedOnTradingWithDistrusted");
		this.tradesWithDistrustedPlayer = pCompound.getString("TradesWithDistrustedPlayer");
		this.hasRewardedCustomer = pCompound.getBoolean("HasRewardedCustomer");
		this.affectedByWeary = pCompound.getInt("AffectedByWeary");
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveDataInject(CompoundTag pCompound, CallbackInfo ci) {
		ListTag list1 = new ListTag();
		blacklisted.forEach((player, timer) -> {
			CompoundTag tag = new CompoundTag();
			tag.put("UUID", NbtUtils.createUUID(player));
			tag.putInt("Timer", timer);
			list1.add(tag);
		});
		pCompound.put("BlacklistedTimer", list1);

		ListTag list2 = new ListTag();
		playersSaidLineTo.forEach(player -> list2.add(NbtUtils.createUUID(player)));
		pCompound.put("SaidLineTo", list2);

		ListTag list8 = new ListTag();
		playersSaidRecoverLineTo.forEach(player -> list8.add(NbtUtils.createUUID(player)));
		pCompound.put("RecoverSaidLineTo", list8);

		ListTag list7 = new ListTag();
		playersSaidUntrustworthyLineTo.forEach(player -> list7.add(NbtUtils.createUUID(player)));
		pCompound.put("UntrustworthySaidLineTo", list7);

		ListTag list3 = new ListTag();
		recentlyWitnessedCrime.forEach((player, crime) -> {
			CompoundTag tag = new CompoundTag();
			tag.put("UUID", NbtUtils.createUUID(player));
			tag.putString("CrimeCommited", crime.getA().crimeIdentifier);
			tag.putInt("TimeSince", crime.getB());
			list3.add(tag);
		});
		pCompound.put("RecentlyWitnessedCrime", list3);


		ListTag list4 = new ListTag();
		trustTimer.forEach((player, timer) -> {
			CompoundTag tag = new CompoundTag();
			tag.put("UUID", NbtUtils.createUUID(player));
			tag.putInt("Timer", timer);
			list4.add(tag);
		});
		pCompound.put("TrustTimer", list4);

		ListTag list5 = new ListTag();
		untrustworthy.forEach(player -> list5.add(NbtUtils.createUUID(player)));
		pCompound.put("Untrustworthy", list5);

		ListTag list6 = new ListTag();
		recentlyTakenBribe.forEach((player, timer) -> {
			CompoundTag tag = new CompoundTag();
			tag.put("UUID", NbtUtils.createUUID(player));
			tag.putInt("Timer", timer);
			list6.add(tag);
		});
		pCompound.put("RecentlyTakenBribe", list6);

		pCompound.putBoolean("DecidedOnTradingWithDistrusted", this.decidedOnTradingWithDistrusted);
		pCompound.putString("TradesWithDistrustedPlayer", this.tradesWithDistrustedPlayer);
		pCompound.putBoolean("HasRewardedCustomer", this.hasRewardedCustomer);
		pCompound.putInt("AffectedByWeary", this.affectedByWeary);
	}

	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	public void mobInteractInject(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
		if (!this.isBaby()) {
			IModdedEntity modded = (IModdedEntity)pPlayer;
			switch (acceptBribe(pPlayer, pPlayer.getItemInHand(pHand), random)) {
			case Accepted:
				if (!level.isClientSide) {
					blacklisted.remove(pPlayer.getUUID());
					new ClientboundVillagerMessagePacket(fromVillager(ObVille.LINES_CONFIG.rare_bribe_success), pPlayer.getUUID()).send((ServerPlayer)pPlayer);
				}
				cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
				break;
			case Rejected:
				if (!level.isClientSide) {
					new ClientboundVillagerMessagePacket(fromVillager(ObVille.LINES_CONFIG.common_bribe_fail), pPlayer.getUUID()).send((ServerPlayer)pPlayer);
				}
				setUnhappy();
				cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
				break;
			default:
				if (!level.isClientSide && modded.getData().IsWeary())
					level.broadcastEntityEvent((Villager)(Object)this, (byte)13); //Does the angry particles
				if (modded.getData().IsDistrusted()) {
					if (!this.decidedOnTradingWithDistrusted) {
						if (this.random.nextInt(2) == 0) {
							this.tradesWithDistrustedPlayer = ObVille.LINES_CONFIG.distrusted_lines.get(random.nextInt(ObVille.LINES_CONFIG.distrusted_lines.size()));
						}
						this.decidedOnTradingWithDistrusted = true;
					}
					if (trustTimer.containsKey(pPlayer.getUUID())) {
						trustTimer.remove(pPlayer.getUUID());
						untrustworthy.add(pPlayer.getUUID());
					}
					if (playersSaidRecoverLineTo.contains(pPlayer.getUUID())) {
						playersSaidRecoverLineTo.remove(pPlayer.getUUID());
					}
					if (tradesWithDistrustedPlayer.length() > 0 || untrustworthy.contains(pPlayer.getUUID())) {
						if (!level.isClientSide) {
							if (untrustworthy.contains(pPlayer.getUUID())) {
								if (!ObVille.MOD_CONFIG.one_liners) {
									Component message = fromVillager(new TextComponent(ObVille.LINES_CONFIG.distrustedAgain.get(random.nextInt(ObVille.LINES_CONFIG.distrustedAgain.size()))));
									ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, pPlayer.getUUID()), ((ServerPlayer)pPlayer).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
								}
								else if (!playersSaidUntrustworthyLineTo.contains(pPlayer.getUUID())) {
									Component message = fromVillager(new TextComponent(ObVille.LINES_CONFIG.distrustedAgain.get(random.nextInt(ObVille.LINES_CONFIG.distrustedAgain.size()))));
									ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, pPlayer.getUUID()), ((ServerPlayer)pPlayer).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
								}
								playersSaidUntrustworthyLineTo.add(pPlayer.getUUID());
//								new ClientboundVillagerMessagePacket(fromVillager(new TextComponent("UNTRUSTWORTHY")), pPlayer.getUUID()).send((ServerPlayer)pPlayer);;
							}
							else {
								if (!ObVille.MOD_CONFIG.one_liners) {
									Component message = fromVillager(ObVille.LINES_CONFIG.distrusted_lines);
									ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, pPlayer.getUUID()), ((ServerPlayer)pPlayer).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
								}
								else if (!playersSaidLineTo.contains(pPlayer.getUUID())) {
									Component message = new TextComponent(tradesWithDistrustedPlayer);
									if (getTags().contains("villagernames.named"))
										message = getCustomName().copy().append(": ").append(message);
									else
										message = getName().copy().append(": ").append(message);
									ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, pPlayer.getUUID()), ((ServerPlayer)pPlayer).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
								}
								playersSaidLineTo.add(pPlayer.getUUID());
//								new ClientboundVillagerMessagePacket(fromVillager(new TextComponent("DISTRUSTED")), pPlayer.getUUID()).send((ServerPlayer)pPlayer);;
							}
							setUnhappy();
						}
						cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
					}
				}
				else if (modded.getData().IsExiled() || blacklisted.containsKey(pPlayer.getUUID())) {
					if (!level.isClientSide) {
						setUnhappy();
						if (blacklisted.containsKey(pPlayer.getUUID())) {
							new ClientboundVillagerMessagePacket(fromVillager(ObVille.LINES_CONFIG.blacklisted), pPlayer.getUUID()).send((ServerPlayer)pPlayer);;
//							new ClientboundVillagerMessagePacket(fromVillager(new TextComponent("BLACKLISTED "+blacklisted.get(pPlayer.getUUID()))), pPlayer.getUUID()).send((ServerPlayer)pPlayer);;
						}
					}
					cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
				}
				else if (!modded.getData().IsExiled()) {
					if (playersSaidLineTo.contains(pPlayer.getUUID())) {
						trustTimer.put(pPlayer.getUUID(), ObVille.MOD_CONFIG.recovery);
						playersSaidLineTo.remove(pPlayer.getUUID());
					}
					if (playersSaidUntrustworthyLineTo.contains(pPlayer.getUUID())) {
						trustTimer.put(pPlayer.getUUID(), ObVille.MOD_CONFIG.recovery);
						playersSaidUntrustworthyLineTo.remove(pPlayer.getUUID());
					}

					if (trustTimer.containsKey(pPlayer.getUUID())) {
						if (!level.isClientSide) {
							if (!ObVille.MOD_CONFIG.one_liners || !playersSaidRecoverLineTo.contains(pPlayer.getUUID())) {
								new ClientboundVillagerMessagePacket(fromVillager(ObVille.LINES_CONFIG.recoverFromDistrusted), pPlayer.getUUID()).send((ServerPlayer)pPlayer);
							}
							playersSaidRecoverLineTo.add(pPlayer.getUUID());
//							new ClientboundVillagerMessagePacket(fromVillager(new TextComponent("RECOVER "+trustTimer.get(pPlayer.getUUID()))), pPlayer.getUUID()).send((ServerPlayer)pPlayer);;
						}
						cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
					} else {
						if (playersSaidRecoverLineTo.contains(pPlayer.getUUID())) {
							playersSaidRecoverLineTo.remove(pPlayer.getUUID());
						}
					}
				} else {
					if (playersSaidRecoverLineTo.contains(pPlayer.getUUID())) {
						playersSaidRecoverLineTo.remove(pPlayer.getUUID());
					}
				}
				if (modded.getData().IsWelcome() && blacklisted.containsKey(pPlayer.getUUID())) {
					blacklisted.remove(pPlayer.getUUID());//I wasn't asked to do this, I did this of my own volition
				}	
				break;
			}
		}
	}


	@Inject(method = "rewardTradeXp", at = @At("TAIL"))
	public void rewardTradeXpInject(CallbackInfo ci) {
		System.out.println(getVillagerData().getLevel()+" "+(lastTradedPlayer == null));
		if (!hasRewardedCustomer && getVillagerData().getLevel() == 4 && shouldIncreaseLevel() && lastTradedPlayer instanceof IModdedEntity player) {
			ObVille.getInstance().channel.sendTo(new ClientboundSoundPacket(true, lastTradedPlayer.getUUID()), ((ServerPlayer)lastTradedPlayer).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			player.getData().incrementReputation(ObVille.REPUTATION_CONFIG.max_trade);
			VillageData.invalidateGounty((ServerPlayer)lastTradedPlayer);
			hasRewardedCustomer = true;
		}
	}

	@Inject(method = "customServerAiStep", at = @At("TAIL"))
	public void customServerAiStepInject(CallbackInfo ci) {
		Map<UUID, Tuple<Law, Integer>> newMap = new HashMap<>();
		recentlyWitnessedCrime.forEach((uuid, tup) -> {
			if (tup.getB()+1 < ObVille.MOD_CONFIG.bribe_window)
				newMap.put(uuid, new Tuple<Law, Integer>(tup.getA(), tup.getB()+1));
		});
		recentlyWitnessedCrime = newMap;
	}

	@Inject(method = "updateSpecialPrices", at = @At("TAIL"))
	public void updateSpecialPricesInject(Player pPlayer, CallbackInfo ci) {
		IModdedEntity modded = (IModdedEntity)pPlayer;
		if (this.affectedByWeary == 0)
			this.affectedByWeary = this.random.nextInt(9)+1;

		boolean flag = modded.getData().IsWeary() && this.affectedByWeary >= 5;
		if (modded.getData().IsDistrusted() && decidedOnTradingWithDistrusted && tradesWithDistrustedPlayer.length() == 0)
			flag = true;
		if (flag) {
			for(MerchantOffer merchantoffer1 : this.getOffers()) {
				int k = -10;
				double d0 = 0.3D + 0.0625D * (double)k;
				int j = (int)Math.floor(d0 * (double)merchantoffer1.getBaseCostA().getCount());
				merchantoffer1.addToSpecialPriceDiff(-Math.min(j, 1));
			}
		}
		else if (modded.getData().IsWelcome() && !pPlayer.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
			for(MerchantOffer merchantoffer1 : this.getOffers()) {
				int k = 10;
				double d0 = 0.3D + 0.0625D * (double)k;
				int j = (int)Math.floor(d0 * (double)merchantoffer1.getBaseCostA().getCount());
				merchantoffer1.addToSpecialPriceDiff(-Math.max(j, 1));
			}
		}
	}

	@Override
	public void blacklist(ServerPlayer player) {
		if (!blacklisted.containsKey(player.getUUID())) {
			blacklisted.put(player.getUUID(), ObVille.MOD_CONFIG.blacklisted);
		}
		level.broadcastEntityEvent((Villager)(Object)this, (byte)13);
	}

	@Override
	public Map<UUID, Tuple<Law, Integer>> recentlyWitnessedCrime() {
		return recentlyWitnessedCrime;
	}

	@Override
	public Map<UUID, Integer> recentlyTakenBribe() {
		return recentlyTakenBribe;
	}

	@Override
	public void witnessCrime(Player player, Crime crime) {
		recentlyWitnessedCrime.put(player.getUUID(), new Tuple<Law, Integer>(crime.lawBroken, 0));
	}

	int invi = 0;
	@Override
	public int invisibleLineCooldown() {
		return invi;
	}

	@Override
	public void setInvisibleLineCooldown(int v) {
		invi = v;
	}

	@Override
	public Villager villager() {
		return (Villager)(Object)this;
	}

	@Override
	public Villager me() {
		return (Villager)(Object)this;
	}

	//For the chief

}
