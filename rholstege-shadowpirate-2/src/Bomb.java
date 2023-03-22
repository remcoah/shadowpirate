import bagel.Image;
import bagel.util.Point;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Bomb class. An obstacle which only appears in level 1
 *
 * @author Remco Holstege
 */
public class Bomb extends Obstacle {
    private final static int DMG_PNTS = 10;
    private final static int BOMB_DELAY = 500;
    private final Image EXPLOSION = new Image("res/explosion.png");
    private boolean mustRemove = false;
    private boolean damageDone = false;

    /**
     * creates bomb at specific coordinate
     * @param x x coordinate
     * @param y y coordinate
     */
    public Bomb(int x, int y) {
        this.setCoordinate(new Point(x, y));
        final Image BOMB = new Image("res/bomb.png");
        this.setCurrentImage(BOMB);
        this.setName("Bomb");
    }

    /**
     * getter for damage points
     * @return bomb's damage points
     */
    public int getDamagePoints() {
        return DMG_PNTS;
    }

    /**
     * getter for removal state of bomb
     * @return bomb's removal state
     */
    public boolean getMustRemove() {
        return mustRemove;
    }

    /**
     * implements explosion of bomb as per spec
     */
    public void explode() {
        this.setCurrentImage(EXPLOSION);
        this.damageDone = true;
        System.out.print("Bomb inflicts "+DMG_PNTS+" damage points on Sailor. ");
        Timer t = new Timer();
        TimerTask removeBomb = new TimerTask() {
            @Override
            public void run() {
                mustRemove = true;
            }
        };
        t.schedule(removeBomb,BOMB_DELAY);
    }

    /**
     * getter for damage done by bomb
     * @return if damage was done by bomb
     */
    public boolean getDamageDone() {
        return this.damageDone;
    }
}
