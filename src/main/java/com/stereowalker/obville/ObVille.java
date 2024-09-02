package com.stereowalker.obville;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.stereowalker.obville.client.renderer.entity.VillageChiefModel;
import com.stereowalker.obville.client.renderer.entity.VillagerChiefRenderer;
import com.stereowalker.obville.compat.GuardVillagersCompat;
import com.stereowalker.obville.config.ClientConfig;
import com.stereowalker.obville.config.ExtraLinesConfig;
import com.stereowalker.obville.config.ModConfig;
import com.stereowalker.obville.config.ReputationAmountConfig;
import com.stereowalker.obville.dat.OVModData;
import com.stereowalker.obville.dat.VillageData;
import com.stereowalker.obville.interfaces.IInvestigator;
import com.stereowalker.obville.interfaces.ILootableBlock;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.interfaces.IVillager;
import com.stereowalker.obville.network.protocol.game.ClientboundOverlayOverridePacket;
import com.stereowalker.obville.network.protocol.game.ClientboundSoundPacket;
import com.stereowalker.obville.network.protocol.game.ClientboundNBTPacket;
import com.stereowalker.obville.network.protocol.game.ClientboundVillagerMessagePacket;
import com.stereowalker.obville.network.protocol.game.ServerboundRelaxPacket;
import com.stereowalker.obville.world.PlacedBlocks;
import com.stereowalker.obville.world.effect.Effects;
import com.stereowalker.obville.world.entity.ModEntities;
import com.stereowalker.obville.world.entity.VillageLeader;
import com.stereowalker.unionlib.client.gui.screens.config.MinecraftModConfigsScreen;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.mod.IPacketHolder;
import com.stereowalker.unionlib.mod.MinecraftMod;
import com.stereowalker.unionlib.network.PacketRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(value = "obville")
public class ObVille extends MinecraftMod implements IPacketHolder {

	public static Map<Potion,List<Fluid>> POTION_FLUID_MAP;
	public static final String MOD_ID = "obville";

	public static final ClientConfig CLIENT_CONFIG = new ClientConfig();
	public static final ExtraLinesConfig LINES_CONFIG = new ExtraLinesConfig();
	public static final ModConfig MOD_CONFIG = new ModConfig();
	public static final ReputationAmountConfig REPUTATION_CONFIG = new ReputationAmountConfig();

	private static ObVille instance;

	public static boolean hasMoreCraftingTables() {
		return ModList.get().isLoaded("mctb");
	}

	public static boolean hasFarmersDelight() {
		return ModList.get().isLoaded("farmersdelight");
	}

	public static boolean hasGuardVillagers() {
		return ModList.get().isLoaded("guardvillagers");
	}

	public static boolean hasQuark() {
		return ModList.get().isLoaded("quark");
	}

	public static boolean hasWaystones() {
		return ModList.get().isLoaded("waystones");
	}

	public static boolean hasVillagerNames() {
		return ModList.get().isLoaded("villagernames");
	}

	public ObVille() 
	{
		super("obville", new ResourceLocation(MOD_ID, "pack.png"), LoadType.BOTH);
		instance = this;
		ConfigBuilder.registerConfig(CLIENT_CONFIG);
		ConfigBuilder.registerConfig(MOD_CONFIG);
		ConfigBuilder.registerConfig(REPUTATION_CONFIG);
		ConfigBuilder.registerConfig(LINES_CONFIG);
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		//		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		Laws.bootstrap();
	}

	@Override
	public void registerServerboundPackets(SimpleChannel channel) {
		PacketRegistry.registerMessage(channel, 0, ServerboundRelaxPacket.class, (packetBuffer) -> {return new ServerboundRelaxPacket(packetBuffer);});
	}

