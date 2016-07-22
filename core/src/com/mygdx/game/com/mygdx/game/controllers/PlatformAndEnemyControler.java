package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;
import com.mygdx.game.com.mygdx.game.entities.Player;
import com.mygdx.game.com.mygdx.game.screens.MenuScreen;

/**
 * Created by pawel_000 on 2016-07-21.
 */
public class PlatformAndEnemyControler {
    private Stage stage;
    private IcyTower game;

    private PlatformsControler platformsControler;

    public PlatformAndEnemyControler(Stage stage, IcyTower game) {
        this.stage = stage;
        this.game = game;

        init();
    }

    private void init() {
        platformsControler = new PlatformsControler(stage, game);
    }

    public void update(Player player, ScoreControler scoreControler, AnimatedImage anim, MenuScreen menuScreen) {
        platformsControler.platformUpdate(player, scoreControler, anim, menuScreen);
        platformsControler.pipeUpdate(player, scoreControler);
    }

    public void clearEnemiesAndPlatforms() {
        platformsControler.clearAllPlatforms();
        platformsControler.enemyControler.clearAllEnemies();
        platformsControler.clearAllPipes();
    }
}
