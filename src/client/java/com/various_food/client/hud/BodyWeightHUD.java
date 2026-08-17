package com.various_food.client.hud;

import com.various_food.nbt.MyComponent;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * プレイヤーの体重データを画面上（HUD）に描画するためのクラス。
 * Fabricの HudRenderCallback を実装して描画イベントを受け取ります。
 */
public class BodyWeightHUD implements HudRenderCallback {

    @Override
    public void onHudRender(GuiGraphics guiGraphics, float partialTick) {
        // Minecraftのクライアントインスタンスを取得
        Minecraft client = Minecraft.getInstance();

        // プレイヤーが存在しない（ワールド未読み込み時など）、またはF1キー等でHUDが非表示の場合は処理を中断
        if (client.player == null || client.options.hideGui) {
            return;
        }

        // HUDを描画する画面上の位置（ピクセル座標: 左上原点）
        int x = 10;
        int y = 20;

        // Cardinal Components APIなどを介してプレイヤーから体重コンポーネントを取得
        MyComponent.BODY_WEIGHT.maybeGet(client.player).ifPresent(component -> {
            try {
                // コンポーネントから現在の体重値（数値）を取得
                double weightValue = component.getValue();

                String weightStep = "Normal";

                // 体重の値に応じてテキストの色を変更（デフォルト: 緑）
                ChatFormatting color = ChatFormatting.GREEN;
                if (weightValue >= 100 && weightValue < 200) {

                    color = ChatFormatting.YELLOW; // 100〜199: 黄色
                    weightStep = "Moderate";
                } else if (weightValue >= 200) {

                    color = ChatFormatting.DARK_RED; // 200以上: 濃い赤
                    weightStep = "Severe";
                }

                // 表示用のチャットテキスト（Component）を構築
                // 例: "Body Weight: 120.5"（"Body Weight: "は白、数値部分は動的な色）
                Component fullText = Component.literal("Body Weight: ").withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(String.format("%.1f", weightValue)).withStyle(color)
                                .append(" " + weightStep).withStyle(color));

                // 画面上にテキストを描画
                // 第3引数: X座標, 第4引数: Y座標, 第5引数: デフォルトのカラーコード（ARGB: 0xFFFFFFFF = 不透明な白）
                guiGraphics.drawString(client.font, fullText, x, y, 0xFFFFFFFF);

            } catch (Exception e) {
                // 例外発生時はゲームをクラッシュさせずに無視（ログを出力したい場合は e.printStackTrace() などを追加）
            }
        });
    }
}