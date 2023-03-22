import bagel.*;
import bagel.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * An abstract class for a Level in ShadowPirate.
 *
 *
 * @author Remco Holstege
 */
public abstract class Level {

    // constants used to print out levels according to specifications
    protected final static int INSTRUCTION_OFFSET = 402;
    protected final static int LINE = 60;
    protected final static String START_MESSAGE = "PRESS SPACE TO START";
    protected final static String CONTROL_MESSAGE = "PRESS S TO ATTACK";
    protected final static String WIN_MESSAGE = "CONGRATULATIONS!";
    protected final static String NEXT_LVL_MSG = "LEVEL COMPLETE!";
    protected final int FONT_SIZE = 55;
    protected final Font FONT  = new Font("res/wheaton.otf", FONT_SIZE);
    private final static String END_MESSAGE = "GAME OVER";

    // the level specific instruction message
    protected String instructionMsg;

    // entities in the level
    protected ArrayList<Entity> components;
    protected Sailor sailor;

    // describes the states of the levels
    private boolean started;
    private boolean completed;

    // describes the bounds of the level
    private Point topLeft;
    private Point bottomRight;
    private Rectangle frame;

    private Image background;

    /**
     * Draws the winning screen of the level according to specs
     */
    public abstract void drawWinScreen();

    /**
     * Determines if the player has finished the level
     * @return winning state
     */
    public abstract boolean hasWon();

    /**
     * Performs a state update for the level.
     * Draws the appropriate screen given the stage of the level.
     * If level is in progress, draws the level and updates all components.
     *
     * @param input the bagel keyboard input
     */
    public void update(Input input) {
        if (!this.isStarted()) {
            this.drawStartScreen();
            if (input.wasPressed(Keys.SPACE)) {
                this.setStarted(true);
            }
        } else if (this.hasWon()) {
            this.drawWinScreen();
        } else if (sailor.getHealth() > 0) {
            this.drawLevel();
            sailor.update(input, components, getFrame());
            updateComponents();
        } else {
            this.displayGameOver();
        }
    }

    /**
     * Prints the first sentence of damage log as required by the specification.
     *
     * @param attacker Bomb or Character that is inflicting damage
     * @param damage the amount of damage inflicted
     * @param target the character that has had damage done to it
     */
    public void printDamage(Entity attacker, int damage, Character target) {
        System.out.print(attacker.getName()+" inflicts "+damage+" damage points on "+target.getName()+". ");
    }

    /**
     * Performs a state update on all components in the level.
     */
    public void updateComponents() {
        // arraylists of all entities to remove and add after iteration
        ArrayList<Entity> removal = new ArrayList<>();
        ArrayList<Entity> add = new ArrayList<>();

        // go over all components
        for (Entity e: components) {
            if (e instanceof Enemy) {
                Enemy enemy = (Enemy) e;

                // remove enemy if necessary, otherwise update
                if (enemy.getHealth() <= 0) {
                    removal.add(enemy);
                } else {
                    enemy.update(components, getFrame());
                }

                // determine if sailor is attacking Enemy
                if (sailor.getBoundingBox().intersects(e.getBoundingBox()) && !sailor.isIdle() &&
                        !enemy.isInvincible()) {
                    printDamage(sailor, sailor.getDamagePoints(), enemy);
                    enemy.damage(sailor.getDamagePoints());
                }

                // determine if sailor is in enemy's target range
                if (sailor.getBoundingBox().intersects(enemy.getAttackRange()) && enemy.canAttack()) {
                    // make enemy shoot projectile
                    add.add(enemy.shoot(sailor.getBoundingBox().centre()));
                }

            } else if (e instanceof Projectile) {
                Projectile p = (Projectile) e;

                // determine if projectile is out of frame
                if (!p.getBoundingBox().intersects(getFrame())) {
                    removal.add(p);
                }

                // determine if projectile has hit sailor
                if (sailor.getBoundingBox().intersects(p.getCoordinate())) {
                    removal.add(p);
                    printDamage(p, p.getDamagePoints(), sailor);
                    sailor.damage(p.getDamagePoints());
                }
            }

            // detect if item was collected or if bomb has finished exploding
            if (((e instanceof Bomb) && ((Bomb) e).getMustRemove()) ||
                    ((e instanceof Item) && ((Item) e).getCollected())) {
                removal.add(e);
            }

            e.update();
        }

        components.removeAll(removal);
        components.addAll(add);
    }

