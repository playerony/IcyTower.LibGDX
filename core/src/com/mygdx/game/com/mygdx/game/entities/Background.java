package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by pawel_000 on 2016-05-29.
 */
public class Background extends Image {
    private static final int WIDTH = 80 * 3;
    private static final int HEIGHT = 35 * 3;

    public Background(float x, float y) {
        super(new Texture("assets/moutain.png"));

        this.setPosition(x, y);
        this.setSize(WIDTH, HEIGHT);
    }

    public void update(float x, float y){
        this.setPosition(x, y);
    }
}
