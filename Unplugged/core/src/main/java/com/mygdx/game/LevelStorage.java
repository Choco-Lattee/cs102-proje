package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class LevelStorage {
    private static final List<Level> flatLevels = new ArrayList<>();
    private static final List<Level> convexLevels = new ArrayList<>();
    private static final List<Level> concaveLevels = new ArrayList<>();

    public static void loadAllLevels() {
        flatLevels.clear();
        convexLevels.clear();
        concaveLevels.clear();

        flatLevels.addAll(loadSubset("PuzzleAssets/flat_levels.json"));
        convexLevels.addAll(loadSubset("PuzzleAssets/convex_levels.json"));
        concaveLevels.addAll(loadSubset("PuzzleAssets/concave_levels.json"));
    }

    private static List<Level> loadSubset(String fileName) {
        List<Level> levels = new ArrayList<>();
        FileHandle file = Gdx.files.internal(fileName);
        JsonValue root = new JsonReader().parse(file);

        for (JsonValue json : root) {
            Level level = new Level();

            List<Vector2> lightPositions = new ArrayList<>();
            for (JsonValue light : json.get("lightSourcePositions")) {
                lightPositions.add(new Vector2(light.getFloat("x"), light.getFloat("y")));
            }
            level.setLightSourcePositions(lightPositions);

            JsonValue dir = json.get("initialLightDirection");
            level.setInitialLightDirection(new Vector2(dir.getFloat("x"), dir.getFloat("y")));

            JsonValue doorPos = json.get("targetDoorPos");
            level.setTargetDoorPos(new Vector2(doorPos.getFloat("x"), doorPos.getFloat("y")));
            level.setDoorHorizontal(json.getBoolean("isDoorHorizontal", true));

            List<Mirror> mirrors = new ArrayList<>();
            for (JsonValue m : json.get("mirrors")) {
                Vector2 pos = new Vector2(
                    m.get("position").getFloat("x"),
                    m.get("position").getFloat("y")
                );
                String type = m.getString("type");
                float rotation = m.getFloat("rotation");
                mirrors.add(new Mirror(pos, type, rotation));
            }

            level.setMirrors(mirrors);
            levels.add(level);
        }
        return levels;
    }

    public static Level getLevel(String type, int index) {
        Level original;
        switch (type.toUpperCase()) {
            case "FLAT":
                original = getSafe(flatLevels, index);
                break;
            case "CONVEX":
                original = getSafe(convexLevels, index);
                break;
            case "CONCAVE":
                original = getSafe(concaveLevels, index);
                break;
            default:
                original = null;
        }
        return original != null ? original.copy() : null;
    }

    public static int getLevelCount(String type) {
        switch (type.toUpperCase()) {
            case "FLAT":
                return flatLevels.size();
            case "CONVEX":
                return convexLevels.size();
            case "CONCAVE":
                return concaveLevels.size();
            default:
                return 0;
        }
    }

    private static Level getSafe(List<Level> list, int index) {
        return (index >= 0 && index < list.size()) ? list.get(index) : null;
    }
}
