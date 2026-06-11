package dev.vissca.svcrates.system.networking;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.system.Vars;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestCrateDataPayload() implements CustomPayload {
    public static final     Identifier CRATE_DATA_PAYLOAD = Identifier.of(SvCrates.MOD_ID, "request_crate_data_payload");
    public static final Id<RequestCrateDataPayload> ID = new Id<>(CRATE_DATA_PAYLOAD);

    public static final PacketCodec<RegistryByteBuf, RequestCrateDataPayload> CODEC =
            PacketCodec.unit(new RequestCrateDataPayload());


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
