package com.various_food.client.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    // protected な loadEffect メソッドを外部から呼べるようにする
    @Invoker("loadEffect")
    void invokeLoadEffect(ResourceLocation shaderLocation);

    // protected な shutdownEffect メソッドを外部から呼べるようにする
    @Invoker("shutdownEffect")
    void invokeShutdownEffect();
}