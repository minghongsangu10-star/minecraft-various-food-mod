package com.various_food;

import com.various_food.entity.Hallucination;
import com.various_food.sound.SpawnHallucinationSound;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnHallucination {

    /**
     * 指定された位置に Hallucination エンティティをスポーンさせます。
     *
     * @param level スポーン先のサーバーワールド
     * @param pos   スポーン基準位置
     */
    public void spawnHallucination(ServerLevel level, BlockPos pos, ServerPlayer player) {
        // 1. エンティティの生成
        Hallucination hallucination = Main.HALLUCINATION_ENTITY_TYPE.create(level);
        if (hallucination == null) {
            return;
        }

        // 2. スポーン位置と回転角度の設定（ブロックの中心 + 0.5 に配置）
        hallucination.moveTo(
                pos.getX() + 0.5D,
                pos.getY(),
                pos.getZ() + 0.5D,
                0.0F,
                0.0F
        );

        // 3. エンティティの初期化（装備、属性、NBTデータ等の適用）
        hallucination.finalizeSpawn(
                level,
                level.getCurrentDifficultyAt(pos),
                MobSpawnType.EVENT,
                null,
                null
        );

        new EffectManager().addEffect(hallucination, MobEffects.GLOWING, 80);

        // 4. ワールドにエンティティを追加
        level.addFreshEntity(hallucination);

        String text = "I'm losing my mind. I'm seeing things.";
        player.displayClientMessage(Component.literal(text), false);

        level.playSound(
                null,                             // 対象外にするプレイヤー（nullで全員）
                player.getX(), player.getY(), player.getZ(),// 再生位置 (double または BlockPos)
                SpawnHallucinationSound.SPAWN_HALLUCINATION_SOUND,// SoundEvent
                SoundSource.MASTER,              // 音のカテゴリ (SoundCategory)
                100.0F,                             // 音量
                1.0F                              // ピッチ
        );
    }

    /**
     * プレイヤーを中心とした指定の半径範囲内で、地表（最高地点）のランダムな座標を取得します。
     *
     * @param minRadius 最小半径（ブロック数）
     * @param maxRadius 最大半径（ブロック数）
     * @param player    基準となるプレイヤー
     * @return 地表の BlockPos
     */
    public static BlockPos getARandomPosOnTheGround(int minRadius, int maxRadius, Player player) {
        RandomSource random = player.getRandom();

        // 指定された最小・最大半径の間でランダムな距離を算出
        double distance = minRadius + random.nextInt(Math.max(1, maxRadius - minRadius));

        // 0 ～ 2π（360度）のランダムな角度（ラジアン）を取得
        double angle = random.nextDouble() * 2.0D * Math.PI;

        // X, Z 座標のオフセット計算
        int x = Mth.floor(player.getX() + distance * Math.cos(angle));
        int z = Mth.floor(player.getZ() + distance * Math.sin(angle));

        // 指定位置の地表のY座標を取得
        int y = player.level().getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

        return new BlockPos(x, y, z);
    }
}