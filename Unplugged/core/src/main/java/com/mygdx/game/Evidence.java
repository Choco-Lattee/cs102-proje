package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Evidence {
    public Texture texture;
    public Vector2 position;
    public String text;
    public boolean isFound;

    public Evidence(String texturePath, Vector2 position, String text) {
        this.texture = new Texture(texturePath);
        this.text = text;
        this.position = position;
        isFound = false;
    }

    public boolean isNear(int x, int y) 
    {
        return position.dst(new Vector2(x, y)) < 50;
    }

    public boolean isFound()
    {
        return isFound;
    }
}
