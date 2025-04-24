package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;

public class MainGame implements Screen{
    private TiledMap mainMap;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private MainPlayer player;
    private Texture all;
    private Texture reversedAll;
    private static final int FRAME_COLS = 8, FRAME_ROWS = 9;

    private int[] background = new int[] {0};
    private int[] foreground = new int[] {1};

    private ShapeRenderer sr;

    

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        mainMap = loader.load("assets/MainGameAssests/maps/mainMap.tmx");

        renderer = new OrthogonalTiledMapRenderer(mainMap);
        sr = new ShapeRenderer();
        sr.setColor(Color.WHITE);
        Gdx.gl.glLineWidth(3);

        camera = new OrthographicCamera();

        all = new Texture("assets/MainGameAssests/AnimationSheet_Character.png");
        reversedAll = new Texture("assets/MainGameAssests/AnimationSheet_Character_Reversed.png");
        TextureRegion[][] tmp = TextureRegion.split(all, all.getWidth() / FRAME_COLS, all.getHeight() / FRAME_ROWS);
        TextureRegion[][] tmpReversed = TextureRegion.split(reversedAll, reversedAll.getWidth() / FRAME_COLS, reversedAll.getHeight() / FRAME_ROWS);
        player = new MainPlayer((TiledMapTileLayer)mainMap.getLayers().get(0), tmp, tmpReversed);
        player.setPosition(9 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 24) * player.getCollisionLayer().getTileHeight());

        Gdx.input.setInputProcessor(player);

        // Animated Tiles 
       // Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>(2);

       // Iterator<TiledMapTile> tiles = mainMap.getTileSets().getTileSet("sky").iterator();
       // while (tiles.hasNext()) {
        //    TiledMapTile tile = tiles.next();
            // if (tile.getProperties().containsKey("animation") && tile.getProperties().get("animation", String.class).equals("flower")) {
                // frameTiles.add((StaticTiledMapTile) tile);
           // }
       // }

       // AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1 / 3f, frameTiles);
        //TiledMapTileLayer layer = (TiledMapTileLayer) mainMap.getLayers().get("background");
       //for (int x = 0; x < layer.getWidth(); x++) {
           // for (int y = 0; y < layer.getHeight(); y++) {
               // Cell cell = layer.getCell(x, y);
               // if (cell.getTile().getProperties().containsKey("animation") && cell.getTile().getProperties().get("animation", String.class).equals("flower")) {
                //    cell.setTile(animatedTile);
               // }
           // }
      // }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.position.set(player.getX() + player.getWidth() /  2, player.getY() + player.getHeight() / 2, 0);
        camera.update();
        renderer.setView(camera);

       // renderer.render(background);

        renderer.getBatch().begin();
        renderer.renderTileLayer((TiledMapTileLayer) mainMap.getLayers().get("background"));;
        renderer.renderTileLayer((TiledMapTileLayer) mainMap.getLayers().get("mainground"));;
        player.draw(renderer.getBatch());
        renderer.renderTileLayer((TiledMapTileLayer) mainMap.getLayers().get("foreground"));;
        renderer.getBatch().end();

        sr.setProjectionMatrix(camera.combined);
       // renderer.render(foreground);

       for (MapObject object: mainMap.getLayers().get("Arrow").getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                sr.begin(ShapeType.Filled);
                sr.rect(rect.x, rect.y, rect.width, rect.height);
                sr.end();
            }
            else if (object instanceof CircleMapObject) {
                Circle circle = ((CircleMapObject)object).getCircle();
                sr.begin(ShapeType.Filled);
                sr.circle(circle.x, circle.y, circle.radius);
                sr.end();
            }
            else if (object instanceof PolylineMapObject) {
                Polyline polyline = ((PolylineMapObject)object).getPolyline();
                sr.begin(ShapeType.Line);
                sr.polyline(polyline.getTransformedVertices());
                sr.end();
            }
            else if (object instanceof EllipseMapObject) {
                Ellipse ellipse = ((EllipseMapObject)object).getEllipse();
                sr.begin(ShapeType.Filled);
                sr.ellipse(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
                sr.end();
            }
            else if (object instanceof PolygonMapObject) { // arrow 
                Polygon polygon = ((PolygonMapObject)object).getPolygon();
                sr.begin(ShapeType.Line);
                sr.polygon(polygon.getTransformedVertices());
                sr.end();
            }
       }

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 2.5f;
        camera.viewportHeight = height / 2.5f;
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
        mainMap.dispose();
        renderer.dispose();
        player.getTexture().dispose();
    }
  
}
