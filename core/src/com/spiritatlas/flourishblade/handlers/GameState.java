package com.spiritatlas.flourishblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class GameState {
    private static final String SAVE_GAME_FILE = "flourishblade.sav";
    private String mapFilePath;
    private Vector2 characterPosition;

    public GameState() {
        mapFilePath = "maps/testmap.tmx";
        characterPosition = new Vector2(8, 19);
    }

    public static GameState loadGameState() {
        FileHandle file = Gdx.files.local(SAVE_GAME_FILE);
        if (file != null && file.exists()) {
            String s = file.readString();
            String decodedState = Base64Coder.decodeString(s);
            Json json = new Json();
            return json.fromJson(GameState.class, decodedState);
        }
        return new GameState();
    }

    public void saveGameState() {
        FileHandle file = Gdx.files.local(SAVE_GAME_FILE);
        Json json = new Json();
        String encodedState = Base64Coder.encodeString(json.toJson(this));
        file.writeString(encodedState, false);
    }

    public void setMapFilePath(String mapFilePath) {
        this.mapFilePath = mapFilePath;
    }

    public void setCharacterPosition(Vector2 vector2) {
        this.characterPosition = vector2;
    }

    public String getMapFilePath() {
        return mapFilePath;
    }

    public Vector2 getCharacterPosition() {
        return characterPosition;
    }
}
