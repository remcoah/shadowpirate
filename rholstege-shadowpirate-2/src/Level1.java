import bagel.*;
import java.util.ArrayList;

/**
 * Level 0 class of Shadow Pirate game.
 *
 * @author Remco Holstege
 */
public class Level1 extends Level {
    private final static String LEVEL_ONE = "res/level1.csv";
    private final static String LVL_ONE_INSTRUCTION_MSG = "FIND THE TREASURE";
    private Treasure treasure;

    /**
     * Creates a level 1 in Shadow Pirate game.
     * Sets appropriate instructions and level parameters.
     * Adds components based on csv input file.
     */
    public Level1() {
        instructionMsg = LVL_ONE_INSTRUCTION_MSG;
        this.setCompleted(false);
        this.setStarted(false);
        this.setBackground(new Image("res/background1.png"));
        this.components = this.readCSV(LEVEL_ONE, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Entity> readCSV(String fileName, boolean levelZero) {
        ArrayList<Entity> out = super.readCSV(LEVEL_ONE, false);
        for (Entity e : out) {
            if (e instanceof Treasure) {
                this.treasure = (Treasure) e;
            }
        }
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawWinScreen() {
        FONT.drawString(WIN_MESSAGE, (Window.getWidth()/2.0-FONT.getWidth(WIN_MESSAGE)/2.0), INSTRUCTION_OFFSET);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasWon() {
        if (sailor.getBoundingBox() != null) {
            return sailor.getBoundingBox().intersects(treasure.getBoundingBox());
        }
        return false;
    }

}
