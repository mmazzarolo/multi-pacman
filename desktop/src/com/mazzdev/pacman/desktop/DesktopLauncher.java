package com.mazzdev.pacman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mazzdev.pacman.PacmanMainClass;

import java.util.ArrayList;
import java.util.List;

public class DesktopLauncher{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		AndroidSubstitiution androidSubstitiution = new AndroidSubstitiution();
        List<String> pNames = new ArrayList();
        pNames.add("Matteo");
        pNames.add("Alberto");
        PacmanMainClass pacmanMainClass = new PacmanMainClass(androidSubstitiution, 2, "Pacman", pNames);
        new LwjglApplication(pacmanMainClass, config);
	}
}
