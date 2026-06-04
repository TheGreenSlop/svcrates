package dev.vissca.svcrates.datagen.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vissca.svcrates.statistic.ModStatistics;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import java.util.Optional;

/// Custom criterion for the advancements, basically a way of checking if the player meets a certain condition, giving them the
/// Advancement if it ends up being true.
public class GetCratesCriterion extends AbstractCriterion<GetCratesCriterion.Conditions> {

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    /// The trigger that checks if the thing I want is true or not.
    /// In this case checking how many crates a player has.
    public void trigger(ServerPlayerEntity player) {
        Stat<Identifier> stat = Stats.CUSTOM.getOrCreateStat(ModStatistics.FISH_UP_CRATE);

        super.trigger(player, conditions ->
                player.getStatHandler().getStat(stat) >= conditions.amount()
        );
    }

    /// This is for letting the datapack understand how my custom criteria works, since this later becomes part of a .json
    /// For the advancement.
    /// How it works is beyond me.
    public record Conditions(
            Optional<LootContextPredicate> playerPredicate,
            int amount) implements AbstractCriterion.Conditions {

        public static final Codec<Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                LootContextPredicate.CODEC.optionalFieldOf("player")
                                        .forGetter(Conditions::player),
                                Codec.INT.fieldOf("amount")
                                        .forGetter(Conditions::amount)
                        ).apply(instance, Conditions::new)
                );

        // To get the player. Me when I freaking get you.
        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

    }
}
