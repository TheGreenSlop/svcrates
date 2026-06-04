package dev.vissca.svcrates.block.entity.render;

import dev.vissca.svcrates.system.Vars;
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
public class CrateBlockEntityBakedModel implements UnbakedModel, BakedModel, FabricBakedModel {
    // Vars
    public static Map<String, List<Sprite>> sprites = new HashMap<>();

    public static void generateSprites() {
        for (int sId = 0; sId < Vars.crateSprites.size(); sId++) {
            // Local Vars
            String targetCrateId = Util.getCrateIdByInt(sId);

            if (!sprites.containsKey(targetCrateId) && Vars.crateSprites.containsKey(targetCrateId)) {
                //Temp Vars
                List<Sprite> tempList = new ArrayList<>();

                for (int csId = 0; csId < Vars.crateSprites.get(targetCrateId).size(); csId++) {
                    //Temp Vars
                    SpriteIdentifier newSprite = new SpriteIdentifier(
                            PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
                            Identifier.of(Vars.crateSprites.get(targetCrateId).get(csId)));
                    tempList.add(newSprite.getSprite());
                }
                sprites.put(targetCrateId, tempList);
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {return List.of();}

    @Override
    public boolean useAmbientOcclusion() { return false; }

    @Override
    public boolean hasDepth() { return false; }

    @Override
    public boolean isSideLit() { return false; }

    @Override
    public boolean isBuiltin() { return false; }

    /// Sets the particle to the wooden one by default, unsure how to get this to work for dynamic ones yet...
    @Override
    public Sprite getParticleSprite() {
        if (sprites.isEmpty() || !sprites.containsKey("wooden")) {
            return MinecraftClient.getInstance()
                    .getBlockRenderManager()
                    .getModel(Blocks.OAK_PLANKS.getDefaultState())
                    .getParticleSprite();
        }

        // Local Vars
        List<Sprite> spriteList = sprites.get("wooden");

        if (spriteList == null || spriteList.isEmpty()) {
            return MinecraftClient.getInstance()
                    .getBlockRenderManager()
                    .getModel(Blocks.OAK_PLANKS.getDefaultState())
                    .getParticleSprite();
        }

        return spriteList.getFirst();
    }
    /// Sets the block's transformations to match a generic Block and BlockItem
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
    public @Nullable BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter,
                                     ModelBakeSettings rotationContainer) {return this;}

    @Override
    public boolean isVanillaAdapter() {return false;}

    /// Puts the sprites on the faces of the block, was made to prevent duplicate code blocks.
    /// Bases it on targetCrateId (String) and sides (int)
    public static void insertSpritesOnMesh(QuadEmitter emitter, String targetCrateId, int sides) {
        // Local Vars
        List<Sprite> list = sprites.get(targetCrateId); if (list == null || list.isEmpty()) return;

        // For each direction apply a texture.
        for (Direction direction : Direction.values()) {
            // Temp Vars
            Sprite sprite;

            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);

            // If the crateDataMap.textures() in this context has exactly 6 entries or more it gives each face
            // A unique texture.
            if (sides >= 6) {
                int index = Math.min(direction.getId(), list.size() - 1);
                sprite = list.get(index);
            // Otherwise it just sets the faces like a log block using the first and last texture on the list.
            } else {
                sprite = (direction == Direction.UP || direction == Direction.DOWN)
                        ? list.getLast()
                        : list.getFirst();
            }

            emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos,
                               Supplier<Random> supplier, RenderContext renderContext) {

        // Local Vars
        QuadEmitter emitter = renderContext.getEmitter();
        BlockEntity blockEntity = blockRenderView.getBlockEntity(blockPos);
        String targetCrateId = "";

        generateSprites();

        if (blockEntity instanceof CrateBlockEntity crateBlockEntity) {
            String id = crateBlockEntity.getCrateId();
            if (id != null && Vars.crateDataMap.containsKey(id)) {
                targetCrateId = id;
            }
        }

        if (targetCrateId.isBlank()) return;
        if (!sprites.containsKey(targetCrateId)) return;

        int sideCount = sprites.get(targetCrateId).size();
        insertSpritesOnMesh(emitter, targetCrateId, sideCount);
    }

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
        // Local Vars
        QuadEmitter emitter = renderContext.getEmitter();
        String targetCrateId = itemStack.get(CrateItem.CRATE_ID);

        generateSprites();

        if (targetCrateId == null || !Vars.crateDataMap.containsKey(targetCrateId)) return;
        if (!sprites.containsKey(targetCrateId)) return;

        int sideCount = sprites.get(targetCrateId).size();
        insertSpritesOnMesh(emitter, targetCrateId, sideCount);
    }
}