package com.payne.games.piemenu;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.payne.games.piemenu.testMenu.core.TestsMenu;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 4; // for smoother rendering of arcs

		/* You can change `TestsMenu` for any of the `codeExamples` example class. */
		new LwjglApplication(new TestsMenu(), config);
	}
}
