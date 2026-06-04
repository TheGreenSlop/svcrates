package dev.vissca.svcrates.block.entity;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.block.entity.custom.CrateBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/// Important Class! Registers block entities, they won't appear in game otherwise, similar to how
/// Items and Blocks work.
public class ModBlockEntities {
    public static final BlockEntityType<CrateBlockEntity> CRATE_BLOCK_ENTITY =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(SvCrates.MOD_ID, "crate_block_entity"),
                    BlockEntityType.Builder.create(
                            CrateBlockEntity::new,
                            ModBlocks.CRATE_BLOCK
                    ).build()
            );
    /// This "wakes up" the class basically. By triggering any function in my init script!
    public static void registerBlockEntities() {SvCrates.LOGGER.info("Registering BlockEntities for SvCrates!");}
}