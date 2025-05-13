package com.mygdx.game;

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
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class Play implements Screen{

    private World world;
    private Box2DDebugRenderer box2dDebugRenderer;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private Box2DMapObjectParser parser;
    private TextureAtlas atlas;
    private Droid droid1;
    private RayHandler rayHandler;
    private ConeLight coneLight1;
    private ConeLight coneLight2;
    private Cctv cctv;
    private boolean debug = true;
    private Viewport pauseViewport;
    private OrthographicCamera pauseCam;
    private SpriteBatch pauseBatch;
    private Sprite pauseSprite;
    private PauseStage pauseStage;
    private TextureAtlas pauseAtlas;
    //private Skin skin;
    private BitmapFont white, black;
    private boolean paused = false;
    private boolean isOnSetting = false;
    private ArrayList<Door> doors = new ArrayList<Door>();
    private Door door1;
    private MyContactListener contactListener;
    private Furnace furnace1;
    private Furnace furnace2;
    private Furnace furnace3;
    private Furnace furnace4;
    private ArrayList<Furnace> furnaces = new ArrayList<Furnace>();
    private SNpc[] npcs = new SNpc[3];
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private boolean playerDied = false;
    private boolean playerSucceed = false;
    
    @Override
    public void show() {
        ArrayList<Vector2> arr = new ArrayList<Vector2>();
        //arr.add(new Vector2(1100f,250));
        arr.add(new Vector2(1000f,250));
        arr.add(new Vector2(1000f,220));
        arr.add(new Vector2(1000f,200));
        arr.add(new Vector2(900f,200));
        arr.add(new Vector2(900f,220));
        arr.add(new Vector2(900f,250));

        for(Vector2 vec: arr){
            System.out.println("arr: x; " + vec.x + " y; " + vec.y);
        }

        contactListener = new MyContactListener();
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(contactListener);
        box2dDebugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        atlas = new TextureAtlas("atlas/walkingTest.atlas");
        
        map = new TmxMapLoader().load("maps/map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        
        parser = new Box2DMapObjectParser();
        parser.load(world, map);

        player = new Player(atlas, furnaces);
        player.setPosition(330f, 125);
        //"1100f, 250"

        player.setBox(world.createBody(player.getBoxDef()));
        player.getBox().createFixture(player.getFixtureDef()).setUserData("player");
        sprites.add(player);

    //droid
        droid1 = new Droid(atlas, arr, player, world, rayHandler, map, furnaces);
        sprites.add(droid1);

    //cctv
        TextureRegion c = atlas.findRegion("cctv");
        cctv = new Cctv(1100f, 250, 2, c, rayHandler, player, map, furnaces);

    //door
        TextureRegion d = atlas.findRegion("door");
        TextureRegion k = atlas.findRegion("key");
        doors.add(new Door(world, d, k, atlas.findRegion("1 - Kopya"), 658f, 320, 658f, 250, 1, contactListener));
        doors.add(new Door(world, d, k, atlas.findRegion("2 - Kopya"), 337f, 112, 668f, 420, 2, contactListener));
        doors.add(new Door(world, d, k, atlas.findRegion("3 - Kopya"), 97f, 417, 337f, 82, 3, contactListener));
        doors.add(new Door(world, d, k, atlas.findRegion("4 - Kopya"), 97f, 592, 97f, 377, 4, contactListener));
        doors.add(new Door(world, d, k, atlas.findRegion("5 - Kopya"), 930f, 496, 97f, 652, 5, contactListener));
        doors.add(new Door(world, d, k, atlas.findRegion("6 - Kopya"), 834f, 895, 930f, 566, 6, contactListener));
    
    //furnace
        TextureRegion f1 = atlas.findRegion("furnace1 - Kopya");
        TextureRegion f2 = atlas.findRegion("furnace2");
        TextureRegion f3 = atlas.findRegion("furnace3");
        TextureRegion f4 = atlas.findRegion("furnace4");

        furnaces.add(new Furnace(f1, 1, 966.64435f, 188.32227f, world));
        furnaces.add(new Furnace(f1, 1, 887.74445f, 188.32227f, world));
        furnaces.add(new Furnace(f1, 1, 808.611f, 189.54448f, world));
        furnaces.add(new Furnace(f1, 1, 727.68884f, 189.61115f, world));
        furnaces.add(new Furnace(f1, 1, 647.46655f, 189.61115f, world));
        furnaces.add(new Furnace(f1, 1, 567.50006f, 189.61115f, world));
        furnaces.add(new Furnace(f1, 1, 170.34648f, 492.83008f, world));
        furnaces.add(new Furnace(f1, 1, 103.34646f, 558.3412f, world));
        furnaces.add(new Furnace(f1, 1, 105.24645f, 492.83008f, world));
        furnaces.add(new Furnace(f1, 1, 39.40201f, 492.83008f, world));
        furnaces.add(new Furnace(f1, 1, 43.44646f, 561.019f, world));
        furnaces.add(new Furnace(f1, 1, 506.13525f, 573.25696f, world));
        furnaces.add(new Furnace(f1, 1, 570.21295f, 573.25696f, world));
        furnaces.add(new Furnace(f1, 1, 775.95984f, 477.09695f, world));
        furnaces.add(new Furnace(f1, 1, 824.0595f, 510.73523f, world));
        furnaces.add(new Furnace(f1, 1, 338.5419f, 790.49335f, world));
        furnaces.add(new Furnace(f1, 1, 522.7871f, 876.2289f, world));
        furnaces.add(new Furnace(f1, 1, 504.09824f, 828.4735f, world));
        furnaces.add(new Furnace(f1, 1, 601.3431f, 828.87354f, world));
        furnaces.add(new Furnace(f1, 1, 584.25433f, 878.00696f, world));
        furnaces.add(new Furnace(f1, 1, 792.04333f, 797.67334f, world));
        furnaces.add(new Furnace(f1, 1, 169.1672f, 555.83514f, world));
        
        furnaces.add(new Furnace(f2, 2, 955.2886f, 292.0111f, world));
        furnaces.add(new Furnace(f2, 2, 827.655f, 293.0111f, world));
        furnaces.add(new Furnace(f2, 2, 411.45584f, 197.56262f, world));
        furnaces.add(new Furnace(f2, 2, 297.8002f, 197.56262f, world));
        furnaces.add(new Furnace(f2, 2, 297.68912f, 361.21835f, world));
        furnaces.add(new Furnace(f2, 2, 411.34476f, 357.35168f, world));
        furnaces.add(new Furnace(f2, 2, 714.255f, 871.5306f, world));
        furnaces.add(new Furnace(f2, 2, 714.9993f, 806.0196f, world));

        furnaces.add(new Furnace(f3, 3, 513.5876f, 293.6103f, world));
        furnaces.add(new Furnace(f3, 3, 532.36487f, 488.38193f, world));
        furnaces.add(new Furnace(f3, 3, 902.9159f, 800.62006f, world));
        
        furnaces.add(new Furnace(f4, 4, 355.268f, 270.06393f, world));
        furnaces.add(new Furnace(f4, 4, 803.2529f, 578.0882f, world));
        
        for(Furnace furnace: furnaces){
            sprites.add(furnace);
        }
    //npc
        npcs[0] = new SNpc(atlas.findRegion("npc1"), 115f, 682, contactListener, 1, world);
        npcs[1] = new SNpc(atlas.findRegion("npc2"), 945f, 586, contactListener, 2, world);
        npcs[2] = new SNpc(atlas.findRegion("npc3"), 844f, 985, contactListener, 3, world);

    //pause menu----------------------------------------------------------------------
        pauseCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pauseCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        pauseViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), pauseCam);

        pauseBatch = new SpriteBatch();
        pauseStage = new PauseStage(pauseViewport, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture splashTexture = new Texture("assets/atlas/Background.png");
        pauseSprite = new Sprite(splashTexture);
        pauseSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        pauseStage.getViewport().apply();

        // input processor
        pauseStage.getContinueButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = false;
            }
        });
        pauseStage.getExitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                
            }
        });
        pauseStage.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //isOnSetting = true; needs to fix
            }
        });    

        rayHandler = new RayHandler(world);
        rayHandler.setCombinedMatrix(
            camera.combined,
            camera.position.x,
            camera.position.y,
            0,
            0
        );
        rayHandler.setAmbientLight(0.75f);

        coneLight1 = new ConeLight(rayHandler, 80, Color.BLUE, 120f, 0, 0, 90f, 45f);
        coneLight1.setSoftnessLength(0f);

        coneLight1.setPosition(droid1.getBox().getPosition());
        droid1.setVisionCone(coneLight1);

        coneLight2 = new ConeLight(rayHandler, 80, Color.RED, 120f, 0, 0, 120f, 15f);
        coneLight2.setSoftnessLength(0f);
        coneLight2.setPosition(new Vector2(cctv.getX() + 20, cctv.getY() + 5));
        cctv.setVisionCone(coneLight2);
        
        player.getBoxShape().dispose();
        droid1.getBoxShape().dispose();

        pauseStage.getContinueButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = false;
            }
        });
        pauseStage.getExitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                
            }
        });
        pauseStage.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //isOnSetting = true; needs to fix
            }
        });

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        System.out.println("PRESSED");
                        if (paused) {
                            paused = false;
                        }
                        else {
                            paused = true;
                        }
                        break;
                }
                return false;
            }
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY / 12.5f;
                return false;
            }
        }, player, pauseStage));
    }

    public void insertionSortY(ArrayList<Sprite> sprites){
        for(int i = 0; i < sprites.size(); i++){
            Sprite temp = sprites.get(i);
            int a = 0;

            while(sprites.get(a).getY() > temp.getY() && a < i) a++;

            for(int b = i - 1; b >= a; b--){
                sprites.set(b + 1, sprites.get(b));
            }

           sprites.set(a, temp);
        }
    }
    
    @Override
    public void render(float delta) {
        if(!paused){
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            world.step(1 / 60f, 8, 3);

            renderer.setView(camera);
            renderer.render();

            if(debug)
                box2dDebugRenderer.render(world, camera.combined);
            
            camera.position.set(player.getBox().getPosition(),0);
            camera.update();

            renderer.getBatch().begin();

            if(contactListener.getNpcRescued()[2]){
                playerSucceed = true;
            }

            if(playerSucceed){
                playerSucceed = false;

                Gdx.app.postRunnable(new Runnable(){
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap2());
                    }
                });
            }

            if(!player.getIsAlive()){
                playerDied = true;
            }

            if(playerDied){
                playerDied = false;

                Gdx.app.postRunnable(new Runnable(){
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new DeathScreen());
                    }
                });
            }

            droid1.draw(renderer.getBatch());

            for(Door door: doors){
                door.draw(renderer.getBatch());
            }

            for(int i = 0; i < npcs.length; i++){
                npcs[i].draw(renderer.getBatch());
            }

            insertionSortY(sprites);
            for(Sprite sprite: sprites){
                sprite.draw(renderer.getBatch());
            }
        
            cctv.draw(renderer.getBatch());
            renderer.getBatch().end();
            
            rayHandler.setCombinedMatrix(
                camera.combined,
                camera.position.x,
                camera.position.y,
                0,
                0
            );
            rayHandler.updateAndRender();
        }
        
        else {
            pauseStage.getBatch().setProjectionMatrix(pauseStage.getCamera().combined);
            pauseStage.getCamera().update();
    
            pauseBatch.begin();
            pauseSprite.draw(pauseBatch);
            if (isOnSetting = true) {
                //
            }
            pauseBatch.end();
    
            pauseStage.act(delta);
            pauseStage.draw();
        }
    }
    
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 6;
        camera.viewportHeight = height / 6;
        camera.update();
    }

    @Override
    public void dispose() {
        world.dispose();
        map.dispose();
        renderer.dispose();
        player.getTexture().dispose();
        droid1.getTexture().dispose();
        cctv.getTexture().dispose();
        // doors.get(0).getTexture().dispose();
        // doors.get(0).getKey().getTexture().dispose();
        door1.getTexture().dispose();
        door1.getKey().getTexture().dispose();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }


    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }
}
