package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PuzzleGame implements Screen {

    private enum State { MENU, GAME }
    private State state = State.MENU;

    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Level currentLevel;
    private String currentLevelType = "FLAT";
    private int currentLevelIndex = 0;

    public int moveCount = 0;

    private Preferences prefs;

    private final Color[] rainbowColors = new Color[] {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.MAGENTA
    };
    private float colorTimer = 0f;
    private float colorChangeInterval = 0.15f;
    private int currentRainbowIndex = 0;
    private Color currentBackgroundColor = Color.LIGHT_GRAY;

    @Override
    public void show() {
        LevelStorage.loadAllLevels();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/PuzzleAssets/skin/plain-james-ui.json"));

        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        prefs = Gdx.app.getPreferences("LightPuzzleProgress");

        setupMenu();
    }

    private void setupMenu() {
        state = State.MENU;
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton flat = new TextButton("Flat Levels", skin);
        TextButton convex = new TextButton("Convex Levels", skin);
        TextButton concave = new TextButton("Concave Levels", skin);
        TextButton cont = new TextButton("Continue", skin);
        TextButton exit = new TextButton("Exit", skin);

        flat.addListener(createLoadListener("FLAT", 0));
        convex.addListener(createLoadListener("CONVEX", 0));
        concave.addListener(createLoadListener("CONCAVE", 0));
        cont.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                loadSavedProgress(); return true;
            }
        });
        exit.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap3()); return true;
            }
        });

        table.add(flat).pad(6).width(180).height(40).row();
        table.add(convex).pad(6).width(180).height(40).row();
        table.add(concave).pad(6).width(180).height(40).row();
        table.add(cont).pad(6).width(180).height(40).row();
        table.add(exit).pad(6).width(180).height(40).row();

        Gdx.input.setInputProcessor(stage);
    }

    private InputListener createLoadListener(String type, int index) {
        return new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                currentLevelType = type;
                currentLevelIndex = index;
                loadLevel(LevelStorage.getLevel(type, index));
                return true;
            }
        };
    }

    private void setupGameUI() {
        state = State.GAME;
        stage.clear();

        Table table = new Table();
        table.top().left().pad(10);
        table.setFillParent(true);
        table.defaults().width(100).height(40).padRight(10);

        TextButton info = new TextButton("Info", skin);
        TextButton reset = new TextButton("Reset", skin);
        TextButton menu = new TextButton("Menu", skin);

        info.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                new Dialog("Info", skin)
                        .text("FLAT: Blue - Normal\nCONVEX: Pink - Diverging\nCONCAVE: Orange - Converging\nClick mirrors to rotate")
                        .button("OK").show(stage);
                return true;
            }
        });

        reset.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                currentLevel.reset(); moveCount = 0;
                colorTimer = 0f; currentRainbowIndex = 0;
                currentBackgroundColor = Color.LIGHT_GRAY;
                return true;
            }
        });

        menu.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                setupMenu(); return true;
            }
        });

        table.add(info); table.add(reset); table.add(menu);
        stage.addActor(table);

        // Mirror click input
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                camera.unproject(touch); // ✅ şimdi uyumlu

                if (currentLevel != null) {
                    currentLevel.handleClick(touch.x, touch.y);
                    moveCount++;
                }
                return false;
            }

        });
    }

    private void loadLevel(Level level) {
        if (level == null) {
            System.err.println("Null level.");
            return;
        }
        currentLevel = level;
        moveCount = 0;
        colorTimer = 0f;
        currentRainbowIndex = 0;
        currentBackgroundColor = Color.LIGHT_GRAY;

        setupGameUI();
        saveProgress();
    }

    private void loadSavedProgress() {
        if (prefs.getBoolean("hasSavedProgress", false)) {
            String type = prefs.getString("lastType", "FLAT");
            int index = prefs.getInteger("lastIndex", 0);
            currentLevelType = type;
            currentLevelIndex = index;
            loadLevel(LevelStorage.getLevel(type, index));
        }
    }

    private void saveProgress() {
        prefs.putBoolean("hasSavedProgress", true);
        prefs.putString("lastType", currentLevelType);
        prefs.putInteger("lastIndex", currentLevelIndex);
        prefs.flush();
    }

    private void updateBackground(float delta) {
        if (currentLevel != null && currentLevel.isCompleted()) {
            colorTimer += delta;
            if (colorTimer >= colorChangeInterval) {
                currentRainbowIndex = (currentRainbowIndex + 1) % rainbowColors.length;
                currentBackgroundColor = rainbowColors[currentRainbowIndex];
                colorTimer = 0f;
            }
        } else {
            currentBackgroundColor = Color.LIGHT_GRAY;
        }
    }

    @Override
    public void render(float delta) {
        updateBackground(delta);

        Gdx.gl.glClearColor(currentBackgroundColor.r, currentBackgroundColor.g, currentBackgroundColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (state == State.GAME && currentLevel != null) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            currentLevel.update(delta);
            currentLevel.draw(shapeRenderer);
        }

        stage.act(delta);
        stage.draw();

        batch.begin();
        font.draw(batch, "Moves: " + moveCount, 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        stage.getViewport().update(width, height, true);

        if (state == State.GAME && currentLevel != null) {
            currentLevel.reset();
        }

        // ✔ input yeniden atanmalı
        if (state == State.GAME) {
            setupGameUI(); // yeniden UI kur
        } else {
            Gdx.input.setInputProcessor(stage); // menüdeyse sadece stage yeter
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }
}
