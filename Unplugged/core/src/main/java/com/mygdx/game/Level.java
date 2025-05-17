package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Level {

    private List<LightSource> lightSources = new ArrayList<>();
    private Vector2 initialLightDirection;
    private Vector2 targetDoorPos;
    private boolean isDoorHorizontal;
    private List<Mirror> mirrors = new ArrayList<>();
    private TargetDoor targetDoor;
    private boolean levelCompleted = false;

    public void update(float delta) {
        for (LightSource light : lightSources) {
            light.updateRays(mirrors, targetDoor, initialLightDirection);
        }

        if (targetDoor != null && targetDoor.isTriggered()) {
            setLevelCompleted(true);
        }
    }

    public void draw(ShapeRenderer sr) {
        for (Mirror mirror : mirrors) {
            mirror.draw(sr);
        }

        for (LightSource light : lightSources) {
            light.draw(sr);
        }

        if (targetDoor != null) {
            targetDoor.draw(sr);
        }
    }

    public void reset() {
        targetDoor = new TargetDoor(targetDoorPos, isDoorHorizontal);
        for (LightSource light : lightSources) {
            light.clearRays();
        }
        setLevelCompleted(false);
    }

    public void handleClick(float x, float y) {
        Vector2 click = new Vector2(x, y);
        for (Mirror mirror : mirrors) {
            if (mirror.containsPoint(click)) {
                mirror.rotate();
                break;
            }
        }
    }

    public Level copy() {
        Level copy = new Level();

        List<LightSource> newLights = new ArrayList<>();
        for (LightSource ls : this.lightSources) {
            newLights.add(new LightSource(ls.getPosition().cpy()));
        }
        copy.lightSources = newLights;

        List<Mirror> newMirrors = new ArrayList<>();
        for (Mirror m : this.mirrors) {
            newMirrors.add(m.copy());
        }
        copy.mirrors = newMirrors;

        copy.initialLightDirection = this.initialLightDirection.cpy();
        copy.targetDoorPos = this.targetDoorPos.cpy();
        copy.isDoorHorizontal = this.isDoorHorizontal;

        return copy;
    }

    // Setters
    public void setLightSourcePositions(List<Vector2> positions) {
        this.lightSources.clear();
        for (Vector2 pos : positions) {
            this.lightSources.add(new LightSource(pos));
        }
    }

    public void setInitialLightDirection(Vector2 dir) {
        this.initialLightDirection = dir;
    }

    public void setTargetDoorPos(Vector2 pos) {
        this.targetDoorPos = pos;
    }

    public void setDoorHorizontal(boolean value) {
        this.isDoorHorizontal = value;
    }

    public void setMirrors(List<Mirror> mirrors) {
        this.mirrors = mirrors;
    }

    // Getters
    public List<Mirror> getMirrors() {
        return mirrors;
    }

    public List<LightSource> getLightSources() {
        return lightSources;
    }

    public Vector2 getInitialLightDirection() {
        return initialLightDirection;
    }

    public Vector2 getTargetDoorPos() {
        return targetDoorPos;
    }

    public boolean isDoorHorizontal() {
        return isDoorHorizontal;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    public void setLevelCompleted(boolean completed) {
        this.levelCompleted = completed;
    }
}
