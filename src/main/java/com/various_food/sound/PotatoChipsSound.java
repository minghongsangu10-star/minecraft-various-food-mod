package com.various_food.sound;

import com.various_food.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class PotatoChipsSound {

    private static final ResourceLocation ID = new ResourceLocation(Main.MOD_ID, "potato_chips_eating");
    public static final SoundEvent POTATO_CHIPS_EATING = Registry.register(BuiltInRegistries.SOUND_EVENT, ID, SoundEvent.createVariableRangeEvent(ID));

    //Method to call in main class. Read the fields and run the registration process.
    public static void init() {
        Main.LOGGER.info("Registering Sounds for " + Main.MOD_ID);
    }
}
