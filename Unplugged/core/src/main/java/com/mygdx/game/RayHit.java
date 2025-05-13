package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class RayHit {
    public final Vector2 position;
    public final float distance;
    public final Mirror mirror;
    public final boolean isDoor;
    public final Vector2 normal;

    public RayHit(Vector2 position, float distance, Mirror mirror, boolean isDoor, Vector2 normal) {
        this.position = position;
        this.distance = distance;
        this.mirror = mirror;
        this.isDoor = isDoor;
        this.normal = normal;
    }

    public boolean isMirror() {
        return mirror != null;
    }

    public boolean isDoor() {
        return isDoor;
    }
}
