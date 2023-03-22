import bagel.Image;
import bagel.util.Point;

import java.util.Timer;
import java.util.TimerTask;

public class Blackbeard extends Enemy {
    private final static int MAX_HEALTH = 90;
    private final static int ATTACK_LENGTH = 200;
    private final Image LEFT = new Image("res/blackbeard/blackbeardLeft.png");
    private final Image LEFT_INVINCIBLE = new Image("res/blackbeard/blackbeardHitLeft.png");
    private final Image RIGHT = new Image("res/blackbeard/blackbeardRight.png");
    private final Image RIGHT_INVINCIBLE = new Image("res/blackbeard/blackbeardHitRight.png");

    /**
     * Creates a blackbeard at specific coordinates
     * @param x x coordinate
     * @param y y coordinate
     */
    public Blackbeard(int x, int y)  {
        this.setCoordinate(new Point(x, y));
        this.oldPoint = getCoordinate();
        this.setRandomVariables();
        this.setHealthPoints(MAX_HEALTH);
        this.setMaxHealth(MAX_HEALTH);
        this.setName("Blackbeard");
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
        return new Projectile(this.getBoundingBox().centre(), target, true);
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
    public int getCoolDown() {return COOLDOWN_TIME;}
}
