package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Door extends Sprite{
    private BodyDef boxDef;
    private BodyDef keyBoxDef;
    private FixtureDef fixtureDef;
    private FixtureDef contactArea;
    private FixtureDef contactKeyDef;
    private Body box;
    private Body keyBox;
    private PolygonShape contactShape;
    private PolygonShape doorShape;
    private PolygonShape contactKeyShape;
    private World world;
    private boolean hasKey = false;
    private Sprite key;
    private Sprite numSpriteKey;
    private Sprite numSpriteDoor;
    private int num;
    private MyContactListener contactListener;

    public Door(World world, TextureRegion textureDoor, TextureRegion textureKey, TextureRegion textureNum, float xDoor, float yDoor, float xKey, float yKey, int num, MyContactListener contactListener){
        super(textureDoor);
        key = new Sprite(textureKey);
        numSpriteKey = new Sprite(textureNum);
        numSpriteDoor = new Sprite(textureNum);

        numSpriteKey.setX(xKey + 2.5f);
        numSpriteKey.setY(yKey + 11);
        key.setX(xKey);
        key.setY(yKey);

        numSpriteDoor.setX(xDoor + 10f);
        numSpriteDoor.setY(yDoor + 30);
        setX(xDoor);
        setY(yDoor);
    //box------------------------------------------
        boxDef = new BodyDef();
        boxDef.type = BodyType.StaticBody;
        boxDef.position.set(xDoor + 15f, yDoor + 25);
        boxDef.fixedRotation = true;

        keyBoxDef = new BodyDef();
        keyBoxDef.type = BodyType.StaticBody;
        keyBoxDef.position.set(xKey + 5f, yKey + 5);
        keyBoxDef.fixedRotation = true;
    //box shape------------------------------------
        contactShape = new PolygonShape();
        contactShape.setAsBox(30f, 35);

        doorShape = new PolygonShape();
        doorShape.setAsBox(12f, 23);

        contactKeyShape = new PolygonShape();
        contactKeyShape.setAsBox(5f, 5);

    //fixture definition--------------------------
        contactArea = new FixtureDef();
        contactArea.shape = contactShape;
        contactArea.friction = .75f;
        contactArea.restitution = .1f;
        contactArea.density = 5;
        contactArea.isSensor = true;

        contactKeyDef = new FixtureDef();
        contactKeyDef.shape = contactKeyShape;
        contactKeyDef.friction = .75f;
        contactKeyDef.restitution = .1f;
        contactKeyDef.density = 5;
        contactKeyDef.isSensor = true;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = doorShape;
        fixtureDef.friction = .75f;
        fixtureDef.restitution = .1f;
        fixtureDef.density = 5;
        fixtureDef.isSensor = false;
    //body-----------------------------------------
        this.world = world;
        box = world.createBody(boxDef);
        box.createFixture(fixtureDef);
        box.createFixture(contactArea).setUserData(num);

        keyBox = world.createBody(keyBoxDef);
        keyBox.createFixture(contactKeyDef).setUserData(num * 10);

        this.num = num;
        this.contactListener = contactListener;
        setPosition(xDoor, yDoor);
    }

    @Override
    public void draw(Batch batch) {

        if(!contactListener.getContactWithDoors()[num - 1] || !contactListener.getAreDoorsOpen()[num - 1]){
            super.draw(batch);
            numSpriteDoor.draw(batch);
        } 

        if(!contactListener.getAreDoorsOpen()[num - 1]){
            key.draw(batch);
            numSpriteKey.draw(batch);
        }

        update(Gdx.graphics.getDeltaTime());
    }

    public void update(float delta){
        if(contactListener.getAreDoorsOpen()[num - 1]){
            box.destroyFixture(box.getFixtureList().get(0));
            fixtureDef.isSensor = true;
            box.createFixture(fixtureDef).setUserData(num);
            box.createFixture(contactArea).setUserData(num);
        }
    }

    public void setHasKey(boolean hasKey) {
        System.out.println("AaAAA");
        this.hasKey = hasKey;
        fixtureDef.isSensor = true;
        System.out.println(fixtureDef.isSensor);
        update(Gdx.graphics.getDeltaTime());
    }

    public Sprite getKey() {
        return key;
    }
}
