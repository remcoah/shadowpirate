import bagel.Image;
import bagel.util.Point;

/**
 * Sword item class
 * @author Remco Holstege
 */
public class Sword extends Item {
    private final static int SWORD_BONUS = 15;

    public Sword(int x, int y) {
        this.setBonus(SWORD_BONUS);
        this.setCoordinate(new Point(x, y));
        final Image SWORD = new Image("res/items/sword.png");
        final Image SWORD_ICON = new Image("res/items/swordIcon.png");
        this.setCurrentImage(SWORD);
        this.setIcon(SWORD_ICON);
        this.setName("Sword");
    }
}
