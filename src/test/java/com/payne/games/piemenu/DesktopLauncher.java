package com.payne.games.piemenu;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.payne.games.piemenu.core.BaseGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 4; // for smoother rendering of arcs

		/* You can change `Demonstration` for any of the `individuals` example class. */
		new LwjglApplication(new BaseGame(), config);
	}
}
