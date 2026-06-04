package dev.vissca.svcrates.system.networking;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.system.Vars;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CrateDataPayload(Vars.CrateData data, String name) implements CustomPayload {
    public static final     Identifier CRATE_DATA_PAYLOAD = Identifier.of(SvCrates.MOD_ID, "crate_data_payload");
    public static final CustomPayload.Id<CrateDataPayload> ID = new CustomPayload.Id<>(CRATE_DATA_PAYLOAD);

    public static final PacketCodec<RegistryByteBuf, CrateDataPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodec.tuple(
                            PacketCodecs.STRING, Vars.CrateData::lootTableId,
                            PacketCodecs.STRING.collect(PacketCodecs.toList()), Vars.CrateData::biomes,
                            PacketCodecs.STRING.collect(PacketCodecs.toList()), Vars.CrateData::textures,
                            PacketCodecs.STRING, Vars.CrateData::id,
                            PacketCodecs.INTEGER, Vars.CrateData::chance,
                            PacketCodecs.STRING.collect(PacketCodecs.toList()), Vars.CrateData::dimension,
                            Vars.CrateData::new
                    ),
                    CrateDataPayload::data,
                    PacketCodecs.STRING, CrateDataPayload::name,
                    CrateDataPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
