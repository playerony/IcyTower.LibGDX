package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.IcyTower;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = IcyTower.SCREEN_WIDTH;
		config.height = IcyTower.SCREEN_HEIGHT;
		config.title = IcyTower.GAME_NAME;
		config.resizable = false;
		new LwjglApplication(new IcyTower(), config);
	}
}
