package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.physics.box2d.World;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class Play implements Screen{

    private World world;
    private Box2DDebugRenderer box2dDebugRenderer;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private Box2DMapObjectParser parser;
    private Texture walking;
    private TextureAtlas atlas;
    
    @Override
    public void show() {
        world = new World(new Vector2(0, 0), true);
        box2dDebugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        atlas = new TextureAtlas("atlas/walkingTest.atlas");
        
        map = new TmxMapLoader().load("maps/map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        
        parser = new Box2DMapObjectParser();
        parser.load(world, map);

        player = new Player(new Sprite(new Texture("idle1.jpeg")), atlas);
        player.setPosition(1100f, 250);
        Gdx.input.setInputProcessor(player);

        player.setBox(world.createBody(player.getBoxDef()));
        player.getBox().createFixture(player.getFixtureDef());

        Animation<TextureRegion> walkingN = new Animation<>(1 / 6f, atlas.findRegion("walkingN"));
        walkingN.setPlayMode(PlayMode.LOOP);
        

        player.getBoxShape().dispose();
    }
    
    @Override
    public void render(float arg0) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 8, 3);

        renderer.setView(camera);
        renderer.render();
        box2dDebugRenderer.render(world, camera.combined);
        
        camera.position.set(player.getBox().getPosition(),0);
        camera.update();
        

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        renderer.getBatch().end();
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
