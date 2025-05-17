package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PuzzleGame implements Screen {

    private final Preferences prefs = Gdx.app.getPreferences("PuzzleSaveData");

    private Stage stage;
    private Skin skin;
    private Table table;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private Level currentLevel;
    private String currentLevelType;
    private int currentLevelIndex;

    private boolean inGame = false;
    private float rainbowTimer = 0;
    private boolean levelCompleted = false;
    private Label completionLabel;
    private Label infoLabel;
    private float infoTimer = 0f;

    private Texture rainbowTexture;
    private BitmapFont font;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1000, 600, camera);
        shapeRenderer = new ShapeRenderer();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/plain-james-ui.json"));
        font = new BitmapFont();

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        completionLabel = new Label("", labelStyle);
        completionLabel.setAlignment(Align.center);
        completionLabel.setVisible(false);
        completionLabel.setFontScale(1.5f);
        stage.addActor(completionLabel);

        infoLabel = new Label("", labelStyle);
        infoLabel.setAlignment(Align.center);
        infoLabel.setVisible(false);
        infoLabel.setFontScale(1.2f);
        stage.addActor(infoLabel);

        LevelStorage.loadAllLevels(); // <- JSON level'ları yükle

        showMenu();
    }

    private void showMenu() {
        inGame = false;
        table.clear();

        TextButton flat = new TextButton("Flat", skin);
        TextButton convex = new TextButton("Convex", skin);
        TextButton concave = new TextButton("Concave", skin);
        TextButton cont = new TextButton("Continue", skin);
        TextButton exit = new TextButton("Exit", skin);

        flat.addListener(createLoadListener("FLAT", 0));
        convex.addListener(createLoadListener("CONVEX", 0));
        concave.addListener(createLoadListener("CONCAVE", 0));
        cont.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (prefs.getBoolean("hasSavedProgress", false)) {
                    String type = prefs.getString("lastType");
                    int index = prefs.getInteger("lastIndex");
                    loadLevel(type, index);
                }
            }
        });
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap3());
            }
        });

        table.add(flat).pad(10).row();
        table.add(convex).pad(10).row();
        table.add(concave).pad(10).row();
        table.add(cont).pad(10).row();
        table.add(exit).pad(10).row();
    }

    private ClickListener createLoadListener(final String type, final int index) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadLevel(type, index);
            }
        };
    }

    private void loadLevel(String type, int index) {
        currentLevel = LevelStorage.getLevel(type, index);
        if (currentLevel == null) {
            System.err.println("Level not found: " + type + " " + index);
            return;
        }
        currentLevelType = type;
        currentLevelIndex = index;
        currentLevel.reset();

        inGame = true;
        levelCompleted = false;
        rainbowTimer = 0;
        completionLabel.setVisible(false);
        infoLabel.setVisible(false);
        table.clear();

        TextButton info = new TextButton("Info", skin);
        TextButton reset = new TextButton("Reset", skin);
        TextButton save = new TextButton("Save", skin);
        TextButton skip = new TextButton("Skip", skin);
        TextButton menu = new TextButton("Menu", skin);

        info.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showInfo("Mirror Types:\n\n" +
                         "FLAT (Blue): Reflects light in a straight, equal-angle path.\n\n" +
                         "CONCAVE (Purple): Focuses rays toward a focal point like a magnifying glass.\n\n" +
                         "CONVEX (Orange): Spreads rays outward like a dome-shaped mirror.");
            }
        });
        
        reset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentLevel.reset();
            }
        });

        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveProgress();
                showInfo("Progress saved!");
            }
        });

        skip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadNextLevel();
            }
        });

        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenu();
            }
        });

        table.top().left();
        table.add(info).pad(5);
        table.add(reset).pad(5);
        table.add(save).pad(5);
        table.add(skip).pad(5);
        table.add(menu).pad(5);
    }

    private void saveProgress() {
        prefs.putBoolean("hasSavedProgress", true);
        prefs.putString("lastType", currentLevelType);
        prefs.putInteger("lastIndex", currentLevelIndex);
        prefs.flush();
        System.out.println("Progress saved!");
    }

    private void showInfo(String message) {
        infoLabel.setText(message);
        infoLabel.setPosition(
            viewport.getWorldWidth() / 2 - infoLabel.getWidth() / 2,
            viewport.getWorldHeight() - 40
        );
        infoLabel.setVisible(true);
        infoTimer = 3f; // show for 3 seconds
    }

    private void handleLevelCompletion() {
        if (currentLevel.isLevelCompleted() && !levelCompleted) {
            levelCompleted = true;
            completionLabel.setText("Level Complete!\nNext level in 5 seconds...");
            completionLabel.setPosition(
                viewport.getWorldWidth() / 2 - completionLabel.getWidth() / 2,
                viewport.getWorldHeight() / 2 - completionLabel.getHeight() / 2
            );
            completionLabel.setVisible(true);
        }
    }

    private void loadNextLevel() {
        int nextIndex = currentLevelIndex + 1;
        if (LevelStorage.getLevel(currentLevelType, nextIndex) != null) {
            loadLevel(currentLevelType, nextIndex);
        } else {
            showMenu();
        }
        levelCompleted = false;
        rainbowTimer = 0;
        completionLabel.setVisible(false);
    }

    @Override
    public void render(float delta) {
        if (levelCompleted) {
            rainbowTimer += delta;
            float r = (float) (Math.sin(rainbowTimer * 2) * 0.5f + 0.5f);
            float g = (float) (Math.sin(rainbowTimer * 2 + 2) * 0.5f + 0.5f);
            float b = (float) (Math.sin(rainbowTimer * 2 + 4) * 0.5f + 0.5f);
            Gdx.gl.glClearColor(r, g, b, 1);
            if (rainbowTimer >= 5) {
                loadNextLevel();
            }
        } else {
            Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();

        if (infoLabel.isVisible()) {
            infoTimer -= delta;
            if (infoTimer <= 0f) {
                infoLabel.setVisible(false);
            }
        }

        if (inGame && currentLevel != null) {
            currentLevel.update(delta);
            handleLevelCompletion();

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            currentLevel.draw(shapeRenderer);
            shapeRenderer.end();

            if (Gdx.input.justTouched()) {
                Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);
                currentLevel.handleClick(touch.x, touch.y);
            }
        }
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        skin.dispose();
        font.dispose();
        if (rainbowTexture != null) {
            rainbowTexture.dispose();
        }
    }
}
