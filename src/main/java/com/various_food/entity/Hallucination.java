package com.various_food.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Hallucination extends PathfinderMob {

    public static final float HITBOX_HEIGHT = 2.9F * 2;
    public static final float HITBOX_WIGHT = 0.6F * 2;

    public Hallucination(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    // AI（ゴールの登録）
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this)); // 水に浮く
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false)); // ターゲットへ接近して近接攻撃を行う
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D)); // 排他・徘徊
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F)); // プレイヤーを見つめる

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10,false,false,null));
    }

    @Override
    public void tick() {
        super.tick();

        // サーバー側かつターゲットがいない（または死んでいる）場合、一番近いプレイヤーを強制セット
        if (!this.level().isClientSide) {
            if (this.getTarget() == null || !this.getTarget().isAlive()) {
                Player nearestPlayer = this.level().getNearestPlayer(this, 128.0D); // 2048ブロック以内の最寄りプレイヤー
                if (nearestPlayer != null && !nearestPlayer.isCreative() && !nearestPlayer.isSpectator()) {
                    this.setTarget(nearestPlayer);
                }
            }
        }
    }

    // 基本ステータスの設定
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)       // 体力 20 (ハート10個分)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)   // 移動速度
                .add(Attributes.ATTACK_DAMAGE, 3.0D)    // 攻撃力
                .add(Attributes.FOLLOW_RANGE,2048.0D);   // 索敵距離
    }
}
