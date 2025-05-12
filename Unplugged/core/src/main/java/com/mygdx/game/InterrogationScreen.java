package io.github.some_example_name;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class InterrogationScreen implements Screen {
    final CrimeSceneGame game;
    Texture background;
    ArrayList<Suspect> suspects;
    ArrayList<BlameButton> blameButtons;
    Suspect correctSuspect;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    Rectangle rect = new Rectangle();
    Vector2 mousePos = new Vector2();

    BitmapFont fontWhite;

    public static boolean isThisPlayedBefore = false;
    public static int addFinalPoint = 900;
    public int howManyWrongGuess = 0;
    public boolean showPoints = false;

    ArrayList<Evidence> evidences;
     ArrayList<Evidence> foundEvidences;

    public InterrogationScreen(final CrimeSceneGame game, ArrayList<Evidence> evidences, ArrayList<Evidence> foundEvidences) {
        this.game = game;
        background = new Texture("arkaplan_interrogation_screen.png");
        suspects = new ArrayList<>();
        blameButtons = new ArrayList<>();

        if(!isThisPlayedBefore)
        {
            suspects.add(new Suspect(null, null, null, false));
            suspects.add(new Suspect(null, null, null, true));
            suspects.add(new Suspect(null, null, null, false));
        }
        else
        {
            suspects.add(new Suspect(null, null, null, true));
            suspects.add(new Suspect(null, null, null, false));
            suspects.add(new Suspect(null, null, null, false));
        }
       

        blameButtons.add(new BlameButton(suspects.get(0),new Vector2(390,200)));
        blameButtons.add(new BlameButton(suspects.get(1),new Vector2(820,200)));
        blameButtons.add(new BlameButton(suspects.get(2),new Vector2(1300,200)));
        this.evidences = evidences;
        this.foundEvidences = foundEvidences;
        Gdx.input.setInputProcessor(new CrimeSceneInputProcessor(this));
        fontWhite = new BitmapFont(Gdx.files.internal("assets/font/black.fnt"));
        fontWhite.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        fontWhite.getData().setScale(2,2);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();

        mousePos.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        

        game.batch.begin();
        game.batch.draw(background, 0, 0, 1920,980);
        for(int i = 0; i < howManyWrongGuess; i++)
        {
            fontWhite.draw(game.batch, "Wrong guess -300 points!", 650, 600);
            if(i == 1)
            {
                fontWhite.draw(game.batch, "Wrong guess -300 points!", 650, 500);
            }
        }
        if(showPoints)
        {
            fontWhite.draw(game.batch, "Found evidence X" + CrimeSceneScreen.foundEvidenceCount + ": " + CrimeSceneScreen.foundEvidenceCount * 100, 650, 600);
            fontWhite.draw(game.batch, "Guess points: " + addFinalPoint, 650, 500);
            fontWhite.draw(game.batch, "Total points: " + CrimeSceneScreen.point, 0, 400);
        }

        for (BlameButton button : blameButtons) {
            button.draw(game.batch);
        }

        for (int i = 0; i < foundEvidences.size(); i++) {           
            game.batch.draw(foundEvidences.get(i).texture, i * 300, 50,100,100);
        }

        
        game.batch.end();

        for (int i = 0; i < foundEvidences.size(); i++) {
            rect.set(i * 300, 50,100,100);
            if(rect.contains(mousePos.x, mousePos.y)) {
                shapeRenderer.begin(ShapeType.Line);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(i * 300, 50,100,100);
                shapeRenderer.end();

                game.batch.begin();
                game.font.draw(game.batch, foundEvidences.get(i).text,i * 300, 50);
                game.batch.end();

            }
        }

        if(Gdx.input.isButtonJustPressed(0)) {
            for (BlameButton button : blameButtons) {
                if(button.isClicked(x, y)) {
                    if(button.suspect.isCulprit)
                    {

                        CrimeSceneScreen.point += addFinalPoint;
                        showPoints = true;
                        // win
                        // delay eklenecek
                        if(!isThisPlayedBefore)
                        {
                            
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new CrimeSceneScreen(game));
                        }
                        else
                        {
                            game.setScreen(new EndGameScreen(game,evidences,foundEvidences));
                            //TODO Talha main oyuna geçiş burdan olcak
                            // CrimeSceneScreen.point toplam puan onu çekmeyi unutma (static variable)
                        }
                            
                    
                    }
                    else 
                    {
                        addFinalPoint -= 300;
                        howManyWrongGuess++;

                        if(addFinalPoint < 0)
                        {
                            addFinalPoint = 0;
                            howManyWrongGuess = 2;
                        }
                        
                    }
                }
            }
        }

    }

    public void checkMove(int x, int y) 
    {
        mousePos.set(x, y);
    }

    @Override public void dispose() { background.dispose(); }
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
