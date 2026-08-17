package com.various_food.creative_mode_tab;

import com.various_food.Main;
import com.various_food.ModThings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class VariousFoodTab {
    public static final ResourceLocation TAB_ID = new ResourceLocation(Main.MOD_ID, "various_food_tab");

    public static final CreativeModeTab VARIOUS_FOOD_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_ID,
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." + Main.MOD_ID + ".various_food_tab"))
                    .icon(() -> new ItemStack(ModThings.POTATO_CHIPS))
                    .displayItems((displayParameters, output) -> {

                        for(int i = 0; i < ModThings.MOD_THINGS.length; i++){
                            output.accept(ModThings.MOD_THINGS[i]);
                        }
                    }).build()
    );


    public static void init(){
        Main.LOGGER.info("Registering CreativeModeTab for " + Main.MOD_ID);
    }
}
