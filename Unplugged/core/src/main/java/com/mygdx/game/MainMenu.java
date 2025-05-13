package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class MainMenu implements Screen {

    private SpriteBatch batch;
    private Sprite splash;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private TextButton buttonPlay, buttonExit, buttonSettings, buttonSubgame;
    private BitmapFont white, black; 
    private Label heading;
    private TweenManager tweenManager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture menuTexture;
    Image menuImage;
    float imageToStageWitdth, imageToStageHeight;

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        final float GAME_WORLD_WIDTH = Gdx.graphics.getWidth();
        final float GAME_WORLD_HEIGHT = Gdx.graphics.getHeight();
        viewport = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);

        batch = new SpriteBatch();
        stage = new Stage(viewport);

        Texture splashTexture = new Texture("Unplugged/assets/Background.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.getViewport().apply();
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("Unplugged/assets/menuTools.atlas");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        menuTexture = new Texture("Unplugged/assets/ui/menu.png");
        menuImage = new Image(menuTexture);
        menuImage.setSize(menuTexture.getWidth() * 5, menuTexture.getHeight() * 5);

        imageToStageWitdth = menuImage.getWidth() / GAME_WORLD_WIDTH;
        imageToStageHeight = menuImage.getHeight() / GAME_WORLD_HEIGHT;

        white = new BitmapFont(Gdx.files.internal("Unplugged/assets/font/white.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("Unplugged/assets/font/black.fnt"), false);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal");
        textButtonStyle.down = skin.getDrawable("button.pressed");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;

        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonExit.pad(20);

        buttonPlay = new TextButton("PLAY", textButtonStyle);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadMenu());
            }
        });
        buttonPlay.pad(20);

        buttonSubgame = new TextButton("SUBGAMES", textButtonStyle);
        buttonSubgame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.25f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new SubgameMenu()); 
                    }
                })));

            }
        });
        buttonSubgame.pad(20);

        buttonSettings = new TextButton("SETTINGS", textButtonStyle);
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Settings());
            }
        });
        buttonSettings.pad(20);

        LabelStyle headingStyle = new LabelStyle(white, Color.WHITE);
        
        heading = new Label("Unplugged", headingStyle);
        heading.setFontScale(3);

        //menuImage.setPosition(Gdx.graphics.getWidth() / 2 - menuImage.getWidth() / 2, Gdx.graphics.getHeight() / 2 - menuImage.getHeight() / 2);
        //stage.addActor(menuImage);

        table.setBackground(skin.getDrawable("menu"));
        table.add(heading);
        table.getCell(heading).spaceBottom(50);
        table.row();
        table.add(buttonPlay);
        table.getCell(buttonPlay).spaceBottom(10);
        table.row();
        table.add(buttonSubgame);
        table.getCell(buttonSubgame).spaceBottom(10);
        table.row();
        table.add(buttonSettings);
        table.getCell(buttonSettings).spaceBottom(10);
        table.row();
        table.padBottom(50);
        table.add(buttonExit);
        stage.addActor(table);


        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        Timeline.createSequence().beginSequence()
        .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,0, 1))
        .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,1, 0))
        .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,0, 0))
        .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,1, 0))
        .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,1, 1))
        .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,0, 1))
        .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,1, 1))
        .end().repeat(Tween.INFINITY, 0).start(tweenManager);

        Timeline.createSequence().beginSequence()
        .push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
        .push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
        .push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
        .push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .25f).target(1))
        .push(Tween.to(buttonExit, ActorAccessor.ALPHA, .25f).target(1))
        .end().start(tweenManager);

        Tween.from(table, ActorAccessor.ALPHA, .5f).target();
        Tween.from(table, ActorAccessor.Y, .5f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.getCamera().update();

        tweenManager.update(delta);

        batch.begin();
        splash.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { 
        stage.getViewport().update(width, height);
        stage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        //stage.setViewport(width, height, true);
        table.setClip(true);
        table.setSize(width, height);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
        white.dispose();
        black.dispose();
        batch.dispose();
        splash.getTexture().dispose();
    }
    
}
