package com.various_food.item;

import com.various_food.Main;
import com.various_food.nbt.FoodWeightManager;
import com.various_food.nbt.MyComponent;
import com.various_food.sound.PotatoChipsSound;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PotatoChips extends Item{
    public static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.3f)
            .build();

    public PotatoChips(Properties properties) {
        super(properties.food(FOOD_PROPERTIES));
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return PotatoChipsSound.POTATO_CHIPS_EATING;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity){
        int value = FoodWeightManager.getWeightValue(stack.getItem());

        // Cardinal Components API を介してプレイヤーのデータを取得・更新
        MyComponent.BODY_WEIGHT.get(livingEntity).addValue(value);

        // 更新されたデータをサーバーからクライアントへ同期
        MyComponent.BODY_WEIGHT.sync(livingEntity);

        return stack;
    }

    public static Item registerItem(String name) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Main.MOD_ID, name), new PotatoChips(new Item.Properties()));
    }
}
