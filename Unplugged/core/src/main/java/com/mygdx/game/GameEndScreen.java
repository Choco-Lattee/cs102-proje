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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameEndScreen implements Screen{
    private SpriteBatch batch;
    private Sprite splash;
    private Stage stage;
    private Table table;
    private TextureAtlas atlas;
    private Skin skin;
    private List<String> list;
    private ScrollPane scrollPane;
    private TextButton play, back;
    private BitmapFont white, black;
    private Label label;
    private OrthographicCamera camera;
    private Viewport viewport;


    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        final float GAME_WORLD_WIDTH = Gdx.graphics.getWidth();
        final float GAME_WORLD_HEIGHT = Gdx.graphics.getHeight();
        viewport = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);

        batch = new SpriteBatch();
        stage = new Stage(viewport);


        Texture splashTexture = new Texture("assets/Background.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.getViewport().apply();

        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("assets/button.atlas");
        skin = new Skin(atlas);

        table = new Table(skin);
        //table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);

        white = new BitmapFont(Gdx.files.internal("assets/font/white.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("assets/font/black.fnt"), false);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal");
        textButtonStyle.down = skin.getDrawable("button.pressed");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;

        //"font": "white", "fontColor": "white"
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = white;
        labelStyle.fontColor = Color.WHITE;
        label = new Label("Game End", labelStyle);

        ListStyle listStyle = new ListStyle();
        listStyle.font = white;
        listStyle.fontColorUnselected = Color.WHITE;
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.selection = skin.getDrawable("button.pressed");
        list = new List<String>(listStyle);
        list.setItems(new String[] { "one", "two", "three", "dfnvdfsvldsnvkjndsvjsd"
        , "cidajmclşmsdlşmvdmsvklmsdvksdvsdvkls", "jndj", "edfs0", "dvsd", "dsfsdf","vfdnvhjnfdv", "fbfgnbfdv"});
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        scrollPaneStyle.hScrollKnob = skin.getDrawable("button.normal");
        scrollPaneStyle.vScrollKnob = skin.getDrawable("button.normal");
        scrollPane =  new ScrollPane(list, scrollPaneStyle);

        play = new TextButton("PLAY AGAIN", textButtonStyle);
        play.pad(15);
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.25f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap1()); 
                    }
                })));
            }
        });

        back = new TextButton("BACK", textButtonStyle);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.25f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu()); 
                    }
                })));
            }
        });
        back.pad(10);

        table.setBounds(0,0, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        table.add(label).colspan(3).expandX().spaceBottom(50).row();
        //table.add(scrollPane).uniformX().expandY().top().left();
        table.add(play).uniformX();
        table.add(back).uniformX().bottom().right();

        stage.addActor(table);

        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.25f)));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.getCamera().update();

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
        splash.setSize(width, height);
        table.invalidateHierarchy();
        table.setClip(true);
        table.setSize(width, height);
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
    }
}
