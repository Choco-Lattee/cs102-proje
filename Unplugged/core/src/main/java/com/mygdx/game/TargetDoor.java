package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class TargetDoor {
    private float x, y;
    private boolean horizontal;
    private boolean isOpen;

    public TargetDoor(Object unused1, Object unused2, float x, float y, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.isOpen = false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public Vector2 getGapCenter() {
        return new Vector2(x, y);
    }

    public List<Polygon> getDoorPolygons() {
        List<Polygon> parts = new ArrayList<>();
        float w = 30;
        float h = 10;

        if (horizontal) {
            parts.add(new Polygon(new float[]{
                -w, -h,
                -10, -h,
                -10, h,
                -w, h
            }));
            parts.add(new Polygon(new float[]{
                10, -h,
                w, -h,
                w, h,
                10, h
            }));
        } else {
            parts.add(new Polygon(new float[]{
                -h, -w,
                h, -w,
                h, -10,
                -h, -10
            }));
            parts.add(new Polygon(new float[]{
                -h, 10,
                h, 10,
                h, w,
                -h, w
            }));
        }

        for (Polygon p : parts) {
            p.setPosition(x, y);
        }

        return parts;
    }
    
    public boolean isHorizontal() {
        return horizontal;
    }

    public void draw(ShapeRenderer renderer) {
        float doorLength = 80f;
        float doorThickness = 14f;
        float gapSize = 20f;
        Color wallColor = new Color(0.45f, 0.26f, 0.13f, 1f);

        renderer.setColor(wallColor);
        if (horizontal) {
            float half = (doorLength - gapSize) / 2f;
            renderer.rect(x - half - gapSize / 2, y - doorThickness / 2, half, doorThickness);
            renderer.rect(x + gapSize / 2, y - doorThickness / 2, half, doorThickness);
        } else {
            float half = (doorLength - gapSize) / 2f;
            renderer.rect(x - doorThickness / 2, y - half - gapSize / 2, doorThickness, half);
            renderer.rect(x - doorThickness / 2, y + gapSize / 2, doorThickness, half);
        }
    }
}
