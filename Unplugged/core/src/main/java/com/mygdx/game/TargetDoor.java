package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class TargetDoor {

    private Vector2 position;
    private boolean isHorizontal;
    private boolean triggered;

    private static final float WIDTH = 60f;
    private static final float HEIGHT = 10f;
    private static final float GAP = 20f; // visible gap size

    public TargetDoor(Vector2 position, boolean isHorizontal) {
        this.position = position;
        this.isHorizontal = isHorizontal;
        this.triggered = false;
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(triggered ? Color.GREEN : Color.GRAY);
        if (isHorizontal) {
            // Draw left and right sides of the door (gap in the middle)
            float half = WIDTH / 2f;
            float gapHalf = GAP / 2f;
            sr.rect(position.x - half, position.y - HEIGHT / 2f, half - gapHalf, HEIGHT); // left
            sr.rect(position.x + gapHalf, position.y - HEIGHT / 2f, half - gapHalf, HEIGHT); // right
        } else {
            // Draw top and bottom parts with vertical gap
            float half = WIDTH / 2f;
            float gapHalf = GAP / 2f;
            sr.rect(position.x - HEIGHT / 2f, position.y - half, HEIGHT, half - gapHalf); // bottom
            sr.rect(position.x - HEIGHT / 2f, position.y + gapHalf, HEIGHT, half - gapHalf); // top
        }
    }

    public boolean intersects(RaySegment ray) {
        Vector2 rayStart = ray.getStart();
        Vector2 rayEnd = ray.getEnd();
        Vector2 p1, p2;

        if (isHorizontal) {
            p1 = new Vector2(position.x - GAP / 2, position.y);
            p2 = new Vector2(position.x + GAP / 2, position.y);
        } else {
            p1 = new Vector2(position.x, position.y - GAP / 2);
            p2 = new Vector2(position.x, position.y + GAP / 2);
        }

        return Intersector.intersectSegments(rayStart, rayEnd, p1, p2, null);
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }
}
