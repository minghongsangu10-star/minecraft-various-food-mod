package com.various_food.nbt;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class MyModComponent implements EntityComponentInitializer {

    /**
     * エンティティへのコンポーネント割り当てを設定する初期化クラス。
     * fabric.mod.json の "cardinal-components" エントリポイントに登録が必要です。
     */
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MyComponent.BODY_WEIGHT, BodyWeightComponentImpl::new, RespawnCopyStrategy.NEVER_COPY);
    }
}
