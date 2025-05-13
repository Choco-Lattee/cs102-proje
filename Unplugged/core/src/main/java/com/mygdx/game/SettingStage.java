package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingStage extends Stage{
	private Table table;
	private Skin skin;
	private final CheckBox vSyncCheckBox;
	private final TextField levelDirectoryInput;
	private final TextButton back;
	private SelectBox selectBox;
	private float width, height;
	ClickListener buttonHandler;

    public SettingStage(Viewport viewport, float width, float height) {
		super(viewport);


        TextureAtlas atlas = new TextureAtlas("Unplugged/assets/settings_tools.atlas");
        skin = new Skin(atlas);


		table = new Table(skin);
		table.setBounds(0, 0, width, height);
        table.setBackground(skin.getDrawable("panel"));

        BitmapFont white = new BitmapFont(Gdx.files.internal("Unplugged/assets/font/white.fnt"), false);
        BitmapFont black = new BitmapFont(Gdx.files.internal("Unplugged/assets/font/black.fnt"), false);

        CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
        checkBoxStyle.checkboxOff = skin.getDrawable("checkbox_off");
        checkBoxStyle.checkboxOn = skin.getDrawable("checkbox_on");
        checkBoxStyle.font = white;
        checkBoxStyle.fontColor = Color.WHITE;

		ListStyle listStyle = new ListStyle();
        listStyle.font = white;
        listStyle.fontColorUnselected = Color.WHITE;
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.selection = skin.getDrawable("button.pressed");
        List<String> list = new List<String>(listStyle);
        list.setItems(new String[] { "Z", "K", "T", "H"
        , "F", "Y", "E", "R", "Q","M", "C"});
		Array<String> keys = new Array<String>(new String[] { "Z", "K", "T", "H"
        , "F", "Y", "E", "R", "Q","M", "C"});
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        scrollPaneStyle.hScrollKnob = skin.getDrawable("button.normal");
        scrollPaneStyle.vScrollKnob = skin.getDrawable("button.normal");

		SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.font = black;
		selectBoxStyle.backgroundDisabled = skin.getDrawable("cursor");
		selectBoxStyle.background = skin.getDrawable("labelBack");
		selectBoxStyle.scrollStyle = scrollPaneStyle;
		selectBoxStyle.listStyle = listStyle;
	

		vSyncCheckBox = new CheckBox("vSync", checkBoxStyle);
		vSyncCheckBox.setChecked(vSync());
        vSyncCheckBox.setScale(10);

        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.cursor = skin.getDrawable("cursor");
        textFieldStyle.font = black;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.messageFontColor = Color.BLACK;
        textFieldStyle.messageFont = black;
        textFieldStyle.background = skin.getDrawable("button");

		levelDirectoryInput = new TextField(levelDirectory().path(), textFieldStyle); // creating a new TextField with the current level directory already written in it
		levelDirectoryInput.setMessageText("level directory"); // set the text to be shown when nothing is in the TextField

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal");
        textButtonStyle.down = skin.getDrawable("button.pressed");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = black;
		back = new TextButton("BACK", textButtonStyle);
        back.setSize(100, 100); // not working
		back.pad(10);

		buttonHandler = new ClickListener() {
            @Override
			public void clicked(InputEvent event, float x, float y) {
				// event.getListenerActor() returns the source of the event, e.g. a button that was clicked
				if(event.getListenerActor() == vSyncCheckBox) {
					// save vSync
					Gdx.app.getPreferences(ParkourGame.TITLE).putBoolean("vsync", vSyncCheckBox.isChecked());

					// set vSync
					Gdx.graphics.setVSync(vSync());

					Gdx.app.log(ParkourGame.TITLE, "vSync " + (vSync() ? "enabled" : "disabled"));
				} else if(event.getListenerActor() == back) {
					// save level directory
					String actualLevelDirectory = levelDirectoryInput.getText().trim().equals("") ? Gdx.files.getExternalStoragePath() + ParkourGame.TITLE + "/levels" : levelDirectoryInput.getText().trim(); // shortened form of an if-statement: [boolean] ? [if true] : [else] // String#trim() removes spaces on both sides of the string
					Gdx.app.getPreferences(ParkourGame.TITLE).putString("leveldirectory", actualLevelDirectory);

					// save the settings to preferences file (Preferences#flush() writes the preferences in memory to the file)
					Gdx.app.getPreferences(ParkourGame.TITLE).flush();

					Gdx.app.log(ParkourGame.TITLE, "settings saved");

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			}
		};

		vSyncCheckBox.addListener(buttonHandler);

		//back.addListener(buttonHandler);
        LabelStyle labelStyle = new LabelStyle(white, Color.WHITE);
        labelStyle.background = skin.getDrawable("labelBack");
        Label setLabel = new Label("SETTINGS", labelStyle);
        setLabel.setFontScale(3, 3);
        Label level = new Label("level directory", labelStyle);
        level.setFontScale(2);
		Label[] keyLabels = new Label[5];
		@SuppressWarnings("unchecked")
		SelectBox<String>[] selectBoxes = (SelectBox<String>[]) new SelectBox[5];

		// Example key options
		String[] keyOptions = {"K", "W", "A", "S", "D", "SHIFT"};

		table.add(setLabel).colspan(2).spaceBottom(30).center().row();
		table.add(new Label("Level:", labelStyle)).left().padRight(10);
		table.add(level).left().padBottom(10).row();
		// vSync + level directory input
		table.add(vSyncCheckBox).left().padRight(10).padBottom(10);
		table.add(levelDirectoryInput).fillX().padBottom(10).row();
		String speeding = "SPEED";
		String up = "UP";
		String down = "DOWN";
		String left = "LEFT";
		String right = "RIGHT";
		for (int i = 0; i < 5; i++) {
			String label;
			if (i == 0) {
				label = speeding;
			}
		    if (i == 1) {
				label = up;
			}
			if (i == 2) {
				label = left;
			}
			if (i == 3) {
				label = down;
			}
			else {
				label = right;
			}
    		keyLabels[i] = new Label(label + ": ", labelStyle);
    		selectBoxes[i] = new SelectBox<String>(selectBoxStyle);
    		selectBoxes[i].setItems(keyOptions);
			table.add(keyLabels[i]).left().padRight(10);  // Label
    		table.add(selectBoxes[i]).left().padBottom(10).row(); 
		}
		selectBoxes[0].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
        		String key = (String) selectBoxes[4].getSelected();
        		if (key.equals("K")) {
            		Box2DPlayer.speedingKey = Input.Keys.K;
            		System.out.println("player speeding key is: " + Box2DPlayer.speedingKey);
        		}
			}
		});
		selectBoxes[1].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
        		String key = (String) selectBoxes[4].getSelected();
        		if (key.equals("K")) {
            		Box2DPlayer.speedingKey = Input.Keys.K;
            		System.out.println("player speeding key is: " + Box2DPlayer.speedingKey);
        		}
			}
		});
		selectBoxes[2].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
        		String key = (String) selectBoxes[4].getSelected();
        		if (key.equals("K")) {
            		Box2DPlayer.speedingKey = Input.Keys.K;
            		System.out.println("player speeding key is: " + Box2DPlayer.speedingKey);
        		}
			}
		});
		selectBoxes[3].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
        		String key = (String) selectBoxes[4].getSelected();
        		if (key.equals("K")) {
            		Box2DPlayer.speedingKey = Input.Keys.K;
            		System.out.println("player speeding key is: " + Box2DPlayer.speedingKey);
        		}
			}
		});
		selectBoxes[4].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
        		String key = (String) selectBoxes[4].getSelected();
        		if (key.equals("K")) {
            		Box2DPlayer.speedingKey = Input.Keys.K;
            		System.out.println("player speeding key is: " + Box2DPlayer.speedingKey);
        		}
			}
		});

		// putting everything in the table
		//table.add(setLabel).spaceBottom(50).colspan(3).expandX().row();
		//table.add();
		//table.add(level);
		//table.add().row();
		//table.add(vSyncCheckBox).top().expandY();
		//table.add(levelDirectoryInput).top().fillX();
		//table.add(back).bottom().right();


		// Back button aligned to bottom right
		table.add(); // empty cell for spacing
		table.add(back).right().bottom();

		table.setFillParent(true);
		this.addActor(table);

		//stage.addAction(sequence(moveTo(0, stage.getHeight()), moveTo(0, 0, .5f))); // coming in from top animation
    }

     public static FileHandle levelDirectory() {
		String prefsDir = Gdx.app.getPreferences(ParkourGame.TITLE).getString("leveldirectory").trim();
		if(prefsDir != null && !prefsDir.equals(""))
			return Gdx.files.absolute(prefsDir);
		else
			return Gdx.files.absolute(Gdx.files.external(ParkourGame.TITLE + "/levels").path()); // return default level directory
	}

	/** @return if vSync is enabled */
	public static boolean vSync() {
		return Gdx.app.getPreferences(ParkourGame.TITLE).getBoolean("vsync");
	}

	public TextButton getBackButton() {
		return back;
	}

	public ClickListener getHandler() {
		return buttonHandler;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Table getTable() {
		return table;
	}

}
