package com.spiritatlas.flourishblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class GameSettings {
    private static final String SETTINGS_FILE = "settings.json";
    private float bgmVolume;
    private float sfxVolume;

    public GameSettings() {
        this.bgmVolume = 0.5f;
        this.sfxVolume = 0.5f;
    }

    public static GameSettings loadSettings() {
        FileHandle file = Gdx.files.local(SETTINGS_FILE);
        if (file != null && file.exists()) {
            String s = file.readString();
            Json json = new Json();
            return json.fromJson(GameSettings.class, s);
        }
        return new GameSettings();
    }

    public void saveSettings() {
        FileHandle file = Gdx.files.local(SETTINGS_FILE);
        Json json = new Json();
        file.writeString(json.toJson(this), false);
    }

    public float getBgmVolume() {
        return bgmVolume;
    }

    public void setBgmVolume(float bgmVolume) {
        this.bgmVolume = bgmVolume;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = sfxVolume;
    }
}
