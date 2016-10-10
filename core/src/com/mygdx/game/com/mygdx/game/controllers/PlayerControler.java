package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;
import com.mygdx.game.com.mygdx.game.entities.Player;

/**
 * Created by pawel_000 on 2016-07-27.
 */
public class PlayerControler {
    private Player player;
    private IcyTower game;
    private AnimatedImage anim;
    private Camera camera;

    public PlayerControler(IcyTower game, AnimatedImage anim, Camera camera) {
        this.game = game;
        this.anim = anim;
        this.camera = camera;

        init();
    }

    private void init() {
        player = new Player(game.assets.manager.get("assets/mario.png", Texture.class), game);

        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + 250, 0);

        anim = new AnimatedImage(player.getAnimation());
        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());
        anim.setOrigin(player.getWidth() / 2, player.getHeight() / 2);
    }

    public void playerUpdate(ScoreControler scoreControler) {
        player.update();
        playerRotation();

        if (player.getJumpVelocity() < -1200 && !player.getDie()) {
            player.setDie(true);
            player.setJump(false);
            player.setJumpVelocity(800);

            game.getSoundService().pauseTitleSound();
            game.getSoundService().playDeathSound();
        }

        if (player.getJumpVelocity() < 0) {
            scoreControler.setAddPoints(true);
        }
    }

    private void playerRotation() {
        if (player.getRotate()) {
            if (player.getAngle() >= 360) {
                player.setAngle(0.0f);
                player.setRotate(false);
                player.setFlip(false);
            }

            anim.setRotation(player.getAngle());
            player.setAngle(player.getAngle() + 10.25f);
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public AnimatedImage getAnim() {
        return this.anim;
    }
}
