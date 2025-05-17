package com.mygdx.game;

import java.security.Key;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Plane.PlaneSide;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Box2DPlayer extends Sprite implements InputProcessor{
    protected static int up = Keys.W, speedingKey = Keys.SHIFT_LEFT, left = Keys.A, down = Keys.S, right = Keys.D;

    private Vector2 velocity, playerPosition;
    private float speed = 300, gravity = 30, width, height;
    private float density = 0.09f, animationTime = 0, countTime = 0, damageTime = 0, deathTime = 0;
    private int heartNumber = 3, point = 0, currentMap = 0;
    private Body body;
    private World world;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef, footDef;
    private Fixture fixture;
    private PolygonShape polygonShape;
    private boolean canJump, lastWayRight = true, isDuck = false, onAttack = false, isDeath = false, hasTakenDamage = false;
    private  Animation<TextureRegion> currentAnimation;
    TextureRegion[] stillFrames;
    private Animation<TextureRegion> stillRight, stillLeft, idle, walkRight, walkLeft,jumpRight, jumpLeft,
     duckLeft, duckRight, attackLeft, attackRight, deathLeft, deathRight, damageLeft, damageRight;

    public Box2DPlayer(World world, Vector2 playerPosition, TextureRegion[][] tmp, TextureRegion[][] tmpReversed) {

        super(tmp[0][0]);
        TextureRegion[] stillRightFrames = new TextureRegion[2];
        TextureRegion[] stillLeftFrames = new TextureRegion[2];
        TextureRegion[] walkRightFrames = new TextureRegion[4];
        TextureRegion[] walkLeftFrames = new TextureRegion[4];
        TextureRegion[] jumpRightFrames = new TextureRegion[8];
        TextureRegion[] jumpLeftFrames = new TextureRegion[8];
        TextureRegion[] duckRightFrames = new TextureRegion[6];
        TextureRegion[] duckLeftFrames = new TextureRegion[6];
        TextureRegion[] attackLFrames = new TextureRegion[8];
        TextureRegion[] attackRFrames = new TextureRegion[8];
        TextureRegion[] deathLeftFrames = new TextureRegion[8];
        TextureRegion[] deathRightFrames = new TextureRegion[8];
        TextureRegion[] damageLeftFrames = new TextureRegion[3];
        TextureRegion[] damageRightFrames = new TextureRegion[3];
        int stillIndex = 0;
        int walkIndex = 0;
        int index = 0;
        for (int i = 0; i < 2; i++) {
            stillRightFrames[stillIndex++] = tmp[0][i];
        }
        stillIndex = 0;
        for (int i = 7; i > 5; i--) {
            stillLeftFrames[stillIndex++] = tmpReversed[0][i];
        }
        for (int k = 7; k > 3; k--) {
            walkLeftFrames[walkIndex++] = tmpReversed[2][k];
        }
        walkIndex = 0;
        for (int k = 0; k < 4; k++) {
            walkRightFrames[walkIndex++] = tmp[2][k];
        }
        for (int i = 0; i < 8; i++) {
            jumpRightFrames[index++] = tmp[5][i]; 
        }
        index = 0;
        for (int i = 7; i >= 0; i--) {
            jumpLeftFrames[index++] = tmpReversed[5][i];
        }
        index = 0;
        for (int i = 0; i < 6; i++) {
            duckRightFrames[index++] = tmp[4][i]; 
        }
        index = 0;
        for (int i = 7; i >= 2; i--) {
            duckLeftFrames[index++] = tmpReversed[4][i];
        }
        index = 0;
        for (int i = 0; i < 8; i++) {
            attackRFrames[index++] = tmp[8][i]; 
        }
        index = 0;
        for (int i = 7; i >= 0; i--) {
            attackLFrames[index++] = tmpReversed[8][i];
        }
        index = 0;
        for (int i = 0; i < 8; i++) {
            deathRightFrames[index++] = tmp[7][i]; 
        }
        index = 0;
        for (int i = 7; i >= 0; i--) {
            deathLeftFrames[index++] = tmpReversed[7][i];
        }
        index = 0;
        for (int i = 0; i < 3; i++) {
            damageRightFrames[index++] = tmp[6][i]; 
        }
        index = 0;
        for (int i = 7; i >= 5; i--) {
            damageLeftFrames[index++] = tmpReversed[6][i];
        }

        stillRight = new Animation<TextureRegion>(1 / 2f, stillRightFrames);
        stillRight.setPlayMode(PlayMode.LOOP);
        stillLeft = new Animation<TextureRegion>(1/ 2f, stillLeftFrames);
        stillLeft.setPlayMode(PlayMode.LOOP);
        walkRight = new Animation<>(1 / 6f, walkRightFrames);
        walkRight.setPlayMode(PlayMode.LOOP);
        walkLeft = new Animation<>(1 / 6f, walkLeftFrames);
        walkLeft.setPlayMode(PlayMode.LOOP);
        jumpRight = new Animation<TextureRegion>(1 / 6f, jumpRightFrames);
        jumpRight.setPlayMode(PlayMode.LOOP);
        jumpLeft = new Animation<TextureRegion>(1 / 6f, jumpLeftFrames);
        jumpLeft.setPlayMode(PlayMode.LOOP);
        duckRight = new Animation<TextureRegion>(1 / 6f, duckRightFrames);
        duckRight.setPlayMode(PlayMode.LOOP);
        duckLeft = new Animation<TextureRegion>(1 / 6f, duckLeftFrames);
        duckLeft.setPlayMode(PlayMode.LOOP);
        attackRight= new Animation<TextureRegion>(1 / 6f, attackRFrames);
        attackRight.setPlayMode(PlayMode.LOOP);
        attackLeft = new Animation<TextureRegion>(1 / 6f, attackLFrames);
        attackLeft.setPlayMode(PlayMode.LOOP);
        deathRight = new Animation<TextureRegion>(1 / 2f, deathRightFrames);
        deathRight.setPlayMode(PlayMode.LOOP);
        deathLeft = new Animation<TextureRegion>(1 / 2f, deathLeftFrames);
        deathLeft.setPlayMode(PlayMode.LOOP);
        damageRight = new Animation<TextureRegion>(3f, damageRightFrames);
        damageRight.setPlayMode(PlayMode.LOOP);
        damageLeft = new Animation<TextureRegion>(3f, damageLeftFrames);
        damageLeft.setPlayMode(PlayMode.LOOP);

        this.playerPosition = new Vector2(playerPosition.x, playerPosition.y);
        this.world = world;
        this.setSize(5.8f * 2, 6.4f * 2);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        velocity = new Vector2();
        createBodyDef();
        createBodyShape();
        createFixtureDef();
        createBody();
        createFootSensor();
    }

        private void createBodyDef() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(playerPosition);
        bodyDef.fixedRotation = true;
    }

    private void createBodyShape() {
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(3.2f, 5.6f);
    }

    private void createFixtureDef() {
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.friction = .5f;
        fixtureDef.restitution = .2f;
        fixtureDef.density = density;
    }

    private void createFootSensor() {
        footDef = new FixtureDef();
        footDef.shape = polygonShape;
        body.createFixture(footDef).setUserData("foot");
        footDef.isSensor = true;
    }

    public void createBody() {
        body = world.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
        body.setUserData(this);
        body.applyAngularImpulse(5, true);
    }

    public void update(float delta, MyContactListener listener) {
            //apply gravity
        velocity.y -= gravity * delta;
        damageTime += delta;

        if (velocity.y > speed) {
            velocity.y = speed;
        }
        else if (velocity.y < -speed) {
            velocity.y = -speed;
        }

        if ((listener.onContactWithFireball()) && damageTime >= 3f) {
            heartNumber--;
            damageTime = 0;
            hasTakenDamage = true;
        }
        if ( listener.onContactWithLava()) {
            heartNumber = 0;
            damageTime = 0;
            hasTakenDamage = true;
            speed = 30;
        }
        if (listener.onContactWithWater()) {
            speed = 50;

        }
        if (!listener.onContactWithWater()) {
            speed = 70;

        }
        if (heartNumber == 0) {
            deathTime += delta;
        }
        if (deathTime > 2.5f && !isDeath) {
            isDeath = true;
        }

        if (velocity.x < 0) { // going left
            lastWayRight = false;
        }
        else if (velocity.x > 0) { // going right
            lastWayRight = true;
        }

        canJump = listener.isPlayerOnGround();
        //body.applyForceToCenter(velocity, true);
        body.setLinearVelocity(velocity);
        if (hasTakenDamage && damageTime >= 0.9f) {
            hasTakenDamage = false;
        }
        // update animation
		animationTime += delta;
        countTime += delta;
        if (deathTime > 0) {
            if(lastWayRight) {
                currentAnimation = deathRight;
            }
            else {
                currentAnimation = deathLeft;
            }
        }
        else if (hasTakenDamage) {
            if(lastWayRight) {
                currentAnimation = damageRight;
            }
            else {
                currentAnimation = damageLeft;
            }
        }
        else if (onAttack) {
            if (lastWayRight) {
                currentAnimation = attackRight;
            }
            else {
                currentAnimation = attackLeft;
            }
            if (countTime >= 2/3f) {
                onAttack = false;
            }
        }
        else if (isDuck && !onAttack) {
            if (lastWayRight) {
                currentAnimation = duckRight;
            }
            else {
                currentAnimation = duckLeft;
            }
            if (countTime >= 2/3f) {
                isDuck = false;
            }
        }
        else {
            if (velocity.x < 0) {
                currentAnimation = walkLeft;
            }
            if (velocity.x > 0) {
                currentAnimation = walkRight;
            }
            else if (velocity.y > 0 && lastWayRight) {
                currentAnimation = jumpRight;
            }
            else if (velocity.y > 0 && !lastWayRight) {
                currentAnimation = jumpLeft;
            }
            else if (lastWayRight && velocity.x == 0) {
                currentAnimation = stillRight;
            }
            else if (!lastWayRight && velocity.x == 0) {
                currentAnimation = stillLeft;
            }
        }
        setRegion(currentAnimation.getKeyFrame(animationTime));
    }

    public void setPlayerPosition() {
        playerPosition = body.getPosition();
    }

    public void setPlayerCurrentMap(int map) {
        currentMap = map;
    }

    public void setPoint(int addedPoint) {
        point += addedPoint;
    }

    public void loadHeartAndPoint(int point, int heartNumber, int map) {
        this.point = point;
        this.heartNumber = heartNumber;
        this.currentMap = map;
    }

    //getter methods
    public BodyDef getBodyDef() {
        return bodyDef;
    }


    public Sprite getSprite() {
        return this;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean getLastWayRight() {
        return lastWayRight;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }


    @Override
    public boolean keyDown(int keycode) {
            if (keycode == up) {
                if (canJump) {
                    velocity.y = speed / 1.8f;
                }
            }
            if (keycode == left) {
                velocity.x = -speed;
            }
            if (keycode == down) {
                isDuck = true;
                countTime = 0;
            }
            if (keycode == right) {
                velocity.x = speed;
            }
            if (keycode == Keys.F) {
                onAttack = true;
                countTime = 0;
            }
            if (keycode == speedingKey) {
                if (speedingKey == Keys.K)
                speed = 120;
            }
            return true;
    }

    @Override
    public boolean keyUp(int keycode) {
            if (keycode == left) {
                velocity.x = 0;
            }
            if (keycode == right) {
                velocity.x = 0;
            }
            if (keycode == speedingKey) {
                speed = 100;
            }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public int getHeart() {
        return heartNumber;
    }

    public int getPoint() {
        return point;
    }

    public boolean isDeath() {
        return isDeath;
    }

    public int getCurrentMap() {
        return currentMap;
    }
}
