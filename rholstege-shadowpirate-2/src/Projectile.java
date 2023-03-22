import bagel.*;
import bagel.util.*;

/**
 * Projectile class for Shadow Pirate
 */
public class Projectile extends Entity {
    private Image PIRATE_PRJ = new Image("res/pirate/pirateProjectile.png");
    private Image BLACKBEARD_PRJ = new Image("res/blackbeard/blackBeardProjectile.png");
    private final static double PIRATE_SPEED = 0.4;
    private final static double BLACKBEARD_SPEED = 0.8;
    private final int DAMAGE_POINTS;
    private final double ROTATION;
    private final Vector2 DIRECTION;

    public Projectile(Point source, Point target, boolean blackbeard) {
        this.setCoordinate(source);
        this.ROTATION = Math.atan((source.y - target.y)/(source.x- target.x));
        if (blackbeard) {
            this.setCurrentImage(BLACKBEARD_PRJ);
            DIRECTION = findDirection(source, target, BLACKBEARD_SPEED);
            DAMAGE_POINTS = 20;
            this.setName("Blackbeard");
        } else {
            this.setCurrentImage(PIRATE_PRJ);
            DIRECTION = findDirection(source, target, PIRATE_SPEED);
            DAMAGE_POINTS = 10;
            this.setName("Pirate");
        }
    }

    public Vector2 findDirection(Point source, Point target, double speed) {
        Vector2 output = target.asVector().sub(source.asVector());
        output = output.normalised().mul(speed);
        return output;
    }

    @Override
    public void update() {
        Point nextPoint = (getCoordinate().asVector().add(DIRECTION)).asPoint();
        setCoordinate(nextPoint);
        this.drawEntity();
    }

    @Override
    public void drawEntity() {;
        getCurrentImage().draw(getCoordinate().x, getCoordinate().y, new DrawOptions().setRotation(ROTATION));
        this.setBoundingBox(getCurrentImage().getBoundingBoxAt(getCoordinate()));
    }

    public int getDamagePoints() {
        return DAMAGE_POINTS;
    }
}
