package io.github.some_example_name;

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
    
    private Animation<TextureRegion> walking;
    private Body box;
    private float speed = 80000;
    private Vector2 movement = new Vector2();
    private BodyDef boxDef;
    private FixtureDef fixtureDef;
    private PolygonShape boxShape;
    private char direction = 'w';

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


    public Player(Sprite sprite, TextureAtlas atlas){
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

        animationHandler.setCurrent(S_WALK);
    //box------------------------------------------
        boxDef = new BodyDef();
        boxDef.type = BodyType.DynamicBody;
        boxDef.position.set(1100f,250);
        boxDef.fixedRotation = true;

    //box shape------------------------------------
        boxShape = new PolygonShape();
        boxShape.setAsBox(5f, 10);

    //fixture definition--------------------------
        fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.friction = .75f;
        fixtureDef.restitution = .1f;
        fixtureDef.density = 5;
        
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        update(Gdx.graphics.getDeltaTime());
        
        setPosition(box.getPosition().x - 12f, box.getPosition().y - 14);
    }

    public void update(float delta){
        //move on x
        setX(getX());
        //move on y
        setY(getY());

        if(box.getLinearVelocity().len() < 50)
            box.applyForceToCenter(getMovement(), true);

        if(box.getLinearVelocity().x < -0.5){
            box.applyForceToCenter(60000, 0, true);
            animationHandler.setCurrent(W_WALK);
            direction = 'w';
        }
                        
        else if(box.getLinearVelocity().x > 0.5){
            box.applyForceToCenter(-60000,0, true);
            animationHandler.setCurrent(E_WALK);
            direction = 'e';
        }
        
        else{
            box.setLinearVelocity(0, box.getLinearVelocity().y);

        }
        
        if(box.getLinearVelocity().y < -0.5){
            box.applyForceToCenter(0,60000, true);
            animationHandler.setCurrent(S_WALK);
            direction = 's';
        }
        
        else if(getBox().getLinearVelocity().y > 0.5){
            box.applyForceToCenter(0,-60000, true);
            animationHandler.setCurrent(N_WALK);
            direction = 'n';
        }
        
        else
            box.setLinearVelocity(box.getLinearVelocity().x, 0);

        if(getBox().getLinearVelocity().y > 0.5 && box.getLinearVelocity().x > 0.5){
            animationHandler.setCurrent(NE_WALK);
            direction = 'f';
        }
        if(getBox().getLinearVelocity().y < -0.5 && box.getLinearVelocity().x > 0.5){
            animationHandler.setCurrent(SE_WALK);
            direction = 'd';
        }
        if(getBox().getLinearVelocity().y < -0.5 && box.getLinearVelocity().x < -0.5){
            animationHandler.setCurrent(SW_WALK);
            direction = 'q';
        }
        if(getBox().getLinearVelocity().y > 0.5 && box.getLinearVelocity().x < -0.5){
            animationHandler.setCurrent(NW_WALK);
            direction = 'b';
        }

        if(getBox().getLinearVelocity().y == 0 && box.getLinearVelocity().x == 0){
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

        setRegion(animationHandler.getFrame(delta));
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

//SETTERS------------------------------------------------------------
    public void setBox(Body box) {
        this.box = box;
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

//UNUSED METHODS-------------------------------------------------------
    @Override
    public boolean keyTyped(char arg0) {
        return false;
    }

    @Override
    public boolean mouseMoved(int arg0, int arg1) {
        return false;
    }

    @Override
    public boolean scrolled(float arg0, float arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'scrolled'");
    }

    @Override
    public boolean touchCancelled(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchCancelled'");
    }

    @Override
    public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchDown'");
    }

    @Override
    public boolean touchDragged(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchDragged'");
    }

    @Override
    public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchUp'");
    }
}
