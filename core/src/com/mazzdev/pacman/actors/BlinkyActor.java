package com.mazzdev.pacman.actors;

/**
 * Created by Matteo on 24/12/2014.
 */
public class BlinkyActor extends GhostActor {

    public BlinkyActor() {
        super(16f, 19f, 1f, 1f, 0.1f, 'B');
        super.setName("Blinky");
    }
}