package dev.vissca.svcrates;

import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.block.entity.ModBlockEntities;
import dev.vissca.svcrates.enchantment.ModEnchantmentEffects;
import dev.vissca.svcrates.item.ModItemGroups;
import dev.vissca.svcrates.item.ModItems;
import dev.vissca.svcrates.system.ModConfig;
import dev.vissca.svcrates.system.ModConfigManager;
import dev.vissca.svcrates.system.ModResourceReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
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

		ModItems.registerModItems();
		ModBlocks.registerBlocks();
		ModBlockEntities.registerBlockEntities();
		ModItemGroups.registerItemGroups();
		ModEnchantmentEffects.registerModEnchantmentEffects();

	}
}

