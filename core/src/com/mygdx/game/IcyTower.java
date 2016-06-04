package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.com.mygdx.game.screens.SplashScreen;

public class IcyTower extends Game {

	public static final String GAME_NAME = "Icy Mario";

	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 700;

	private boolean pause;

	public void create () {
		this.setScreen(new SplashScreen(this));
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
}
