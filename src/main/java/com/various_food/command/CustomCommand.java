package com.various_food.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface CustomCommand {
    void register(CommandDispatcher<CommandSourceStack> dispatcher);
}
