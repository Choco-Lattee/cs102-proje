package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Furnace extends Sprite{
    private int type;
    private Body box;
    private BodyDef boxDef;
    private PolygonShape boxShape;
    private FixtureDef fixtureDef;
    private float height;
    private float width;
    private World world;
    private float spriteX;
    private float spriteY;
    private Player player;
    private Sprite spriteNormal;
    private Sprite spriteTransparent;

    public Furnace(TextureRegion textureRegion, TextureRegion transparent, int type, float x, float y, World world, Player player){
        super(textureRegion);
        this.type = type;

        spriteNormal = new Sprite(textureRegion);
        spriteTransparent = new Sprite(transparent);

        switch(type){
            case 1:
                width = 13f;
                height = 13;
                spriteX = -32f;
                spriteY = -32;
            break;
            case 2:
                width = 15f;
                height = 15;
                spriteX = -45f;
                spriteY = -25;
            break;
            case 3:
                width = 30f;
                height = 20;
                spriteX = -44f;
                spriteY = -25;
            break;
            case 4:
                width = 25f;
                height = 30;
                spriteX = -38f;
                spriteY = -35;
            break;
        }

    //box------------------------------------------
        boxDef = new BodyDef();
        boxDef.type = BodyType.StaticBody;
        boxDef.position.set(new Vector2(x, y));
        boxDef.fixedRotation = true;

    //box shape------------------------------------
        boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);

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
        this.player = player;
        setPosition(box.getPosition().x + spriteX, box.getPosition().y + spriteY);
        spriteNormal.setPosition(box.getPosition().x + spriteX, box.getPosition().y + spriteY);
        spriteTransparent.setPosition(box.getPosition().x + spriteX, box.getPosition().y + spriteY);
    }

    @Override
    public void draw(Batch batch) {
        if(player.getBox().getPosition().y > box.getPosition().y)
            spriteTransparent.draw(batch);
        
        else
            spriteNormal.draw(batch);
    }

    public Body getBox() {
        return box;
    }

    public int getType() {
        return type;
    }

    public PolygonShape getBoxShape() {
        return boxShape;
    }

    public float getHeight(){
        return height;
    }

    public float getWidth(){
        return width;
    }

    @Override
    public float getX() {
        return box.getPosition().x;
    }
    
    @Override
    public float getY() {
        return box.getPosition().y;
    }
}

