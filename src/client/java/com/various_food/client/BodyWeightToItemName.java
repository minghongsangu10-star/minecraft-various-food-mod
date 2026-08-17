package com.various_food.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class BodyWeightToItemName {
    public void add(List<Component> lines , int addValue){

        if (!lines.isEmpty()) {
            // 1行目（元のアイテム名）を取得
            Component originalName = lines.get(0);

            // 追加したい文字と装飾（金色）を作成
            MutableComponent extraText;
            if(addValue >= 0){
                extraText = Component.literal(" [+"+ addValue +"]").withStyle(ChatFormatting.GOLD);
            }else{
                extraText = Component.literal(" ["+ addValue +"]").withStyle(ChatFormatting.RED);
            }

            // 元の名前の末尾にテキストを結合して1行目を差し替える
            MutableComponent newName = originalName.copy().append(extraText);
            lines.set(0, newName);
        }
    }
}
