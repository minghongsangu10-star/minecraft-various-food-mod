package com.various_food.client;

import com.various_food.Main;
import com.various_food.ModThings;
import com.various_food.client.entity.renderer.HallucinationRenderer;
import com.various_food.client.hud.BodyWeightHUD;
import com.various_food.client.hud.HUDTimerManager;
import com.various_food.nbt.FoodWeightManager;
import com.various_food.nbt.MyComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * クライアントサイド専用のエントリポイントクラス。
 * HUDの描画、ネットワークパケットの受信、UI系のイベントコールバックなどを登録します。
 */
public class MainClient implements ClientModInitializer {

	public static boolean isEffectActive = false;

	// 霧からプレイヤーまでの距離を動的に変更するための進行度 (0.0F~1.0F, 大きいほど遠い)
	public static PingPongValue fogProgress = new PingPongValue(1.0F);

	//ガンマ値(0.0F=暗い, 1.0F=明るい, 10.0F=暗視状態)
	private static PingPongValue gamma = new PingPongValue(1.0F);

	//霧の色
	public static PingPongValue fogRed = new PingPongValue(0.5F);
	public static PingPongValue fogGreen = new PingPongValue(0.05F);
	public static PingPongValue fogBlue = new PingPongValue(0.05F);

	@Override
	public void onInitializeClient() {

		// 1. HUD描画機能の登録
		// 体重表示用HUDのレンダリング処理を登録
		HudRenderCallback.EVENT.register(new BodyWeightHUD());

		BlockRenderLayerMap.INSTANCE.putBlock(ModThings.CROP_BLOCK_RICE, RenderType.cutout());

		// 2. クライアント側の毎ティック更新処理
		ClientTickEvents.END_CLIENT_TICK.register(this::clientTick);

		// 3. サーバーからのネットワークパケット受信
		// HUD表示要求パケットを受け取ったときの処理
		ClientPlayNetworking.registerGlobalReceiver(Main.HUD_SPAWN_PACKET_ID, (client, handler, buf, responseSender) -> {
			// パケットから表示時間（ティック数など）を読み取る
			int duration = buf.readInt();

			// 描画スレッド（メインスレッド）でタイマーを開始させる
			client.execute(() -> {
				HUDTimerManager.show(duration);
			});
		});

		// 4. アイテムツールチップのカスタマイズ
		// アイテム説明欄の1行目（アイテム名）の末尾に任意の文字を追加する
		ItemTooltipCallback.EVENT.register(this::itemToolTip);

		EntityRendererRegistry.register(Main.HALLUCINATION_ENTITY_TYPE, HallucinationRenderer::new);
	}

	/**
	 * 毎Tick呼び出される処理
	 * body_weightの値によって変更されるエフェクトの管理
	 *
	 * @param client クライアント
	 */
	private void clientTick(Minecraft client){

		if (client.player == null || client.level == null) {
			// ワールドから離脱した場合は状態をリセット
			isEffectActive = false;
			fogProgress.setValue(1.0F);
			gamma.setValue(1.0F);

			fogRed.setValue(0.5F);
			fogGreen.setValue(0.05F);
			fogBlue.setValue(0.05F);

			return;
		}

		HUDTimerManager.tick();

		int weight = MyComponent.BODY_WEIGHT.get(client.player).getValue();
		if (weight >= 300) {

			fogProgress.update(0.02F);
			fogRed.update(0.05F);
			fogGreen.update(0.07F);
			fogBlue.update(0.09F);
			gamma.update(0.05F);

			client.options.gamma().set((double) gamma.getValue());

			isEffectActive = true;
		}
		else {
			isEffectActive = false;
		}
	}

	/**
	 * 食べ物のアイテム名にbody_weightの増減幅を記載する
	 *
	 * @param stack ItemStack
	 * @param context TooltipFlag
	 * @param lines List<Component>
	 */
	private void itemToolTip(ItemStack stack, TooltipFlag context, List<net.minecraft.network.chat.Component> lines){

		// ItemStack や Item から直接値を取得する
		int weight = FoodWeightManager.getWeightValue(stack.getItem());

		// 値が設定されているアイテム（例: 0より大きい場合）のみ表示を追加
		if (stack.isEdible() || stack.getItem() == Items.CAKE) {
			new BodyWeightToItemName().add(lines,weight);
		}
	}
}