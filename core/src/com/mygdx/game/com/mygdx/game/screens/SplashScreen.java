package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.IcyTower;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class SplashScreen extends AbstractScreen{
    private Texture texture;

    public SplashScreen(final IcyTower game){
        super(game);
        init();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new GameScreen(game));
            }
        }, 1);
    }

    private void init(){
        texture = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
    }
}