	@Override
	public void registerClientboundPackets(SimpleChannel channel) {
		PacketRegistry.registerMessage(channel, 3, ClientboundOverlayOverridePacket.class, ClientboundOverlayOverridePacket::new);
		channel.registerMessage(4, ClientboundSoundPacket.class, ClientboundSoundPacket::encode, ClientboundSoundPacket::decode, ClientboundSoundPacket::handle);
		channel.registerMessage(5, ClientboundNBTPacket.class, ClientboundNBTPacket::encode, ClientboundNBTPacket::decode, ClientboundNBTPacket::handle);
		PacketRegistry.registerMessage(channel, 6, ClientboundVillagerMessagePacket.class, ClientboundVillagerMessagePacket::new);
	}

	public static boolean isPotentialBandit(Player player) {
		return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARVED_PUMPKIN && !EnchantmentHelper.hasBindingCurse(player.getItemBySlot(EquipmentSlot.HEAD)) ;
	}

	public static boolean upsetOnOpen(RandomizableContainerBlockEntity chest, ServerPlayer pPlayer, BlockPos pPos , BlockPos pPos2) {
		PlacedBlocks pb = PlacedBlocks.getInstance(pPlayer.getLevel());
		ILootableBlock loot = (ILootableBlock)chest;

		if (pb.didPlayerPlaceBlock(pPos) && (pPos2 == null || pb.didPlayerPlaceBlock(pPos2))) {
			return false;
		}

		if (loot != null && loot.getLoot() != null)
			System.out.println(loot.getLoot().getPath());
		if (loot != null && MOD_CONFIG.COnts().contains(loot.getLoot()) && !loot.hasPlayerOpened(pPlayer) && upsetNearby(pPlayer, pPos, true, -REPUTATION_CONFIG.opening_containers, null)) {
			loot.addPlayer(pPlayer);
			return true;
		}
		if ((!pb.didPlayerPlaceBlock(pPos) || (pPos2 != null && !pb.didPlayerPlaceBlock(pPos2))) && upsetNearby(pPlayer, pPos, true, -REPUTATION_CONFIG.opening_containers, null)) {
			pb.playerPlacedBlock(pPos);
			if (pPos2 != null) pb.playerPlacedBlock(pPos2);
			return true;
		}
		return false;
	}

	public static boolean upsetNearby(Player player, boolean angerOnlyIfCanSee, Supplier<Crime> crime) {
		return upsetNearby(player, player.blockPosition(), angerOnlyIfCanSee, 0, crime);
	}

	public static boolean isLookingAtPlayer(LivingEntity guard, Player player) {
		//		Vec3 vec3 = guard.getViewVector(1.0F).normalize();
		//        Vec3 vec31 = new Vec3(player.getX() - guard.getX(), player.getEyeY() - guard.getEyeY(), player.getZ() - guard.getZ());
		//        double d0 = vec31.length();
		//        vec31 = vec31.normalize();
		//        double d1 = vec3.dot(vec31);
		//        return d1 > 1.0D - 0.025D / d0 ? guard.hasLineOfSight(player) : false;
		return guard.hasLineOfSight(player);
	}

