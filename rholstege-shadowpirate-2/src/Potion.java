import bagel.Image;
import bagel.util.Point;

/**
 * Potion item class.
 * @author Remco Holstegew
 */
public class Potion extends Item {
    private final static int POTION_BONUS = 25;

    public Potion(int x, int y) {
        final Image POTION = new Image("res/items/potion.png");
        final Image POTION_ICON = new Image("res/items/potionIcon.png");
        this.setCoordinate(new Point(x, y));
        this.setCurrentImage(POTION);
        this.setIcon(POTION_ICON);
        this.setBonus(POTION_BONUS);
        this.setName("Potion");
    }
}
