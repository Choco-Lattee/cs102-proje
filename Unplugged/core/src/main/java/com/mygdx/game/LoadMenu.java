package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadMenu implements Screen {
     SaveLoadStage loadStage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch loadBatch;
    private Sprite splash;
    
    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        final float GAME_WORLD_WIDTH = Gdx.graphics.getWidth();
        final float GAME_WORLD_HEIGHT = Gdx.graphics.getHeight();
        viewport = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);

        loadBatch = new SpriteBatch();

        Texture splashTexture = new Texture("Unplugged/assets/Background.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        loadStage = new SaveLoadStage(viewport, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, false);

        loadStage.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });

        Gdx.input.setInputProcessor(loadStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        loadStage.getBatch().setProjectionMatrix(loadStage.getCamera().combined);
        loadStage.getCamera().update();

        loadBatch.begin();
        splash.draw(loadBatch);
        loadBatch.end();

		loadStage.act(delta);
	    loadStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        loadStage.getViewport().update(width, height);
        loadStage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		loadStage.getTable().invalidateHierarchy();
        splash.setSize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        loadStage.dispose();
    }
    
}
