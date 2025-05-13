package com.mygdx.game;

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
import java.util.HashMap;
import java.util.Objects;

public class AnimationHandler {
    
    private float timer = 0;
    private boolean looping = true;
    private String current;
    private final HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();

    public void add( String name, Animation<TextureRegion> animation){
        animations.put(name, animation);
    }

    public void setCurrent(String name){
        if(Objects.equals(current, name)) return;
        assert(animations.containsKey(name)): "NO SUCH ANIMATION" + name;
        current = name;
        timer = 0;
        looping = true;
    }

    public void setCurrent(String name, boolean looping){
        setCurrent(name);
        this.looping = looping;
    }
    public void setAnimationDuration(long duration){
        animations.get(current).setFrameDuration(duration / (float) animations.get(current).getKeyFrames().length * 100);
    }

    public boolean isCurrent(String name){ return current.equals(name);}
    public boolean isFinished(){return animations.get(current).isAnimationFinished(timer);}
    public int frameIndex(){ return animations.get(current).getKeyFrameIndex(timer);}

    public TextureRegion getFrame(float delta){
        timer += delta;
        return animations.get(current).getKeyFrame(timer, looping); 
    }

}
