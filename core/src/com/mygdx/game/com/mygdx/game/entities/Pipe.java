package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by pawel_000 on 2016-07-20.
 */
public class Pipe extends Entity {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    private Rectangle box;
    private Rectangle bottom;

    private boolean points = true;

    public Pipe(final Texture texture, float x, float y) {
        super(texture, x, y, WIDTH, HEIGHT);

        init();
    }

    private void init() {
        box = new Rectangle(getX(), getY(), WIDTH, HEIGHT);
        bottom = new Rectangle(getX() + 10, getY() - getHeight() - 1, getWidth() - 20, 5);
    }

    public boolean isPoints() {
        return points;
    }

    public void setPoints(boolean points) {
        this.points = points;
    }

    public Rectangle getBounds() {
        return box;
    }

    public Rectangle getBottomBound() {
        return bottom;
    }
}
