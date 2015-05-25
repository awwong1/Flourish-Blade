package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

public class ResourceLoader {
    // UI Related
    public static final String FONT = "font";
    public static final String UI_TEX = "ui";
    public static final String CLICK = "click";

    // Splash Screen
    public static final String SPIRIT_ATLAS_LOGO = "SpiritAtlasLogo";

    // Menu Screen
    public static final String FLOURISH_BLADE_LOGO = "FlourishBladeLogo";
    public static final String MUSIC_MAIN_THEME = "music_main";

    // Player Spritesheet
    public static final String PLAYER_SPRITESHEET = "player_spritesheet";
    public static final String PLAYER_SWORD = "player_sword";


    private ObjectMap<String, Texture> textures;
    private ObjectMap<String, Music> musics;
    private ObjectMap<String, Sound> sounds;
    private ObjectMap<String, Skin> skins;
    private ObjectMap<String, BitmapFont> fonts;

    public ResourceLoader() {
        textures = new ObjectMap<String, Texture>();
        musics = new ObjectMap<String, Music>();
        sounds = new ObjectMap<String, Sound>();
        skins = new ObjectMap<String, Skin>();
        fonts = new ObjectMap<String, BitmapFont>();
    }

    public void loadTexture(String path, String key) {
        Texture texture = new Texture(Gdx.files.internal(path));
        textures.put(key, texture);
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public void removeTexture(String key) {
        Texture texture = textures.get(key);
        if (texture != null) {
            textures.remove(key);
            texture.dispose();
        }
    }

    public void loadMusic(String path, String key) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        musics.put(key, music);
    }

    public Music getMusic(String key) {
        return musics.get(key);
    }

    public void removeMusic(String key) {
        Music music = musics.get(key);
        if (music != null) {
            musics.remove(key);
            music.dispose();
        }
    }

    public void setMusicVolume(float volume) {
        Iterator it = musics.iterator();
        while (it.hasNext()) {
            ObjectMap.Entry pair = (ObjectMap.Entry) it.next();
            ((Music) pair.value).setVolume(volume);
        }
    }

    public void loadSound(String path, String key) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(key, sound);
    }

    public Sound getSound(String key) {
        return sounds.get(key);
    }

    public void removeSound(String key) {
        Sound sound = sounds.get(key);
        if (sound != null) {
            sounds.remove(key);
            sound.dispose();
        }
    }

    public void setSkin(Skin skin, String key) {
        skins.put(key, skin);
    }

    public Skin getSkin(String key) {
        return skins.get(key);
    }

    public void removeSkin(String key) {
        Skin skin = skins.get(key);
        if (skin != null) {
            skins.remove(key);
            skin.dispose();
        }
    }

    public void setFont(BitmapFont font, String key) {
        fonts.put(key, font);
    }

    public BitmapFont getFont(String key) {
        return fonts.get(key);
    }

    public void removeFont(String key) {
        BitmapFont font = fonts.get(key);
        if (font != null) {
            fonts.remove(key);
            font.dispose();
        }
    }

    public void removeAll() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
        for (Music music : musics.values()) {
            music.dispose();
        }
        musics.clear();
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
        for (Skin skin : skins.values()) {
            skin.dispose();
        }
        skins.clear();
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();
    }
}
