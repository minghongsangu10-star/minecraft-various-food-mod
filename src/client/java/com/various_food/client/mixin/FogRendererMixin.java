package com.various_food.client.mixin;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.various_food.client.MainClient;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    // 霧のカラー計算結果を保持するフィールドをShadowで取得
    @Shadow private static float fogRed;
    @Shadow private static float fogGreen;
    @Shadow private static float fogBlue;

    @Unique private static final float FOG_DISTANCE_MIN = 10.0f;
    @Unique private static final float FOG_DISTANCE_MAX = 80.0f;

    @Inject(
            method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V",
            at = @At("RETURN")
    )
    private static void onSetupFog(Camera camera, FogRenderer.FogMode fogMode, float viewDistance, boolean thickFog, float partialTick, CallbackInfo ci) {

        if (MainClient.isEffectActive) {

            float fogStart = Mth.lerp(MainClient.fogProgress.getValue(), FOG_DISTANCE_MIN, FOG_DISTANCE_MAX);

            // 霧の開始距離と終了距離（ブロック単位。小さくするほど視界が狭まる）
            RenderSystem.setShaderFogStart(fogStart);
            RenderSystem.setShaderFogEnd(fogStart * 2);

            // 霧の形状（CYLINDER または SPHERE）
            RenderSystem.setShaderFogShape(FogShape.CYLINDER);
        }
    }

    @Inject(
            method = "setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V",
            at = @At("RETURN")
    )
    private static void onSetupColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float bossColorModifier, CallbackInfo ci) {
        if (MainClient.isEffectActive) {
            fogRed = MainClient.fogRed.getValue();
            fogGreen = MainClient.fogGreen.getValue();
            fogBlue = MainClient.fogBlue.getValue();
            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0f);
        }
    }
}
