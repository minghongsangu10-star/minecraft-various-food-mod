package com.various_food;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class SpeedChanger {

    private static final UUID WEIGHT_PENALTY_UUID = UUID.fromString("7f36916c-9742-4545-9853-66236371c667");

    /**
     * プレイヤーの移動速度モディファイアを更新・着脱するヘルパーメソッド
     *
     * @param amount 適用する速度補正の割合（例: -0.5f で 50% 低下）
     * @param speedInstance プレイヤーの移動速度属性インスタンス
     */
    public void change(float amount, AttributeInstance speedInstance) {
        // 既存の体重ペナルティ用モディファイアを取得
        AttributeModifier currentModifier = speedInstance.getModifier(WEIGHT_PENALTY_UUID);

        if (currentModifier != null) {
            // ペナルティが解除された（amount == 0）か、値が変わった場合は一旦削除する
            if (amount == 0.0f || currentModifier.getAmount() != (double) amount) {
                speedInstance.removeModifier(WEIGHT_PENALTY_UUID);
            } else {
                // すでに正しい値のモディファイアが適用されている場合は何もしない
                return;
            }
        }

        // ペナルティ値が存在する場合、新たに一時的なモディファイア（ログアウトや死で消滅しない属性値）を追加
        if (amount != 0.0f) {
            speedInstance.addTransientModifier(new AttributeModifier(
                    WEIGHT_PENALTY_UUID,
                    "Body Weight Penalty",
                    amount,
                    AttributeModifier.Operation.MULTIPLY_BASE // 基本移動速度に対して乗算
            ));
        }
    }
}
