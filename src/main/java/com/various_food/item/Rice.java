package com.various_food.item;

import com.various_food.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;

public class Rice extends ItemNameBlockItem {

    public Rice(Block block, Properties properties) {
        super(block, properties);
    }

    public static Item registerItem(String name,Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Main.MOD_ID, name), new Rice(block,new Item.Properties()));
    }
}
