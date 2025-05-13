package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player extends Sprite implements InputProcessor{
    
    private Body box;
    private float speed = 100000;
    private Vector2 movement = new Vector2();
    private BodyDef boxDef;
    private FixtureDef fixtureDef;
    private PolygonShape boxShape;
    private char direction = 'w';
    private boolean isCardBoard = false;
    private float timer = 5;
    private boolean isAlive = true;
    private ArrayList<Furnace> furnaces = new ArrayList<Furnace>();

    private AnimationHandler animationHandler;
    private final String N_WALK= "walkingN";  
    private final String NE_WALK= "walkingNE";
    private final String NW_WALK= "walkingNW";
    private final String S_WALK= "walkingS";
    private final String SE_WALK= "walkingSE";
    private final String SW_WALK= "walkingSW";
    private final String E_WALK= "walkingE";
    private final String W_WALK= "walkingW";

    private final String N_IDLE= "idleN";  
    private final String NE_IDLE= "idleNE";
    private final String NW_IDLE= "idleNW";
    private final String S_IDLE= "idleS";
    private final String SE_IDLE= "idleSE";
    private final String SW_IDLE= "idleSW";
    private final String E_IDLE= "idleE";
    private final String W_IDLE= "idleW";

    private final String CB = "cardboard";


    public Player(TextureAtlas atlas, ArrayList<Furnace> furnaces){
        super(atlas.findRegion("walkingS"));
        Animation<TextureRegion> walkingN = new Animation<>(1 / 6f, atlas.findRegions("walkingN"), PlayMode.LOOP);
        Animation<TextureRegion> walkingNE = new Animation<>(1 / 6f, atlas.findRegions("walkingNE"), PlayMode.LOOP);
        Animation<TextureRegion> walkingNW = new Animation<>(1 / 6f, atlas.findRegions("walkingNW"), PlayMode.LOOP);
        Animation<TextureRegion> walkingS = new Animation<>(1 / 6f, atlas.findRegions("walkingS"), PlayMode.LOOP);
        Animation<TextureRegion> walkingSE = new Animation<>(1 / 6f, atlas.findRegions("walkingSE"), PlayMode.LOOP);
        Animation<TextureRegion> walkingSW = new Animation<>(1 / 6f, atlas.findRegions("walkingSW"), PlayMode.LOOP);
        Animation<TextureRegion> walkingE = new Animation<>(1 / 6f, atlas.findRegions("walkingE"), PlayMode.LOOP);
        Animation<TextureRegion> walkingW = new Animation<>(1 / 6f, atlas.findRegions("walkingW"), PlayMode.LOOP);

        Animation<TextureRegion> idleN = new Animation<>(1 / 6f, atlas.findRegions("idleN"), PlayMode.LOOP);
        Animation<TextureRegion> idleNE = new Animation<>(1 / 6f, atlas.findRegions("idleNE"), PlayMode.LOOP);
        Animation<TextureRegion> idleNW = new Animation<>(1 / 6f, atlas.findRegions("idleNW"), PlayMode.LOOP);
        Animation<TextureRegion> idleS = new Animation<>(1 / 6f, atlas.findRegions("idleS"), PlayMode.LOOP);
        Animation<TextureRegion> idleSE = new Animation<>(1 / 6f, atlas.findRegions("idleSE"), PlayMode.LOOP);
        Animation<TextureRegion> idleSW = new Animation<>(1 / 6f, atlas.findRegions("idleSW"), PlayMode.LOOP);
        Animation<TextureRegion> idleE = new Animation<>(1 / 6f, atlas.findRegions("idleE"), PlayMode.LOOP);
        Animation<TextureRegion> idleW = new Animation<>(1 / 6f, atlas.findRegions("idleW"), PlayMode.LOOP);      
        Animation<TextureRegion> cardboard = new Animation<>(1 / 6f, atlas.findRegions("cardboard"), PlayMode.LOOP);
        
        animationHandler = new AnimationHandler();

        animationHandler.add(N_WALK, walkingN);
        animationHandler.add(NE_WALK, walkingNE);
        animationHandler.add(NW_WALK, walkingNW);
        animationHandler.add(S_WALK, walkingS);
        animationHandler.add(SE_WALK, walkingSE);
        animationHandler.add(SW_WALK, walkingSW);
        animationHandler.add(E_WALK, walkingE);
        animationHandler.add(W_WALK, walkingW);

        animationHandler.add(N_IDLE, idleN);
        animationHandler.add(NE_IDLE, idleNE);
        animationHandler.add(NW_IDLE, idleNW);
        animationHandler.add(S_IDLE, idleS);
        animationHandler.add(SE_IDLE, idleSE);
        animationHandler.add(SW_IDLE, idleSW);
        animationHandler.add(E_IDLE, idleE);
        animationHandler.add(W_IDLE, idleW);

        animationHandler.add(CB, cardboard);

        animationHandler.setCurrent(S_WALK);
    //box------------------------------------------
        boxDef = new BodyDef();
        boxDef.type = BodyType.DynamicBody;
        boxDef.position.set(900f, 250);
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
        
        this.furnaces = furnaces;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        update(Gdx.graphics.getDeltaTime());
        
        setPosition(box.getPosition().x - 12f, box.getPosition().y - 7);
    }

    public void update(float delta){
        //move on x
        setX(getX());
        //move on y
        setY(getY());

        checkCardBoard(delta);

        if(box.getLinearVelocity().x < 10 || box.getLinearVelocity().x > -10)
            box.applyForceToCenter(getMovement().x,0, true);

        if(box.getLinearVelocity().y < 10 || box.getLinearVelocity().y > -10)
            box.applyForceToCenter(0,getMovement().y, true);

        if(box.getLinearVelocity().x < -1){
            box.applyForceToCenter(60000, 0, true);
            direction = 'w';
        }
                        
        else if(box.getLinearVelocity().x > 1){
            box.applyForceToCenter(-60000,0, true);
            direction = 'e';
        }
        
        else{
            box.setLinearVelocity(0, box.getLinearVelocity().y);
        }
        
        if(box.getLinearVelocity().y < -1){
            box.applyForceToCenter(0,60000, true);
            direction = 's';
        }
        
        else if(getBox().getLinearVelocity().y > 1){
            box.applyForceToCenter(0,-60000, true);
            direction = 'n';
        }
        
        else
            box.setLinearVelocity(box.getLinearVelocity().x, 0);

        if(getBox().getLinearVelocity().y > 1 && box.getLinearVelocity().x > 1){
            direction = 'f';
        }
        if(getBox().getLinearVelocity().y < -1 && box.getLinearVelocity().x > 1){
            direction = 'd';
        }
        if(getBox().getLinearVelocity().y < -1 && box.getLinearVelocity().x < -1){
            direction = 'q';
        }
        if(getBox().getLinearVelocity().y > 1 && box.getLinearVelocity().x < -1){
            direction = 'b';
        }

        if(getBox().getLinearVelocity().y == 0 && box.getLinearVelocity().x == 0){

            if(!isCardBoard){
                switch(direction){
                    case 'n':
                    animationHandler.setCurrent(N_IDLE);
                    break;
                    case 's':
                    animationHandler.setCurrent(S_IDLE);
                    break;
                    case 'e':
                    animationHandler.setCurrent(E_IDLE);
                    break;
                    case 'w':
                    animationHandler.setCurrent(W_IDLE);
                    break;
                    case 'q':
                    animationHandler.setCurrent(SW_IDLE);
                    break;
                    case 'f':
                    animationHandler.setCurrent(NE_IDLE);
                    break;
                    case 'b':
                    animationHandler.setCurrent(NW_IDLE);
                    break;
                    case 'd':
                    animationHandler.setCurrent(SE_IDLE);
                    break;
                }

            }
            else
                animationHandler.setCurrent(CB);
        }

        else{
            if(!isCardBoard){
                switch(direction){
                    case 'n':
                    animationHandler.setCurrent(N_WALK);
                    break;
                    case 's':
                    animationHandler.setCurrent(S_WALK);
                    break;
                    case 'e':
                    animationHandler.setCurrent(E_WALK);
                    break;
                    case 'w':
                    animationHandler.setCurrent(W_WALK);
                    break;
                    case 'q':
                    animationHandler.setCurrent(SW_WALK);
                    break;
                    case 'f':
                    animationHandler.setCurrent(NE_WALK);
                    break;
                    case 'b':
                    animationHandler.setCurrent(NW_WALK);
                    break;
                    case 'd':
                    animationHandler.setCurrent(SE_WALK);
                    break;
                }
            }

            else
                animationHandler.setCurrent(CB);
        }

        setRegion(animationHandler.getFrame(delta));
    }

    public void checkCardBoard(float delta){
        timer += delta;

        if(timer > 3) isCardBoard = false;
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
    
    public Vector2 getMovement() {
        return movement;
    }

    public boolean getIsCardBoard(){
        return isCardBoard;
    }

    public boolean getIsAlive(){
        return isAlive;
    }

//SETTERS------------------------------------------------------------
    public void setBox(Body box) {
        this.box = box;
    }

    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

//INPUT PROCESSOR----------------------------------------------------
    @Override
    public boolean keyDown(int keyCode) {
        switch(keyCode){
            case Keys.A:
            movement.x = -speed;
            break;

            case Keys.S:
            movement.y = -speed;
            break;
            
            case Keys.D:
            movement.x = speed;
            break;
            
            case Keys.W:
            movement.y = speed;
            break;
        }
        
        return true;
    }
    
    @Override
    public boolean keyUp(int keyCode) {
        switch(keyCode){
            case Keys.A:
            movement.x = 0;
            break;
            
            case Keys.S:
            movement.y = 0;
            break;
            
            case Keys.D:
            movement.x = 0;
            break;
            
            case Keys.W:
            movement.y = 0;
            break;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char keyCode) {
        switch(keyCode){
            case 'c':
            System.out.println("keytyped C");
            
            if(!isCardBoard && timer > 5){
                isCardBoard = true;
                timer = 0;
            }
            System.out.println(isCardBoard);
            break;
            
            case 'e':
            Furnace temp = furnaces.get(0);
            float min = Float.MAX_VALUE;

            System.out.println("keytyped E");
            for(Furnace furnace: furnaces){
                if(Math.abs(furnace.getBox().getPosition().dst(box.getPosition())) < 35f && furnace.getBox().getPosition().dst(box.getPosition()) < min){
                    System.out.println("SUCCESFUL");
                    min = furnace.getBox().getPosition().dst(box.getPosition());
                    temp = furnace;
                    break;
                }
            }
            if(min != Float.MAX_VALUE)
                box.applyForceToCenter((temp.getBox().getPosition().x - box.getPosition().x) * 999999999, (temp.getBox().getPosition().y - box.getPosition().y) * 999999999, true);
            
            break;

            case 'u':
            System.out.println(box.getPosition().x + ", " + box.getPosition().y);
            break;
        }
        return true;
    }
//UNUSED METHODS-------------------------------------------------------

    @Override
    public boolean mouseMoved(int arg0, int arg1) {
        return false;
    }

    @Override
    public boolean scrolled(float arg0, float arg1) {
        return false;
    }

    @Override
    public boolean touchCancelled(int arg0, int arg1, int arg2, int arg3) {
        return false;
    }

    @Override
    public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
        return false;
    }

    @Override
    public boolean touchDragged(int arg0, int arg1, int arg2) {
        return false;
    }

    @Override
    public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
        return false;
    }
}
