package com.mygdx.game;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ParkourGame extends Game {
    public final static String TITLE = "Unplugged";
    @Override
    public void create() {
        //setScreen(new FirstScreen());
        setScreen(new MainMenu());
        //setScreen(new SubgameMenu());
        //setScreen(new Play());
    }
}