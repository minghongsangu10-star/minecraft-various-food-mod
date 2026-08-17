package com.various_food.item;

import com.various_food.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class HallucinationSpawnEgg extends SpawnEggItem {

    public HallucinationSpawnEgg(EntityType<? extends Mob> type, int primaryColor, int secondaryColor, Properties properties) {
        super(type, primaryColor, secondaryColor, properties);
    }

    public static Item registerItem(String name) {
        return Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(Main.MOD_ID, name),
                new HallucinationSpawnEgg(
                        Main.HALLUCINATION_ENTITY_TYPE,
                        0x1A1A1A, 0x7A0099
                        ,new Item.Properties()));
    }
}
