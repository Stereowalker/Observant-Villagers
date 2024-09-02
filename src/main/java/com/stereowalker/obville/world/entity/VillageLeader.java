package com.stereowalker.obville.world.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.stereowalker.obville.Crime;
import com.stereowalker.obville.Law;
import com.stereowalker.obville.Laws;
import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.compat.VillagerNamesCompat;
import com.stereowalker.obville.dat.OVModData;
import com.stereowalker.obville.dat.VillageData;
import com.stereowalker.obville.interfaces.IInvestigator;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.interfaces.IPlayerFollower;
import com.stereowalker.obville.network.protocol.game.ClientboundVillagerMessagePacket;
import com.stereowalker.obville.world.PlacedBlocks;
import com.stereowalker.obville.world.entity.ai.goal.FollowCriminalGoal;
import com.stereowalker.obville.world.entity.ai.goal.InvestigateCrimeGoal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;

public class VillageLeader extends Villager implements IPlayerFollower, IInvestigator {
	private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(VillageLeader.class, EntityDataSerializers.INT);
	int redemptionTradesPerformed = 0;
	public Player followedCriminal = null;
	public int followedtime = 0;
	public int currentVillage = -1;
	public BlockPos investigatePos = BlockPos.ZERO;
	public Crime crimeToInvestigate;