	@SuppressWarnings("unchecked")
	public static boolean upsetNearby(Player player, BlockPos pos, boolean angerOnlyIfCanSee, int amount, Supplier<Crime> crime) {
		float distance_from_site = 20;
		List<Villager> vills = new ArrayList<Villager>();
		List<Villager> villagersInRange = player.level.getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(16.0));
		villagersInRange.stream().filter(villager -> (
				!angerOnlyIfCanSee || (((BehaviorUtils.canSee(villager, player) || player.hasEffect(MobEffects.INVISIBILITY)) && villager.hasLineOfSight(player))) && 
				!((IVillager<Villager>)villager).recentlyTakenBribe().containsKey(player.getUUID()) &&
				!villager.isDeadOrDying())).forEach(villager -> {
					Brain<Villager> brain = villager.getBrain();
					if (brain.getMemory(MemoryModuleType.HOME).isPresent() && brain.getMemory(MemoryModuleType.HOME).get().pos().closerThan(pos, distance_from_site)) {
						vills.add(villager);
						villager.setUnhappyCounter(40);
					} else if (brain.getMemory(MemoryModuleType.JOB_SITE).isPresent() && brain.getMemory(MemoryModuleType.JOB_SITE).get().pos().closerThan(pos, distance_from_site)) {
						vills.add(villager);
						villager.setUnhappyCounter(40);
					} else {
						vills.add(villager);
						villager.setUnhappyCounter(40);
					}
				});
		List<VillageLeader> leaders = player.level.getEntitiesOfClass(VillageLeader.class, player.getBoundingBox().inflate(16.0));
		leaders.stream().filter(leader -> !angerOnlyIfCanSee || (BehaviorUtils.canSee(leader, player) && isLookingAtPlayer(leader, player)) || (player.hasEffect(MobEffects.INVISIBILITY) && leader.hasLineOfSight(player))).forEach(chief -> {
			Brain<VillageLeader> brain = chief.getBrainC();
			if (brain.getMemory(MemoryModuleType.HOME).isPresent() && brain.getMemory(MemoryModuleType.HOME).get().pos().closerThan(pos, distance_from_site)) {
				vills.add(chief);
				chief.setUnhappyCounter(40);
			} else if (brain.getMemory(MemoryModuleType.JOB_SITE).isPresent() && brain.getMemory(MemoryModuleType.JOB_SITE).get().pos().closerThan(pos, distance_from_site)) {
				vills.add(chief);
				chief.setUnhappyCounter(40);
			} else {
				vills.add(chief);
				chief.setUnhappyCounter(40);
			}
		});
		List<LivingEntity> guardians = new ArrayList<LivingEntity>();
		if (hasGuardVillagers()) {
			GuardVillagersCompat.tryToAnger(player, angerOnlyIfCanSee, guardians, vills);
		}
		Crime crimeCommited = null;
		if (crime != null) crimeCommited = crime.get();
		if (!player.hasEffect(MobEffects.INVISIBILITY)) {
			vills.forEach(villager ->{
				if (player instanceof ServerPlayer sPlayer)
					((IVillager<Villager>)villager).blacklist(sPlayer);
			});

			List<LivingEntity> b = new ArrayList<LivingEntity>();
			b.addAll(vills);
			b.addAll(guardians);
			List<IronGolem> list2 = player.level.getEntitiesOfClass(IronGolem.class, player.getBoundingBox().inflate(16.0));
			list2.stream().filter(golem -> !angerOnlyIfCanSee || isLookingAtPlayer(golem, player) || BehaviorUtils.canSee(golem, player) || vills.size() > 0).forEach(golem -> {
				b.add(golem);
			});
			if (!b.isEmpty()) {

				if (ObVille.isPotentialBandit(player)) {
					OVModData modEn = ((IModdedEntity)player).getData();
					modEn.caughtBanditry();

					b.forEach(liv -> {
						if (hasGuardVillagers()) {
							//							if (liv.getTags().contains("villagernames.named"))
							//								ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(liv.getCustomName().copy().append(": ").append(new TranslatableComponent("I found a bandit")), player.getUUID()), ((ServerPlayer)player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
							//							else
							//								ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(liv.getName().copy().append(": ").append(new TranslatableComponent("I found a bandit")), player.getUUID()), ((ServerPlayer)player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
							GuardVillagersCompat.target(liv, player);
						}
						if (liv instanceof IronGolem golem) {
							//							ObVille.getInstance().channel.sendTo(new ClientboundVillagerMessagePacket(liv.getName().copy().append(": ").append(new TranslatableComponent("I found a bandit")), player.getUUID()), ((ServerPlayer)player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
							golem.setTarget(player);
						}
					});

					return false;
				} else {
					if (guardians.size() >= 1) {
						if (hasGuardVillagers()) {
							GuardVillagersCompat.wit(player, guardians, crimeCommited);
						}
					}
					if (vills.size() == 1) {
						if (vills.get(0) instanceof IVillager && crimeCommited != null) {
							IVillager<Villager> villager = (IVillager<Villager>)vills.get(0);
							if (!villager.recentlyWitnessedCrime().containsKey(player.getUUID())) {
								villager.witnessCrime(player, crimeCommited);
								new ClientboundOverlayOverridePacket(player.getUUID()).send((ServerPlayer)player);
								new ClientboundVillagerMessagePacket(villager.fromVillager(LINES_CONFIG.caught), player.getUUID()).send((ServerPlayer)player);
							}
						}
					}

					if (crimeCommited != null && !crimeCommited.lawBroken.isPardonable()) {
						b.forEach(liv -> {
							if (hasGuardVillagers()) {
								GuardVillagersCompat.target(liv, player);
							}
						});
					}


					OVModData modEn = ((IModdedEntity)player).getData();
					modEn.incrementReputation(crime != null ? 
							crimeCommited.lawBroken.getRepHit() : -amount);
					if (crimeCommited != null) {
						modEn.commitCrime(crimeCommited);
					}
					if (player instanceof ServerPlayer serverplayer) {
						PlacedBlocks pb = PlacedBlocks.getInstance(serverplayer.getLevel());
						ObVille.getInstance().channel.sendTo(new ClientboundSoundPacket(false, player.getUUID()), serverplayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
						if (modEn.IsExiled() && !modEn.reput().generatedBounty) {
							modEn.reput().generatedBounty = true;
							pb.villages.get(modEn.currentVillage()).generatedBounties.add(VillageData.bounty(serverplayer, modEn.currentVillage()));
						}
					}

					return true;
				}

			}
		} else {
			vills.forEach(villag -> {
				IVillager<Villager> villager = (IVillager<Villager>)villag;
				if (villager.invisibleLineCooldown() <= 0 && !(villag instanceof VillageLeader)) {
					villager.setInvisibleLineCooldown(MOD_CONFIG.invi);
					new ClientboundVillagerMessagePacket(villager.fromVillager(LINES_CONFIG.invisible), player.getUUID()).send((ServerPlayer)player);
				}
			});
			final Crime cc = crimeCommited;
			List<LivingEntity> investigators = new ArrayList<>();
			investigators.addAll(guardians);
			investigators.addAll(leaders);
			IInvestigator investigator = (IInvestigator) investigators.get(player.getRandom().nextInt(investigators.size()));
			investigator.setInvestigatePos(pos);
			if (cc != null)
				investigator.crimeToInvestigate(cc);
		}
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return new MinecraftModConfigsScreen(previousScreen, new TranslatableComponent("gui.obville.config.title"), MOD_CONFIG, REPUTATION_CONFIG, CLIENT_CONFIG);
	}

	public static int determineVillage(ServerLevel level, BlockPos blockpos) {
		PlacedBlocks pb = PlacedBlocks.getInstance(level);
		if (level.isCloseToVillage(blockpos, 2)) {
			if (ObVille.MOD_CONFIG.global_rep)
				return 0;
			else
				return pb.isVillageOrRegisterVillage(level, blockpos);
		}
		return -1;
	}

	public void clientRegistries(final FMLClientSetupEvent event)
	{
		GuiHelper.registerOverlays();
		ForgeHooksClient.registerLayerDefinition(VillageChiefModel.LAYER_LOCATION, VillageChiefModel::createBodyLayer);
		EntityRenderers.register(ModEntities.VILLAGE_CHIEF, VillagerChiefRenderer::new);
	}

	@Override
	public IRegistries getRegistries() {
		return (reg) -> {
			reg.add(Effects.class);
		};
	}

	@Override
	public void registerServerRelaodableResources(ReloadListeners reloadListener) {
	}

	public static ObVille getInstance() {
		return instance;
	}
}
