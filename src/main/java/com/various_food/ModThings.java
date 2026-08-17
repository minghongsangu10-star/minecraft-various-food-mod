package com.various_food;

import com.various_food.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CropBlock;

public class ModThings {

    public static final CropBlock CROP_BLOCK_RICE = com.various_food.block.Rice.registerCropBlock("crop_block_rice");

    public static final Item POTATO_CHIPS = PotatoChips.registerItem("potato_chips");
    public static final Item SLICED_POTATO = SlicedPotato.registerItem("sliced_potato");
    public static final Item FRIED_SLICED_POTATO = FriedSlicedPotato.registerItem("fried_sliced_potato");
    public static final Item RICE_BALL = RiceBall.registerItem("rice_ball");
    public static final Item ITEM_RICE = Rice.registerItem("item_rice",CROP_BLOCK_RICE);

    public static final Item HALLUCINATION_SPAWN_EGG = HallucinationSpawnEgg.registerItem("hallucination_spawn_egg");

    public static final Item[] MOD_THINGS = {
            POTATO_CHIPS,
            SLICED_POTATO,
            FRIED_SLICED_POTATO,
            RICE_BALL,
            ITEM_RICE,
            HALLUCINATION_SPAWN_EGG
    };

    public static void load(){

    }
}