	public VillageLeader(EntityType<? extends Villager> p_35381_, Level p_35382_) {
		super(p_35381_, p_35382_);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ID_TYPE, 0);
		super.defineSynchedData();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(3, new FollowCriminalGoal(this, 1D));
		this.goalSelector.addGoal(0, new InvestigateCrimeGoal(this, .5D));
		super.registerGoals();
	}

	@Override
	protected Component getTypeName() {
		return new TranslatableComponent(this.getType().getDescriptionId());
	}

	@Override
	public boolean canBreed() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		pCompound.putInt("InvX", investigatePos.getX());
		pCompound.putInt("InvY", investigatePos.getY());
		pCompound.putInt("InvZ", investigatePos.getZ());
		super.addAdditionalSaveData(pCompound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		investigatePos = new BlockPos(pCompound.getInt("InvX"), pCompound.getInt("InvY"), pCompound.getInt("InvZ"));
		super.readAdditionalSaveData(pCompound);
	}

	public int getHatType() {
		return this.entityData.get(DATA_ID_TYPE);
	}

	boolean hasSpokenOnContact = false;
	@Override
	protected void customServerAiStep() {
		currentVillage = ObVille.determineVillage((ServerLevel)this.level, this.blockPosition());

		if (getHatType() == 0)
			this.entityData.set(DATA_ID_TYPE, random.nextInt(1, 4));
		this.level.getProfiler().push("villagerBrain");
		this.getBrain().tick((ServerLevel)this.level, this);
		this.level.getProfiler().pop();

		if (!this.isNoAi() && this.random.nextInt(100) == 0) {
			Raid raid = ((ServerLevel)this.level).getRaidAt(this.blockPosition());
			if (raid != null && raid.isActive() && !raid.isOver()) {
				this.level.broadcastEntityEvent(this, (byte)42);
			}
		}

		if (followedCriminal == null) hasSpokenOnContact = false;

		if (followedCriminal != null && distanceTo(followedCriminal) <= 4f && !hasSpokenOnContact) {
			followedtime = 0;
			hasSpokenOnContact = true;
			Component message = new TranslatableComponent("obville.messages.need_to_talk");
			if (hasCustomName())
				message = getCustomName().copy().append(": ").append(message);
			ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, followedCriminal.getUUID()), ((ServerPlayer)followedCriminal).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		}
		if (followedtime >= 300 && followedCriminal != null) {
			followedtime = 0;
			Component message = new TranslatableComponent("obville.messages.need_to_talk");
			if (hasCustomName())
				message = getCustomName().copy().append(": ").append(message);
			ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, followedCriminal.getUUID()), ((ServerPlayer)followedCriminal).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		}


		//TODO: This calls Mob.customServerAiStep() which is protected
		//		((AbstractVillager)this).customServerAiStep();
	}

	@Override
	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);

		if (currentVillage >= 0 && itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.isTrading() && !this.isSleeping() && !pPlayer.isSecondaryUseActive()) {
			boolean flag = true;
			if (pPlayer instanceof ServerPlayer serv) {
				OVModData data = ((IModdedEntity)serv).getData();
				flag = data.IsWelcome() || data.IsNeutral();

				if (data.crimesCommitedAt(currentVillage).size() > 0) flag = false;
				if (data.reputAtNoSave(currentVillage).hasSpokenToLeader) flag = false;
			}
			if (pHand == InteractionHand.MAIN_HAND) {
				if (flag && !this.level.isClientSide) {
					this.setUnhappy();
				}

				//				pPlayer.awardStat(Stats.TALKED_TO_VILLAGER);
			}

			System.out.println("Check Plkayer");
			if (pPlayer instanceof ServerPlayer serv) {
				if (ObVille.isPotentialBandit(serv)) {
					new ClientboundVillagerMessagePacket(fromVillager(new TranslatableComponent("obville.chat.no_bandits")), pPlayer.getUUID()).send((ServerPlayer)pPlayer);
					return InteractionResult.sidedSuccess(this.level.isClientSide);
				}
				else if (flag) {
					pPlayer.displayClientMessage(new TranslatableComponent("obville.messages.not_now"), true);
					return InteractionResult.sidedSuccess(this.level.isClientSide);
				} 
				else {
					this.startTrading(pPlayer);
					return InteractionResult.sidedSuccess(this.level.isClientSide);
				}
			}
		} else {
			return super.mobInteract(pPlayer, pHand);
		}
		return super.mobInteract(pPlayer, pHand);
	}

	@SuppressWarnings("unchecked")
	public Brain<VillageLeader> getBrainC() {
		return (Brain<VillageLeader>)((LivingEntity)this).getBrain();
	}

	@Override
	public void setTradingPlayer(Player pPlayer) {
		if (!level.isClientSide && redemptionTradesPerformed > 0 && getTradingPlayer() != null && pPlayer == null) {
			Component message = new TextComponent(ObVille.LINES_CONFIG.leader_lines.get(random.nextInt(ObVille.LINES_CONFIG.leader_lines.size())));
			if (getTags().contains("villagernames.named"))
				message = getCustomName().copy().append(": ").append(message);
			else
				message = getTypeName().copy().append(": ").append(message);
			ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, getTradingPlayer().getUUID()), ((ServerPlayer)getTradingPlayer()).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			redemptionTradesPerformed = 0;
		}
		super.setTradingPlayer(pPlayer);
	}

	private void startTrading(Player pPlayer) {

		Component title = new TextComponent("Redemption Trades");
		if (pPlayer != null) {
			OVModData data = ((IModdedEntity)pPlayer).getData();
			data.reputAtNoSave(currentVillage).hasSpokenToLeader = true;
			title = new TextComponent("Redemption Trades ("+data.getReputation()+")");
		}
		if (ObVille.hasVillagerNames()) {
			VillagerNamesCompat.overrideMerchantScreen(title, this);
		}
		this.setTradingPlayer(pPlayer);
		this.getOffers().forEach(offer -> offer.resetSpecialPriceDiff());
		this.openTradingScreen(pPlayer, title, this.getVillagerData().getLevel());
		this.getOffers().forEach(offer -> offer.resetSpecialPriceDiff());
	}

	private void setUnhappy() {
		this.setUnhappyCounter(40);
		if (!this.level.isClientSide()) {
			this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
		}
	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	@Override
	public MerchantOffers getOffers() {
		MerchantOffers offers = new MerchantOffers();
		if (getTradingPlayer() != null && currentVillage >= 0) {
			OVModData data = ((IModdedEntity)getTradingPlayer()).getData();
			int repleftToRecover = data.getReputation();
			if (repleftToRecover < 0) {
				Map<Law, MerchantOffer> offers2 = Maps.newHashMap();
				for (int i = 0; i < data.crimesCommitedAt(currentVillage).size(); i++) {
					Crime crime = data.crimesCommitedAt(currentVillage).get(i);
					if (!offers2.containsKey(crime.lawBroken)) {
						repleftToRecover -= crime.lawBroken.getRepHit() * data.crimesCommitedOfType(currentVillage, crime.lawBroken);
						offers2.put(crime.lawBroken, crime.frogive(data, currentVillage));
					}
				}
				offers2.values().forEach((offer) -> offers.add(offer));
				if (repleftToRecover < 0) {
					offers.add(new MerchantOffer(new ItemStack(Items.EMERALD, 40), 
							Crime.forgive(1, "generic", currentVillage), -repleftToRecover, 0, 1));
				}
				if (this.level instanceof ServerLevel server) {
					PlacedBlocks pb = PlacedBlocks.getInstance(server);
					for (ItemStack stack : pb.villages.get(currentVillage).generatedBounties) {
						offers.add(new MerchantOffer(stack, new ItemStack(Items.EMERALD, 20), 1, 0, 1));
					}
				}
			}
		}
		this.offers = offers;
		return super.getOffers();
	}

	@Override
	protected void updateTrades() {
		super.updateTrades();
	}

	@Override
	public void notifyTrade(MerchantOffer pOffer) {
		redemptionTradesPerformed++;
		System.out.println((getTradingPlayer() != null)+" is?"+redemptionTradesPerformed+" "+pOffer.getMaxUses());
		ItemStack stack = pOffer.getResult();
		if (getTradingPlayer() != null) {
			OVModData data = ((IModdedEntity)getTradingPlayer()).getData();
			if (stack.getItem() == Items.PAPER) {
				if (stack.hasTag() && stack.getTag().contains("obville:to_forgive")) {
					CompoundTag tag = stack.getTag().getCompound("obville:to_forgive");
					if (tag.getString("forWhat") != "generic") {
						data.resolveCrime(tag.getInt("whichVillage"), Laws.lawsToUphold.get(tag.getString("forWhat")));
					}
					data.incrementReputation(tag.getInt("amount"));
					if (getTradingPlayer() instanceof ServerPlayer)
						VillageData.invalidateGounty((ServerPlayer)getTradingPlayer());
					stack.setCount(0);
				}
			}
			if (VillageData.isBounty(pOffer.getCostA())) {
				if (this.level instanceof ServerLevel server) {
					PlacedBlocks pb = PlacedBlocks.getInstance(server);
					pb.villages.get(currentVillage).generatedBounties.remove(pOffer.getCostA());
					data.incrementReputation(ObVille.REPUTATION_CONFIG.bounty);
					Component message = new TextComponent(ObVille.LINES_CONFIG.bounty.get(random.nextInt(ObVille.LINES_CONFIG.bounty.size())));
					if (getTags().contains("villagernames.named"))
						message = getCustomName().copy().append(": ").append(message);
					else
						message = getTypeName().copy().append(": ").append(message);
					ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(message, getTradingPlayer().getUUID()), ((ServerPlayer)getTradingPlayer()).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
				}
			}
		}
		pOffer.increaseUses();
	}

	public Component fromVillager(Component original) {
		if (getTags().contains("villagernames.named"))
			return getCustomName().copy().append(": ").append(original);
		else
			return getName().copy().append(": ").append(original);
	}

	@Override
	public Player followedCriminal() {
		return followedCriminal;
	}

	@Override
	public int followedtime() {
		return followedtime;
	}

	@Override
	public void setFollowedCriminal(Player player) {
		followedCriminal = player;
	}

	@Override
	public void setFollowedtime(int time) {
		followedtime = time;
	}

	@Override
	public BlockPos investigatePos() {
		return investigatePos;
	}

	@Override
	public Crime crimeToInvestigate() {
		return crimeToInvestigate;
	}

	@Override
	public void setInvestigatePos(BlockPos pos) {
		this.investigatePos = pos;
	}

	@Override
	public void crimeToInvestigate(Crime crime) {
		this.crimeToInvestigate = crime;
	}
}
