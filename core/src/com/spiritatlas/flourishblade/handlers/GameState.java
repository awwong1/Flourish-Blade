package com.spiritatlas.flourishblade.handlers;

import com.badlogic.gdx.math.Vector2;

public class GameState {
    private String mapFilePath;
    private Vector2 characterPosition;

    public GameState() {
        mapFilePath = "maps/testmap.tmx";
        characterPosition = new Vector2(8, 19);
    }

    public String getMapFilePath() {
        return mapFilePath;
    }

    public Vector2 getCharacterPosition() {
        return characterPosition;
    }
}
