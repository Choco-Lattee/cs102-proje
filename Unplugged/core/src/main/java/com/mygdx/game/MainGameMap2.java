package com.mygdx.game;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class MainGameMap2 implements Screen {
    // game variables
    private static final float TIMESTEP = 1 / 60f;
    private static final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
    private static final int GAME_STATE = 1, PAUSE_STATE = 2, SETTING_STATE = 3, SAVE_STATE = 4;
    private boolean debug = false;
    private int screenState = 1;
    private World world;
    private SpriteBatch spriteBatch;
    private Box2DDebugRenderer debugRenderer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private Box2DPlayer player;
    private boolean playerDied = false, canPassNextGame = false;
    private ArrayList<MicrowaveRobot> robots;
    private Npc npc1;
    private MyContactListener cl;
    private Texture all;
    private Box2DMapObjectParser parser;
    private Texture reversedAll;
    private static final int FRAME_COLS = 8, FRAME_ROWS = 9;
    private GameData gameData;
    private Vector2 playerPosition = new Vector2(50, 50);

    private final float PIXEL_TO_METERS = 32;
    private final float UNIT_SCALE = 1 / PIXEL_TO_METERS;
    private int game;

    // pause menu variables
    Viewport viewportPause, viewportSetting, viewportSave;
    OrthographicCamera pauseCam, settingCam, saveCam;
    private SpriteBatch pauseBatch;
    private Sprite splash;
    private PauseStage pauseStage;
    private Stage dialogStage, gameStage;
    private TextureAtlas atlas;
    private Skin skin;
    private BitmapFont white, black; 
    private Dialog dialog;
    private Image threeHeartImage, twoHeartImage, oneHeartImage;
    private TextField pointPanel;
    

    // settings menu variables
    private SettingStage settingStage;
    // save menu variables
    private SaveLoadStage saveStage;

    @Override
    public void show() {
        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener(); 
        world.setContactListener(cl);


        debugRenderer = new Box2DDebugRenderer();
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera(Gdx.graphics.getWidth() / 12.5f, Gdx.graphics.getHeight() / 12.5f);


        TiledMap map = new TmxMapLoader().load("assets/maps/mainMap2.tmx");

        parser = new Box2DMapObjectParser(UNIT_SCALE);
        parser.load(world, map);

        mapRenderer = new OrthogonalTiledMapRenderer(map, parser.getUnitScale());

        game = SaveLoadStage.getLastGameData();
        gameData = GameData.loadPlayerPosition(game, 2);
        playerPosition = new Vector2(gameData.x, gameData.y);

        all = new Texture("assets/AnimationSheet_Character.png");
        reversedAll = new Texture("assets/AnimationSheet_Character_Reversed.png");
        TextureRegion[][] tmp = TextureRegion.split(all, all.getWidth() / FRAME_COLS, all.getHeight() / FRAME_ROWS);
        TextureRegion[][] tmpReversed = TextureRegion.split(reversedAll, reversedAll.getWidth() / FRAME_COLS, reversedAll.getHeight() / FRAME_ROWS);
        player = new Box2DPlayer(world, playerPosition, tmp, tmpReversed);
        player.loadHeartAndPoint(gameData.point, gameData.heart, gameData.currentMap);
        player.setPlayerCurrentMap(2);
        Texture still = new Texture("assets/Microwave/still.png");
        MicrowaveRobot robot = new MicrowaveRobot(world, still, 230, 505);
        robots = new ArrayList<MicrowaveRobot>();
        robots.add(robot);
        MicrowaveRobot robot2 = new MicrowaveRobot(world, still, 700, 485);
        robots.add(robot2);
        MicrowaveRobot robot3 = new MicrowaveRobot(world, still, 1030, 255);
        robots.add(robot3);
        MicrowaveRobot robot4 = new MicrowaveRobot(world, still, 835, 105);
        robots.add(robot4);
        MicrowaveRobot robot5 = new MicrowaveRobot(world, still, 770, 250);
        robots.add(robot5);

        Texture npcTex = new Texture("assets/npc1.png");
        TextureRegion[][] npcTmp = TextureRegion.split(npcTex, npcTex.getWidth() / 24, npcTex.getHeight() / 1);
        npc1 = new Npc(world, npcTmp, 1470, 117);

        MapLayer objectLayer = map.getLayers().get("box2d"); // Replace with your layer name
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
            Body body = (Body) object.getProperties().get("lavaBody");
        
                if (object.getProperties().containsKey("isSensor")) {
                    boolean isSensor = Boolean.parseBoolean(object.getProperties().get("isSensor").toString());

                // Set all fixtures in that body to be sensors
                    for (Fixture fixture : body.getFixtureList()) {
                        fixture.setSensor(isSensor);
                        fixture.setUserData("lava");
                    }
                }
            }
        }
        //player.getBody().applyAngularImpulse(5, true); // açısal etki 

        final float GAME_WORLD_WIDTH = Gdx.graphics.getWidth();
        final float GAME_WORLD_HEIGHT = Gdx.graphics.getHeight();

        // dialog and game stages
        Viewport dialogViewport = new FitViewport(GAME_WORLD_WIDTH / 5, GAME_WORLD_HEIGHT / 5, camera);
        Viewport gameViewport = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);
        gameStage = new Stage(gameViewport);
        dialogStage = new Stage(dialogViewport);
        atlas = new TextureAtlas("assets/menuTools.atlas");
        skin = new Skin(atlas);
        white = new BitmapFont(Gdx.files.internal("assets/font/white.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("assets/font/black.fnt"), false);
        BitmapFont miniFont = new BitmapFont(Gdx.files.internal("assets/font/miniFont.fnt"));
        miniFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        miniFont.getData().setScale(0.25f,0.25f);
        threeHeartImage = new Image(skin.getDrawable("threeHeart"));
        twoHeartImage = new Image(skin.getDrawable("twoHeart"));
        oneHeartImage = new Image(skin.getDrawable("oneHeart"));
        TextureAtlas fieldAtlas = new TextureAtlas("assets/settings_tools.atlas");
        Skin fieldSkin = new Skin(fieldAtlas);
        TextFieldStyle panelStyle = new TextFieldStyle();
        panelStyle.background = fieldSkin.getDrawable("panel");
        panelStyle.messageFont = miniFont;
        panelStyle.font = miniFont;
        panelStyle.fontColor = Color.WHITE;
        pointPanel = new TextField("Point: " + player.getPoint(), panelStyle);
        WindowStyle windowStyle =  new WindowStyle();
        windowStyle.background = skin.getDrawable("menu");
        windowStyle.titleFont = miniFont;
        windowStyle.titleFontColor = Color.BLACK;
        dialog = new Dialog("ALEX", windowStyle);
        LabelStyle labelStyle = new LabelStyle(black, Color.BLACK);
        Label label = new Label("\"Hey help me!\" \nPress K to go to\n the next game", labelStyle);
        label.setFontScale(0.25f);
        dialog.text(label);
        dialog.setSize(Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 25);
        dialogStage.addActor(dialog);
        dialogStage.getViewport().apply();
        gameStage.getViewport().apply();

        //pause menu
        pauseCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pauseCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        settingCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        settingCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        saveCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        saveCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        viewportPause = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, pauseCam);
        viewportSetting = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, settingCam);
        viewportSave = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, saveCam);

        pauseBatch = new SpriteBatch();
        pauseStage = new PauseStage(viewportPause, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        settingStage = new SettingStage(viewportSetting, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        saveStage = new SaveLoadStage(viewportSave, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, true);

        Texture splashTexture = new Texture("assets/Background.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        pauseStage.getViewport().apply();
        settingStage.getViewport().apply();
        saveStage.getViewport().apply();

        // input processor
        pauseStage.getContinueButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenState = GAME_STATE;
            }
        });
        pauseStage.getExitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadMenu()); 
            }
        });
        pauseStage.getSaveButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenState = SAVE_STATE;
            }
        });
        pauseStage.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenState = SETTING_STATE;
            }
        });
        saveStage.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenState = PAUSE_STATE;
            }
        });
        settingStage.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenState = PAUSE_STATE;
            }
        });

        Stage addedStage;
        if (screenState == SETTING_STATE) {
            addedStage = settingStage;
        }
        else if (screenState == SAVE_STATE) {
            addedStage = saveStage;
        }
        else {
            addedStage = pauseStage;
        }

        saveStage.setInputHandler(player.getPlayerPosition().x, player.getPlayerPosition().y, player.getPoint(), player.getHeart(), player.getCurrentMap());

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        if (screenState == PAUSE_STATE) {
                            screenState = GAME_STATE;
                        }
                        else {
                            screenState = PAUSE_STATE;
                        }
                        break;
                    case Keys.K:
                        if (canPassNextGame) {
                            GameData.savePlayerPosition(50, 70, player.getPoint(),  player.getHeart(), player.getCurrentMap(), game);
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(new PuzzleGame()));
                        }
                    case Keys.P:
                        player.setPlayerPosition();
                        System.out.println(playerPosition);
                        GameData.savePlayerPosition(player.getPlayerPosition().x, player.getPlayerPosition().y, player.getPoint(),  player.getHeart(), player.getCurrentMap(), game);
                        System.out.println(player.getPlayerPosition());
                }
                return false;
            }
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY / 12.5f;
                return false;
            }
        }, player, addedStage));

    


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (screenState == GAME_STATE) {
            mapRenderer.setView(camera);
            mapRenderer.render();
    
            world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
    
            camera.position.set(player.getX() + player.getWidth() /  2, player.getY() + player.getHeight() / 2, 0);
            camera.update();
            
            spriteBatch.setProjectionMatrix(camera.combined);
            spriteBatch.begin();
            player.setPosition(player.getBody().getPosition().x - player.getWidth() / 2, player.getBody().getPosition().y - 5.6f);
            player.setRotation(player.getBody().getAngle() * MathUtils.radiansToDegrees);
            player.update(delta, cl);
            if (player.isDeath()) {
                playerDied = true;
            }
            if (playerDied) {
                playerDied = false; // reset flag
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new DeathScreen());
                    }
                });
            }
            float x = 0;
            for (MicrowaveRobot robot: robots) {
                if (robot.isRight()) {
                    x = +0.5f;
                }
                else {
                    x = -0.5f;
                }
                robot.setPosition(robot.getBody().getPosition().x - robot.getWidth() / 2 + x, robot.getBody().getPosition().y - robot.getHeight() / 3);
                robot.setRotation(robot.getBody().getAngle() * MathUtils.radiansToDegrees);
                robot.update(delta, cl, player);
            }
            npc1.setPosition(npc1.getBody().getPosition().x - npc1.getWidth() / 2 + x, npc1.getBody().getPosition().y - npc1.getHeight() / 2);
            npc1.setRotation(npc1.getBody().getAngle() * MathUtils.radiansToDegrees);
            npc1.update(delta, cl, player);
            gameStage.clear();
            if (player.getHeart() == 3) {
                gameStage.addActor(threeHeartImage);
                threeHeartImage.setSize(15, 5);
                threeHeartImage.setPosition(player.getX() + Gdx.graphics.getWidth() / 11, player.getY() + Gdx.graphics.getHeight() / 12);
            }
            if (player.getHeart() == 2) {
                gameStage.addActor(twoHeartImage);
                twoHeartImage.setSize(10, 5);
                twoHeartImage.setPosition(player.getX() + Gdx.graphics.getWidth() / 11, player.getY() + Gdx.graphics.getHeight() / 12);
            }

            if (player.getHeart() == 1) {
                gameStage.addActor(oneHeartImage);
                oneHeartImage.setSize(5, 5);
                oneHeartImage.setPosition(player.getX() + Gdx.graphics.getWidth() / 11, player.getY() + Gdx.graphics.getHeight() / 12);
            }
            gameStage.addActor(pointPanel);
            pointPanel.setSize(30, 30);
            pointPanel.setMessageText("Point: " + player.getPoint());
            pointPanel.setPosition(player.getX() + Gdx.graphics.getWidth() / 15, player.getY() + Gdx.graphics.getHeight() / 15);
            gameStage.act(delta);
            gameStage.draw();

            if (cl.isOnContactWithNPC()) {
                canPassNextGame = true;
                dialog.setPosition(player.getX() + 5, player.getY() + 5);
                dialogStage.act(delta);
                dialogStage.draw();
            }
            player.draw(spriteBatch);
            npc1.draw(spriteBatch);
            for (MicrowaveRobot robot: robots) {
                 if (!robot.getDeathCondition()) {
                    robot.draw(spriteBatch);
                }
                for (Fireball fireball: robot.getFireballs()) {
                    if (!fireball.hasExploded()) {
                        fireball.setPosition(fireball.getBody().getPosition().x - fireball.getWidth() / 2, fireball.getBody().getPosition().y - fireball.getHeight() / 2);
                        fireball.draw(spriteBatch);
                    }
                }
            }
            spriteBatch.end();
            if (debug) {
                debugRenderer.render(world, camera.combined);
            }
        }
        else if (screenState == PAUSE_STATE) {
            pauseStage.getBatch().setProjectionMatrix(pauseStage.getCamera().combined);
            pauseStage.getCamera().update();
    
            pauseBatch.begin();
            splash.draw(pauseBatch);
            pauseBatch.end();
    
            pauseStage.act(delta);
            pauseStage.draw();
        }
        else if (screenState == SETTING_STATE) {
            // setting
            settingStage.getBatch().setProjectionMatrix(settingStage.getCamera().combined);
            settingStage.getCamera().update();
    
            pauseBatch.begin();
            splash.draw(pauseBatch);
            pauseBatch.end();
    
            settingStage.act(delta);
            settingStage.draw();
        }
        else {
            // save
            saveStage.getBatch().setProjectionMatrix(saveStage.getCamera().combined);
            saveStage.getCamera().update();
    
            pauseBatch.begin();
            splash.draw(pauseBatch);
            pauseBatch.end();
    
            saveStage.act(delta);
            saveStage.draw();
        }
        Stage addedStage;
        if (screenState == SETTING_STATE) {
            addedStage = settingStage;
        }
        else if (screenState == SAVE_STATE) {
            player.setPlayerPosition();
            System.out.println(playerPosition);
            saveStage.setInputHandler(player.getPlayerPosition().x, player.getPlayerPosition().y, player.getPoint(), player.getHeart(), player.getCurrentMap());
            addedStage = saveStage;
        }
        else {
            addedStage = pauseStage;
        }

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        if (screenState == PAUSE_STATE) {
                            screenState = GAME_STATE;
                        }
                        else {
                            screenState = PAUSE_STATE;
                        }
                        break;
                    case Keys.K:
                        if (canPassNextGame) {
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap3());
                        }
                }
                return false;
            }
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY / 12.5f;
                return false;
            }
        }, player, addedStage));
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 5f;
        camera.viewportHeight = height / 5f;
        camera.update();

        pauseStage.getViewport().update(width, height);
        pauseStage.setSize(width, height);
        pauseStage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        pauseStage.getTable().invalidateHierarchy();

        settingStage.getViewport().update(width, height);
        settingStage.setSize(width, height);
        settingStage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        settingStage.getTable().setClip(true);
        settingStage.getTable().setSize(width, height);

        saveStage.getViewport().update(width, height);
        saveStage.setSize(width, height);
        saveStage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        saveStage.getTable().setClip(true);
        saveStage.getTable().setSize(width, height);
        //stage.setViewport(width, height, true);
        pauseStage.getTable().setClip(true);
        pauseStage.getTable().setSize(width, height);
        splash.setSize(width, height);

    }



    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        mapRenderer.dispose();
        debugRenderer.dispose();
        player.getTexture().dispose();
    }
}
