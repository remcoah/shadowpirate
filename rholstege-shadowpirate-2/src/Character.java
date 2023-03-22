import bagel.*;
import bagel.util.*;
import java.util.ArrayList;

/**
 * Character class for Shadow Pirate.
 * Characters are entities that can move and have health.
 * In Shadow Pirate there are Enemy and Sailor characters.
 *
 * @author Remco Holstege
 */
public abstract class Character extends Entity {
    // determines the colour of health display based on its percentage
    protected final static int ORANGE_BOUNDARY = 65;
    protected final static int RED_BOUNDARY = 35;
    protected final Colour GREEN = new Colour(0 ,0.8, 0.2);
    protected final Colour ORANGE = new Colour(0.9, 0.6, 0);
    protected final Colour RED = new Colour(1, 0, 0);

    protected Point oldPoint;
    protected Point nextPoint;
    protected boolean inFrame = true;

    private int healthPoints;
    private int maxHealth;
    private double speed;

    /**
     * Checks the movement of the character based on level.
     * Sets old point and next point accordingly based on the movement.
     *
     * @param components entities in the level.
     * @param frame level bound.
     */
    public abstract void checkMovement(ArrayList<Entity> components, Rectangle frame);

    /**
     * Draws the health of the character as required by specifications.
     *
     * @param x x-coordinate to draw the health.
     * @param y y-coordinate to draw the health.
     * @param fontSize font size to draw health in.
     */
    public void drawHealth(int x, int y, int fontSize) {
        Font healthFont = new Font("res/wheaton.otf", fontSize);
        DrawOptions drawOpt = new DrawOptions();
        String out = this.getHealth() + "%";
        if (this.getHealth() > ORANGE_BOUNDARY) {
            // set health display color to green
            healthFont.drawString(out, x, y, drawOpt.setBlendColour(GREEN));
        } else if (this.getHealth() > RED_BOUNDARY) {
            // set health display color to orange
            healthFont.drawString(out, x, y, drawOpt.setBlendColour(ORANGE));
        } else {
            // set health display color to red
            healthFont.drawString(out, x, y, drawOpt.setBlendColour(RED));
        }
    }

    /**
     * Deals damage to the character and prints character's log per spec-requirement.
     *
     * @param damage damage points inflicted
     */
    public void damage(int damage) {
        healthPoints -= damage;
        System.out.println(this.getName()+"'s current health: "+getHealthPoints()+"/"+getMaxHealth());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        super.update();
        this.oldPoint = getCoordinate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCoordinate(Point coordinate) {
        super.setCoordinate(coordinate);
        nextPoint = oldPoint;
    }

    /**
     * Returns the bounding box of the character based on its next point
     *
     * @param nextPoint Top left coordinate of character
     * @return bounding box at nextPoint
     */
    public Rectangle getNextRectangleAt(Point nextPoint) {
        Point centreImage = new Point(nextPoint.x + getCurrentImage().getWidth()/2,
                nextPoint.y+ getCurrentImage().getHeight()/2);
        return getCurrentImage().getBoundingBoxAt(centreImage);
    }

    /**
     * Getter for character's health in percentage
     * @return character's health percent
     */
    public int getHealth() {
        double healthPercent = ((double)this.healthPoints/this.maxHealth)*100;
        return (int)Math.round(healthPercent);
    }

    /**
     * Getter method for character's total health points
     * @return character's total health points
     */
    public int getHealthPoints() {
        return Math.max(healthPoints, 0);
    }

    /**
     * Setter method for character's total health points.
     * @param healthPoints character's total health points.
     */
    public void setHealthPoints(int healthPoints) {this.healthPoints = healthPoints;}

    /**
     * Getter method for character's maximum possible health points.
     * @return character's maximum possible health points.
     */
    public int getMaxHealth() {return maxHealth;}

    /**
     * Setter method for character's maximum possible health points.
     * @param maxHealth character's maximum possible health points.
     */
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}

    /**
     * Getter method for character's speed.
     * @return character's speed.
     */
    public double getSpeed() {return speed;}

    /**
     * Setter method for character's speed.
     * @param speed character's speed.
     */
    public void setSpeed(double speed) {this.speed = speed;}
}
