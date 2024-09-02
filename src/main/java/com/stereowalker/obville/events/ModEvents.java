package com.stereowalker.obville.events;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import com.stereowalker.obville.Crime;
import com.stereowalker.obville.Laws;
import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.compat.FarmersDelightCompat;
import com.stereowalker.obville.compat.GuardVillagersCompat;
import com.stereowalker.obville.compat.MoreCTCompat;
import com.stereowalker.obville.compat.QuarkCompat;
import com.stereowalker.obville.compat.WaystonesCompat;
import com.stereowalker.obville.core.ModdedStats;
import com.stereowalker.obville.dat.VillageData;
import com.stereowalker.obville.interfaces.IGeneratableBlockEntity;
import com.stereowalker.obville.interfaces.IModdedEntity;
import com.stereowalker.obville.interfaces.ISheep;
import com.stereowalker.obville.network.protocol.game.ClientboundNBTPacket;
import com.stereowalker.obville.world.PlacedBlocks;
import com.stereowalker.obville.world.entity.VillageLeader;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.HayBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.MelonBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkDirection;

@EventBusSubscriber
public class ModEvents {

	@SubscribeEvent
	public static void on(PlayerSleepInBedEvent event) {
		if (event.getPlayer() instanceof ServerPlayer spl) {
			IModdedEntity ent = (IModdedEntity)spl;
			if (ent.getData().currentVillage() >= 0) {
				if (spl.level.getBlockEntity(event.getPos()) instanceof BedBlockEntity bed && ((IGeneratableBlockEntity)bed).wasGenerated()) {
					ObVille.upsetNearby(event.getPlayer(), event.getPlayer().blockPosition(), true, -ObVille.REPUTATION_CONFIG.sleeping_in_bed, null);
				}
			}
		}
	}

