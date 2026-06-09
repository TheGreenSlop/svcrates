package dev.vissca.svcrates.block.custom;

import com.mojang.serialization.MapCodec;
import dev.vissca.svcrates.system.Vars;
import dev.vissca.svcrates.block.entity.custom.CrateBlockEntity;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends BlockWithEntity implements BlockEntityProvider {
    public CrateBlock(Settings settings) {super(settings);}
    /// This tells my game about what the hell it should spawn whenever it tries to make the block entity
    /// Appear in the world.
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    /// Hitbox! The getOutlineShape returns my custom one whenever the game tries to get it.
    public static final VoxelShape SHAPE = Block.createCuboidShape(0,0,0,16,16,16);
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    /// Picking the block normally gives you a data-less Crate which causes all sorts of issue, this fixes
    /// That by overriding the stack.
    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack stack = new ItemStack(this.asItem());

        if (blockEntity instanceof CrateBlockEntity crate) {
            stack.set(CrateItem.CRATE_ID, crate.getCrateId());
            stack.set(CrateItem.CRATE_LOOT_ID, Vars.crateDataMap.get(crate.getCrateId()).lootTableId());
        }
        return stack;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            // Local Vars
            BlockEntity crateEntity = world.getBlockEntity(pos);
            ItemStack stack = this.asItem().getDefaultStack();
            if (crateEntity instanceof CrateBlockEntity crate) {
                stack.set(CrateItem.CRATE_ID, crate.getCrateId());
                stack.set(CrateItem.CRATE_LOOT_ID, Vars.crateDataMap.get(crate.getCrateId()).lootTableId());

                ItemEntity ent = new ItemEntity(
                        world, pos.toCenterPos().getX(), pos.toCenterPos().getY(), pos.toCenterPos().z, stack);
                ent.setToDefaultPickupDelay();

                world.spawnEntity(ent);
            }
        }
        return super.onBreak(world, pos, state, player);
    }


    /// Tells the game what kind of render this should have, I think? It came with my freaking class
    /// I don't remember :p (I think)
    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState,
                                boolean notify) {super.onBlockAdded(state, world, pos, oldState, notify);}

    /// This is to update my block's data, to tell the game to do it at least, one of the places that do that
    /// If I remember correctly.
    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrateBlockEntity) {
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

}
