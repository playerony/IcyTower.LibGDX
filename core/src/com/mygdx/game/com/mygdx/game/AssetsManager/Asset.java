package com.mygdx.game.com.mygdx.game.AssetsManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by pawel_000 on 2016-06-11.
 */
public class Asset implements Disposable {

    public final AssetManager manager = new AssetManager();

    public void load() {
        manager.load("assets/mario.png", Texture.class);
        manager.load("assets/brick.png", Texture.class);
        manager.load("assets/cloud.png", Texture.class);
        manager.load("assets/font.png", Texture.class);
        manager.load("assets/moutain.png", Texture.class);
        manager.load("assets/floor.png", Texture.class);
        manager.load("assets/logo.png", Texture.class);

        manager.load("assets/mob_1_red.png", Texture.class);
        manager.load("assets/mob_1_blue.png", Texture.class);
        manager.load("assets/mob_1_grey.png", Texture.class);

        manager.load("assets/mob_2_green.png", Texture.class);
        manager.load("assets/mob_2_red.png", Texture.class);
        manager.load("assets/mob_2_blue.png", Texture.class);

        manager.load("assets/mob_3_black.png", Texture.class);
        manager.load("assets/mob_3_blue.png", Texture.class);
        manager.load("assets/mob_3_grey.png", Texture.class);
    }

    public void dispose() {
        manager.dispose();
    }
}
