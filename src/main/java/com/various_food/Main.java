package com.various_food;

import com.mojang.brigadier.CommandDispatcher;
import com.various_food.command.BodyWeightCommand;
import com.various_food.creative_mode_tab.VariousFoodTab;
import com.various_food.entity.Hallucination;
import com.various_food.nbt.FoodWeightManager;
import com.various_food.nbt.MyComponent;
import com.various_food.sound.PotatoChipsSound;
import com.various_food.sound.SpawnHallucinationSound;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * モドのメインエントリーポイントクラス (Minecraft 1.20.1 / Fabric)
 */
public class Main implements ModInitializer {

	// --- 定数定義 ---
	public static final String MOD_ID = "various_food";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// ネットワークパケットID
	public static final ResourceLocation HUD_SPAWN_PACKET_ID = new ResourceLocation(MOD_ID, "show_hud");

	// 体重ペナルティのしきい値・数値設定
	private static final int WEIGHT_THRESHOLD_HEAVY = 200;
	private static final int WEIGHT_THRESHOLD_MEDIUM = 100;

	private static final float SPEED_REDUCTION_HEAVY = -0.5f;
	private static final float SPEED_REDUCTION_MEDIUM = -0.3f;
	private static final float DAMAGE_AMOUNT = 1.0f;// ハート0.5個分

	private int spawnHallucinationCount = 0;

	// エンティティタイプの登録（幻覚エンティティ）
	public static final EntityType<Hallucination> HALLUCINATION_ENTITY_TYPE = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			new ResourceLocation(MOD_ID, "hallucination"),
			FabricEntityTypeBuilder.create(MobCategory.CREATURE, Hallucination::new)
					.dimensions(EntityDimensions.scalable(Hallucination.HITBOX_WIGHT, Hallucination.HITBOX_HEIGHT))
					.build()
	);

	private static boolean hallucinationSpawnFlag = false;

	/**
	 * MODの初期化処理（サーバー・クライアント共通）
	 */
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Various Food Mod...");

		// エンティティのデフォルト属性を登録
		FabricDefaultAttributeRegistry.register(HALLUCINATION_ENTITY_TYPE, Hallucination.createAttributes());

		// 各種レジストリ・機能の読み込み
		registerModComponents();

		// サーバーTick毎の処理を登録
		ServerTickEvents.END_SERVER_TICK.register(this::handlePlayerWeightEffects);

		CommandRegistrationCallback.EVENT.register(this::registerCommands);

		ServerPlayerEvents.AFTER_RESPAWN.register(this::WhenPlayerDie);
	}

	/**
	 * サウンド、アイテム、クリエイティブタブ、コンポーネント等の読み込み
	 */
	private void registerModComponents() {
		ModThings.load();
		PotatoChipsSound.init();
		SpawnHallucinationSound.init();
		VariousFoodTab.init();
		FoodWeightManager.load();
	}

	/**
	 * 毎Tick終了時に呼び出されるプレイヤーの体重チェックおよびデバフ適用処理
	 *
	 * @param server 現在稼働中の Minecraft サーバーインスタンス
	 */
	private void handlePlayerWeightEffects(MinecraftServer server) {
		// パフォーマンス最適化：1秒間隔（20ティックごと）に実行制限
		if (server.getTickCount() % 20 != 0) return;

		// オンライン中の全プレイヤーに対して判定を実行
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			processPlayerWeightStatus(server, player);
		}
	}

	/**
	 * 個々のプレイヤーの体重を判定し、移動速度減少・ダメージ・状態異常を付与する
	 */
	private void processPlayerWeightStatus(MinecraftServer server, @NotNull ServerPlayer player) {
		// プレイヤーの移動速度属性を取得（存在しない場合はスキップ）
		AttributeInstance speedInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
		if (speedInstance == null) return;

		// 体重データの取得 (Cardinal Components API)
		int weight = MyComponent.BODY_WEIGHT.get(player).getValue();
		float reduction = 0.0f; // 移動速度補正率

		// --- 体重ペナルティの判定 ---
		if (weight >= WEIGHT_THRESHOLD_HEAVY) {
			// 【体重200以上】速度-50% / 毎秒ダメージ / 盲目付与
			reduction = SPEED_REDUCTION_HEAVY;

			if (server.getTickCount() % 40 == 0) {
				player.hurt(player.damageSources().generic(), DAMAGE_AMOUNT);
			}

			// 盲目効果の付与
			new EffectManager().addEffect(player, MobEffects.BLINDNESS, 10);
			// new EffectManager().addEffect(player, MobEffects.DARKNESS, 30);

			if (!hallucinationSpawnFlag){
				spawnHallucinationCount++;
				if(spawnHallucinationCount == 60){
					new SpawnHallucination().spawnHallucination(
							player.serverLevel(),
							SpawnHallucination.getARandomPosOnTheGround(32, 64, player),
							player);
				}
			}

		} else if (weight >= WEIGHT_THRESHOLD_MEDIUM) {
			// 【体重100以上】速度-30%
			reduction = SPEED_REDUCTION_MEDIUM;

			hallucinationSpawnFlag = false;
			spawnHallucinationCount = 0;
		}

		// 速度変更処理を実行
		new SpeedChanger().change(reduction, speedInstance);
	}

	/**
	 * コマンドを登録する
	 *
	 * @param dispatcher ゲーム内でコマンドを登録・管理・実行するための司令塔
	 */
	private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment){
		new BodyWeightCommand().register(dispatcher);
	}

	/**
	 * プレイヤーが死んだときの処理
	 *
	 * @param oldPlayer 死ぬ前のプレイヤー
	 * @param newPlayer　リスポーン後のプレイヤー
	 * @param alive　リスポーンの種類
	 */
	private void WhenPlayerDie(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive){

		// 1. 旧プレイヤーから死亡前の値を取得
		int oldWeight = MyComponent.BODY_WEIGHT.get(oldPlayer).getValue();

		if (!alive){
			oldWeight = oldWeight / 2;
		}

		// 2. 新しいプレイヤーに値を設定
		MyComponent.BODY_WEIGHT.get(newPlayer).setValue(oldWeight);
	}
}