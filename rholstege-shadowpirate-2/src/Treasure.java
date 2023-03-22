import bagel.Image;
import bagel.util.Point;

/**
 * Treasure entity that sailor must collide to complete level.
 * @author Remco Holstege
 */
public class Treasure extends Entity {
    public Treasure(int x, int y) {
        final Image TREASURE = new Image("res/treasure.png");
        this.setCoordinate(new Point(x, y));
        this.setCurrentImage(TREASURE);
        this.setName("Treasure");
    }
}
