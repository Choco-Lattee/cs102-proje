package io.github.some_example_name;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class EndGameScreen implements Screen {
    final CrimeSceneGame game;
    Texture background;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();


    BitmapFont fontWhite;

    public boolean showPoints = true;
    public EndGameScreen(final CrimeSceneGame game, ArrayList<Evidence> evidences, ArrayList<Evidence> foundEvidences) {
        this.game = game;
        background = new Texture("EndGameScreenBackground");
        fontWhite = new BitmapFont(Gdx.files.internal("assets/font/black.fnt"));
        fontWhite.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        fontWhite.getData().setScale(1,1);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        game.batch.begin();
        game.batch.draw(background, 0, 0, 1920,980);
        
        
            fontWhite.draw(game.batch, "Found evidence X" + CrimeSceneScreen.foundEvidenceCount + ": " + CrimeSceneScreen.foundEvidenceCount * 100, 650, 600);
            fontWhite.draw(game.batch, "Guess points: " + InterrogationScreen.addFinalPoint, 650, 500);
            fontWhite.draw(game.batch, "Total points: " + CrimeSceneScreen.point, 650, 400);
            fontWhite.draw(game.batch, "Press Anywhere to continue", 650, 200);

            if ( CrimeSceneScreen.point < 1200)
            {
                fontWhite.draw(game.batch, "You Lose! (max possible points:3000)", 650, 300);
            }
            else
            {
                fontWhite.draw(game.batch, "You Win! (max possible points:3000)", 650, 300);
            }

            if (Gdx.input.justTouched()) 
            {
              //TODO  game.setScreen(new ANAOYUN(game)); 
            }
            
        game.batch.end();
    }

    @Override public void dispose() { background.dispose(); }
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
