package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class CrimeSceneInputProcessor implements InputProcessor {
    CrimeSceneScreen screen;
    InterrogationScreen iscreen;

    public CrimeSceneInputProcessor(CrimeSceneScreen screen) {
        this.screen = screen;
    }

    

    public CrimeSceneInputProcessor(InterrogationScreen screen) {
        this.iscreen = screen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screen.checkClick(screenX, Gdx.graphics.getHeight() - screenY);
        return true;
    }
    

    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override public boolean mouseMoved(int screenX, int screenY) {
        if(screen != null) {
            screen.checkMove(screenX, Gdx.graphics.getHeight() - screenY);
        }
        
        if(iscreen != null) {
            iscreen.checkMove(screenX, Gdx.graphics.getHeight() - screenY);
        }  
        return true;
     }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) 
    {
        return false;
        
    }
}
 