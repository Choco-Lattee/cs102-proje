package com.mygdx.game;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Unplugged extends Game {
    @Override
    public void create() {
        //setScreen(new FirstScreen());
        setScreen(new MainGame());
    }
}