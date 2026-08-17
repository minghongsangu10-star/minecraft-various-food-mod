package com.various_food.nbt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 食品の重量（Weight）設定をJSONから読み込み、管理するクラス
 */
public class FoodWeightManager {
    // ログ出力用のロガー
    private static final Logger LOGGER = LoggerFactory.getLogger("various_food");

    // アイテム（Item）と重量値（Integer）の紐付けを保持するマップ
    private static final Map<Item, Integer> WEIGHT_MAP = new HashMap<>();

    /**
     * JSONファイルから重量データを読み込み、WEIGHT_MAPに展開するメソッド
     */
    public static void load() {
        // assets/various_foods/data/food_weights/values.json をInputStreamとして取得
        try (InputStream is = FoodWeightManager.class.getResourceAsStream("/assets/various_food/data/food_weight/values.json")) {

            // ファイルが存在しない場合のエラーハンドリング
            if (is == null) {
                LOGGER.warn("Weight config file not found!");
                return;
            }

            // JSONファイルの解析（パース）
            JsonObject json = new Gson().fromJson(new InputStreamReader(is), JsonObject.class);
            JsonObject weights = json.getAsJsonObject("weights");

            // マップをリセット（再読み込みに対応するため）
            WEIGHT_MAP.clear();

            // JSONの"weights"オブジェクト内にあるすべてのキー（"minecraft:apple"など）をループ処理
            for (String key : weights.keySet()) {
                // 文字列キーをResourceLocationオブジェクトに変換
                ResourceLocation location = new ResourceLocation(key);

                // BuiltInRegistries.ITEM（1.20.1のアイテムレジストリ）からアイテムを取得
                BuiltInRegistries.ITEM.getOptional(location).ifPresentOrElse(item -> {
                            // レジストリに存在し、AIR（空気）でない場合のみマップに登録
                            if (item != Items.AIR) {
                                int value = weights.get(key).getAsInt();
                                WEIGHT_MAP.put(item, value);
                            }
                        }, () ->
                                // IDが存在しない（未登録のModアイテムやタイポ）場合はエラーログを出力
                                LOGGER.error("Unknown item ID in config: {}", key)
                );
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load food weight config", e);
        }
    }

    /**
     * 指定されたアイテムの重量値を取得するメソッド
     *
     * @param item 対象のアイテム
     * @return 重量値（マップ未登録の場合は 0 を返す）
     */
    public static int getWeightValue(Item item) {
        return WEIGHT_MAP.getOrDefault(item, 0);
    }
}