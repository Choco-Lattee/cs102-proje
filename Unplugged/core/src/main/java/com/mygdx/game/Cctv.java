package io.github.some_example_name;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.PolygonRegionLoader.PolygonRegionParameters;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class Cctv extends Sprite{
    private TextureRegion texture;
    private ConeLight visionCone;
    private RayHandler rayHandler;
    private Player player;
    private TiledMap map;
    //private int direction = 1;
    private float angle = 0;
    private float increment = 5f;
    private float angleLimit = 0f;
    ArrayList<Furnace> furnaces = new ArrayList<Furnace>();

    public Cctv(float x, float y, int direction, TextureRegion texture, RayHandler rayHandler, Player player, TiledMap map, ArrayList<Furnace> furnaces){
        super(texture);

        setX(x);
        setY(y);
        this.rayHandler = rayHandler;
        this.player = player;
        this.texture = texture;
        this.map = map;
        this.furnaces = furnaces;

        switch(direction){
            case 1:
            angle = 300f;
            angleLimit = 300;
            break;
            case 2:
            angle = 210f;
            angleLimit = 210f;
            break;
        }
    }

    //RAY CASTING--------------------------------------------------------
    public boolean isPlayerDetected(){
        boolean ret = false;
        Vector2 bottomLeft = new Vector2(player.getBox().getPosition().x - 3f, player.getBox().getPosition().y - 6);
        Vector2 bottomRight = new Vector2(player.getBox().getPosition().x + 3f, player.getBox().getPosition().y - 6);
        Vector2 topLeft = new Vector2(player.getBox().getPosition().x - 3f, player.getBox().getPosition().y + 6);
        Vector2 topRight = new Vector2(player.getBox().getPosition().x + 3f, player.getBox().getPosition().y + 6);
        Vector2 center = player.getBox().getPosition();
        Vector2 eye = new Vector2(getX(), getY());

        ArrayList<Vector2> rays = new ArrayList<Vector2>();

        rays.add(bottomLeft );
        rays.add(bottomRight);
        rays.add(topLeft);
        rays.add(topRight);
        rays.add(center);

        if(isPlayerInRadius() && !player.getIsCardBoard()){
            for(Vector2 ray: rays){
                float angleRad = MathUtils.atan2(ray.y - eye.y, ray.x - eye.x);
                float angleDeg = angleRad * MathUtils.radiansToDegrees;
                boolean inCone = Math.abs(angleDeg - visionCone.getDirection()) < visionCone.getConeDegree();

                System.out.println(inCone + "-" + isAnyTiledObjectOnLine(map, eye, ray, furnaces));
                if(inCone && !isAnyTiledObjectOnLine(map, eye, ray, furnaces)){
                    System.out.println("PLAYER DETECTED CCTV");
                    return true;
                }
            }
        }
        
        return ret;
    }
    
    public boolean isPlayerInRadius(){
        Vector2 position = new Vector2(getX(), getY());

        if(Math.abs(position.dst(player.getBox().getPosition())) < 120f){
            return true;
        }
        
        return false;
    }

    public static boolean isAnyTiledObjectOnLine(TiledMap map, Vector2 start, Vector2 end, ArrayList<Furnace> furnaces) {
        MapLayer objectLayer = map.getLayers().get("Object Layer 2");
        if (objectLayer == null) return false;

        for (MapObject object : objectLayer.getObjects()) {
            if (!object.isVisible()) continue;

            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (intersectsLineRectangle(start, end, rect)) {
                    return true;
                }
            }
        }

        for(Furnace furnace: furnaces){
            Rectangle rect = new Rectangle(furnace.getBox().getPosition().x - furnace.getWidth(), furnace.getBox().getPosition().y - furnace.getHeight(), furnace.getWidth() * 2, furnace.getHeight() * 2);

            if(intersectsLineRectangle(start, end, rect)){
                System.out.println("Object");
                return true;
            }
        }

        return false;
    }

    public static boolean intersectsLineRectangle(Vector2 lineStart, Vector2 lineEnd, Rectangle rect) {
        // Edges of the rectangle
        Vector2 bl = new Vector2(rect.x, rect.y);
        Vector2 br = new Vector2(rect.x + rect.width, rect.y);
        Vector2 tr = new Vector2(rect.x + rect.width, rect.y + rect.height);
        Vector2 tl = new Vector2(rect.x, rect.y + rect.height);

        return
            Intersector.intersectSegments(lineStart, lineEnd, bl, br, null) ||
            Intersector.intersectSegments(lineStart, lineEnd, br, tr, null) ||
            Intersector.intersectSegments(lineStart, lineEnd, tr, tl, null) ||
            Intersector.intersectSegments(lineStart, lineEnd, tl, bl, null) ||
            rect.contains(lineStart) || rect.contains(lineEnd); // Optional: check if inside
    }
    
//------------------------------------------------------------------------------------------------

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        update(Gdx.graphics.getDeltaTime());
    }

    public void update(float delta){

        float angleRad = MathUtils.atan2(player.getBox().getPosition().y - getY(), player.getBox().getPosition().y - getX());
        float angleDeg = angleRad * MathUtils.radiansToDegrees;

        if(isPlayerDetected()){
            visionCone.setDirection(angleDeg);
            player.setBeingChased(true);
        }

        else{
            if(angle + increment < angleLimit + 60f && angle + increment > angleLimit - 30f){
                angle = angle + increment;
                visionCone.setDirection(angle);
            }
            else{
                increment = - increment;
                angle = angle + increment;
                visionCone.setDirection(angle);
            } 
        }

    }

    public void setVisionCone(ConeLight visionCone) {
        this.visionCone = visionCone;
    }
    
}
