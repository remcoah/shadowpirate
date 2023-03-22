import bagel.Image;
import bagel.util.Point;

/**
 * Block class for Shadow Pirate.
 * Only appear in level 0.
 *
 * @author Remco Holstege
 */
public class Block extends Obstacle {
    public Block(int x, int y) {
        this.setCoordinate(new Point(x, y));
        final Image BLOCK = (new Image("res/block.png"));
        this.setCurrentImage(BLOCK);
        this.setName("Block");
    }
}
