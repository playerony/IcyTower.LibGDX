package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by pawel_000 on 2016-05-29.
 */
public class Floor extends Image {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;

    public Floor(float x, float y) {
        super(new Texture("assets/floor.png"));

        this.setPosition(x, y);
        this.setSize(WIDTH, HEIGHT);
    }
}