	@SubscribeEvent
	public static void on(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof ServerPlayer spl) {
			if (event.getEntity() instanceof VillageLeader) {				
				ObVille.upsetNearby(spl, spl.blockPosition(), true, -ObVille.REPUTATION_CONFIG.killing_chiefs, null);
			}
			else if (event.getEntity() instanceof Villager) {	
				ObVille.upsetNearby(spl, spl.blockPosition(), true, 0,  ()->
				new Crime(Laws.KILLING_VILLAGERS, new ItemStack(Items.DIAMOND_SWORD, 1)));
			}
			else if (event.getEntity() instanceof IronGolem) {				
				ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
				new Crime(Laws.KILLING_GOLEMS, new ItemStack(Items.PUMPKIN, 2), new ItemStack(Items.IRON_BLOCK, 4)));
			}
			else if (ObVille.hasGuardVillagers() && GuardVillagersCompat.isGuard(event.getEntity())) {				
				ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
				new Crime(Laws.KILLING_GUARDS, new ItemStack(Items.DIAMOND_BLOCK, 1)));
			}
			else if (event.getEntity() instanceof ServerPlayer victim) {				
				IModdedEntity ent = (IModdedEntity)victim;
				ent.getData().vill().forEach(village -> {
					if (ent.getData().IsExiledAt(village) && ent.getData().reputAtNoSave(village).droppedBounty) {
						victim.drop(VillageData.bounty(victim, ent.getData().currentVillage()), true, false);
						ent.getData().reputAtNoSave(village).droppedBounty = true;
					}
				});
			}
		}
	}

	@SubscribeEvent
	public static void trample(FarmlandTrampleEvent event) {
		if (event.getEntity() instanceof ServerPlayer spl) {
			IModdedEntity ent = (IModdedEntity)spl;
			if (ent.getData().currentVillage() >= 0) {
				if (event.getWorld().getBlockState(event.getPos().above()).getBlock() instanceof CropBlock crop) {
					if (ent.getData().isWatchedForBreakingCrops()) {
						ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						{
							if (FarmersDelightCompat.plantFromMod(crop))
								return FarmersDelightCompat.equivalentCrime(crop);
							else if (crop == Blocks.CARROTS)
								return new Crime(Laws.BREAKING_CARROT, 
										new ItemStack(Items.CARROT, 2));
							else if (crop == Blocks.POTATOES)
								return new Crime(Laws.BREAKING_POTATO, 
										new ItemStack(Items.POTATO, 2));
							else if (crop == Blocks.BEETROOTS)
								return new Crime(Laws.BREAKING_BEETROOT, 
										new ItemStack(Items.BEETROOT, 2), new ItemStack(Items.BEETROOT_SEEDS, 1));
							else
								return new Crime(Laws.BREAKING_CROPS, 
										new ItemStack(Items.WHEAT, 2), new ItemStack(Items.WHEAT_SEEDS, 1));
						});
					}
					ent.getData().watchForBreakingCrops();
				}
			}
		}
	}

	@SubscribeEvent
	public static void on(BlockEvent.EntityPlaceEvent event) {
		if (event.getWorld() instanceof ServerLevel server && event.getEntity() instanceof IModdedEntity mod) {
			PlacedBlocks pb = PlacedBlocks.getInstance(server);
			Runnable r = () -> {
				pb.playerPlacedBlock(event.getPos());
				pb.setDirty();
				System.out.println("PLaced "+event.getState()+" "+event.getPos());
			};
			if (pb.blockChanges.getOrDefault(event.getPos(), 0) != 2 && mod.getData().currentVillage() >= 0) {
				Block block = event.getState().getBlock();
				if (event.getState().getBlock() instanceof MelonBlock) {
					r.run();
				}
				else if (event.getState().getBlock() instanceof PumpkinBlock) {
					r.run();
				}
				else if (event.getState().getBlock() instanceof HayBlock) {
					r.run();
				}
				else if (event.getState().getBlock() == Blocks.COMPOSTER) r.run();
				else if (event.getState().getBlock() == Blocks.FURNACE) {
					r.run();
				}
				else if (event.getState().getBlock() == Blocks.BLAST_FURNACE) {
					r.run();
				}
				else if (block == Blocks.ANVIL) r.run();
				else if (block == Blocks.CRAFTING_TABLE) r.run();
				else if (ObVille.hasMoreCraftingTables() && MoreCTCompat.fromMod(block)) r.run();
				else if (ObVille.hasQuark() && QuarkCompat.isTable(block)) r.run();
				else if (block == Blocks.FLETCHING_TABLE) r.run();
				else if (event.getState().getBlock() == Blocks.STONECUTTER) {
					r.run();
				}
				else if (event.getState().getBlock() == Blocks.SMITHING_TABLE) {
					r.run();
				}
				else if (event.getState().getBlock() == Blocks.BREWING_STAND) {
					r.run();
				}
				else if (event.getState().getBlock() == Blocks.CARTOGRAPHY_TABLE) {
					r.run();
				}
				else if (event.getState().getBlock() == Blocks.TORCH || event.getState().getBlock() == Blocks.WALL_TORCH) {
					r.run();
				}
				else if (event.getWorld().getBlockEntity(event.getPos()) instanceof BellBlockEntity bell) {
					r.run();
				}
				else if (block == Blocks.LECTERN) r.run();
				else if (block == Blocks.OBSERVER) r.run();
				else if (block == Blocks.LOOM) r.run();
				else if (block instanceof BannerBlock) r.run();
				else if (block instanceof FlowerPotBlock) r.run();
				else if (block instanceof AbstractChestBlock) r.run();
				else if (block instanceof BarrelBlock) r.run();
				else if (block instanceof HopperBlock) r.run();
				else if (block == Blocks.TARGET) r.run();
				else if (block == Blocks.JUKEBOX) r.run();
				else if (block == Blocks.SMOKER) r.run();
				else if (block == Blocks.GRINDSTONE) r.run();
				else if (block instanceof AbstractCauldronBlock) r.run();
				else if (block == Blocks.CAMPFIRE) r.run();
				else if (block == Blocks.BOOKSHELF) r.run();
				else if (block == Blocks.LANTERN) r.run();
			}
		}
	}

	@SubscribeEvent
	public static void on(BlockEvent.BreakEvent event) {
		if (event.getPlayer() instanceof ServerPlayer spl) {
			IModdedEntity ent = (IModdedEntity)spl;
			if (ent.getData().currentVillage() >= 0) {
				PlacedBlocks pb = PlacedBlocks.getInstance(spl.getLevel());
				Runnable broke = () -> {
					pb.blockChanges.remove(event.getPos());
					pb.setDirty();
				};
				Runnable replace = () -> {
					pb.brokeGeneratedBlock(event.getPos());
				};

				if (pb.blockChanges.getOrDefault(event.getPos(), 0) == 1) {
					broke.run();
				} else {
					Block block = event.getState().getBlock();
					if (event.getState().getBlock() instanceof CropBlock crop) {
						if (ent.getData().isWatchedForBreakingCrops()) {
							ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
							{
								if (FarmersDelightCompat.plantFromMod(crop))
									return FarmersDelightCompat.equivalentCrime(crop);
								else if (crop == Blocks.CARROTS)
									return new Crime(Laws.BREAKING_CARROT, new ItemStack(Items.CARROT, 2));
								else if (crop == Blocks.POTATOES)
									return new Crime(Laws.BREAKING_POTATO, new ItemStack(Items.POTATO, 2));
								else if (crop == Blocks.BEETROOTS)
									return new Crime(Laws.BREAKING_BEETROOT, 
											new ItemStack(Items.BEETROOT, 2), new ItemStack(Items.BEETROOT_SEEDS, 1));
								else
									return new Crime(Laws.BREAKING_CROPS, new ItemStack(Items.WHEAT, 2), new ItemStack(Items.WHEAT_SEEDS, 1));
							});
						}
						ent.getData().watchForBreakingCrops();
						broke.run();
					}
					else if (block instanceof MelonBlock) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_MELONS, new ItemStack(Items.MELON, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block instanceof PumpkinBlock) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_PUMPKINS, new ItemStack(Items.PUMPKIN, 2)))) broke.run();
						else replace.run();
					}
					else if (block instanceof HayBlock) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true,0, ()->
						new Crime(Laws.BREAKING_HAY, new ItemStack(Items.HAY_BLOCK, 1)))) broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.COMPOSTER) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_COMPOSTERS, new ItemStack(Items.COMPOSTER, 2))))
							broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.GRINDSTONE) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_GRINDSTONES, new ItemStack(Items.GRINDSTONE, 2))))
							broke.run();
						else replace.run();
					}
					else if (block instanceof FlowerPotBlock pot) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_POT, new ItemStack(Items.FLOWER_POT, 2), new ItemStack(pot.getContent().asItem(), 2))))
							broke.run();
						else replace.run();
					}
					else if (block instanceof AbstractChestBlock) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_CONTAINER, new ItemStack(block.asItem(), 2))))
							broke.run();
						else replace.run();
					}
					else if (block instanceof BarrelBlock) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_CONTAINER, new ItemStack(block.asItem(), 2))))
							broke.run();
						else replace.run();
					}
					else if (block instanceof HopperBlock) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_HOPPER, new ItemStack(block.asItem(), 2))))
							broke.run();
						else replace.run();
					}
					else if (block == Blocks.FURNACE) {
						if (ObVille.upsetNearby(spl, true, ()->
						new Crime(Laws.BREAKING_FURNACES, new ItemStack(Items.FURNACE, 2))))
							broke.run();
						else replace.run();
					}
					else if (block == Blocks.BLAST_FURNACE) {
						if (ObVille.upsetNearby(spl, true, ()->
						new Crime(Laws.BREAKING_BLAST_FURNACE, new ItemStack(Items.BLAST_FURNACE, 2))))
							broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.ANVIL) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_ANVILS, new ItemStack(Items.ANVIL, 1))))
							broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.CRAFTING_TABLE) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_CRAFTING_TABLES, new ItemStack(Items.CRAFTING_TABLE, 2))))
							broke.run();
						else replace.run();
					}
					else if (ObVille.hasMoreCraftingTables() && MoreCTCompat.fromMod(block)) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, -ObVille.REPUTATION_CONFIG.breaking_job_sites, ()->
						new Crime(MoreCTCompat.equivalentLaw(block), new ItemStack(MoreCTCompat.equivalentItem(block), 2))))
							broke.run();
						else replace.run();
					}
					else if (ObVille.hasFarmersDelight() && FarmersDelightCompat.fromMod(block)) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(FarmersDelightCompat.equivalentLaw(block), new ItemStack(FarmersDelightCompat.equivalentItem(block), 2))))
							broke.run();
						else replace.run();
					}
					else if (ObVille.hasQuark() && QuarkCompat.isTable(block)) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, -ObVille.REPUTATION_CONFIG.breaking_job_sites, ()->
						new Crime(QuarkCompat.equivalentLaw(block), new ItemStack(QuarkCompat.equivalentItem(block), 2))))
							broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.FLETCHING_TABLE) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_FLETCHING_TABLES, new ItemStack(Items.FLETCHING_TABLE, 2))))
							broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.STONECUTTER) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_STONECUTTTER, new ItemStack(Items.STONECUTTER, 2))))
							broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.SMITHING_TABLE) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, -ObVille.REPUTATION_CONFIG.breaking_job_sites, ()->
						new Crime(Laws.BREAKING_SMITHING_TABLE, new ItemStack(Items.SMITHING_TABLE, 2))))
							broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.BREWING_STAND) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_BREWING_STAND, Items.BREWING_STAND))) broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.CARTOGRAPHY_TABLE) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_CARTOGRAPHY_TABLE, Items.CARTOGRAPHY_TABLE))) broke.run();
						else replace.run();
					}
					else if (event.getState().getBlock() == Blocks.TORCH || event.getState().getBlock() == Blocks.WALL_TORCH) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_TORCHES, Items.TORCH))) broke.run();
						else replace.run();
					}
					else if (block == Blocks.LECTERN) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_LECTERN, new ItemStack(Items.LECTERN, 2))))
							broke.run();
						else replace.run();
					}
					else if (block == Blocks.OBSERVER) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_OBSERVER, new ItemStack(Items.OBSERVER, 2))))
							broke.run();
						else replace.run();
					}
					else if (block == Blocks.LOOM) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_LOOM, new ItemStack(Items.LOOM, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block == Blocks.TARGET) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_TARGET, new ItemStack(Items.TARGET, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block == Blocks.JUKEBOX) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_JUKEBOX, new ItemStack(Items.JUKEBOX, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block == Blocks.SMOKER) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_SMOKER, new ItemStack(Items.SMOKER, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block instanceof AbstractCauldronBlock) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_CAULDRON, new ItemStack(Items.CAULDRON, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block == Blocks.CAMPFIRE) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_CAMPFIRE, new ItemStack(Items.CAMPFIRE, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block == Blocks.BOOKSHELF) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_BOOKSHELF, new ItemStack(Items.BOOKSHELF, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (block == Blocks.LANTERN) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_LANTERN, new ItemStack(Items.LANTERN, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (event.getWorld().getBlockEntity(event.getPos()) instanceof BellBlockEntity bell) {
						if (ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
						new Crime(Laws.BREAKING_BELLS, new ItemStack(Items.BELL, 2))))
							broke.run();
						else
							replace.run();
					}
					else if (event.getWorld().getBlockEntity(event.getPos()) instanceof BedBlockEntity bed) {
						if (((IGeneratableBlockEntity)bed).wasGenerated())
							ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
							new Crime(Laws.BREAKING_BED, new ItemStack(Items.WHITE_BED, 1)));
					}
				}
				//Blocks here already have a was generated check
				if (ObVille.hasWaystones() && WaystonesCompat.isTable(event.getWorld().getBlockEntity(event.getPos()))) {
					ObVille.upsetNearby(spl, spl.blockPosition(), true, 0, ()->
					new Crime(WaystonesCompat.equivalentLaw(event.getState().getBlock()), new ItemStack(event.getState().getBlock().asItem(), 1)));
				}
			}
		}
	}

	@Nullable
	public static Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> findNearestMapFeature(ServerLevel serverlevel, TagKey<ConfiguredStructureFeature<?, ?>> pStructureTag, BlockPos pPos, int pRadius, boolean pSkipExistingChunks) {
		if (!serverlevel.getServer().getWorldData().worldGenSettings().generateFeatures()) {
			return null;
		} else {
			Optional<HolderSet.Named<ConfiguredStructureFeature<?, ?>>> optional = serverlevel.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY).getTag(pStructureTag);
			if (optional.isEmpty()) {
				return null;
			} else {
				Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = serverlevel.getChunkSource().getGenerator().findNearestMapFeature(serverlevel, optional.get(), pPos, pRadius, pSkipExistingChunks);
				return pair != null ? pair : null;
			}
		}
	}

	@SubscribeEvent
	public static void sendToClient(LivingUpdateEvent event) {
		if (event.getEntityLiving() != null && !event.getEntityLiving().level.isClientSide && event.getEntityLiving() instanceof ServerPlayer player) {
			ObVille.getInstance().channel.sendTo(new ClientboundNBTPacket(player), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			ServerLevel serverlevel = (ServerLevel)player.level;

			BlockPos blockpos = player.blockPosition();
			IModdedEntity ent = (IModdedEntity)player;

			ent.getData().setInVillage(ObVille.determineVillage(serverlevel, blockpos));


			player.getInventory().items.forEach((stack) ->{
				if (stack.getItem() == Items.PAPER) {
					if (stack.hasTag() && stack.getTag().contains("obville:to_forgive")) {
						stack.setCount(0);
					}
				}
			});

			if (ent.getData().currentVillage() >= 0 && player.tickCount % 20 == 0) {
				List<VillageLeader> list = player.level.getEntitiesOfClass(VillageLeader.class, player.getBoundingBox().inflate(64.0));
				if (list.isEmpty()) {
					List<Villager> list2 = player.level.getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(64.0));
					if (!list2.isEmpty()) {
						if (((ISheep)list2.get(list2.size()-1)).spawnLeaderIfNeeded(serverlevel, 1, 2)) {
							System.out.println("Spawning New Leader");
						}
					}
				}
				else if (list.size() > 1) {
					for (int i = 1; i < list.size(); i++) {
						list.get(i).discard();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void restoreStats(PlayerEvent.Clone event) {
		ModdedStats.getOrCreateModNBT(event.getPlayer());
		IModdedEntity original = ((IModdedEntity)event.getOriginal());
		IModdedEntity player = ((IModdedEntity)event.getPlayer());
		player.setData(original.getData());
		if (!event.isWasDeath()) {
		}
	}
}
