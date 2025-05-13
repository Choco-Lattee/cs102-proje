package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class LightHandler {
    
    public ConeLight visionConeCreator(RayHandler rayHandler, Body body, Color c, float dist){
        ConeLight c1 = new ConeLight(rayHandler, 120, c, dist, dist, dist, dist, dist);
        c1.setSoftnessLength(0f);
        c1.attachToBody(body);
        c1.setXray(false);
        return c1;
    }
}
