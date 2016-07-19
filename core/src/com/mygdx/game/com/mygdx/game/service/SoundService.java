package com.mygdx.game.com.mygdx.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.com.mygdx.game.AssetsManager.Asset;

/**
 * Created by pawel_000 on 2016-07-16.
 */
public class SoundService {

    public static final float DIE_SOUND_LENGTH = 2.5f;

    private Sound titleSound;
    private Sound jumpSound;
    private Sound superJumpSound;
    private Sound stompSound;
    private Sound dieSound;

    private Asset assets;

    public SoundService(final Asset assets) {
        this.assets = assets;

        init();
    }

    private void init() {
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("assets/jump.wav"));
        titleSound = Gdx.audio.newSound(Gdx.files.internal("assets/backgroundMusic.mp3"));
        dieSound = Gdx.audio.newSound(Gdx.files.internal("assets/death.wav"));
        superJumpSound = Gdx.audio.newSound(Gdx.files.internal("assets/superJump.wav"));
        stompSound = Gdx.audio.newSound(Gdx.files.internal("assets/stomp.wav"));
    }

    public void playTitleSound(boolean looped) {
        titleSound.setVolume(titleSound.play(), 0.4f);
        titleSound.setLooping(1, looped);
    }

    public void pauseTitleSound() {
        titleSound.pause();
    }

    public void continueTitleSound() {
        titleSound.stop();
        dieSound.stop();
        titleSound.setVolume(titleSound.play(), 0.4f);
    }

    public void playJumpSound() {
        jumpSound.setVolume(jumpSound.play(), 0.05f);
    }

    public void playSuperJumpSound() {
        superJumpSound.setVolume(superJumpSound.play(), 0.5f);
    }

    public void playStompSound() {
        stompSound.setVolume(stompSound.play(), 0.5f);
    }

    public void playDeathSound() {
        dieSound.setVolume(dieSound.play(), 1);
    }
}
