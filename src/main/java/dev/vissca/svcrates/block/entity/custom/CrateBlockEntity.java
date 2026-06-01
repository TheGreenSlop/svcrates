package dev.vissca.svcrates.block.entity.custom;

import dev.vissca.svcrates.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CrateBlockEntity extends BlockEntity {
    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRATE_BLOCK_ENTITY, pos, state);
    }
    public static final String CRATE_ID_STRING = "CrateId";
    private String crateId = "wooden";

    /// Self explanatory.
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.crateId = nbt.getString(CRATE_ID_STRING);
        if (getWorld() != null) { // This tells the game to actually update the nbt, otherwise the textures break.
            getWorld().updateListeners(pos, getCachedState(), getCachedState(), 3);
            getWorld().scheduleBlockRerenderIfNeeded(pos, getCachedState(), getCachedState());
        }
    }

    /// Also self-explanatory.
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putString(CRATE_ID_STRING, crateId);
    }

    /// Setter for crateId. I'm not sure why I can't just reference 'crateId' but y'know.
    /// Probs some jank on how item data works?
    public void setCrateId(String crateId) {
        this.crateId = crateId;
    }
    /// Getter for crateId, same reason as above.
    public String getCrateId() {
        return this.crateId;
    }

    /// This is to make sure that the client's nbt matches the server's nbt, whenever that changes.
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    /// Also send data to client BUT It's for when the client first gets into a chunk with this data
    /// I think... IDK ask Reddit I just copied this from there.
    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }
}