    /**
     * Reads the level file and sets all level components.
     *
     * @param fileName name of the level csv file
     * @param levelZero is the file a level 0 file
     * @return an array list of entities in the level
     */
    public ArrayList<Entity> readCSV(String fileName, boolean levelZero) {
        boolean readTopLeft = false;
        boolean readBottomRight = false;
        ArrayList<Entity> output = new ArrayList<Entity>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] inputData = text.split(",");
                int x = Integer.parseInt(inputData[1]);
                int y = Integer.parseInt(inputData[2]);

                // draw objects based on first column of csv
                if (inputData[0].equals("Sailor")) {
                    sailor = new Sailor(x, y);
                } else if (inputData[0].equals("Block")) {
                    if (levelZero) {
                        output.add(new Block(x, y));
                    } else {
                        output.add(new Bomb(x, y));
                    }
                } else if (inputData[0].equals("Pirate")) {
                    output.add(new Pirate(x, y));
                } else if (inputData[0].equals("Blackbeard")){
                    output.add(new Blackbeard(x, y));
                } else if (inputData[0].equals("Elixir")){
                    output.add(new Elixir(x, y));
                } else if (inputData[0].equals("Sword")) {
                    output.add(new Sword(x, y));
                } else if (inputData[0].equals("Potion")) {
                    output.add(new Potion(x, y));
                } else if (inputData[0].equals("Treasure")){
                    output.add(new Treasure(x, y));
                } else if (inputData[0].equals("TopLeft")) {
                    topLeft = new Point(x, y);
                    readTopLeft = true;
                } else if (inputData[0].equals("BottomRight")) {
                    bottomRight = new Point(x, y);
                    readBottomRight = true;
                }
                if (readTopLeft && readBottomRight) {
                    frame = new Rectangle(topLeft, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Draws level background, sailor, and all components
     */
    public void drawLevel() {
        this.getBackground().draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        sailor.drawEntity();
        for (Entity e: components) {
            e.drawEntity();
        }
    }

    /**
     * Displays game over screen as described by specifications
     */
    public void displayGameOver() {
        FONT.drawString(END_MESSAGE,(Window.getWidth()/2.0-FONT.getWidth(END_MESSAGE)/2.0), INSTRUCTION_OFFSET);
    }

    /**
     * Displays the start screen as described by specifications
     */
    public void drawStartScreen() {
        FONT.drawString(START_MESSAGE,(Window.getWidth()/2.0-FONT.getWidth(START_MESSAGE)/2.0), INSTRUCTION_OFFSET);
        FONT.drawString(CONTROL_MESSAGE, (Window.getWidth()/2.0-FONT.getWidth(CONTROL_MESSAGE)/2.0),
                INSTRUCTION_OFFSET+LINE);
        FONT.drawString(instructionMsg, (Window.getWidth()/2.0-FONT.getWidth(instructionMsg)/2.0),
                INSTRUCTION_OFFSET+2*LINE);
    }

    /**
     * Getter method for the starting state of the level.
     * @return starting state of level
     */
    public boolean isStarted() {return started;}

    /**
     * Setter method for the starting state of level
     * @param started starting state of level
     */
    public void setStarted(boolean started) {this.started = started;}

    /**
     * Getter method to determine if level can be terminated
     * @return termination state of level
     */
    public boolean isCompleted() {return completed;}

    /**
     * Setter for termination of level
     * @param completed termination state of level
     */
    public void setCompleted(boolean completed) {this.completed = completed;}

    /**
     * Getter for level bounds
     * @return Rectangle of the level bounds
     */
    public Rectangle getFrame() {return this.frame;}

    /**
     * Getter for level background
     * @return Image of background of level
     */
    public Image getBackground() {return background;}

    /**
     * Setter for level background
     * @param background an image of the level background
     */
    public void setBackground(Image background) {this.background = background;}
}
