package dev.vissca.svcrates;

import dev.vissca.svcrates.datagen.enchantment.ModEnchantmentProvider;
import dev.vissca.svcrates.enchantment.ModEnchantments;
import dev.vissca.svcrates.system.*;
import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.block.entity.ModBlockEntities;
import dev.vissca.svcrates.advancement.criterion.ModCriteria;
import dev.vissca.svcrates.item.ModItemGroups;
import dev.vissca.svcrates.item.ModItems;
import dev.vissca.svcrates.statistic.ModStatistics;
import dev.vissca.svcrates.system.config.ModConfig;
import dev.vissca.svcrates.system.config.ModConfigManager;
import dev.vissca.svcrates.system.networking.CrateDataPayload;
import dev.vissca.svcrates.system.networking.RequestCrateDataPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/// I'm the bug, I live in your code.
public class SvCrates implements ModInitializer {
	public static final String MOD_ID = "svcrates";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ModConfig CONFIG;

	@Override
	public void onInitialize() {
		LOGGER.info("I'm the bug, I live in your code.");

		ModConfigManager.load();
		CONFIG = ModConfigManager.config;
		ResourceManagerHelper.get(ResourceType.SERVER_DATA)
				.registerReloadListener(new ModResourceReloadListener());
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
				.registerReloadListener(new ModResourceReloadListener());

		PayloadTypeRegistry.playS2C().register(CrateDataPayload.ID, CrateDataPayload.CODEC);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			syncPlayerCratePayload(player);
		});

		PayloadTypeRegistry.playC2S().register(RequestCrateDataPayload.ID, RequestCrateDataPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(
				RequestCrateDataPayload.ID,
				(payload, context) -> {
					syncPlayerCratePayload(context.player());
				}
		);

		ModItems.registerModItems();
		ModBlocks.registerBlocks();
		ModBlockEntities.registerBlockEntities();
		ModItemGroups.registerItemGroups();
		ModEnchantments.registerModEnchantments();
		ModStatistics.registerStatistics();
		ModCriteria.registerModCriteria();
		LOGGER.info("Loaded {} crates", Vars.crateDataMap.size());
	}

	public static void syncPlayerCratePayload(ServerPlayerEntity player){
		if (!Vars.crateDataMap.isEmpty()){
			for (int dId = 0; dId < Vars.crateDataMap.size(); dId += 1) {
				String crateId = Util.getCrateIdByInt(dId);
				Vars.CrateData crate = Vars.crateDataMap.get(crateId);
				ServerPlayNetworking.send(player, new CrateDataPayload(crate, crateId));
			}
		}
	}
}

