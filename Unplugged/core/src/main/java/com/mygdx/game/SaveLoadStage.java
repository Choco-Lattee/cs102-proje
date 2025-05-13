package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SaveLoadStage extends Stage {
    // Game data controllers
    private static int gameData1 = 1;
    private static int gameData2 = 2;
    private static int gameData3 = 3;
    protected static int lastGame = 1;

    // menu design stuffs
    private Table table;
    private TextureAtlas atlas;
    private Skin skin;
    private TextButton game1, game2, game3, back;
    TextButton del1, del2, del3;
    private BitmapFont white, black; 
    private Label heading;
    Image menuImage;
    float imageToStageWitdth, imageToStageHeight, width, height;
    boolean isSave;
    String type;

    public SaveLoadStage(Viewport viewport, float width, float height, boolean isSaveStage) {
        super(viewport);
        isSave = isSaveStage;
        if (isSave) {
            type = "SAVE";
        }
        else {
            type = "LOAD";
        }
        this.width = width;
        this.height = height;
        setProperties();
    }

    private void setProperties() {
        atlas = new TextureAtlas("assets/settings_tools.atlas");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setBounds(0, 0, width, height);
        table.setBackground(skin.getDrawable("menu"));


        white = new BitmapFont(Gdx.files.internal("assets/font/white.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("assets/font/black.fnt"), false);
        white.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        white.getData().setScale(1.5f,1.5f);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal");
        textButtonStyle.down = skin.getDrawable("button.pressed");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;

        game1 = new TextButton(type + "I", textButtonStyle);
        game2 = new TextButton(type + "II", textButtonStyle);
        game3 = new TextButton(type + "III", textButtonStyle);
        back = new TextButton("BACK", textButtonStyle);
        del1= new TextButton("X", textButtonStyle);
        del2= new TextButton("X", textButtonStyle);
        del3= new TextButton("X", textButtonStyle);

        del1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.files.local("player_data1.json").delete();
            }
        });
        del2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.files.local("player_data2.json").delete();
            }
        });
        del3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.files.local("player_data3.json").delete();            
            }
        });

        setInputHandler(100, 100, 0, 3, 1); // default position 

        LabelStyle labelStyle = new LabelStyle(white, Color.WHITE);
        labelStyle.background = skin.getDrawable("labelBack");

        heading = new Label(type , labelStyle);
        heading.setFontScale(3);

		// putting everything in the table

        table.add(heading);
        table.getCell(heading).spaceBottom(50);
        table.row();
        table.add(game1);
        table.add(del1).right().padLeft(20);
        table.getCell(game1).spaceBottom(10);
        table.row();
        table.add(game2);
        table.add(del2).right().padLeft(20);
        table.getCell(game2).spaceBottom(10);
        table.row();
        table.add(game3);
        table.add(del3).right().padLeft(20);
        table.getCell(game3).spaceBottom(10);
        table.row();
        table.padBottom(50);
        table.add(back);
        this.addActor(table);
		this.addActor(table);
    }


    public static int getGameData1() {
        return gameData1;
    }
    public static int getGameData2() {
        return gameData2;
    }
    public static int getGameData3() {
        return gameData3;
    }

    public static int getLastGameData() {
        return lastGame;
    }

    public Table getTable() {
        return table;
    }

    public TextButton getBackButton() {
        return back;
    }

    public TextButton getGame1() {
        return game1;
    }
        public TextButton getGame2() {
        return game2;
    }
        public TextButton getGame3() {
        return game3;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setInputHandler(float playerX, float playerY, int point, int heart, int map) {
        game1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isSave) {
                    lastGame = 1;
                    int currentMap = GameData.getCurrentMap(lastGame);
                    if (currentMap == 1)
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap1());
                    else if (currentMap == 2)
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap2());
                    else if (currentMap == 3) 
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap3());
                }
                else {
                    System.out.println("saved");
                    GameData.savePlayerPosition(playerX, playerY, point, heart, map, gameData1);
                }
            }
        });
        game2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isSave) {
                    lastGame = 2;
                    int currentMap = GameData.getCurrentMap(lastGame);
                    if (currentMap == 1)
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap1());
                    else if (currentMap == 2)
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap2());
                    else if (currentMap == 3) 
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap3());
                }
                else {
                    GameData.savePlayerPosition(playerX, playerY, point, heart, map, gameData2);
                }
            }
        });
        game3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isSave) {
                    lastGame = 3;
                    int currentMap = GameData.getCurrentMap(lastGame);
                    if (currentMap == 1)
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap1());
                    else if (currentMap == 2)
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap2());
                    else if (currentMap == 3) 
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainGameMap3());
                }
                else {
                    GameData.savePlayerPosition(playerX, playerY, point, heart, map, gameData3);
                }
            }
        });
    }


}
