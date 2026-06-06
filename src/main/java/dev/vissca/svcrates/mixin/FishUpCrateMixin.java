package dev.vissca.svcrates.mixin;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.enchantment.ModEnchantments;
import dev.vissca.svcrates.system.Vars;
import dev.vissca.svcrates.advancement.criterion.ModCriteria;
import dev.vissca.svcrates.statistic.ModStatistics;
import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// I'm shocked I didn't learn about these before
/// Mixins let you add, replace and modify vanilla code!
/// Delicious...
@Mixin(FishingBobberEntity.class)
public abstract class FishUpCrateMixin { // Modifying the output Loot from when you like, hook smth at a chance.
	@Shadow
	@Nullable
	public abstract PlayerEntity getPlayerOwner();

	@ModifyVariable(method = "use", at = @At(value = "STORE"), ordinal = 0)
	private List<ItemStack> hookLoot(List<ItemStack> list) {
		Objects.requireNonNull(getPlayerOwner()).getWorld().getBiome(getPlayerOwner().getBlockPos());
		RegistryEntry<Enchantment> unboxing = getPlayerOwner().getWorld().getRegistryManager()
				.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
				.getOrThrow(ModEnchantments.UNBOXING);
		int level = EnchantmentHelper.getLevel(unboxing, getPlayerOwner().getInventory().getMainHandStack());

		RegistryEntry<Biome> biome = getPlayerOwner().getWorld().getBiome(getPlayerOwner().getBlockPos());
		String dimension = getPlayerOwner().getWorld().getRegistryKey().getValue().toString();
		Vars.CratedUp myCrate = getFishingStack(biome.getIdAsString(), biome, dimension);
		if ((Random.create().nextInt(SvCrates.CONFIG.crateChance - level)) == 0){
			getPlayerOwner().increaseStat(ModStatistics.FISH_UP_CRATE, 1);
			ModCriteria.GET_CRATES.trigger((ServerPlayerEntity) getPlayerOwner());
			return myCrate.stack();
		}
		return list;
	}

	@Unique
    public Vars.CratedUp getFishingStack(String biome, RegistryEntry<Biome> biomeLiteral, String dimension){
		List<ItemStack> newStack = new ArrayList<>();

		if (Vars.biomeMap.containsKey(biome)) {
			return getFishedCrate(newStack, biome, dimension);
		} else {
			for (String possibleTag : Vars.biomeMap.keySet()) {
				if (possibleTag.startsWith("#")) {
					String biomeTag = possibleTag.replaceFirst("#", "");
					TagKey<Biome> tag = TagKey.of(
							RegistryKeys.BIOME,
							Identifier.of(biomeTag)
					);
					if (biomeLiteral.isIn(tag)) {
						return getFishedCrate(newStack, possibleTag, dimension);
					}
				}
			}
		}
		return getFishedCrate(newStack, "*", dimension);
	}

	@Unique
	public List<String> dimensionCheck(String dimension, List<String> crateList){
		List<String> allowedCrates =  new ArrayList<>();
            for (String curCrateId : crateList) {
				if (Vars.dimensionMap.get(dimension).contains(curCrateId)|| Vars.dimensionMap.get("*").contains(curCrateId)) {
					if (!allowedCrates.contains(curCrateId)) {
						allowedCrates.add(curCrateId);
					}
				}
            }
        return allowedCrates;
    }

	@Unique
    public Vars.CratedUp getFishedCrate(List<ItemStack> newStack, String targetCrateId, String dimension) {
		List<String> biomeMapData = new ArrayList<>(Vars.biomeMap.get(targetCrateId));
		List<String> biomeMapAllData =  new ArrayList<>(Vars.biomeMap.get("*"));
		if (Objects.equals(targetCrateId, "*"))biomeMapAllData.clear();

		biomeMapAllData.addAll(biomeMapData);
		biomeMapAllData = dimensionCheck(dimension, biomeMapAllData);

		int crateWeight = 0;
        for (String bData : biomeMapAllData) {
            crateWeight += Vars.crateDataMap.get(bData).chance();
        }
		int weight = Random.create().nextInt(crateWeight);
		String crateId = crateRoll(biomeMapAllData, weight);

		newStack.add(new ItemStack(ModBlocks.CRATE_BLOCK.asItem()));
		newStack.getFirst().set(CrateItem.CRATE_LOOT_ID, getCrateLootJson(crateId));
		newStack.getFirst().set(CrateItem.CRATE_ID, getCrateIdJson(crateId));

		return new Vars.CratedUp(newStack, Vars.crateDataMap.get(crateId).chance());
	}

	@Unique
    public final String crateRoll(List<String> biomeMapAllData, int roll){
		for (String crateId : biomeMapAllData) {
			roll -= Vars.crateDataMap.get(crateId).chance();
			if (roll < 0) {
				return crateId;
			}
		}
		return "wooden";
	}

	@Unique
    public final String getCrateLootJson(String crateId){
		return Vars.crateDataMap.get(crateId).lootTableId();
	}
	@Unique
    public final String getCrateIdJson(String crateId){
		return Vars.crateDataMap.get(crateId).id();
	}


}