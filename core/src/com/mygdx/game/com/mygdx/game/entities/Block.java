package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by pawel_000 on 2016-05-25.
 */
public class Block extends Image{
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;

    public Block(float x, float y) {
        super(new Texture("assets/brick.png"));

        this.setPosition(x, y);
        this.setSize(WIDTH, HEIGHT);
    }
}
