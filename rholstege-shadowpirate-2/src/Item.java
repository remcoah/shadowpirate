import bagel.Image;

/**
 * Abstract item class for Shadow Pirate.
 * Items are entities which can be picked up by the sailor.
 *
 * @author Remco Holstege
 */
public abstract class Item extends Entity {
    private boolean collected = false;
    private int BONUS;
    private Image icon;

    /**
     * Getter for item's collection state
     * @return if item has been collected
     */
    public boolean getCollected() {return collected; }

    /**
     * setter for item's collection state
     * @param b if item has been collected
     */
    public void setCollected(boolean b) {
        collected = b;
    }

    /**
     * Getter for item's bonus points
     * @return item's bonus points
     */
    public int getBonus() {return BONUS;}

    /**
     * Setter for item's bonus points
     * @param bonus item's bonus points
     */
    public void setBonus(int bonus) {this.BONUS = bonus;}

    /**
     * Getter for item's icon
     * @return item's Image icon
     */
    public Image getIcon(){return icon;}

    /**
     * Setter for item's icon
     * @param i item's Image icon
     */
    public void setIcon(Image i) {this.icon = i;}

}
