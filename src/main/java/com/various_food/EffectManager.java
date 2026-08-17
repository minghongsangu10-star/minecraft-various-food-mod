package com.various_food;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectManager {
    public void addEffect(LivingEntity entity, MobEffect mobEffect, int time) {
        // サーバー側かつエンティティが存在する場合のみ処理
        if (entity != null && !entity.level().isClientSide) {

            // 移動速度上昇 (SPEED) のエフェクトを作成
            MobEffectInstance effectInstance = new MobEffectInstance(
                    mobEffect,                // エフェクトの種類
                    time,                      // 効果時間（200 ticks = 10秒）
                    0,                        // レベル（0 = Lv1, 1 = Lv2 ...）
                    false,                    // ambient（環境効果の有無）
                    false,                     // visible（パーティクル表示）
                    false                      // showIcon（右上のアイコン表示）
            );

            // エンティティにエフェクトを付与
            entity.addEffect(effectInstance);
        }
    }
}
