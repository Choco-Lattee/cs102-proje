package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Level {
    private List<Mirror> mirrors = new ArrayList<>();
    private List<Mirror> originalMirrors = new ArrayList<>();
    private List<LightSource> lightSources = new ArrayList<>();
    private List<RaySegment> rays = new ArrayList<>();
    private TargetDoor door;
    private boolean completed = false;

    public void setLightSourcePositions(List<Vector2> positions) {
        lightSources.clear();
        for (Vector2 pos : positions) {
            lightSources.add(new LightSource(pos, new Vector2(1, 0)));
        }
    }

    public void setInitialLightDirection(Vector2 direction) {
        for (LightSource source : lightSources) {
            source.setDirection(direction);
        }
    }

    public void setTargetDoorPos(Vector2 pos) {
        this.door = new TargetDoor(null, null, pos.x, pos.y, true);
    }

    public void setDoorHorizontal(boolean horizontal) {
        if (door != null) {
            door = new TargetDoor(null, null, door.getX(), door.getY(), horizontal);
        }
    }

    public void setMirrors(List<Mirror> mirrorList) {
        mirrors.clear();
        originalMirrors.clear();
        for (Mirror m : mirrorList) {
            mirrors.add(m.copy());
            originalMirrors.add(m.copy());
        }
    }

    public void reset() {
        mirrors.clear();
        for (Mirror m : originalMirrors) {
            mirrors.add(m.copy());
        }
        rays.clear();
        completed = false;
        if (door != null) door.close();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void update(float delta) {
        if (completed) return;

        rays.clear();
        for (LightSource source : lightSources) {
            traceLight(source.getPosition(), source.getDirection(), 0);
        }

        if (door != null && isLightThroughGap()) {
            door.open();
            completed = true;
        } else if (door != null) {
            door.close();
        }
    }

    private boolean isLightThroughGap() {
        Vector2 center = door.getGapCenter();
        float gapSize = 20f;
        Vector2 gapStart, gapEnd;

        if (door.isHorizontal()) {
            gapStart = new Vector2(center.x - gapSize / 2f, center.y);
            gapEnd = new Vector2(center.x + gapSize / 2f, center.y);
        } else {
            gapStart = new Vector2(center.x, center.y - gapSize / 2f);
            gapEnd = new Vector2(center.x, center.y + gapSize / 2f);
        }

        for (RaySegment ray : rays) {
            if (Intersector.intersectSegments(ray.getStart(), ray.getEnd(), gapStart, gapEnd, null)) {
                return true;
            }
        }
        return false;
    }

    private void traceLight(Vector2 origin, Vector2 direction, int depth) {
        if (depth > 20) return; // Sonsuz döngüyü önle

        Vector2 start = origin.cpy();
        Vector2 end = start.cpy().add(direction.cpy().setLength(1500));
        RaySegment ray = new RaySegment(start, end);
        RayHit closestHit = findClosestHit(ray);

        if (closestHit != null) {
            end = closestHit.position.cpy();
        }

        rays.add(new RaySegment(start, end));

        if (closestHit != null && closestHit.isMirror()) {
            Vector2 newDir = closestHit.mirror.reflect(direction, closestHit.normal, closestHit.position);
            Vector2 newOrigin = end.cpy().add(newDir.cpy().scl(0.1f));
            traceLight(newOrigin, newDir, depth + 1);
        }
    }

    private RayHit findClosestHit(RaySegment ray) {
        RayHit closestHit = null;
        for (Mirror m : mirrors) {
            RayHit hit = m.intersect(ray);
            if (hit != null && (closestHit == null || hit.distance < closestHit.distance)) {
                closestHit = hit;
            }
        }

        if (door != null) {
            for (Polygon part : door.getDoorPolygons()) {
                Vector2 intersection = findIntersection(ray.getStart(), ray.getEnd(), part);
                if (intersection != null) {
                    float dist = ray.getStart().dst(intersection);
                    if (closestHit == null || dist < closestHit.distance) {
                        closestHit = new RayHit(intersection, dist, null, true, null);
                    }
                }
            }
        }
        return closestHit;
    }
        

    public Level copy() {
        Level newLevel = new Level();
        newLevel.setTargetDoorPos(new Vector2(door.getX(), door.getY()));
        newLevel.setDoorHorizontal(door.isHorizontal());

        List<Vector2> lightCopy = new ArrayList<>();
        for (LightSource ls : lightSources) {
            lightCopy.add(ls.getPosition().cpy());
        }
        newLevel.setLightSourcePositions(lightCopy);
        newLevel.setInitialLightDirection(lightSources.get(0).getDirection().cpy());

        List<Mirror> mirrorCopy = new ArrayList<>();
        for (Mirror m : mirrors) {
            mirrorCopy.add(m.copy());
        }
        newLevel.setMirrors(mirrorCopy);

        return newLevel;
    }

    public int getMirrorCount() {
        return mirrors.size();
    }

    public int getLightCount() {
        return lightSources.size();
    }

    private Vector2 findIntersection(Vector2 start, Vector2 end, Polygon polygon) {
        float[] vertices = polygon.getTransformedVertices();
        for (int i = 0; i < vertices.length; i += 2) {
            Vector2 p1 = new Vector2(vertices[i], vertices[i + 1]);
            Vector2 p2 = new Vector2(vertices[(i + 2) % vertices.length], vertices[(i + 3) % vertices.length]);
            Vector2 intersection = new Vector2();
            if (Intersector.intersectSegments(start, end, p1, p2, intersection)) {
                return intersection;
            }
        }
        return null;
    }

    public void handleClick(float x, float y) {
        Vector2 click = new Vector2(x, y);
        for (Mirror m : mirrors) {
            if (m.getBounds().contains(click)) {
                m.rotateBy(45); // 45 derece döndür
                PuzzleGame game = (PuzzleGame) Gdx.app.getApplicationListener();
                game.moveCount++;
                break;
            }
        }
    }
    public void draw(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (LightSource source : lightSources) source.draw(renderer);
        for (Mirror mirror : mirrors) mirror.draw(renderer);
        renderer.setColor(Color.YELLOW);
        for (RaySegment ray : rays) renderer.rectLine(ray.getStart(), ray.getEnd(), 2);
        if (door != null) door.draw(renderer);
        renderer.end();
    }
}
