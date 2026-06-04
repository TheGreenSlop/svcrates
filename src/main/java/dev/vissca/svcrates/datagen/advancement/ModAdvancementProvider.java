package dev.vissca.svcrates.datagen.advancement;

import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.datagen.advancement.criterion.GetCratesCriterion;
import dev.vissca.svcrates.advancement.criterion.ModCriteria;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/// Class that provides advancement stuff for the data gen.
public class ModAdvancementProvider extends FabricAdvancementProvider {
    // Variables
    AdvancementEntry FISHY_BUSINESS = new AdvancementEntry(
            Identifier.ofVanilla("husbandry/fishy_business"),
            null);
    AdvancementEntry CollectCrateOne;
    AdvancementEntry CollectCrateTwo;
    AdvancementEntry CollectCrateThree;
    AdvancementEntry CollectCrateFour;

    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    /// Helper method that streamlines adding these 4 advancements, because they are basically the same thing lol.
    public AdvancementEntry generateCrateAdvancementHelper(String iconId, String translationId, String id, AdvancementFrame frame,
                                               Consumer<AdvancementEntry> consumer, AdvancementEntry parent, int amount){
        // Local Vars
        ItemStack icon = new ItemStack(ModBlocks.CRATE_BLOCK);
        icon.set(CrateItem.CRATE_ID, iconId);

        return Advancement.Builder.create()
                .display(icon,
                        Text.translatable("advancement.svcrates."+translationId+".title"),
                        Text.translatable("advancement.svcrates."+translationId+".description"),
                        null, frame, true, true, false)
                .parent(parent)
                .criterion(translationId, ModCriteria.GET_CRATES.create(
                        new GetCratesCriterion.Conditions(Optional.empty(), amount)))
                .build(consumer, "svcrates:" + id);
    }

    /// Generates the advancements. Duh.
    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        CollectCrateOne = generateCrateAdvancementHelper(
                "wooden", "collect_crate_one", "collect_crates_one",
                AdvancementFrame.TASK, consumer, FISHY_BUSINESS, 1);

        CollectCrateTwo = generateCrateAdvancementHelper(
                "copper", "collect_crate_two", "collect_crates_two",
                AdvancementFrame.TASK, consumer, CollectCrateOne, 25);

        CollectCrateThree = generateCrateAdvancementHelper(
                "iron", "collect_crate_three", "collect_crates_three",
                AdvancementFrame.TASK, consumer, CollectCrateTwo, 50);

        CollectCrateFour = generateCrateAdvancementHelper(
                "diamond", "collect_crate_four", "collect_crates_four",
                AdvancementFrame.TASK, consumer, CollectCrateOne, 100);


    }
}
