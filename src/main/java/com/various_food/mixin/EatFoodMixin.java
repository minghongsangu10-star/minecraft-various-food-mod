package com.various_food.mixin;

import com.various_food.Main;
import com.various_food.nbt.FoodWeightManager;
import com.various_food.nbt.MyComponent;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@link Item} クラスに対する Mixin。
 * プレイヤーがアイテム（食べ物やポーションなど）の使用を終えた際の処理を割り込ませます。
 * Target: Minecraft 1.20.1 (Fabric / Mojang Mapping)
 */
@Mixin(Item.class)
public abstract class EatFoodMixin {

    /**
     * Item#finishUsingItem メソッドの実行直前（@At("HEAD")）に処理を割り込ませます。
     *
     * @param stack 使用されたアイテムスタック
     * @param level 実行対象のワールド (サーバー / クライアント)
     * @param user  アイテムを使用したエンティティ
     * @param cir   戻り値 (ItemStack) の制御用コールバック情報
     */
    @Inject(
            method = "finishUsingItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD")
    )
    private void onFinishUsingItem(ItemStack stack, Level level, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        // 処理をサーバーサイドに限定し、かつ使用者をプレイヤー(ServerPlayer)に絞り込む
        if (!level.isClientSide && user instanceof ServerPlayer player) {

            // 1. アイテム設定から増加させる重量値を取得
            int value = getWeightValue(stack);

            // 2. 重量値が設定されている場合のみ、プレイヤーのコンポーネントデータを更新
            if (value != 0) {
                addValue(player, value);
            }

            // 3. HUD表示更新用のネットワークパケットを作成・送信
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(100); // クライアント側に渡すデータ: HUDの表示保持時間 (100 ticks = 5秒)

            // 対象プレイヤーのクライアントへパケットを送信
            ServerPlayNetworking.send(player, Main.HUD_SPAWN_PACKET_ID, buf);
        }
    }

    /**
     * アイテムから対応する重量値を取得するヘルパーメソッド。
     *
     * @param stack 対象のアイテムスタック
     * @return アイテム固有の重量増加値
     */
    @Unique
    private int getWeightValue(ItemStack stack) {
        return FoodWeightManager.getWeightValue(stack.getItem());
    }

    /**
     * プレイヤーの体重データを加算し、クライアントへ同期するヘルパーメソッド。
     *
     * @param player 加算対象のプレイヤー
     * @param amount 加算する数値
     */
    @Unique
    private void addValue(Player player, int amount) {
        // Cardinal Components API を介してプレイヤーのデータを取得・更新
        MyComponent.BODY_WEIGHT.get(player).addValue(amount);

        // 更新されたデータをサーバーからクライアントへ同期
        MyComponent.BODY_WEIGHT.sync(player);
    }
}