package com.various_food.block;

import com.various_food.Main;
import com.various_food.ModThings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class Rice extends CropBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_7; // 成長段階 (0~7)

    public Rice(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        // 右クリック等で取得できる種アイテムを指定
        return ModThings.ITEM_RICE;
    }

    @Override
    public @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 7; // 最大成長段階
    }

    public static CropBlock registerCropBlock(String name){
        return Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(Main.MOD_ID, name),
                new Rice(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .noOcclusion()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.CROP)
                        .pushReaction(PushReaction.DESTROY))
        );
    }
}
