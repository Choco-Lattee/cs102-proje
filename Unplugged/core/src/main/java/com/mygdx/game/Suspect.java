package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Suspect {
    public Texture portrait;
    public Vector2 position;
    public boolean isCulprit;
    public String name;

    public Suspect(String name, String portraitPath, Vector2 position, boolean isCulprit) {
        this.name = name;
       // this.portrait = new Texture(portraitPath);
        this.position = position;
        this.isCulprit = isCulprit;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(portrait, position.x, position.y);
    }
}
