package com.various_food.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.various_food.Main;
import com.various_food.entity.Hallucination;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HallucinationRenderer extends MobRenderer<Hallucination, EndermanModel<Hallucination>> {

    private static final float SCALE = 2.0F; //　倍率

    // テクスチャのパス
    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/entity/hallucination.png");

    public HallucinationRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanModel<>(context.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Hallucination entity) {
        return TEXTURE;
    }

    // ★描画倍率を指定（例: 2.0F で縦横高さが2倍）
    @Override
    protected void scale(Hallucination entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(SCALE, SCALE, SCALE);
    }
}
