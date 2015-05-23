package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class SpriteAnimation implements Disposable {
    private boolean disposed;

    private final Texture tex;
    private final float[] delays;
    private final float totalDelays;
    private final TextureRegion[] keyframes;
    private final String name;

    private float time;
    private int frame;

    public SpriteAnimation(SpriteAnimation other) {
        this.tex = other.tex;
        this.delays = other.delays;
        this.totalDelays = other.totalDelays;
        this.keyframes = other.keyframes;
        this.name = other.name;
    }

    public SpriteAnimation(Texture texture, float[] delays, float totalDelays, TextureRegion[] keyframes, String name) {
        this.tex = texture;
        this.delays = delays;
        this.totalDelays = totalDelays;
        this.keyframes = keyframes;
        this.name = name;
    }

    public void setDelay(float delay) {
        for (int i = 0; i < delays.length; i++) {
            delays[i] = delay;
        }
    }

    public String getName() {
        return name;
    }

    public int getCurrentFrame() {
        float lookupTime = time % totalDelays;
        float visitedTime = 0;

        for (int i = 0; i < delays.length - 1; i++) {
            visitedTime += delays[i];
            if (lookupTime < visitedTime) {
                return i;
            }
        }
        return 0;
    }

    public void tick(float delta) {
        time += delta;
        frame = getCurrentFrame();
    }

    public TextureRegion getCurrentKeyframe() {
        return keyframes[frame];
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            tex.dispose();
        }
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void reset() {
        setFrame(0);
        setTime(0f);
    }

}

