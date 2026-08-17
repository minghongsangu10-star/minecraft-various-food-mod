package com.various_food.client;

public class PingPongValue {

    private float value;
    private boolean increasing = true;

    public PingPongValue(float initialValue) {
        this.value = initialValue;
    }

    public void update(float speed) {
        if (increasing) {
            value += speed;
            if (value >= 1.0f) {
                value = 1.0f;
                increasing = false;
            }
        } else {
            value -= speed;
            if (value <= 0.0f) {
                value = 0.0f;
                increasing = true;
            }
        }
    }

    public float getValue() { return value; }

    public void setValue(float value){
        this.value = value;
    }
}
