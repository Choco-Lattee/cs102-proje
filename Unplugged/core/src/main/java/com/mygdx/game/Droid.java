package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class Droid extends Sprite{
    
    private ArrayList<Vector2> road;
    private ArrayList<Vector2> temp;
    private Body box;
    private float speed = 0.5f;
    private int goalIndex = 1;
    private BodyDef boxDef;
    private FixtureDef fixtureDef;
    private PolygonShape boxShape;
    private boolean isAngry = false;
    private char direction = 'w';
    private Player player;
    private ConeLight visionCone;
    private World world;
    private float angleDeg;
    private TiledMap map;
    ArrayList<Furnace> furnaces = new ArrayList<Furnace>();

    private AnimationHandler animationHandler;
    private final String E_WALK = "walkingE";
    private final String W_WALK = "walkingW";

    private final String E_ANGRY = "angryE";
    private final String W_ANGRY = "angryW";

    private final String E_ATTACK = "attackK_frame";
    private final String W_ATTACK = "attackKW_frame";


    public Droid(TextureAtlas atlas, ArrayList<Vector2> road, Player player, World world, RayHandler rayHandler, TiledMap map, ArrayList<Furnace> furnaces){
        super(atlas.findRegion("runN"));
        Animation<TextureRegion> walkingE = new Animation<>(1 / 6f, atlas.findRegions("runN"), PlayMode.LOOP);
        Animation<TextureRegion> walkingW = new Animation<>(1 / 6f, atlas.findRegions("runW"), PlayMode.LOOP);

        Animation<TextureRegion> angryE = new Animation<>(1 / 6f, atlas.findRegions("runE2"), PlayMode.LOOP);
        Animation<TextureRegion> angryW = new Animation<>(1 / 6f, atlas.findRegions("runW2"), PlayMode.LOOP);      
        
        Animation<TextureRegion> attackE = new Animation<>(1 / 6f, atlas.findRegions("attackK_frame"), PlayMode.LOOP);
        Animation<TextureRegion> attackW = new Animation<>(1 / 6f, atlas.findRegions("attackKW_frame"), PlayMode.LOOP);

        animationHandler = new AnimationHandler();

        animationHandler.add(E_WALK, walkingE);
        animationHandler.add(W_WALK, walkingW);

        animationHandler.add(E_ANGRY, angryE);
        animationHandler.add(W_ANGRY, angryW);

        animationHandler.add(E_ATTACK, attackE);
        animationHandler.add(W_ATTACK, attackW);

        animationHandler.setCurrent(E_WALK);
    //box------------------------------------------
        boxDef = new BodyDef();
        boxDef.type = BodyType.DynamicBody;
        boxDef.position.set(road.get(0));
        boxDef.fixedRotation = true;

    //box shape------------------------------------
        boxShape = new PolygonShape();
        boxShape.setAsBox(5f, 5);

    //fixture definition--------------------------
        fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.friction = .75f;
        fixtureDef.restitution = .1f;
        fixtureDef.density = 5;
    //body-----------------------------------------
        this.world = world;
        box = world.createBody(boxDef);
        box.createFixture(fixtureDef);
        setPosition(road.get(0).x, road.get(0).y);
    //player---------------------------------------
        this.player = player;
        Vector2 droidPos = getBox().getPosition();
        Vector2 goal = road.get(goalIndex);

        float angleRad = MathUtils.atan2(goal.y - droidPos.y, goal.x - droidPos.x);
        this.angleDeg = angleRad * MathUtils.radiansToDegrees;
    //road-----------------------------------------
        this.road = road;
        temp = road;

    //map------------------------------------------
        this.map = map;
        this.furnaces = furnaces;
    }

//RAY CASTING--------------------------------------------------------
    public boolean isPlayerDetected(){
        boolean ret = false;
        Vector2 bottomLeft = new Vector2(player.getBox().getPosition().x - 5f, player.getBox().getPosition().y - 10);
        Vector2 bottomRight = new Vector2(player.getBox().getPosition().x + 5f, player.getBox().getPosition().y - 10);
        Vector2 topLeft = new Vector2(player.getBox().getPosition().x - 5f, player.getBox().getPosition().y + 10);;
        Vector2 topRight = new Vector2(player.getBox().getPosition().x + 5f, player.getBox().getPosition().y + 10);;
        Vector2 center = player.getBox().getPosition();
        Vector2 eye = new Vector2(box.getPosition().x - 5f, box.getPosition().y + 5);

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


                if(inCone && !isAnyTiledObjectOnLine(map, eye, ray, furnaces)){
                    return true;
                }
            }
        }
        
        return ret;
    }
    
    public boolean isPlayerInRadius(){
        if(Math.abs(box.getPosition().dst(player.getBox().getPosition())) < 120f){
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
                //System.out.println("Object");
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

        if(direction == 'w')
            setPosition(box.getPosition().x - 60f, box.getPosition().y - 7);
        else if(direction == 'e')   
            setPosition(box.getPosition().x - 16f, box.getPosition().y - 7);
    }

    public void update(float delta){
        //move on x
        setX(getX());
        //move on y
        setY(getY());
        
        Vector2 droidPos = getBox().getPosition();
        Vector2 goal = road.get(goalIndex);

        float angleRad = MathUtils.atan2(goal.y - droidPos.y, goal.x - droidPos.x);
        angleDeg = angleRad * MathUtils.radiansToDegrees;
        
        if(Math.abs(box.getPosition().x - road.get(goalIndex).x) < 1 && Math.abs(box.getPosition().y - road.get(goalIndex).y) < 1){
            box.setLinearVelocity(0, 0);
            goalIndex = (goalIndex + 1) % (road.size());
        }
        else{
            
            box.setLinearVelocity((road.get(goalIndex).x - box.getPosition().x) * speed, (road.get(goalIndex).y - box.getPosition().y) * speed);
            
            if(Math.abs(box.getPosition().x - road.get(goalIndex).x) < 1){ 
                box.setLinearVelocity(0, box.getLinearVelocity().y);
                box.getPosition().x = road.get(goalIndex).x;
            }
            
            if(Math.abs(box.getPosition().y - road.get(goalIndex).y) < 1){
                box.setLinearVelocity(box.getLinearVelocity().x, 0);
                box.getPosition().y = road.get(goalIndex).y;
            }
        }
        
        if(isPlayerDetected()){
            isAngry = true;
            goalIndex = 1;
        }
        else isAngry = false;
        
        if(isAngry){
            road.clear();
            road.add(player.getBox().getPosition());
            road.add(player.getBox().getPosition());
        }
        
        else{
            road = (ArrayList<Vector2>)temp.clone();
        }
        
        if(box.getLinearVelocity().x < -1)
        direction = 'w';
        else if(box.getLinearVelocity().x > 1)
            direction = 'e';
        if(box.getLinearVelocity().y < -1)
        direction = 's';
        else if(box.getLinearVelocity().y > 1)
        direction = 'n';
        
        visionCone.setPosition(droidPos);
        visionCone.setDirection(angleDeg);
        
        if(isAngry){
            if(Math.abs(player.getBox().getPosition().dst(box.getPosition())) < 50f){
    
                switch(direction){
                    case 'e':
                    animationHandler.setCurrent(E_ATTACK);
                    break;
                    case 'w':
                    animationHandler.setCurrent(W_ATTACK);
                    break;
                }
    
                if(animationHandler.isFinished()){
                    player.setIsAlive(false);
                    //System.out.println("PLAYER DIED");
                }
            }

            else{
                switch(direction){
                    case 'e':
                    animationHandler.setCurrent(E_ANGRY);
                    break;
                    case 'w':
                    animationHandler.setCurrent(W_ANGRY);
                    break;
                }
            }
        }
        else{
            switch(direction){
                case 'e':
                animationHandler.setCurrent(E_WALK);
                break;
                case 'w':
                animationHandler.setCurrent(W_WALK);
                break;
            }
        }

        setRegion(animationHandler.getFrame(delta));
        visionCone.setPosition(getBox().getPosition());
    }

//GETTERS-----------------------------------------------------------
    public Body getBox() {
        return box;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public BodyDef getBoxDef() {
        return boxDef;
    }

    public PolygonShape getBoxShape() {
        return boxShape;
    }
    

//SETTERS------------------------------------------------------------
    public void setBox(Body box) {
        this.box = box;
    }

    public void setVisionCone(ConeLight visionCone) {
        this.visionCone = visionCone;
    }
}