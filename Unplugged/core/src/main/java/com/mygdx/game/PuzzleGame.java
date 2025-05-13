package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PuzzleGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public Level currentLevel;
    public int currentLevelIndex = 0;
    public String currentLevelType = "FLAT";

    public Stage stage;
    private Skin skin;
    public ShapeRenderer shapeRenderer;
    public OrthographicCamera camera;

    public int moveCount = 0;

    public Color[] rainbowColors = new Color[] {
        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
        Color.CYAN, Color.BLUE, Color.MAGENTA
    };

    public float colorTimer = 0f;
    public float colorChangeInterval = 0.15f;
    public int currentRainbowIndex = 0;
    public Color currentBackgroundColor = Color.LIGHT_GRAY;

    private Preferences prefs;
    private final Random random = new Random();

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        LevelStorage.loadAllLevels();
        setScreen(new PuzzleMenuScreen(this));
    }

    public void loadLevel(Level level) {
        if (prefs == null) prefs = Gdx.app.getPreferences("LightPuzzleProgress");
        if (level == null) {
            System.err.println("Level is null!");
            return;
        }

        this.currentLevel = level;
        this.moveCount = 0;
        this.colorTimer = 0f;
        this.currentRainbowIndex = 0;
        this.currentBackgroundColor = Color.LIGHT_GRAY;

        // Set up UI stage
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/PuzzleAssets/skin/plain-james-ui.json"));
        setupUI();

        // InputProcessor will be set in LevelScreen (do not set it here)
        setScreen(new LevelScreen(this));
        saveProgress();
    }

    private void setupUI() {
        Table table = new Table();
        table.top().left().pad(10);
        table.setFillParent(true);
        table.defaults().width(100).height(40).padRight(10);

        TextButton info = new TextButton("Info", skin);
        TextButton reset = new TextButton("Reset", skin);
        TextButton skip = new TextButton("Skip", skin);
        TextButton save = new TextButton("Save", skin);
        TextButton menu = new TextButton("Menu", skin);

        // Each button has its own ClickListener
        info.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Dialog dialog = new Dialog("Mirror Types Guide", skin);
                dialog.text("FLAT (Blue): Basic reflection\nCONVEX (Pink): Diverges rays\nCONCAVE (Orange): Converges rays\nClick mirrors to rotate\nGoal: Direct light through door gap");
                dialog.button("OK").show(stage);
            }
        });

        reset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (currentLevel != null) {
                    currentLevel.reset();
                    moveCount = 0;
                    colorTimer = 0f;
                    currentRainbowIndex = 0;
                    currentBackgroundColor = Color.LIGHT_GRAY;
                }
            }
        });

        skip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                skipRandomLevel();
            }
        });

        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                saveProgress();
                Dialog dialog = new Dialog("Saved", skin);
                dialog.text("Progress saved.").button("OK").show(stage);
            }
        });

        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                returnToMenu();
            }
        });

        table.add(info);
        table.add(reset);
        table.add(skip);
        table.add(save);
        table.add(menu);
        stage.addActor(table);
    }

    private void skipRandomLevel() {
        if (currentLevelType == null) {
            System.err.println("currentLevelType is null. Defaulting to FLAT.");
            currentLevelType = "FLAT";
        }

        int total = LevelStorage.getLevelCount(currentLevelType);
        if (total <= 1) {
            System.err.println("Not enough levels to skip in type: " + currentLevelType);
            return;
        }

        int newIndex = currentLevelIndex;
        int attempts = 0;
        while (newIndex == currentLevelIndex && attempts < 10) {
            newIndex = random.nextInt(total);
            attempts++;
        }

        if (newIndex < 0 || newIndex >= total) {
            System.err.println("Invalid index selected for type: " + currentLevelType);
            return;
        }

        Level newLevel = LevelStorage.getLevel(currentLevelType, newIndex);
        if (newLevel != null) {
            currentLevelIndex = newIndex;
            System.out.println("Skipping to " + currentLevelType + " level index: " + currentLevelIndex);
            loadLevel(newLevel);
        } else {
            System.err.println("Failed to load level: " + currentLevelType + " index: " + newIndex);
        }
    }

    public void returnToMenu() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        setScreen(new PuzzleMenuScreen(this));
    }

    public void loadSavedProgress() {
        if (prefs == null) prefs = Gdx.app.getPreferences("LightPuzzleProgress");

        if (prefs.getBoolean("hasSavedProgress", false)) {
            String type = prefs.getString("lastType", "FLAT");
            int index = prefs.getInteger("lastIndex", 0);
            currentLevelType = type;
            currentLevelIndex = index;
            loadLevel(LevelStorage.getLevel(type, index));
        } else {
            currentLevelType = "FLAT";
            currentLevelIndex = 0;
            loadLevel(LevelStorage.getLevel("FLAT", 0));
        }
    }

    private void saveProgress() {
        if (prefs != null && currentLevelType != null) {
            prefs.putBoolean("hasSavedProgress", true);
            prefs.putString("lastType", currentLevelType);
            prefs.putInteger("lastIndex", currentLevelIndex);
            prefs.flush();
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }

        if (currentLevel != null) {
            currentLevel.reset();
        }
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
