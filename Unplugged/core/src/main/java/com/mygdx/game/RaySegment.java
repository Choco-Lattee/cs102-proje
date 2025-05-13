package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class RaySegment {
    private final Vector2 start;
    private final Vector2 end;

    public RaySegment(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }
}
