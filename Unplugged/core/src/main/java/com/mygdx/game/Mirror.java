package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Mirror {
    private final Vector2 position;
    private float rotation;
    private final float width = 80f;
    private final float height = 20f;
    private final String type;
    private Polygon bounds;

    public Mirror(Vector2 position, String type, float rotation) {
        this.position = position.cpy();
        this.type = type.toUpperCase();
        this.rotation = normalizeRotation(rotation);
        updatePolygon();
    }

    private void updatePolygon() {
        float[] vertices = new float[] {
            -width/2, -height/2,
            width/2, -height/2,
            width/2, height/2,
            -width/2, height/2
        };

        bounds = new Polygon(vertices);
        bounds.setPosition(position.x, position.y);
        bounds.setRotation(rotation);
    }

    public boolean containsPoint(Vector2 point) {
        return bounds.contains(point.x, point.y);
    }

    public void rotateBy(float degrees) {
        rotation = normalizeRotation(rotation + degrees);
        updatePolygon();
    }

    private float normalizeRotation(float degrees) {
        return ((Math.round(degrees / 45f) * 45f) % 360f);
    }

    public RayHit intersect(RaySegment ray) {
        float[] verts = bounds.getTransformedVertices();
        Vector2 intersection = new Vector2();
        float minDist = Float.MAX_VALUE;
        Vector2 closest = null;
        Vector2 normal = null;

        for (int i = 0; i < verts.length; i += 2) {
            Vector2 p1 = new Vector2(verts[i], verts[i + 1]);
            Vector2 p2 = new Vector2(verts[(i + 2) % verts.length], verts[(i + 3) % verts.length]);

            Vector2 temp = new Vector2();
            if (Intersector.intersectSegments(ray.getStart(), ray.getEnd(), p1, p2, temp)) {
                float dist = ray.getStart().dst2(temp);
                if (dist < minDist) {
                    minDist = dist;
                    closest = temp.cpy();
                    Vector2 edge = p2.cpy().sub(p1).nor();
                    normal = new Vector2(-edge.y, edge.x).nor();
                    if (normal.dot(ray.getEnd().cpy().sub(ray.getStart())) > 0) {
                        normal.scl(-1);
                    }
                }
            }
        }

        if (closest != null) {
            return new RayHit(closest, ray.getStart().dst(closest), this, false, normal);
        }

        return null;
    }

    public Vector2 reflect(Vector2 inDir, Vector2 normal, Vector2 hitPoint) {
        Vector2 reflected = inDir.cpy().sub(normal.cpy().scl(2 * inDir.dot(normal))).nor();

        if ("CONVEX".equals(type)) {
            return reflected.rotateDeg(15f).nor();
        } else if ("CONCAVE".equals(type)) {
            return hitPoint.cpy().sub(position).nor();
        } else {
            return reflected;
        }
    }

    public void draw(ShapeRenderer renderer) {
        if ("CONVEX".equals(type)) {
            renderer.setColor(Color.PINK);
        } else if ("CONCAVE".equals(type)) {
            renderer.setColor(Color.ORANGE);
        } else {
            renderer.setColor(Color.SKY);
        }

        float[] verts = bounds.getTransformedVertices();

        renderer.triangle(verts[0], verts[1], verts[2], verts[3], verts[4], verts[5]);
        renderer.triangle(verts[4], verts[5], verts[6], verts[7], verts[0], verts[1]);

    }



    public Mirror copy() {
        return new Mirror(position.cpy(), type, rotation);
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public float getRotation() {
        return rotation;
    }

    public String getType() {
        return type;
    }
}
