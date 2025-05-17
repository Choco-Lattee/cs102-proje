package com.mygdx.game;

import java.util.Base64;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Fireball extends Sprite {
    private Body fireballBody;
    private World world;
    private MicrowaveRobot robot;
    private Vector2 velocity = new Vector2(), startPosition;
    private float speed = 10, deathTime = 0;
    private float animationTime;
    private boolean hasExploded = false, hasStartedToDie = false;
    private int death;
    private Animation<TextureRegion> currentAnimation, loopLeft, explosionLeft;

    public Fireball(TextureRegion[][] tmp, World world, MicrowaveRobot robot) {
        super(tmp[0][0]);
        this.world = world;
        this.robot = robot;
        Texture explosionTex = new Texture("Microwave/Fireball_ExplosionRight.png");
        TextureRegion[][] explosionFr = TextureRegion.split(explosionTex, explosionTex.getWidth() / 7, explosionTex.getHeight() / 1);
        TextureRegion[] explosionLeftFrames = new TextureRegion[7];
        TextureRegion[] loopFrames = new TextureRegion[5];
        int index = 0;
        for (int i = 4; i >= 0; i--) {
            loopFrames[index++] = tmp[0][i];
        }
        index = 0;
        for (int i = 6; i >= 0; i--) {
            explosionLeftFrames[index++] = explosionFr[0][i];
        }

        loopLeft = new Animation<TextureRegion>(1 / 2f, loopFrames);
        loopLeft.setPlayMode(PlayMode.LOOP);
        explosionLeft = new Animation<TextureRegion>(1 / 6f, explosionLeftFrames);
        explosionLeft.setPlayMode(PlayMode.LOOP);
        this.setSize(20f, 16f);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        createFireball();
        death = 0;
    }

    public void createFireball() {
        PolygonShape fireballShape = new PolygonShape();
        fireballShape.setAsBox(4.8f, 2f);
        FixtureDef fireDef = new FixtureDef();
        fireDef.shape = fireballShape;
        fireDef.friction = .25f;
        fireDef.restitution = .1f;
        fireDef.density = 0;
        fireDef.isSensor = true;
        BodyDef fireballBodyDef = new BodyDef();
        fireballBodyDef.type = BodyType.KinematicBody;
        fireballBodyDef.position.set(robot.getBody().getPosition());
        fireballBodyDef.fixedRotation = true;
        fireballBody = world.createBody(fireballBodyDef);
        fireballBody.createFixture(fireDef).setUserData("fireball");
        startPosition = new Vector2(fireballBody.getPosition());
    }

    public void setMovement(float delta, boolean isRight, MyContactListener listener) {
        animationTime += delta;
        if (!checkForDestruction(listener)) {
            if (isRight) {
                velocity.x = speed;
                velocity.y = 0;
            }
            else {
                velocity.x = -speed;
                velocity.y = 0;
            }
            fireballBody.setLinearVelocity(velocity);
            currentAnimation = loopLeft;
        }
        else {
            hasStartedToDie = true;
        }
        if (hasStartedToDie) {
            deathTime += delta;
        }
        if (deathTime > 0.6f && !hasExploded) {
            destroy();
        }
        if (deathTime > 0) {
            currentAnimation = explosionLeft;
        }
        setRegion(currentAnimation.getKeyFrame(animationTime));
    }

    public void destroy() {
        world.destroyBody(fireballBody);
        hasExploded = true;
    }

    public boolean checkForDestruction(MyContactListener listener) {
        if (Math.abs(fireballBody.getPosition().dst(startPosition)) >= 20f || listener.onContactWithFireball()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hasExploded() {
        return hasExploded;
    }

    public Body getBody() {
        return fireballBody;
    }
}
