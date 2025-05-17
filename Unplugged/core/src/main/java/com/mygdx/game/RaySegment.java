package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class RaySegment {

    private Vector2 start;
    private Vector2 end;

    public RaySegment(Vector2 start, Vector2 direction) {
        this.start = start;
        Vector2 safeDir = direction.isZero() ? new Vector2(1, 0) : direction.nor();
        this.end = start.cpy().add(safeDir.scl(2000));
    }

    public void setEndPoint(Vector2 end) {
        this.end = end;
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public Vector2 getDirection() {
        return end.cpy().sub(start).nor();
    }

    public void draw(ShapeRenderer sr) {
        sr.rectLine(start, end, 2);
    }

    public RayHit intersectMirror(Mirror mirror) {
        float width = 64f;
        Vector2 center = mirror.getPosition();
        float angleDeg = mirror.getRotation();

        Vector2 p1 = new Vector2(-width / 2f, 0).rotateDeg(angleDeg).add(center);
        Vector2 p2 = new Vector2(width / 2f, 0).rotateDeg(angleDeg).add(center);

        Vector2 intersection = new Vector2();
        boolean hit = Intersector.intersectSegments(start, end, p1, p2, intersection);
        if (!hit) return null;

        float distance = intersection.dst(start);

        // Correct normal based on rotated direction
        Vector2 mirrorDir = new Vector2(1, 0).rotateDeg(angleDeg).nor();
        Vector2 normal = new Vector2(-mirrorDir.y, mirrorDir.x).nor();

        if (normal.dot(getDirection()) > 0) {
            normal.scl(-1);
        }

        return new RayHit(intersection, normal, distance, mirror, this);
    }
} 
