package com.various_food.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 体重コンポーネントの具体実装クラス。
 * NBTタグを用いたデータの永続化と、ネットワーク同期を担当します。
 */
public class BodyWeightComponentImpl implements BodyWeightComponent{
    private int value = 0;
    private final Player player;

    /**
     * CCAがコンポーネントをアタッチする際に呼び出すコンストラクタ。
     *
     * @param player アタッチ対象のプレイヤーオブジェクト
     */
    public BodyWeightComponentImpl(Player player) {
        this.player = player;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void addValue(int amount) {
        this.value += amount;

        MyComponent.BODY_WEIGHT.sync(this.player);
    }

    @Override
    public void setValue(int amount) {
        this.value = amount;

        MyComponent.BODY_WEIGHT.sync(this.player);
    }

    /**
     * ワールド読み込み時やプレイヤー参加時に、NBTデータから値を復元します。
     */
    @Override public void readFromNbt(@NotNull CompoundTag tag) {
        this.value = tag.getInt("value");
    }

    /**
     * ワールド保存時やプレイヤーデータ保存時に、内部状態をNBTデータへ書き出します。
     */
    @Override public  void writeToNbt(@NotNull CompoundTag tag) {
        tag.putInt("value", this.value);
    }
}