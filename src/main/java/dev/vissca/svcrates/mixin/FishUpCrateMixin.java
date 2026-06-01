package dev.vissca.svcrates.mixin;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.Vars;
import dev.vissca.svcrates.system.ModResourceReloadListener;
import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
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

		RegistryEntry<Biome> biome = getPlayerOwner().getWorld().getBiome(getPlayerOwner().getBlockPos());
		CratedUp myCrate = getFishingStack(biome.getIdAsString(), biome);
		if (Random.create().nextInt(4) == 0){
			return myCrate.stack;
		}
		return list;
	}

	@Unique
    public CratedUp getFishingStack(String biome, RegistryEntry<Biome> biomeLiteral){
		List<ItemStack> newStack = new ArrayList<>();

		if (Vars.biomeMap.containsKey(biome)) {
			return getFishedCrate(newStack, biome);
		} else {
			for (String possibleTag : Vars.biomeMap.keySet()) {
				if (possibleTag.startsWith("#") && possibleTag != "all") {
					String biomeTag = possibleTag.replaceFirst("#", "");
					TagKey<Biome> tag = TagKey.of(
							RegistryKeys.BIOME,
							Identifier.of(biomeTag)
					);
					if (biomeLiteral.isIn(tag)) {
						return getFishedCrate(newStack, possibleTag);
					}
				}
			}
		}
		return getFishedCrate(newStack, "all");
	}

	@Unique
    public CratedUp getFishedCrate(List<ItemStack> newStack, String targetCrateId){
		List<String> biomeMapData = Vars.biomeMap.get(targetCrateId);
		List<String> biomeMapAllData =  new ArrayList<>(Vars.biomeMap.get("all"));
		if (Objects.equals(targetCrateId, "all"))biomeMapAllData.clear();
		biomeMapAllData.addAll(biomeMapData);
		int crateWeight = 0;

		for (int weight = 0; weight < biomeMapAllData.size(); weight += 1){
			crateWeight += Vars.crateDataMap.get(biomeMapAllData.get(weight)).chance();
		}

		int weight = Random.create().nextInt(crateWeight);
		String crateId = crateRoll(biomeMapAllData, weight);

		newStack.add(new ItemStack(ModBlocks.CRATE_BLOCK.asItem()));
		newStack.getFirst().set(CrateItem.CRATE_LOOT_ID, getCrateLootJson(crateId));
		newStack.getFirst().set(CrateItem.CRATE_ID, getCrateIdJson(crateId));

		return new CratedUp(newStack, Vars.crateDataMap.get(crateId).chance());
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

	public record CratedUp(List<ItemStack> stack, Integer chance){ }
}