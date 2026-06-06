package dev.vissca.svcrates.item.custom;

import com.mojang.serialization.Codec;
import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.statistic.ModStatistics;
import dev.vissca.svcrates.system.Vars;
import dev.vissca.svcrates.block.entity.custom.CrateBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class CrateItem extends BlockItem {
    public CrateItem(Block block, Settings settings) {
        super(block, settings);
    }

    // NBT COMPONENTS
    public static final ComponentType<String> CRATE_LOOT_ID = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SvCrates.MOD_ID, "crate_loot_id"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );
    public static final ComponentType<String> CRATE_ID = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SvCrates.MOD_ID, "crate_id"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    /// Oh boy, this is the code that handles using the item.
    /// Shift Right Click while not looking at anything = Give the player loot tied to the Item's
    /// 'crate_loot_id' data. If null or looking at another block, do nothing or place it.
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.get(CRATE_LOOT_ID) == null)return TypedActionResult.fail(stack);

        RegistryKey<LootTable> tableName = RegistryKey.of(RegistryKeys.LOOT_TABLE,
                Identifier.of(Objects.requireNonNull(stack.get(CRATE_LOOT_ID))));
        if (tableName == null)return TypedActionResult.fail(stack);

        if (!world.isClient && player.isSneaking()){
            if (!player.isCreative()){
                stack.decrement(1);
            }
            ServerWorld serverWorld = (ServerWorld)player.getWorld();
            LootTable lootTable =
                    serverWorld.getServer().getReloadableRegistries().getLootTable(tableName);

            LootContextParameterSet lootContextParameterSet = (
                    new LootContextParameterSet.Builder(serverWorld))
                    .add(LootContextParameters.ORIGIN, player.getPos())
                    .add(LootContextParameters.THIS_ENTITY, player)
                    .build(LootContextTypes.CHEST);
            player.increaseStat(ModStatistics.OPEN_CRATE, 1);
            List<ItemStack> stacks = lootTable.generateLoot(lootContextParameterSet);
            for (ItemStack itemStack : stacks) {
                player.swingHand(hand);
                if (!player.giveItemStack(itemStack)) {
                    player.dropItem(itemStack, false);
                }
            }
        } else {
            return TypedActionResult.fail(stack);
        }
        return TypedActionResult.consume(stack);
    }

    /// Changes the crate item's name to be appropriate, if the data is bad, become a "Broken Crate".s
    @Override
    public Text getName(ItemStack stack) {
        String id = stack.get(CRATE_ID);
        Vars.CrateData data = Vars.crateDataMap.get(id);
        if (data != null){
            return Text.translatable("item.svcrates."+ Vars.crateDataMap.get(stack.get(CRATE_ID)).id() +"_crate");
        }
        return Text.translatable("item.svcrates.broken_crate");

    }

    /// This makes the item place the CrateBlock and passes its data into it.
    /// I don't feel like noting this down, yet I'll do later
    /// But I'll say, remember to mark changed stuff with nbt as dirt (markDirt())
    /// It tells the game "hey save my freakinge block alright?".
    @Override
    protected boolean place(ItemPlacementContext context, BlockState state) {
        boolean result = super.place(context, state);
        if (!result)return false;

        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        ItemStack itemStack = context.getStack();
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!world.isClient){
            if (blockEntity instanceof CrateBlockEntity targetBlockEntity){
                String crateId = itemStack.get(CrateItem.CRATE_ID);
                targetBlockEntity.setCrateId(crateId);
                targetBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 3);
            }
        } else {
            if (blockEntity instanceof CrateBlockEntity targetBlockEntity) {
                String crateId = itemStack.get(CrateItem.CRATE_ID);
                targetBlockEntity.setCrateId(crateId);
                world.updateListeners(pos, state, state, 3);
                world.scheduleBlockRerenderIfNeeded(pos, state, state);
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()){
            if (!stack.contains(CRATE_ID)){
                stack.set(CRATE_ID, "wooden");
                if (!stack.contains(CRATE_LOOT_ID)){
                    stack.set(CRATE_LOOT_ID, Vars.crateDataMap.get(stack.get(CRATE_ID)).lootTableId());
                }
            } else {
                if (!Vars.crateDataMap.containsKey(stack.get(CRATE_ID))){
                    stack.set(CRATE_ID, "wooden");
                    if (!stack.contains(CRATE_LOOT_ID)){
                        stack.set(CRATE_LOOT_ID, Vars.crateDataMap.get(stack.get(CRATE_ID)).lootTableId());
                    }
                }
            }

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}


