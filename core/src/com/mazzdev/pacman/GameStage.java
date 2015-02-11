package com.mazzdev.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mazzdev.pacman.actors.BaseActor;
import com.mazzdev.pacman.actors.BlinkyActor;
import com.mazzdev.pacman.actors.ClydeActor;
import com.mazzdev.pacman.actors.PacmanActor;
import com.mazzdev.pacman.enums.Direction;
import com.mazzdev.pacman.enums.Status;

/**
 * Created by Matteo on 10/12/2014.
 */
public class GameStage extends Stage {

    private final String TAG = "GameStage";
    private final int MAX_SCORE = 160;

    public ActionResolver actionResolver;
    public UIStage uiStage;

    public Status gameStatus;

    private String playerName;
    private int nPlayers;

    private TiledMap map;
    public WorldMap worldMap;

    public Group actorsGroup;
    public BaseActor user;
    public PacmanActor pacmanActor;
    public BlinkyActor blinkyActor;
    public ClydeActor clydeActor;
    private Array<Actor> actorList;

    byte[] msg = new byte[6];
    int heartBeat = 0;

    private int pacmanScore = 0;

    private boolean isFirstActing = true;

    public MovementManager movementManager;


    private AudioManager audioManager;

    public GameStage(Viewport viewport, ActionResolver actionResolver, UIStage uiStage, int nPlayers, String playerName) {

        super(viewport);
        this.actionResolver = actionResolver;
        this.uiStage = uiStage;
        this.nPlayers = nPlayers;
        this.playerName = playerName;

        worldMap = new WorldMap(nPlayers);
        map = worldMap.getMap();

        gameStatus = Status.WAITING;

        actorsGroup = new Group();
        populateActorsGroup();
        addActor(actorsGroup);
        user = actorsGroup.findActor(playerName);
        actorList = actorsGroup.getChildren();

        Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener(this)));

        movementManager = new MovementManager(this);
        audioManager = new AudioManager();

        uiStage.textUI.setText("wait!");

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        startGame();
    }


    // Called externally when all the players are ready
    public void startGame() {
        gameStatus = Status.STARTING;
        setTimers();
        audioManager.playStart();
        uiStage.textUI.setText("ready");
    }

    private void setTimers() {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                uiStage.textUI.setText(" go!");
            }
        }, 3);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameStatus = Status.PLAYING;
                uiStage.textUI.setVisible(false);
                audioManager.playSiren1();
            }
        }, 4);
    }

    private void populateActorsGroup() {

        pacmanActor = new PacmanActor();
        if (playerName.equalsIgnoreCase("Pacman"))
            pacmanActor.isThePlayer = true;
        actorsGroup.addActor(pacmanActor);

        blinkyActor = new BlinkyActor();
        if (playerName.equalsIgnoreCase("Blinky"))
            blinkyActor.isThePlayer = true;
        actorsGroup.addActor(blinkyActor);

        if (nPlayers > 2) {
            clydeActor = new ClydeActor();
            if (playerName.equalsIgnoreCase("Clyde"))
                clydeActor.isThePlayer = true;
            actorsGroup.addActor(clydeActor);
        }

    }


    @Override
    public void act() {

        super.act();

        // Sends the first message for being ready
        if (isFirstActing) {
            msg[0] = (byte) 'R';
            actionResolver.broadcastMsg(msg);
            actionResolver.setMeAsReady();
            isFirstActing = false;
        }

        if (gameStatus == Status.PLAYING) {

            // Checking if pacman collided with a pellet
            movementManager.checkCoinsCollision(pacmanActor);

            // Checking if pacman collided with any ghost
            if (movementManager.checkGhostsCollisions(pacmanActor)) {
                onPacmanCatched();
            }

            if (heartBeat == 2) {
                heartBeat = 0;
                prepareMovementMsg('M', user.getMovingDirection(), user.getX(), user.getY());
                actionResolver.broadcastMsg(msg);
            }

            heartBeat++;

        }

    }

    public void onPacmanScore() {
        pacmanScore++;
        uiStage.scoreUI.onScored(pacmanScore);
        audioManager.playEating();
        if (pacmanScore >= MAX_SCORE) {
            endGame((user == pacmanActor));
        }
    }

    public void onPacmanCatched() {
        endGame((user != pacmanActor));
    }

    public void removeActor(String name) {
        BaseActor actorToRemove = actorsGroup.findActor(name);
        actorsGroup.removeActor(actorToRemove);
        actorList = actorsGroup.getChildren();

        if (actorList.size == 1 || name.equals("Pacman")) {
            endGame(true);
        }
    }

    private void endGame(boolean didIWin) {
        gameStatus = Status.ENDING;
        String text = (didIWin) ? "you win" : "loserrr";
        uiStage.textUI.setText(text);
        uiStage.textUI.setPosition(100, 145);
        uiStage.textUI.setVisible(true);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                actionResolver.onGameEnded();
            }
        }, 3);
    }

    // Sending a message with the coordinates of the player by trasforming X and Y into bytes
    private void prepareMovementMsg(char msgId, Direction direction, float x, float y) {

        msg[0] = (byte) msgId;
        msg[1] = (byte) direction.getSymbol();

        float asdX = x * 10;
        float asdY = y * 10;

        int intX = (int) asdX;
        int intY = (int) asdY;

        msg[2] = (byte) ((intX & 0x0000FF00) >> 8);
        msg[3] = (byte) (intX & 0x000000FF);

        msg[4] = (byte) ((intY & 0x0000FF00) >> 8);
        msg[5] = (byte) (intY & 0x000000FF);
    }

    //==============================================================================================
    // Standard getters and setters
    //==============================================================================================
    public TiledMap getMap() {
        return map;
    }

    public BaseActor getUser() {
        return user;
    }

    public Array<Actor> getActorList() {
        return actorList;
    }

    public MovementManager getMovementManager() {
        return movementManager;
    }
}