package dev.vissca.svcrates.block.entity.render;

import dev.vissca.svcrates.Vars;
import dev.vissca.svcrates.item.custom.CrateItem;
import dev.vissca.svcrates.block.entity.custom.CrateBlockEntity;
import dev.vissca.svcrates.system.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;


@Environment(EnvType.CLIENT)
/// This is the class that allows me to dynamically render the crates!
public class CrateBlockEntityBakedModel implements UnbakedModel, BakedModel, FabricBakedModel {

    /// Map of Sprites attached to a string identifier, used for faces of the crates (CLIENT ONLY)
    public static Map<String, List<Sprite>> sprites = new HashMap<>();

    /// Gets a list of all sprites that are in the current datapack/respack! Needs some work it is not ideal...
    public static void generateSprites(){
        for (int sId = 0; sId < Vars.crateSprites.size(); sId = sId + 1){
            String targetCrate = Util.getCrateIdByInt(sId);

            if (!sprites.containsKey(targetCrate)){
                List<Sprite> tempList = new ArrayList<>();
                for (int csId = 0; csId < Vars.crateSprites.get(targetCrate).size(); csId = csId + 1){
                    SpriteIdentifier newSprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
                            Identifier.of(Vars.crateSprites.get(targetCrate).get(csId)));
                    tempList.add(newSprite.getSprite());
                }
                sprites.put(targetCrate, tempList);

            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {return List.of();}

    @Override
    public boolean useAmbientOcclusion() {return false;}

    @Override
    public boolean hasDepth() {return false;}

    @Override
    public boolean isSideLit() {return false;}

    @Override
    public boolean isBuiltin() {return false;}

    @Override
    /// Sets the particle to the default wooden crate one, not sure how to improve it...
    /// Returns oak planks texture as a fallback, close enough.
    public Sprite getParticleSprite() {
        if (sprites.isEmpty()){
            return MinecraftClient.getInstance().getBlockRenderManager()
                    .getModel(Blocks.OAK_PLANKS.getDefaultState()).getParticleSprite();
        }
        return sprites.get("wooden").getFirst();
    }

    /// This sets the block and item rendering to be the same as any other block's!
    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public ModelOverrideList getOverrides() {return ModelOverrideList.EMPTY;}

    @Override
    public Collection<Identifier> getModelDependencies() {return List.of();}

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {}

    @Override
    public @Nullable BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {return this;}

    @Override
    public boolean isVanillaAdapter() {return false;}

    public static void insertSpritesOnMesh(QuadEmitter emitter, String targetCrate, Integer sides){
        for(Direction direction : Direction.values()) {
            if (sides >= 6) {
                emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                emitter.spriteBake(sprites.get(targetCrate).get(direction.getId()), MutableQuadView.BAKE_LOCK_UV);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            } else {
                emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                if (direction == Direction.UP || direction == Direction.DOWN){
                    emitter.spriteBake(sprites.get(targetCrate).getLast(), MutableQuadView.BAKE_LOCK_UV);
                } else {
                    emitter.spriteBake(sprites.get(targetCrate).getFirst(), MutableQuadView.BAKE_LOCK_UV);
                }
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
        }
    }

    /// Renders the block, the item will use a similar method.
    /// Kind of sloppy but if sprites is somehow empty the block is invisible, better than a crash right?
    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
        generateSprites();
        if (sprites == null) return;

        QuadEmitter emitter = renderContext.getEmitter();
        int targetId = 0;

        BlockEntity blockEntity = blockRenderView.getBlockEntity(blockPos);
        if (blockEntity instanceof CrateBlockEntity crateBlockEntity){
            String id = crateBlockEntity.getCrateId();
            if (id == null) return;
            for (int cId = 0; cId < Vars.crateDataMap.size(); cId = cId + 1){
                String targetCrate = Util.getCrateIdByInt(cId);
                if (Objects.equals(targetCrate, id)){
                    targetId = cId;
                    break;
                }
            }
        }

        String targetCrate = Vars.crateDataMap.keySet().stream().toList().get(targetId);

        if (sprites.get(targetCrate) == null) return;
        Integer sideCount = sprites.get(targetCrate).size();
        insertSpritesOnMesh(emitter, targetCrate, sideCount);
    }

    /// Basically the same as the above, typing this here for consistency
    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
        generateSprites();
        if (sprites == null) return;

        QuadEmitter emitter = renderContext.getEmitter();
        int targetId = 0;

        String id = itemStack.get(CrateItem.CRATE_ID);
        if (id == null) return;
        for (int cId = 0; cId < Vars.crateDataMap.size(); cId = cId + 1){
            String targetCrate = Util.getCrateIdByInt(cId);
            if (Objects.equals(targetCrate, id)){
                targetId = cId;
                break;
            }
        }
        String targetCrate = Vars.crateDataMap.keySet().stream().toList().get(targetId);
        if (sprites.get(targetCrate) == null) return;
        Integer sideCount = sprites.get(targetCrate).size();
        insertSpritesOnMesh(emitter, targetCrate, sideCount);
    }
}