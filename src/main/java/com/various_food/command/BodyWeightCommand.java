package com.various_food.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.various_food.nbt.MyComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class BodyWeightCommand implements CustomCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("bodyweight")
                        .then(Commands.literal("set") // ★ argument から literal に変更
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");

                                            ServerPlayer player = ctx.getSource().getPlayer();

                                            if (player != null) {
                                                // 1. コンポーネント値の加算と同期
                                                MyComponent.BODY_WEIGHT.get(player).setValue(amount);
                                                MyComponent.BODY_WEIGHT.sync(player);

                                                // 2. 成功メッセージ（プレイヤーが存在する場合のみ送信する方が安全）
                                                ctx.getSource().sendSuccess(
                                                        () -> Component.literal("I set it to " + amount),
                                                        false
                                                );
                                                return 1;
                                            } else {
                                                // コンソールから実行された場合のエラー表示
                                                ctx.getSource().sendFailure(Component.literal("This command can only be executed by a player."));
                                                return 0;
                                            }
                                        })
                                )
                        )
        );
    }
}
