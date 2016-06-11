package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/**
 * Created by pawel_000 on 2016-05-25.
 */
public class Platform extends Image {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;

    private static int SIZE = 0;
    private static float posX = 0;
    private static float posY = 0;

    private boolean points = true;

    private Rectangle bounds;

    private Array<Block> blocks;

    public Platform(float x, float y, int size) {
        SIZE = size;

        this.setWidth(SIZE * WIDTH);
        this.setHeight(HEIGHT);
        this.setPosition(x, y);

        bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());

        init();
    }

    private void init(){
        blocks = new Array<Block>();

        for(int i=0 ; i<SIZE ; i++){
            Block b = new Block( posX + ( i * WIDTH), posY );

            blocks.add(b);
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void onStage(Stage stage){
        for(Block b : blocks)
            stage.addActor(b);
    }

    public int getSIZE(){

        return SIZE;
    }

    public void setSIZE(int size) {
        this.setWidth(SIZE * WIDTH);
        SIZE = size;
    }

    public void setPos(Stage stage, float x, float y){
        int i=0;

        for(Block b : blocks) {
            b.setPosition(x + WIDTH * i, y);
            i++;
        }

        bounds.setPosition(x, y);
        setPosition(x, y);
    }

    public void setPosition(float x, float y){
        posX = x;
        posY = y;

        this.setX(x);
        this.setY(y);
    }

    public boolean isPoints() {
        return points;
    }

    public void setPoints(boolean points) {
        this.points = points;
    }
}
