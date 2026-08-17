package com.various_food.nbt;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * Cardinal Components API (CCA) のコンポーネントキーを登録・管理するクラス。
 * 1.20.1 Fabric / Mojang mapping 環境用。
 */
public class MyComponent {

    /**
     * 体重データを保持するコンポーネントへのアクセスキー。
     * - "various_food:body_weight" という識別子（ResourceLocation）でコンポーネントを登録・参照します。
     * - 対象のエンティティやプレイヤーから BodyWeightComponent のインスタンスを取得する際に使用します。
     */
    public static final ComponentKey<BodyWeightComponent> BODY_WEIGHT =
            ComponentRegistry.getOrCreate(
                    new ResourceLocation("various_food", "body_weight"),
                    BodyWeightComponent.class
            );
}
