package com.payne.games.piemenu;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		/* You can change `Demonstration` for any of the `individuals` example class. */
		new LwjglApplication(new Demonstration(), config);
	}
}
