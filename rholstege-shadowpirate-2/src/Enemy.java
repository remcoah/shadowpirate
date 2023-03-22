import bagel.*;
import bagel.util.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract Enemy class for Shadow Pirate.
 * Enemies are either pirates or blackbeard. They can move and attack sailors.
 *
 * @author Remco Holstege
 */
public abstract class Enemy extends Character {
    protected final static int HEALTH_FONT_SIZE = 15;
    protected final static int HEALTH_BUFFER = 6;
    protected boolean MOVES_HORIZONTAL;
    protected final static int COOLDOWN_TIME = 1500;
    private final static int INVINCIBLE_TIME = 1500;
    private boolean invincible = false;
    private Rectangle attackRange;
    private boolean readyToAttack = true;
    private int direction;
    private boolean collided = false;

    /**
     * Shoots a projectile towards the target.
     * Enters cool down state and exits after cool down time finishes.
     *
     * @param target Point of the shooting target
     * @return Projectile enemy has shot
     */
    public abstract Projectile shoot(Point target);

    /**
     * Getter for left facing image
     * @return left facing enemy image
     */
    public abstract Image getLeft();

    /**
     * Getter for left facing invincible image
     * @return left facing invincible enemy image
     */
    public abstract Image getLeftInvincible();

    /**
     * Getter for right facing image
     * @return right facing enemy image
     */
    public abstract Image getRight();

    /**
     * Getter for right facing invincible image
     * @return right facing invincible enemy image
     */
    public abstract Image getRightInvincible();

    /**
     * Getter for the attack range of enemy
     * @return amount of pixels from the center of the pirate
     */
    public abstract int getAttackLength();

    /**
     * Getter for cool down state length of enemy
     * @return cool down time length
     */
    public abstract int getCoolDown();

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (getHealth() > 0) {
            super.update();
        }
    }

    /**
     * Performs a state update.
     * Draws enemy according to spec. Also implements its movement.
     *
     * @param components entities in the level.
     * @param frame level bound
     */
    public void update(ArrayList<Entity> components, Rectangle frame) {
        if (getHealth() > 0) {
            double attackX = this.getBoundingBox().centre().x - getAttackLength();
            double attackY = this.getBoundingBox().centre().y - getAttackLength();
            this.setAttackRange(new Rectangle(attackX, attackY, 2*getAttackLength(), 2*getAttackLength()));

            // draws health accordingly
            Rectangle pirBox = this.getBoundingBox();
            this.drawHealth((int)pirBox.left(), (int)pirBox.top() - HEALTH_BUFFER, HEALTH_FONT_SIZE);

            move(components, frame);

            // updates current image based on state
            if (this.isInvincible()) {
                if (getCurrentImage().equals(getLeft())) {
                    this.setCurrentImage(getLeftInvincible());
                } else if (getCurrentImage().equals(getRight())) {
                    this.setCurrentImage(getRightInvincible());
                }
            }
        }
    }

    /**
     * Sets the enemy's random variable as described by spec
     */
    public void setRandomVariables() {
        Random r = new Random();
        MOVES_HORIZONTAL = r.nextBoolean();

        if (r.nextBoolean()) {
            this.direction = 1;
            this.setCurrentImage(getRight());
        } else {
            this.direction = -1;
            this.setCurrentImage(getLeft());
        }

        // set speed between 0.2 and 0.7 randomly as required by spec
        this.setSpeed(0.2+0.5*r.nextDouble());
    }

    /**
     * Determines enemy's next point
     *
     * @param components entities in the level
     * @param frame level bounds
     */
    public void move(ArrayList<Entity> components, Rectangle frame) {
        if (MOVES_HORIZONTAL) {
            this.nextPoint = new Point(oldPoint.x+direction*this.getSpeed(), oldPoint.y);
        } else {
            this.nextPoint = new Point(oldPoint.x, oldPoint.y+direction*this.getSpeed());
        }
        this.checkMovement(components, frame);
        updatePos();
    }

    /**
     * Moves enemy to its next point if valid
     */
    public void updatePos() {
        if(!collided) {
            setCoordinate(nextPoint);
        } else {
            setCoordinate(oldPoint);
        }
    }

    /**
     * Makes enemy switch directions.
     * Meant to use when collided
     */
    public void switchDirections() {
        this.setDirection(this.getDirection()*(-1));
        this.setCoordinate(oldPoint);
        this.setCollided(true);

        // to ensure enemy does not freeze, allow for small delay
        Timer t = new Timer();
        TimerTask endCollision = new TimerTask() {
            @Override
            public void run() {
                setCollided(false);
            }
        };
        t.schedule(endCollision, 1);

        // update current image as required
        if (this.getDirection() == 1) {
            if(this.isInvincible()) {
                this.setCurrentImage(getRightInvincible());
            } else {
                this.setCurrentImage(getRight());
            }
        } else {
            if(this.isInvincible()) {
                this.setCurrentImage(getLeftInvincible());
            } else {
                this.setCurrentImage(getLeft());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void damage(int damage) {
        super.damage(damage);
        invincible = true;

        // end invincible state after invincibility ends time
        Timer t = new Timer();
        TimerTask endInvincible = new TimerTask() {
            @Override
            public void run() {
                invincible = false;
            }
        };
        t.schedule(endInvincible,INVINCIBLE_TIME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkMovement(ArrayList<Entity> components, Rectangle frame) {
        Rectangle nextRect = this.getNextRectangleAt(nextPoint);
        this.inFrame = checkInFrame(frame, nextPoint);
        for (Entity e : components) {
            Rectangle obstacle = e.getBoundingBox();
            if ((e instanceof Obstacle) && nextRect.intersects(obstacle)) {
                switchDirections();
                break;
            }
        }
    }

    /**
     * Checks if enemy is in the bounds of the level
     *
     * @param frame level bound
     * @param nextPoint next point of the enemy
     * @return if the enemy's next point is in the frame
     */
    public boolean checkInFrame(Rectangle frame, Point nextPoint) {
        boolean out = true;
        if (!frame.intersects(nextPoint)) {
            out = false;
            switchDirections();
        }
        return out;
    }

    /**
     * getter for invincibility state
     * @return enemy invincibility state
     */
    public boolean isInvincible() {return invincible; }

    /**
     * getter for enemy's attack range
     * @return enemy's Rectangle attack range
     */
    public Rectangle getAttackRange() {
        return attackRange;
    }

    /**
     * Setter for enemuy's attack range
     * @param attackRange enemy's attack range
     */
    public void setAttackRange(Rectangle attackRange) {
        this.attackRange = attackRange;
    }

    /**
     * getter if enemy can attack
     * @return if enemy can attack
     */
    public boolean canAttack() {return this.readyToAttack;}

    /**
     * Setter for enemy's attack state
     * @param b enemy's attack state
     */
    public void setReadyToAttack(boolean b) {this.readyToAttack = b;}

    /**
     * Gets direciton of enemy
     * @return direction of enemy
     */
    public int getDirection() {
        return direction;
    }

    /**
     * sets direction of enemy
     * @param direction enemy's direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * sets collision state of enemy
     * @param collided collision state
     */
    public void setCollided(boolean collided) {
        this.collided = collided;
    }
}
