package dev.vissca.svcrates.datagen.advancement;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    AdvancementEntry FISHY_BUSINESS = new AdvancementEntry(
            Identifier.ofVanilla("husbandry/fishy_business"),
            null
    );

    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        ItemStack wooden_icon = new ItemStack(ModBlocks.CRATE_BLOCK);
        wooden_icon.set(CrateItem.CRATE_ID, "wooden");
        AdvancementEntry GET_WOODEN_CRATE = Advancement.Builder.create()
                .display(wooden_icon,
                        Text.translatable("advancement.svcrates.get_wooden_crate.title"),
                        Text.translatable("advancement.svcrates.get_wooden_crate.description"),
                                null, AdvancementFrame.TASK, true, true, false)
                .parent(FISHY_BUSINESS)
                .criterion("collect_wooden_crate", InventoryChangedCriterion.Conditions.items(
                        ItemPredicate.Builder.create().items(
                                ModBlocks.CRATE_BLOCK.asItem())
                                .component(ComponentPredicate.builder().add(CrateItem.CRATE_ID, "wooden").build())))
                .build(consumer, SvCrates.MOD_ID + "/get_wooden_crate");

        ItemStack copper_icon = new ItemStack(ModBlocks.CRATE_BLOCK);
        copper_icon.set(CrateItem.CRATE_ID, "copper");
        AdvancementEntry GET_COPPER_CRATE = Advancement.Builder.create()
                .display(copper_icon,
                        Text.translatable("advancement.svcrates.get_copper_crate.title"),
                        Text.translatable("advancement.svcrates.get_copper_crate.description"),
                        null, AdvancementFrame.TASK, true, true, false)
                .parent(GET_WOODEN_CRATE)
                .criterion("collect_copper_crate", InventoryChangedCriterion.Conditions.items(
                        ItemPredicate.Builder.create().items(
                                        ModBlocks.CRATE_BLOCK.asItem())
                                .component(ComponentPredicate.builder().add(CrateItem.CRATE_ID, "copper").build())))
                .build(consumer, SvCrates.MOD_ID + "/get_copper_crate");

        ItemStack iron_icon = new ItemStack(ModBlocks.CRATE_BLOCK);
        iron_icon.set(CrateItem.CRATE_ID, "iron");
        AdvancementEntry GET_IRON_CRATE = Advancement.Builder.create()
                .display(iron_icon,
                        Text.translatable("advancement.svcrates.get_iron_crate.title"),
                        Text.translatable("advancement.svcrates.get_iron_crate.description"),
                        null, AdvancementFrame.TASK, true, true, false)
                .parent(GET_COPPER_CRATE)
                .criterion("collect_iron_crate", InventoryChangedCriterion.Conditions.items(
                        ItemPredicate.Builder.create().items(
                                        ModBlocks.CRATE_BLOCK.asItem())
                                .component(ComponentPredicate.builder().add(CrateItem.CRATE_ID, "iron").build())))
                .build(consumer, SvCrates.MOD_ID + "/get_iron_crate");

        ItemStack diamond_icon = new ItemStack(ModBlocks.CRATE_BLOCK);
        diamond_icon.set(CrateItem.CRATE_ID, "diamond");
        AdvancementEntry GET_DIAMOND_CRATE = Advancement.Builder.create()
                .display(diamond_icon,
                        Text.translatable("advancement.svcrates.get_diamond_crate.title"),
                        Text.translatable("advancement.svcrates.get_diamond_crate.description"),
                        null, AdvancementFrame.TASK, true, true, false)
                .parent(GET_IRON_CRATE)
                .criterion("collect_diamond_crate", InventoryChangedCriterion.Conditions.items(
                        ItemPredicate.Builder.create().items(
                                        ModBlocks.CRATE_BLOCK.asItem())
                                .component(ComponentPredicate.builder().add(CrateItem.CRATE_ID, "diamond").build())))
                .build(consumer, SvCrates.MOD_ID + "/get_diamond_crate");
    }
}
