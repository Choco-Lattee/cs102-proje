package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private final PuzzleGame game;
    private final Stage stage;
    private final Skin skin;

    public MainMenuScreen(PuzzleGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        setupMenu();
    }

    private void setupMenu() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton flatButton = new TextButton("Flat Levels", skin);
        TextButton convexButton = new TextButton("Convex Levels", skin);
        TextButton concaveButton = new TextButton("Concave Levels", skin);
        TextButton continueButton = new TextButton("Continue", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        flatButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.currentLevelType = "FLAT";
                game.currentLevelIndex = 0;
                game.loadLevel(LevelStorage.getLevel("FLAT", 0));
            }
            return true;
        });

        convexButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.currentLevelType = "CONVEX";
                game.currentLevelIndex = 0;
                game.loadLevel(LevelStorage.getLevel("CONVEX", 0));
            }
            return true;
        });

        concaveButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.currentLevelType = "CONCAVE";
                game.currentLevelIndex = 0;
                game.loadLevel(LevelStorage.getLevel("CONCAVE", 0));
            }
            return true;
        });

        continueButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.loadSavedProgress();
            }
            return true;
        });

        exitButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                Gdx.app.exit();
            }
            return true;
        });

        table.add(flatButton).pad(6).width(180).height(40).row();
        table.add(convexButton).pad(6).width(180).height(40).row();
        table.add(concaveButton).pad(6).width(180).height(40).row();
        table.add(continueButton).pad(6).width(180).height(40).row();
        table.add(exitButton).pad(6).width(180).height(40).row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
