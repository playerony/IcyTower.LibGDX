package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.com.mygdx.game.AssetsManager.Asset;
import com.mygdx.game.com.mygdx.game.screens.SplashScreen;
import com.mygdx.game.com.mygdx.game.service.SoundService;

public class IcyTower extends Game {

	public static final String GAME_NAME = "Icy Mario";

	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 700;

	public SoundService soundService;
	public Asset assets;

	private boolean pause;

	public void create () {
		initAssets();
	}

	private void initAssets() {
		assets = new Asset();
		assets.loadTextures();
		assets.manager.finishLoading();

		assets.loadSounds();
		assets.manager.finishLoading();

		if (assets.manager.update()) {
			init();
			this.setScreen(new SplashScreen(this));
		} else
			return;
	}

	private void init() {
		soundService = new SoundService(assets);
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public SoundService getSoundService() {
		return soundService;
	}
}
