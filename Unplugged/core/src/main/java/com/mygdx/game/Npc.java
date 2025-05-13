package com.mygdx.game;

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

public class Npc extends Sprite {
    private Body body;
    private World world;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef, footDef;
    private PolygonShape polygonShape;
    private Animation<TextureRegion> still, idleLeft, idleRight;
    private Animation<TextureRegion> currentAnimation;
    private float animationTime = 0, posX, posY;


    public Npc(World world, TextureRegion[][] tmp, float x, float y) {
        super(tmp[0][0]);
        TextureRegion[] idleLeftFrames = new TextureRegion[6];
        TextureRegion[] idleRightFrames = new TextureRegion[6];
        TextureRegion[] stillFrames = new TextureRegion[6];
        int index = 0;
        posX = x; posY = y;
        for (int i = 0; i < 6; i++) {
            idleRightFrames[index++] = tmp[0][i];
        }
        index = 0;
        for (int i = 12; i < 18; i++) {
            idleLeftFrames[index++] = tmp[0][i];
        }
        index = 0;
        for (int k = 18; k < 24; k++) {
            stillFrames[index++] = tmp[0][k];
        }
        still = new Animation<TextureRegion>(1 / 2f, stillFrames);
        still.setPlayMode(PlayMode.LOOP);
        idleRight = new Animation<TextureRegion>(1 / 2f, idleRightFrames);
        idleRight.setPlayMode(PlayMode.LOOP);
        idleLeft = new Animation<TextureRegion>(1/ 2f, idleLeftFrames);
        idleLeft.setPlayMode(PlayMode.LOOP);

        this.world = world;
        this.setSize(5.8f * 2, 6.4f * 2);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        createBodyDef();
        createBodyShape();
        createFixtureDef();
        createBody();
        createFootSensor();
    }


    public void update(float delta, MyContactListener listener, Box2DPlayer player) {
        if (listener.isOnContactWithNPC()) {
            if (player.getBody().getPosition().x - body.getPosition().x < 0) {
                currentAnimation = idleLeft;
            }
            else {

                currentAnimation = idleRight;
            }
        }
        else {
            currentAnimation = still;
        }
        // update animation
		animationTime += delta;
		setRegion(currentAnimation.getKeyFrame(animationTime));
    }



    private void createBodyDef() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(posX, posY);
        bodyDef.fixedRotation = true;
    }

    private void createBodyShape() {
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(4f, 6f);
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
        PolygonShape footShape =  new PolygonShape();
        footShape.setAsBox(20f, 5.6f);
        footDef.shape = footShape;
        footDef.isSensor = true;
        body.createFixture(footDef).setUserData("npc");

    }

    public void createBody() {
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);
        body.applyAngularImpulse(5, true);
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
    
}
