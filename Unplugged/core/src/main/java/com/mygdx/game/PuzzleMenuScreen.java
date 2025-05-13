package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PuzzleMenuScreen implements Screen {
    private final PuzzleGame game;
    private final Stage stage;
    private final Skin skin;

    public PuzzleMenuScreen(PuzzleGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("assets/PuzzleAssets/skin/plain-james-ui.json"));
        setupMenu();
    }

    private void setupMenu() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create menu buttons
        TextButton flatButton = new TextButton("Flat Levels", skin);
        TextButton convexButton = new TextButton("Convex Levels", skin);
        TextButton concaveButton = new TextButton("Concave Levels", skin);
        TextButton continueButton = new TextButton("Continue", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Button: Start flat mirror levels
        flatButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.currentLevelType = "FLAT";
                game.currentLevelIndex = 0;
                game.loadLevel(LevelStorage.getLevel("FLAT", 0));
            }
            return true;
        });

        // Button: Start convex mirror levels
        convexButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.currentLevelType = "CONVEX";
                game.currentLevelIndex = 0;
                game.loadLevel(LevelStorage.getLevel("CONVEX", 0));
            }
            return true;
        });

        // Button: Start concave mirror levels
        concaveButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.currentLevelType = "CONCAVE";
                game.currentLevelIndex = 0;
                game.loadLevel(LevelStorage.getLevel("CONCAVE", 0));
            }
            return true;
        });

        // Button: Continue saved progress
        continueButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.loadSavedProgress();
            }
            return true;
        });

        // Button: Exit application
        exitButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                Gdx.app.exit();
            }
            return true;
        });

        // Add buttons to menu table
        table.add(flatButton).pad(6).width(180).height(40).row();
        table.add(convexButton).pad(6).width(180).height(40).row();
        table.add(concaveButton).pad(6).width(180).height(40).row();
        table.add(continueButton).pad(6).width(180).height(40).row();
        table.add(exitButton).pad(6).width(180).height(40).row();
    }

    @Override
    public void show() {
        // Assign stage as input processor for button interaction
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear screen and render UI
        Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        // Dispose stage and skin resources
        stage.dispose();
        skin.dispose();
    }
}