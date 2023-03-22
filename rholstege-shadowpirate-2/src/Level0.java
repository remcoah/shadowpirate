import bagel.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Level 0 class of Shadow Pirate game.
 *
 * @author Remco Holstege
 */
public class Level0 extends Level {
    private final static String LEVEL_ZERO = "res/level0.csv";
    private final static String LVL_ZERO_INSTRUCTION_MSG = "USE ARROW KEYS TO FIND LADDER";

    // the delay between winning the level and starting next level
    private final static int LEVEL_DELAY = 3000;

    // the end point of the level
    private final static double END_X = 990;
    private final static double END_Y = 630;

    private boolean skipLevel = false;

    /**
     * Creates a level 0 in Shadow Pirate game.
     * Sets appropriate instructions and level parameters.
     * Adds components based on csv input file.
     */
    public Level0() {
        instructionMsg = LVL_ZERO_INSTRUCTION_MSG;
        this.components = this.readCSV(LEVEL_ZERO, true);
        this.setCompleted(false);
        this.setStarted(false);
        this.setBackground(new Image("res/background0.png"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Input input) {
        super.update(input);
        if (input.wasPressed(Keys.W)) {
            skipLevel = true;
        }
    }

    /**
     * Draws winning screen as described in project spec.
     * Terminates the level after 3 seconds.
     */
    @Override
    public void drawWinScreen() {
        FONT.drawString(NEXT_LVL_MSG, (Window.getWidth()/2.0-FONT.getWidth(NEXT_LVL_MSG)/2.0), INSTRUCTION_OFFSET);
        Timer t = new Timer();
        TimerTask endLevel = new TimerTask() {
            @Override
            public void run() {
                setCompleted(true);
            }
        };
        t.schedule(endLevel,LEVEL_DELAY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasWon() {
        return (((this.sailor.getCoordinate().x >= END_X) && (this.sailor.getCoordinate().y >= END_Y)) || skipLevel);
    }
}
