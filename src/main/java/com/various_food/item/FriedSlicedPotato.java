package com.various_food.item;

import com.various_food.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class FriedSlicedPotato extends Item {

    public FriedSlicedPotato(Properties properties) {
        super(properties);
    }

    public static Item registerItem(String name) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Main.MOD_ID, name), new FriedSlicedPotato(new Item.Properties()));
    }
}
