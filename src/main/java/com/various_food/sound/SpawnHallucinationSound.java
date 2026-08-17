package com.various_food.sound;

import com.various_food.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SpawnHallucinationSound {

    private static final ResourceLocation ID = new ResourceLocation(Main.MOD_ID, "spawn_hallucination");
    public static final SoundEvent SPAWN_HALLUCINATION_SOUND = Registry.register(BuiltInRegistries.SOUND_EVENT, ID, SoundEvent.createVariableRangeEvent(ID));

    //Method to call in main class. Read the fields and run the registration process.
    public static void init() {
        Main.LOGGER.info("Registering Sounds for " + Main.MOD_ID);
    }
}
