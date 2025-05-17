package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class RayHit {

    private Vector2 point;
    private Vector2 normal;
    private float distance;
    private Mirror mirror;
    private RaySegment ray;

    public RayHit(Vector2 point, Vector2 normal, float distance, Mirror mirror, RaySegment ray) {
        this.point = point;
        this.normal = normal.nor();
        this.distance = distance;
        this.mirror = mirror;
        this.ray = ray;
    }

    public Vector2 getPoint() {
        return point;
    }

    public float getDistance() {
        return distance;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public RaySegment getRay() {
        return ray;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public Vector2 getReflectedDirection() {
        Vector2 incoming = ray.getDirection().nor();
        if (incoming.isZero()) return new Vector2(0, 0);

        Vector2 normalCopy = normal.cpy().nor();

        switch (mirror.getType()) {
            case "FLAT":
                return incoming.sub(normalCopy.scl(2 * incoming.dot(normalCopy))).nor();

            case "CONCAVE":
                Vector2 focalPoint = mirror.getPosition().cpy().add(normalCopy.scl(-150));
                return focalPoint.sub(point).nor();

            case "CONVEX":
                Vector2 away = point.cpy().sub(mirror.getPosition()).nor();
                return away.rotateDeg(15).nor();

            default:
                return incoming;
        }
    }
}