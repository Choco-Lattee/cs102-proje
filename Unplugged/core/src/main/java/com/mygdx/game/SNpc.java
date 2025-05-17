package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class SNpc extends Sprite{

    private BodyDef boxDef;
    private FixtureDef fixtureDef;
    private FixtureDef contactArea;
    private Body box;
    private PolygonShape contactShape;
    private PolygonShape npcShape;
    private World world;
    private boolean rescued = false;
    private MyContactListener contactListener;
    private int num;

    public SNpc(TextureRegion textureNpc, float x, float y, MyContactListener contactListener, int num, World world){
        super(textureNpc);

        setX(x);
        setY(y);
    //box------------------------------------------
        boxDef = new BodyDef();
        boxDef.type = BodyType.StaticBody;
        boxDef.position.set(x, y);
        boxDef.fixedRotation = true;

    //box shape------------------------------------
        contactShape = new PolygonShape();
        contactShape.setAsBox(7f, 7);

        npcShape = new PolygonShape();
        npcShape.setAsBox(5f, 5);

    //fixture definition--------------------------
        contactArea = new FixtureDef();
        contactArea.shape = contactShape;
        contactArea.friction = .75f;
        contactArea.restitution = .1f;
        contactArea.density = 5;
        contactArea.isSensor = true;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = npcShape;
        fixtureDef.friction = .75f;
        fixtureDef.restitution = .1f;
        fixtureDef.density = 5;
        fixtureDef.isSensor = false;
    //body-----------------------------------------
        this.world = world;
        box = world.createBody(boxDef);
        box.createFixture(fixtureDef);
        box.createFixture(contactArea).setUserData("stealthNpc" + num);

        this.num = num;
        this.contactListener = contactListener;
        setPosition(x - 7f, y - 7);
    }

    @Override
    public void draw(Batch batch) {

        if(!contactListener.getNpcRescued()[num - 1])
            super.draw(batch);
        
        update(Gdx.graphics.getDeltaTime());
    }

    public void update(float delta){
        if(contactListener.getNpcRescued()[num - 1]){
            box.destroyFixture(box.getFixtureList().get(0));
            fixtureDef.isSensor = true;
            box.createFixture(fixtureDef).setUserData(num);
            box.createFixture(contactArea).setUserData(num);
        }
    }
}

