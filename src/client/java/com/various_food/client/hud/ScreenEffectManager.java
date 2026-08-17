package com.various_food.client.hud;

import com.various_food.client.mixin.GameRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ScreenEffectManager {

    /**
     * 画面にポストエフェクト（シェーダー）を適用します。
     * @param shaderLocation 適用したいシェーダーJSONのResourceLocation
     */
    public static void applyShader(ResourceLocation shaderLocation) {
        Minecraft mc = Minecraft.getInstance();
        // クライアントのスレッドで実行
        mc.execute(() -> {
            ((GameRendererAccessor) mc.gameRenderer).invokeLoadEffect(shaderLocation);
        });
    }

    /**
     * 適用中のエフェクトをクリアして元の画面に戻します。
     */
    public static void clearShader() {
        Minecraft mc = Minecraft.getInstance();
        ((GameRendererAccessor) mc.gameRenderer).invokeShutdownEffect();
    }
}
