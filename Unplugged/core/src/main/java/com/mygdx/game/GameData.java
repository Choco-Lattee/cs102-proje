package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

public class GameData {
    public float x;
    public float y;
    public boolean lastWayRight;
    public int point;
    public int heart;
    public int currentMap;

    public GameData() {} // Required for JSON

    public GameData(float x, float y, int point, int heart, int currentMap) {
        this.x = x;
        this.y = y;
        this.point = point;
        this.heart = heart;
        this.currentMap = currentMap;
        // will added other necessary datas 
    }

    public static int getCurrentMap(int game) {
        FileHandle file;
        if (game == 3) {
            file = Gdx.files.local("player_data3.json");
        }
        else if (game == 2) {
            file = Gdx.files.local("player_data2.json");
        }
        else {
            file = Gdx.files.local("player_data1.json");
        }
        if (file.exists()) {
            Json json = new Json();
            return json.fromJson(GameData.class, file.readString()).currentMap;
        }
        else {
            return 1;
        }

    }

    public static void savePlayerPosition(float x, float y, int point, int heart, int currentMap, int game) {
        System.out.println("Saving");
        GameData data = new GameData(x, y, point, heart, currentMap);
        Json json = new Json();
        FileHandle file;
        if (game == 3) {
            file = Gdx.files.local("player_data3.json");
        }
        else if (game == 2) {
            file = Gdx.files.local("player_data2.json");
        }
        else {
            file = Gdx.files.local("player_data1.json");
        }
        file.writeString(json.toJson(data), false);
    }

    public static GameData loadPlayerPosition(int game, int map) {
        FileHandle file;
        int x, y;
        if (game == 3) {
            file = Gdx.files.local("player_data3.json");
        }
        else if (game == 2) {
            file = Gdx.files.local("player_data2.json");
        }
        else {
            file = Gdx.files.local("player_data1.json");
        }
        if (file.exists()) {
            Json json = new Json();
            System.out.println("loading");
            if (json.fromJson(GameData.class, file.readString()).currentMap == map)
                return json.fromJson(GameData.class, file.readString());
            else {
            if (map == 1) {
                x = 50;
                y = 70;
            }
            if (map == 2) {
                x = 50;
                y = 515;
            }
            else {
                x = 50;
                y = 70;
            }
            return new GameData(x, y, 0,3, map); // Default position
            }
        } else {
            if (map == 1) {
                x = 50;
                y = 70;
            }
            if (map == 2) {
                x = 50;
                y = 515;
            }
            else {
                x = 50;
                y = 70;
            }
            return new GameData(x, y, 0,3, map); // Default position
        }
    }
}
