package dev.vissca.svcrates.block;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.block.custom.CrateBlock;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/// Same as before, registers things to the game so the game knows what the freak to do with it.
public class ModBlocks {
    public static final Block CRATE_BLOCK = register(
            new CrateBlock(AbstractBlock.Settings.create().hardness(0.2f).nonOpaque().sounds(BlockSoundGroup.WOOD)),
            "crate_block", true);
    /// VERY temporary, in case I ever add new block types I should redo this function
    /// Because right now it ALWAYS makes their item be a CrateItem, #notideal.
    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(SvCrates.MOD_ID, name);
        if (shouldRegisterItem) {
            Registry.register(Registries.ITEM, id, new CrateItem(block, new Item.Settings()));
        }
        return Registry.register(Registries.BLOCK, id, block);
    }

    /// Currently unused, if you are reading my code, know that in the future there will be decorative crates!
    /// Non-data-driven tho.
    public static Block registerBlockWithItem(Block block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(SvCrates.MOD_ID, name);
        if (shouldRegisterItem) {
            Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        }
        return Registry.register(Registries.BLOCK, id, block);
    }

    /// WAKE UP, CLASS, NOW.
    public static void registerBlocks(){SvCrates.LOGGER.info("Registering Blocks for SvCrates!");}
}
