package com.various_food.nbt;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * プレイヤーの体重データを管理するコンポーネントインターフェース。
 * - Component: NBT保存・読み込みの基礎機能を提供
 * - AutoSyncedComponent: サーバー・クライアント間の自動データ同期機能を提供
 */
public interface BodyWeightComponent extends Component, AutoSyncedComponent {

    /**
     * 現在の体重値を取得します。
     *
     * @return 現在の体重値
     */
    int getValue();

    /**
     * 体重値を加算（または減算）し、クライアントへ変更を同期します。
     *
     * @param amount 加算する値（負の値を渡すと減算）
     */
    void addValue(int amount);

    /**
     * 体重値を設定し、クライアントへ変更を同期します。
     *
     * @param amount 加算する値（負の値を渡すと減算）
     */
    void setValue(int amount);
}
