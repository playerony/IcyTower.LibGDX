package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by pawel_000 on 2016-06-18.
 */
public abstract class Entity extends Image {
    protected final float GRAVITY = -(9.81f * 2.5f);
    public AnimatedImage animation;
    protected boolean direction = true;
    protected Texture texture;

    public Entity(final Texture texture, float x, float y, final int WIDTH, final int HEIGHT) {
        super(texture);

        setTexture(texture);
        this.setPosition(x, y);
        this.setSize(WIDTH, HEIGHT);
        this.setOrigin(WIDTH / 2, HEIGHT / 2);
    }

    protected void setTexture(final Texture texture) {
        this.texture = texture;
    }
}
