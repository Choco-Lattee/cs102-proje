package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class LightSource {
    private Vector2 position;
    private Vector2 direction;

    public LightSource(Vector2 position, Vector2 direction) {
        this.position = position;
        this.direction = direction.nor();
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction.nor();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.YELLOW);
        renderer.rect(position.x - 10, position.y - 10, 20, 20); // Kutu şeklinde ışık kaynağı
    }
}
