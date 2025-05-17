package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Mirror {

    private Vector2 position;
    private float rotation;
    private String type;

    private static final float WIDTH = 64;
    private static final float HEIGHT = 16;

    public Mirror(Vector2 position, String type, float rotation) {
        this.position = position;
        this.type = type.toUpperCase();
        this.rotation = rotation;
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(getColor());
        sr.rect(position.x - WIDTH / 2, position.y - HEIGHT / 2,
                WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT, 1, 1, rotation);
    }

    private Color getColor() {
        switch (type) {
            case "FLAT": return Color.BLUE;
            case "CONCAVE": return Color.PURPLE;
            case "CONVEX": return Color.ORANGE;
            default: return Color.BLACK;
        }
    }

    public boolean containsPoint(Vector2 point) {
        float halfWidth = WIDTH / 2f;
        float halfHeight = HEIGHT / 2f;
        return point.x >= position.x - halfWidth &&
               point.x <= position.x + halfWidth &&
               point.y >= position.y - halfHeight &&
               point.y <= position.y + halfHeight;
    }

    public void rotate() {
        rotation += 45f;
        if (rotation >= 360f) {
            rotation -= 360f;
        }
    }

    public Mirror copy() {
        return new Mirror(position.cpy(), type, rotation);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public String getType() {
        return type;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setType(String type) {
        this.type = type;
    }
}
