package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BlameButton {
    public Texture buttonTexture;
    public Rectangle bounds;
    public Suspect suspect;

    public BlameButton(Suspect suspect, Vector2 position) {
        this.suspect = suspect;
        this.buttonTexture = new Texture("blame_button.png");
        this.bounds = new Rectangle(position.x, position.y, buttonTexture.getWidth() * 0.4f, buttonTexture.getHeight() * 0.4f);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(buttonTexture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public boolean isClicked(int screenX, int screenY) {
        return bounds.contains(screenX, screenY);
    }
}
