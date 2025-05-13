package com.mygdx.game;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class Settings implements Screen {
    private Stage stage;
	private Table table;
	private Skin skin;


    @Override
    public void show() {
        stage = new Stage();

		Gdx.input.setInputProcessor(stage);

        TextureAtlas atlas = new TextureAtlas("assets/settings_tools.atlas");
        skin = new Skin(atlas);


		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setBackground(skin.getDrawable("panel"));

        BitmapFont white = new BitmapFont(Gdx.files.internal("assets/font/white.fnt"), false);
        BitmapFont black = new BitmapFont(Gdx.files.internal("assets/font/black.fnt"), false);

        CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
        checkBoxStyle.checkboxOff = skin.getDrawable("checkbox_off");
        checkBoxStyle.checkboxOn = skin.getDrawable("checkbox_on");
        checkBoxStyle.font = white;
        checkBoxStyle.fontColor = Color.WHITE;

		final CheckBox vSyncCheckBox = new CheckBox("vSync", checkBoxStyle);
		vSyncCheckBox.setChecked(vSync());
        vSyncCheckBox.setScale(10);

        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.cursor = skin.getDrawable("cursor");
        textFieldStyle.font = black;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.messageFontColor = Color.BLACK;
        textFieldStyle.messageFont = black;
        textFieldStyle.background = skin.getDrawable("button");

		final TextField levelDirectoryInput = new TextField(levelDirectory().path(), textFieldStyle); // creating a new TextField with the current level directory already written in it
		levelDirectoryInput.setMessageText("level directory"); // set the text to be shown when nothing is in the TextField

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal");
        textButtonStyle.down = skin.getDrawable("button.pressed");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = black;
		final TextButton back = new TextButton("BACK", textButtonStyle);
        back.setSize(100, 100); // not working
		back.pad(10);

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
		selectBoxStyle.fontColor = Color.BLACK;
		selectBoxStyle.backgroundDisabled = skin.getDrawable("cursor");
		selectBoxStyle.background = skin.getDrawable("labelBack");
		selectBoxStyle.scrollStyle = scrollPaneStyle;
		selectBoxStyle.listStyle = listStyle;
		
		final SelectBox selectBox = new SelectBox<String>(selectBoxStyle);
		selectBox.setItems(keys);

		ClickListener buttonHandler = new ClickListener() {
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

					stage.addAction(sequence(moveTo(0, stage.getHeight(), .5f), run(new Runnable() {

						@Override
						public void run() {
							((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
						}
					})));
				}
			}
		};

		vSyncCheckBox.addListener(buttonHandler);
		selectBox.addListener(new ChangeListener() {
    		@Override
    		public void changed(ChangeEvent event, Actor actor) {
        		String key = (String) selectBox.getSelected();
        		if (key.equals("K")) {
            		Box2DPlayer.speedingKey = Input.Keys.K;
            		System.out.println("player speeding key is: " + Box2DPlayer.speedingKey);
        		}
    		}
        });




		back.addListener(buttonHandler);
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
		String[] keyOptions = {"K", "L", "M", "N", "O"};

		table.add(setLabel).colspan(2).spaceBottom(30).center().row();
		table.add(new Label("Level:", labelStyle)).left().padRight(10);
		table.add(level).left().padBottom(10).row();
		// vSync + level directory input
		table.add(vSyncCheckBox).left().padRight(10).padBottom(10);
		table.add(levelDirectoryInput).fillX().padBottom(10).row();
		for (int i = 0; i < 5; i++) {
    		keyLabels[i] = new Label("Key " + (i + 1) + ": ", labelStyle);
    		selectBoxes[i] = new SelectBox<String>(selectBoxStyle);
    		selectBoxes[i].setItems(keyOptions);
			table.add(keyLabels[i]).left().padRight(10);  // Label
    		table.add(selectBoxes[i]).left().padBottom(10).row(); 
		}
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
		stage.addActor(table);

		stage.addAction(sequence(moveTo(0, stage.getHeight()), moveTo(0, 0, .5f))); // coming in from top animation
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		table.invalidateHierarchy();
		table.setSize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        
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

}
