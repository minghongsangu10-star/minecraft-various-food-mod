package com.various_food.item;

import com.various_food.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class RiceBall extends Item {
    public static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
            .nutrition(9)
            .saturationMod(0.6f)
            .build();

    public RiceBall(Properties properties) {
        super(properties.food(FOOD_PROPERTIES));
    }

    public static Item registerItem(String name) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Main.MOD_ID, name), new RiceBall(new Item.Properties()));
    }
}
