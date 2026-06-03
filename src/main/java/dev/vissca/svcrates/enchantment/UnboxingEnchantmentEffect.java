package dev.vissca.svcrates.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record UnboxingEnchantmentEffect(EnchantmentLevelBasedValue level) implements EnchantmentEntityEffect {
    public static final MapCodec<UnboxingEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("level").forGetter(UnboxingEnchantmentEffect::level)
            ).apply(instance, UnboxingEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return null;
    }
}
