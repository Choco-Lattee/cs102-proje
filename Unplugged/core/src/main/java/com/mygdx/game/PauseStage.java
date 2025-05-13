package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseStage extends Stage {
    private Table table;
    private TextureAtlas atlas;
    private Skin skin;
    private TextButton buttonContinue, buttonExit, buttonSettings, buttonSave;
    private BitmapFont white, black; 
    private Label heading;
    private Texture menuTexture;
    Image menuImage;
    float imageToStageWitdth, imageToStageHeight, width, height;

    public PauseStage(Viewport viewport, float width, float height) {
        super(viewport);
        atlas = new TextureAtlas("Unplugged/assets/menuTools.atlas");
        skin = new Skin(atlas);
        this.width = width;
        this.height = height;

        table = new Table(skin);
        table.setBounds(0, 0, width, height);
        table.setBackground(skin.getDrawable("menu"));

        //menuImage = new Image(menuTexture);
        //menuImage.setSize(menuTexture.getWidth() * 5, menuTexture.getHeight() * 5);

        //imageToStageWitdth = menuImage.getWidth() / GAME_WORLD_WIDTH;
        //imageToStageHeight = menuImage.getHeight() / GAME_WORLD_HEIGHT;

        white = new BitmapFont(Gdx.files.internal("Unplugged/assets/font/white.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("Unplugged/assets/font/black.fnt"), false);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal");
        textButtonStyle.down = skin.getDrawable("button.pressed");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;

        buttonSettings = new TextButton("SETTINGS", textButtonStyle);
        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonContinue = new TextButton("CONTINUE", textButtonStyle);
        buttonSave = new TextButton("SAVE", textButtonStyle);
        buttonContinue.pad(20);
        buttonSave.pad(20);
        buttonSave.setSize(buttonContinue.getWidth(), buttonContinue.getHeight());
        buttonSettings.pad(20);
        buttonExit.pad(20);

        LabelStyle headingStyle = new LabelStyle(white, Color.WHITE);
        
        heading = new Label("PAUSE", headingStyle);
        heading.setFontScale(3);

        //menuImage.setPosition(width / 2 - menuImage.getWidth() / 2, height / 2 - menuImage.getHeight() / 2);
        //this.addActor(menuImage);

        table.add(heading);
        table.getCell(heading).spaceBottom(50);
        table.row();
        table.add(buttonContinue);
        table.getCell(buttonContinue).spaceBottom(10);
        table.row();
        table.add(buttonSave);
        table.getCell(buttonSave).spaceBottom(10);
        table.row();
        table.add(buttonSettings);
        table.getCell(buttonSettings).spaceBottom(10);
        table.row();
        table.padBottom(50);
        table.add(buttonExit);
        this.addActor(table);
    }

    public TextButton getContinueButton() {
        return buttonContinue;
    }
    public TextButton getExitButton() {
        return buttonExit;
    }
    public TextButton getSettingsButton() {
        return buttonSettings;
    }
        public TextButton getSaveButton() {
        return buttonSave;
    }

    public Table getTable() {
        return table;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    
}
