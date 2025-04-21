package com.mygdx.game;

import javax.swing.SpinnerDateModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {
    //* the movement velocity */
    private Vector2 velocity = new Vector2();
    private float animationTime = 0, increment;
    private float speed = 120, gravity = 60 * 1.8f;
    private TiledMapTileLayer collisionLayer;
    private boolean canJump, lastWayRight = true;
    private int currentAnimation;
    private static final int FRAME_COLS = 8, FRAME_ROWS = 9;
    private Animation<TextureRegion> stillRight, stillLeft, idle, walkRight, walkLeft, jumpRight, jumpLeft;

    public Player( TiledMapTileLayer collisionLayer, TextureRegion[][] tmp, TextureRegion[][] tmpReversed) {
        super(tmp[0][0]);
        TextureRegion[] stillRightFrames = new TextureRegion[2];
        TextureRegion[] stillLeftFrames = new TextureRegion[2];
        TextureRegion[] walkRightFrames = new TextureRegion[4];
        TextureRegion[] walkLeftFrames = new TextureRegion[4];
        TextureRegion[] jumpRightFrames = new TextureRegion[8];
        TextureRegion[] jumpLeftFrames = new TextureRegion[8];
        int stillIndex = 0;
        int walkIndex = 0;
        int jumpIndex = 0;
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
            jumpRightFrames[jumpIndex++] = tmp[5][i]; 
        }
        jumpIndex = 0;
        for (int i = 7; i >= 0; i--) {
            jumpLeftFrames[jumpIndex++] = tmpReversed[5][i];
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
        this.collisionLayer = collisionLayer;
    }
    public void setCurrentAnimation(int currentAnimation) {

    }

    public int getCurrentAnimation() {
        return currentAnimation;
    }
    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public void update(float delta) {
        //apply gravity
        velocity.y -= gravity * delta;

        // clamp velocity
        if (velocity.y > speed) {
            velocity.y = speed;
        }
        else if (velocity.y < -speed) {
            velocity.y = -speed;
        }

        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;
        
        setX(getX() + velocity.x * delta);

        increment =  collisionLayer.getTileWidth();
        increment = getWidth() < increment ? getWidth() / 2 : increment / 2;

        if (velocity.x < 0) { // going left
            collisionX = collidesLeft();
            lastWayRight = false;
        }
        else if (velocity.x > 0) { // going right
            collisionX = collidesRight();
            lastWayRight = true;
        }
        if (collisionX) {
            setX(oldX);
            velocity.x = 0;
        }

        //move on y
        setY(getY() + velocity.y * delta * 5f);

        increment =  collisionLayer.getTileHeight();
        increment = getHeight() < increment ? getHeight() / 2 : increment / 2;

        if (velocity.y < 0) { // going down
            canJump = collisionY = collidesBottom();
        }

        else if (velocity.y > 0) { // going up 
            collisionY = collidesTop();
        }

        // react to y collision
        if (collisionY) {
            setY(oldY);
            velocity.y = 0;
        }

        // update animation
		animationTime += delta;
		setRegion(velocity.x < 0 ? walkLeft.getKeyFrame(animationTime) : velocity.x > 0 ?  walkRight.getKeyFrame(animationTime) :
          velocity.y > 0 && lastWayRight ? jumpRight.getKeyFrame(animationTime) : velocity.y > 0 && !lastWayRight ? jumpLeft.getKeyFrame(animationTime) :
          lastWayRight && velocity.x == 0 ? stillRight.getKeyFrame(animationTime) :  stillLeft.getKeyFrame(animationTime));
    }

    public  TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int)( x  / collisionLayer.getTileWidth()), (int)( y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
    }

    public boolean collidesRight() {
        boolean collides = false;

        for (float step = 0; step < this.getWidth(); step += collisionLayer.getTileWidth() / 2) {
            collides = isCellBlocked(getX() + getWidth(), getY() + step);
            if (collides) {
                break;
            }
        }
        return collides;
    }

    public boolean collidesLeft() {
        boolean collides = false;

        for (float step = 0; step < this.getHeight(); step += collisionLayer.getTileHeight() / 2) {
            collides = isCellBlocked(getX(), getY() + step);
            if (collides) {
                break;
            }
        }
        return collides;
    }

    public boolean collidesTop() {
        boolean collides = false;

        for (float step = 0; step < this.getWidth(); step += collisionLayer.getTileWidth() / 2) {
            collides = isCellBlocked(getX() + step, getY() + getHeight());
            if (collides) {
                break;
            }
        }
        return collides;
    }

    public boolean collidesBottom() {
        boolean collides = false;

        for (float step = 0; step < this.getWidth(); step += collisionLayer.getTileWidth() / 2) {
            collides = isCellBlocked(getX() + step, getY());
            if (collides) {
                break;
            }
        }
        return collides;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.W:
                if (canJump) {
                    velocity.y = speed / 1.8f;
                    canJump = false;
                }
                break;
            case Keys.A:
                velocity.x = - speed;
                animationTime = 0;
                break;
            case Keys.D:
                velocity.x = speed;
                animationTime = 0;
                break;
            case Keys.SHIFT_LEFT:
                speed = speed * 1.5f;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.A:
            case Keys.D:
                velocity.x = 0;
                animationTime = 0;
                break;
            case Keys.SHIFT_LEFT:
                speed = speed / 1.5f;
                break;
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



}
