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
        float hw = width / 2f;
        float hh = height / 2f;

        float[] vertices = new float[] {
            -hw, -hh,
             hw, -hh,
             hw,  hh,
            -hw,  hh
        };

        bounds = new Polygon(vertices);
        bounds.setOrigin(0, 0); // dönme merkezi: kendi merkezi
        bounds.setPosition(position.x, position.y);
        bounds.setRotation(rotation);
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

            if (Intersector.intersectSegments(ray.getStart(), ray.getEnd(), p1, p2, intersection)) {
                float dist = ray.getStart().dst2(intersection);
                if (dist < minDist) {
                    minDist = dist;
                    closest = intersection.cpy();
                    Vector2 edge = p2.cpy().sub(p1).nor();
                    normal = new Vector2(-edge.y, edge.x).nor();

                    // ışın p1 → p2 yönüne göre gelmiyorsa düzelt
                    Vector2 dir = ray.getEnd().cpy().sub(ray.getStart()).nor();
                    if (normal.dot(dir) > 0f) {
                        normal.scl(-1); // iç yüzeyden geliyor → dışa çevir
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
         switch (type) {
            case "CONVEX":
             return reflected.rotateDeg(20f).nor();
            case "CONCAVE":
              return reflected.lerp(position.cpy().sub(hitPoint).nor(), 0.5f).nor();
            default:
             return reflected;
        }
    }

    public void draw(ShapeRenderer renderer) {
        Color color;
        switch (type) {
            case "CONVEX":
              color = Color.PINK;
            case "CONCAVE":
             color = Color.ORANGE;
            default:
             color = Color.SKY;
        }
        renderer.setColor(color);

        float[] verts = bounds.getTransformedVertices();
        renderer.triangle(verts[0], verts[1], verts[2], verts[3], verts[4], verts[5]);
        renderer.triangle(verts[4], verts[5], verts[6], verts[7], verts[0], verts[1]);
    }

    public void rotateBy(float degrees) {
        rotation = normalizeRotation(rotation + degrees);
        updatePolygon();
    }

    private float normalizeRotation(float degrees) {
        float normalized = ((Math.round(degrees / 45f) * 45f) % 360f + 360f) % 360f;
        return normalized;
    }

    public Mirror copy() {
        return new Mirror(position.cpy(), type, rotation);
    }

    public Polygon getBounds() {
        return bounds;
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
