import bagel.Image;
import bagel.util.Point;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Pirate class, a specific type of entity
 *
 * @author Remco Holstege
 */
public class Pirate extends Enemy {
    private final static int MAX_HEALTH = 45;
    private final static int ATTACK_LENGTH = 100;
    private final static int COOL_DOWN_TIME = 3000;
    private final Image LEFT = new Image("res/pirate/pirateLeft.png");
    private final Image LEFT_INVINCIBLE = new Image("res/pirate/pirateHitLeft.png");
    private final Image RIGHT = new Image("res/pirate/pirateRight.png");
    private final Image RIGHT_INVINCIBLE = new Image("res/pirate/pirateHitRight.png");

    /**
     * Creates a pirate at specific coordinates
     * @param x x coordinate
     * @param y y coordinate
     */
    public Pirate(int x, int y) {
        this.setCoordinate(new Point(x, y));
        this.oldPoint = getCoordinate();
        this.setRandomVariables();
        this.setHealthPoints(MAX_HEALTH);
        this.setMaxHealth(MAX_HEALTH);
        this.setName("Pirate");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Projectile shoot(Point target) {
        setReadyToAttack(false);
        Timer t = new Timer();
        TimerTask endCoolDown = new TimerTask() {
            @Override
            public void run() {
                setReadyToAttack(true);
            }
        };
        t.schedule(endCoolDown,getCoolDown());
        return new Projectile(this.getBoundingBox().centre(), target, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getLeft() {
        return LEFT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getLeftInvincible() {
        return LEFT_INVINCIBLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getRight() {
        return RIGHT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getRightInvincible() {
        return RIGHT_INVINCIBLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackLength() {
        return ATTACK_LENGTH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCoolDown() {return COOL_DOWN_TIME;}

}
