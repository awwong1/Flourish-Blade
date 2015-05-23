package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.math.Vector2;

public class Direction {
    public static class InvalidDirectionException extends RuntimeException {
        private static final long serialVersionUID = 6197289793612370308L;

        public InvalidDirectionException(int dir) {
            super("Not a valid direction: " + dir + ". Has to be in range 0-3");
        }
    }

    /**
     * Don't use the constructor! I dare you!
     */
    public Direction() {
        System.err.println("What did I say you in the Javadoc about the Dir() constructor?!");
        new Direction();
    }

    public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;

    public static String getName(int dir) {
        switch (dir) {
            case UP:
                return "up";
            case DOWN:
                return "down";
            case LEFT:
                return "left";
            case RIGHT:
                return "right";
        }
        return "unknown-direction";
    }

    public static int getDirection(String name) {
        String s = name.toLowerCase();
        if (s.equals("up")) {
            return UP;
        } else if (s.equals("down")) {
            return DOWN;
        } else if (s.equals("left")) {
            return LEFT;
        } else if (s.equals("right")) {
            return RIGHT;
        }
        return -1;
    }

    public static boolean isValid(int dir) {
        if (dir >= 0 && dir < 4) return true;
        return false;
    }

    public static void validCheck(int dir) {
        if (!isValid(dir)) throw new InvalidDirectionException(dir);
    }

    public static int makeValid(int dir) {
        while (dir < 0) dir += 4;
        while (dir >= 4) dir -= 4;
        return dir;
    }

    private static final Vector2 tmpVec = new Vector2();

    public static int getDirection(float vecX, float vecY) {
        return makeValid((int) ((tmpVec.set(vecX, vecY).angle() + 45) / 90));
    }
}