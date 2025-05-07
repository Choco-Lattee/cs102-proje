package io.github.some_example_name;

import java.util.ArrayList;

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
import com.badlogic.gdx.utils.TimeUtils;



public class CrimeSceneScreen implements Screen {
    final CrimeSceneGame game;
    Texture background;
    ArrayList<Evidence> evidences;
    ArrayList<Evidence> foundEvidences;

    long startTime;
    int totalTime = 90;

    public static boolean isThisPlayedBefore = false;
    public static int point = 0;
    public static int foundEvidenceCount = 0;
    

    String pos = "";
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    Rectangle rect = new Rectangle();

    Vector2 mousePos = new Vector2();

    BitmapFont fontWhite;

    public CrimeSceneScreen(final CrimeSceneGame game) {
        this.game = game;
        if(!isThisPlayedBefore)
        {
            background = new Texture("temizlikci.png");
            evidences = new ArrayList<>();
            foundEvidences = new ArrayList<>();
            startTime = TimeUtils.millis();
            Gdx.input.setInputProcessor(new CrimeSceneInputProcessor(this));
            setForLevel1();
        }
        else
        {
            background = new Texture("ascı.png");
            evidences = new ArrayList<>();
            foundEvidences = new ArrayList<>();
            startTime = TimeUtils.millis();
            Gdx.input.setInputProcessor(new CrimeSceneInputProcessor(this));
            setForLevel2();
        }
        


        fontWhite = new BitmapFont(Gdx.files.internal("assets/font/black.fnt"));
        fontWhite.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        fontWhite.getData().setScale(3,3);
        rect.setSize(100, 100);
        
           // fontWhite = new BitmapFont(Gdx.files.internal("assets/minecraft.fnt"));

          
    }

    private void setForLevel1() {
        evidences.add(new Evidence("all evidance at temizlikci scene/pixel_evidence_object_1.png", new Vector2(430, 480) , "a bloody knife found \n" + " in crime scene"));
        evidences.add(new Evidence("all evidance at temizlikci scene/pixel_evidence_object_2.png", new Vector2(175, 240) , "Why is this rope here?"));
        evidences.add(new Evidence("all evidance at temizlikci scene/pixel_evidence_object_3.png", new Vector2(1210, 450) , "a trash found in \n crime scene Didn't anyone \n clean this room?"));
        evidences.add(new Evidence("all evidance at temizlikci scene/pixel_evidence_object_4.png", new Vector2(340, 620) , "The victim's bed was not \n made, how suspicious?"));
        evidences.add(new Evidence("all evidance at temizlikci scene/pixel_evidence_object_5.png", new Vector2(48, 484) , "what a beautifully \n made coffee"));
        evidences.add(new Evidence("all evidance at temizlikci scene/pixel_evidence_object_6.png", new Vector2(410, 335) , "a trash pile found in \n" +" crime scene Didn't anyone \n" +  " clean this room?"));
    }

    private void setForLevel2()
    {
        evidences.add(new Evidence("2nd evidence palet all evidences/evidence_object_1.png", new Vector2(395, 232), "Was a spilled coffee \n cup really that hot?"));
        evidences.add(new Evidence("2nd evidence palet all evidences/evidence_object_2.png", new Vector2(550, 486), "A truly wonderful \n slice of cake"));
        evidences.add(new Evidence("2nd evidence palet all evidences/evidence_object_3.png", new Vector2(1150, 460), "A cleaned garbage \n bag in front of the victim's \n door"));
        evidences.add(new Evidence("2nd evidence palet all evidences/evidence_object_4.png", new Vector2(841, 438), "A clean room"));
        evidences.add(new Evidence("2nd evidence palet all evidences/evidence_object_5.png", new Vector2(410, 570), "A very nicely made bed"));
        evidences.add(new Evidence("2nd evidence palet all evidences/evidence_object_6.png", new Vector2(681, 104), "Why are there napkins \n on the floor?"));
    
    }

    int xdraw = 1600;
    int ydraw = 50;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.BROWN.r,Color.BROWN.g,Color.BROWN.b,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if((totalTime - (TimeUtils.timeSinceMillis(startTime) / 1000)) <= 0 || evidences.size() < 1) 
        {
            isThisPlayedBefore = true;
            point += foundEvidences.size() * 100;
            foundEvidenceCount += foundEvidences.size();
            // go to integorration
            game.setScreen(new InterrogationScreen(game,evidences,foundEvidences));
            Gdx.input.setInputProcessor(null);

        }


        // shape render
        for (int i = 0; i < foundEvidences.size(); i++) {
            rect.setPosition(xdraw + (i % 2) * 100, ydraw + (i / 2) * 200);
            if(rect.contains(mousePos.x, mousePos.y)) {
                shapeRenderer.begin(ShapeType.Line);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(xdraw + (i % 2) * 100, ydraw + (i / 2) * 200,100,100);
                shapeRenderer.end();
                game.batch.begin();
                game.font.draw(game.batch, foundEvidences.get(i).text, xdraw + (i % 2) * 100, ydraw + (i / 2) * 200);
                game.batch.end();

                //fontWhite.draw(batch, pos, delta, delta)
            }
        }

        // batch render
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        fontWhite.draw(game.batch, "Time: " + (totalTime - (TimeUtils.timeSinceMillis(startTime) / 1000)), 1000, 1000);

       
        for (int i = 0; i < foundEvidences.size(); i++) {           
            game.batch.draw(foundEvidences.get(i).texture, xdraw + (i % 2) * 100, ydraw + (i / 2) * 200,100,100);
        }


        game.batch.end();
    }

    public void checkClick(int x, int y) 
    {
        pos = x + "-" + y;
        for (int i = 0; i < evidences.size(); i++) {
            if(evidences.get(i).isNear(x, y)) {
                foundEvidences.add(evidences.get(i));
                evidences.remove(i);
                i--;
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
