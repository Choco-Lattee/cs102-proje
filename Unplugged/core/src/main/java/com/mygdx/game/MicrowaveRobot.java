package com.mygdx.game;

import java.io.FileFilter;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Plane.PlaneSide;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MicrowaveRobot extends Sprite {
    private Vector2 velocity;
    private float speed = 30;
    private float animationTime = 0, deathTime = 0, fireballTime = 0, posX, posY;
    private Body body;
    private World world;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef, footDef;
    private Fixture fixture;
    private PolygonShape polygonShape;
    private boolean lastWayRight = true, right = true, targetDetected = false, death = false;;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> stillRight, stillLeft, walkRight, walkLeft, aimDetectedL, aimDetectedR, deathAniLeft, 
    deathAniRight, attackLeft, attackRight, fireballLoopLeft, fireballExpLeft;
    private ArrayList<Fireball> fireballs; 

    public MicrowaveRobot(World world, Texture stillTex, float x, float y) {
        super(stillTex);
        Texture stillLeftTex = new Texture("Unplugged/assets/Microwave/Idle.png");
        Texture stillRightTex = new Texture("Unplugged/assets/Microwave/IdleRight.png");
        Texture walkLeftTex = new Texture("Unplugged/assets/Microwave/Walk.png");
        Texture walkRightTex = new Texture("Unplugged/assets/Microwave/WalkRight.png");
        Texture aimDetectedRTex = new Texture("Unplugged/assets/Microwave/hitLeft.png");
        Texture aimDetectedLTex = new Texture("Unplugged/assets/Microwave/Hit.png");
        Texture deathTex = new Texture("Unplugged/assets/Microwave/Death.png");
        Texture deathRightTex = new Texture("Unplugged/assets/Microwave/DeathRight.png");
        Texture attackLeftTex = new Texture("Unplugged/assets/Microwave/Attack.png");
        Texture attackRightTex = new Texture("Unplugged/assets/Microwave/AttackRight.png");
        TextureRegion[][] deathLeftFr = TextureRegion.split(deathTex, deathTex.getWidth() / 8, deathTex.getHeight() / 1);
        TextureRegion[] deathLeftFrames = new TextureRegion[7];
        TextureRegion[][] deathRightFr = TextureRegion.split(deathRightTex, deathRightTex.getWidth() / 8, deathRightTex.getHeight() / 1);
        TextureRegion[] deathRightFrames = new TextureRegion[7];
        TextureRegion[][] aimDetectedRFr = TextureRegion.split(aimDetectedRTex, aimDetectedRTex.getWidth() / 4, aimDetectedRTex.getHeight() / 1 );
        TextureRegion[] aimDetectedRFrames = new TextureRegion[4];
        TextureRegion[][] aimDetectedLFr = TextureRegion.split(aimDetectedLTex, aimDetectedLTex.getWidth() / 4, aimDetectedLTex.getHeight() / 1 );
        TextureRegion[] aimDetectedLFrames = new TextureRegion[4];
        TextureRegion[][] stillLeftFr  = TextureRegion.split(stillLeftTex, stillLeftTex.getWidth() / 5, stillLeftTex.getHeight() / 1);
        TextureRegion[] stillLeftFrames = new TextureRegion[5];
        TextureRegion[][] stillRightFr  = TextureRegion.split(stillRightTex, stillRightTex.getWidth() / 5, stillRightTex.getHeight() / 1);
        TextureRegion[] stillRightFrames = new TextureRegion[5];
        TextureRegion[][] walkRightFr  = TextureRegion.split(walkRightTex, walkRightTex.getWidth() / 6, walkRightTex.getHeight() / 1);
        TextureRegion[] walkRightFrames = new TextureRegion[6];
        TextureRegion[][] walkLeftFr  = TextureRegion.split(walkLeftTex, walkLeftTex.getWidth() / 6, walkLeftTex.getHeight() / 1);
        TextureRegion[] walkLeftFrames = new TextureRegion[6];
        TextureRegion[][] attackLeftFr = TextureRegion.split(attackLeftTex, attackLeftTex.getWidth() / 15, attackLeftTex.getHeight() / 1);
        TextureRegion[] attackLeftFrames = new TextureRegion[15];
        TextureRegion[][] attackRightFr = TextureRegion.split(attackRightTex, attackRightTex.getWidth() / 15, attackRightTex.getHeight() / 1);
        TextureRegion[] attackRightFrames = new TextureRegion[15];
        int stillIndex = 0;
        int walkIndex = 0;
        int aimIndex = 0;
        for (int i = 0; i < 7; i++) {
            deathRightFrames[stillIndex++] = deathRightFr[0][i];
        }
        stillIndex = 0;
        for (int i = 7; i >= 1; i--) {
            deathLeftFrames[stillIndex++] = deathLeftFr[0][i];
        }
        stillIndex = 0;
        for (int i = 0; i < 5; i++) {
            stillRightFrames[stillIndex++] = stillRightFr[0][i];
        }
        stillIndex = 0;
        for (int i = 4; i >= 0; i--) {
            stillLeftFrames[stillIndex++] = stillLeftFr[0][i];
        }
        for (int k = 5; k >= 0; k--) {
            walkLeftFrames[walkIndex++] = walkLeftFr[0][k];
        }
        walkIndex = 0;
        for (int k = 0; k < 6; k++) {
            walkRightFrames[walkIndex++] = walkRightFr[0][k];
        }
        for (int k = 3; k >= 0; k--) {
            aimDetectedLFrames[aimIndex++] = aimDetectedLFr[0][k];
        }
        aimIndex = 0;
        for (int k = 0; k < 4; k++) {
            aimDetectedRFrames[aimIndex++] = aimDetectedRFr[0][k];
        }
        aimIndex = 0;
        for (int k = 14; k >= 0; k--) {
            attackLeftFrames[aimIndex++] = attackLeftFr[0][k];
        }
        aimIndex = 0;
        for (int k = 0; k < 15; k++) {
            attackRightFrames[aimIndex++] = attackRightFr[0][k];
        }

        deathAniLeft = new Animation<TextureRegion>(1 / 2f, deathLeftFrames);
        deathAniLeft.setPlayMode(PlayMode.LOOP);
        deathAniRight = new Animation<TextureRegion>(1 / 2f, deathRightFrames);
        deathAniRight.setPlayMode(PlayMode.LOOP);
        stillRight = new Animation<TextureRegion>(1 / 2f, stillRightFrames);
        stillRight.setPlayMode(PlayMode.LOOP);
        stillLeft = new Animation<TextureRegion>(1/ 2f, stillLeftFrames);
        stillLeft.setPlayMode(PlayMode.LOOP);
        walkRight = new Animation<>(1 / 6f, walkRightFrames);
        walkRight.setPlayMode(PlayMode.LOOP);
        walkLeft = new Animation<>(1 / 6f, walkLeftFrames);
        walkLeft.setPlayMode(PlayMode.LOOP);
        aimDetectedL = new Animation<>(1 / 6f, aimDetectedLFrames);
        aimDetectedL.setPlayMode(PlayMode.LOOP);
        aimDetectedR = new Animation<>(1 / 6f, aimDetectedRFrames);
        aimDetectedR.setPlayMode(PlayMode.LOOP);
        attackLeft = new Animation<TextureRegion>(1 / 6f, attackLeftFrames);
        attackLeft.setPlayMode(PlayMode.LOOP);
        attackRight = new Animation<TextureRegion>(1 / 6f, attackRightFrames);
        attackRight.setPlayMode(PlayMode.LOOP);

        this.world = world;
        this.setSize(12f * 2, 12f * 2);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        velocity = new Vector2();
        posX = x;
        posY = y;
        createBodyDef();
        createBodyShape();
        createFixtureDef();
        createBody();
        createFootSensor();
        fireballs = new ArrayList<Fireball>();
    }

    private void createBodyDef() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(posX, posY);
        bodyDef.fixedRotation = true;
    }

    private void createBodyShape() {
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(4.8f, 4f);
    }

    private void createFixtureDef() {
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.friction = .25f;
        fixtureDef.restitution = .1f;
        fixtureDef.density = 0.0675f;
    }

    private void createFootSensor() {
        footDef = new FixtureDef();
        PolygonShape footShape = new PolygonShape();
        footShape.setAsBox(4.8f, 1f );
        footDef.shape = polygonShape;
        body.createFixture(footDef).setUserData("robot");
        footDef.isSensor = true;
    }

    public void createBody() {
        body = world.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
        body.setUserData(this);
        body.applyAngularImpulse(5, true);
    }

    public boolean isRight() {
        if (velocity.x < 0) { // going left
            lastWayRight = false;
        }
        else if (velocity.x > 0) { // going right
            lastWayRight = true;
        }
        return lastWayRight;
    }

    public void update(float delta, MyContactListener listener, Box2DPlayer player) {
        if(listener.onContactWithRobotAndPlayer() && (listener.getLastPos1Robot().getBody().equals(body) || listener.getLastPos2Robot().getBody().equals(body))) {
            player.setPoint(250);
            deathTime += delta;
        }
        if (deathTime > 0.4f && !death) {
            death = true;
            world.destroyBody(body);
        }
        if (right) {
            velocity.x = speed;
        }
        if (!right) {
            velocity.x = -speed;
        }
        if (Math.abs(body.getPosition().x-(posX-15)) <= 2) {
            right = true;
        }
        if (Math.abs(body.getPosition().x-(posX+15)) <= 2) {
            right = false;
        }

        if (velocity.x < 0) { // going left
            lastWayRight = false;
        }
        else if (velocity.x > 0) { // going right
            lastWayRight = true;
        }

        if (Math.abs(body.getPosition().x- player.getBody().getPosition().x) <= 50 && isTowardsPlayer(player) && Math.abs(body.getPosition().y- player.getBody().getPosition().y) <= 30) {
            targetDetected = true;
            velocity.x = 0;
            fireballTime += delta;
            if (fireballTime > 1.8f && !death) {
                Texture fireTex = new Texture("Unplugged/assets/Microwave/Fireball_loop.png");
                TextureRegion[][] fireFr = TextureRegion.split(fireTex, fireTex.getWidth() / 5, fireTex.getHeight() / 1);
                Fireball fireball = new Fireball(fireFr, world, this);
                fireballs.add(fireball);
                fireballTime = 0;
            }
            for (Fireball fireball: fireballs) {
                fireball.setMovement(delta, lastWayRight, listener);
            }
        }
        else {
            targetDetected = false;
        }

        if (deathTime == 0) {
            body.applyForceToCenter(velocity, true);
        }
        // update animation
		animationTime += delta;
        if (deathTime > 0) {
            if (lastWayRight) {
                currentAnimation = deathAniRight;
            }
            else {
                currentAnimation = deathAniLeft;
            }
        }
        else if (targetDetected ) {
            if (lastWayRight) {
                currentAnimation = attackRight;
            }
            else {
                currentAnimation = attackLeft;
            }
        }
        else {
            if (velocity.x < 0) {
                currentAnimation = walkLeft;
            }
            if (velocity.x > 0) {
                currentAnimation = walkRight;
            }
            if (lastWayRight && velocity.x == 0) {
                currentAnimation = stillRight;
            }
            if ((lastWayRight && velocity.x == 0)) {
                currentAnimation = stillLeft;
            }
        }
		setRegion(currentAnimation.getKeyFrame(animationTime));
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

    public ArrayList<Fireball> getFireballs() {
        return fireballs;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean getDeathCondition() {
        return death;
    }

    private boolean isTowardsPlayer(Box2DPlayer player) {
        if (body.getPosition().x - player.getBody().getPosition().x > 0 && !lastWayRight) {
            return true;
        }
        else if(body.getPosition().x - player.getBody().getPosition().x < 0 && lastWayRight) {
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteFireball(Fireball fire) {
        fireballs.remove(fire);
    }

}
