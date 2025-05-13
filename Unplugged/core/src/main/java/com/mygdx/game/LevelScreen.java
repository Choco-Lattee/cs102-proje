package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

public class LevelScreen implements Screen {
    private final PuzzleGame game;

    public LevelScreen(PuzzleGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        InputAdapter clickHandler = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldCoords = game.stage.getViewport().unproject(new Vector2(screenX, screenY));
                if (game.currentLevel != null) {
                    game.currentLevel.handleClick(worldCoords.x, worldCoords.y);
                }
                return true;
            }
        };

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game.stage);         // UI input
        multiplexer.addProcessor(clickHandler);       // Mirror input
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        updateBackgroundColor(delta); // rainbow logic

        Gdx.gl.glClearColor(game.currentBackgroundColor.r, game.currentBackgroundColor.g,
                            game.currentBackgroundColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.shapeRenderer.setProjectionMatrix(game.camera.combined);
        game.currentLevel.update(delta);
        game.currentLevel.draw(game.shapeRenderer);

        game.stage.act(delta);
        game.stage.draw();

        game.batch.begin();
        game.font.draw(game.batch, "Moves: " + game.moveCount, 10, Gdx.graphics.getHeight() - 10);
        game.batch.end();
    }

    private void updateBackgroundColor(float delta) {
        if (game.currentLevel.isCompleted()) {
            game.colorTimer += delta;
            if (game.colorTimer >= game.colorChangeInterval) {
                game.currentRainbowIndex = (game.currentRainbowIndex + 1) % game.rainbowColors.length;
                game.currentBackgroundColor = game.rainbowColors[game.currentRainbowIndex];
                game.colorTimer = 0f;
            }
        } else {
            game.currentBackgroundColor = Color.LIGHT_GRAY;
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}