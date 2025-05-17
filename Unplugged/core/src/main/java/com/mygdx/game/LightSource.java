package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class LightSource {

    private Vector2 position;
    private List<RaySegment> rays = new ArrayList<>();

    public LightSource(Vector2 position) {
        this.position = position;
    }

    public void updateRays(List<Mirror> mirrors, TargetDoor door, Vector2 initialDirection) {
        rays.clear();

        Vector2 currentPos = position.cpy();
        Vector2 direction = initialDirection.cpy().nor();
        int maxReflections = 10;

        for (int i = 0; i < maxReflections; i++) {
            RaySegment ray = new RaySegment(currentPos.cpy(), direction.cpy());
            RayHit closestHit = null;

            for (Mirror mirror : mirrors) {
                RayHit hit = ray.intersectMirror(mirror);
                if (hit != null && (closestHit == null || hit.getDistance() < closestHit.getDistance())) {
                    closestHit = hit;
                }
            }

            if (closestHit != null) {
                ray.setEndPoint(closestHit.getPoint());
                rays.add(ray);

                direction = closestHit.getReflectedDirection();
                currentPos = closestHit.getPoint().cpy();
            } else {
                ray.setEndPoint(currentPos.cpy().add(direction.cpy().scl(2000)));
                rays.add(ray);
                break;
            }
        }

        if (door != null) {
            for (RaySegment ray : rays) {
                if (door.intersects(ray)) {
                    door.setTriggered(true);
                }
            }
        }
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(Color.RED);
        sr.circle(position.x, position.y, 5);

        sr.setColor(Color.YELLOW);
        for (RaySegment ray : rays) {
            ray.draw(sr);
        }
    }

    public void clearRays() {
        rays.clear();
    }

    public Vector2 getPosition() {
        return position;
    }
}