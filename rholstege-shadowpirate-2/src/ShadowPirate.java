import bagel.*;

/**
 * Code for SWEN20003 Project 2, Semester 1, 2022
 * Immediately runs a ShadowPirate video game in a new window
 *
 * @RemcoHolstege
 * @version 1.0
 */

public class ShadowPirate extends AbstractGame {
    public final static int WINDOW_WIDTH = 1024;
    public final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private final Level LEVEL_0 = new Level0();
    private final Level LEVEL_1 = new Level1();

    /**
     * Constructor of ShadowPirate game.
     * Sends the width (1024 pixels), height (768 pixels), and game title ("ShadowPirate") to the AbstractGame
     * constructor to create the game window.
     */
    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        if (!LEVEL_0.isCompleted()) {
            LEVEL_0.update(input);
        } else if (!LEVEL_1.isCompleted()) {
            LEVEL_1.update(input);
        }

    }

}
