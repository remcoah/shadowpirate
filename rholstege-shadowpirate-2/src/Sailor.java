import bagel.*;
import bagel.util.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Sailor extends Character {
    private final static int HEALTH_FONT_SIZE = 30;
    private final static int X_HEALTH = 10;
    private final static int Y_HEALTH = 25;
    private final static int X_LEFT_INVENTORY = 20;
    private final static int Y_TOP_INVENTORY = 60;
    private final static int SPEED = 2;
    private final static int ATTACK_TIME = 1000;
    private final static int IDLE_TIME = 2000;
    private final static int INVENTORY_BUFFER = 40;
    private final Image LEFT = new Image("res/sailor/sailorLeft.png");
    private final Image LEFT_HIT = new Image("res/sailor/sailorHitLeft.png");
    private final Image RIGHT = new Image("res/sailor/sailorRight.png");
    private final Image RIGHT_HIT = new Image("res/sailor/sailorHitRight.png");
    private boolean idle = true;
    private boolean canAttack = true;
    private boolean canMoveDown = true;
    private boolean canMoveUp = true;
    private boolean canMoveLeft = true;
    private boolean canMoveRight = true;
    private int damagePoints = 15;
    private ArrayList<Item> items = new ArrayList<>();

    public Sailor(int x, int y) {
        this.setCoordinate((new Point(x, y)));
        oldPoint = getCoordinate();
        nextPoint = getCoordinate();
        this.setHealthPoints(100);
        this.setMaxHealth(100);
        this.setCurrentImage(RIGHT);
        this.setSpeed(SPEED);
        this.setName("Sailor");
    }

    public Sailor(int x, int y, int health) {
        this.setCoordinate((new Point(x, y)));
        this.setHealthPoints(health);
        this.setMaxHealth(100);
        this.setCurrentImage(RIGHT);
        this.setSpeed(SPEED);
        this.setName("Sailor");
    }

    public void update(Input input, ArrayList<Entity> components, Rectangle frame) {
        this.drawHealth(X_HEALTH, Y_HEALTH, HEALTH_FONT_SIZE);
        this.drawInventory();
        super.update();
        seeNextMove(input);
        checkMovement(components, frame);
        updatePos();
        if (input.wasPressed(Keys.S) && canAttack && idle) {
            this.attack();
        }
    }

    private void drawInventory() {
        for (int i=0; i<items.size(); i++) {
            items.get(i).getIcon().draw(X_LEFT_INVENTORY, Y_TOP_INVENTORY+i*INVENTORY_BUFFER);
        }
    }

    private void seeNextMove(Input input) {
        if(input.isDown(Keys.LEFT) && canMoveLeft) {
            nextPoint = new Point(oldPoint.x - getSpeed(), oldPoint.y);
            if (idle) {
                setCurrentImage(LEFT);
            } else {
                setCurrentImage(LEFT_HIT);
            }
        }
        if (input.isDown(Keys.RIGHT) && canMoveRight) {
            nextPoint = new Point(oldPoint.x + getSpeed(), oldPoint.y);
            if (idle) {
                setCurrentImage(RIGHT);
            } else {
                setCurrentImage(RIGHT_HIT);
            }
        }
        if (input.isDown(Keys.UP) && canMoveUp) {
            nextPoint = new Point(oldPoint.x, oldPoint.y - getSpeed());
        }
        if (input.isDown(Keys.DOWN) && canMoveDown) {
            nextPoint = new Point(oldPoint.x, oldPoint.y + getSpeed());
        }
    }

    public void attack() {
        idle = false;
        canAttack = false;
        if(getCurrentImage().equals(RIGHT)) {
            setCurrentImage(RIGHT_HIT);
        } else if (getCurrentImage().equals(LEFT)) {
            setCurrentImage(LEFT_HIT);
        }
        Timer t = new Timer();
        TimerTask endIdle = new TimerTask() {
            @Override
            public void run() {
                canAttack = true;
            }
        };
        TimerTask makeIdle = new TimerTask() {
            @Override
            public void run() {
                idle = true;
            }
        };
        t.schedule(makeIdle,ATTACK_TIME);
        t.schedule(endIdle,ATTACK_TIME + IDLE_TIME);
    }

    public void checkMovement(ArrayList<Entity> components, Rectangle frame) {
        Rectangle nextRect = this.getNextRectangleAt(nextPoint);
        this.inFrame = checkInFrame(frame, nextRect);
        boolean noCollisions = true;
        for (Entity e : components) {
            Rectangle entity = e.getBoundingBox();
            if (nextRect.intersects(entity)) {
                if (e instanceof Obstacle) {
                    noCollisions = false;
                    obstacleCollision(nextRect, ((Obstacle) e));
                    setCoordinate(oldPoint);
                }
                if (e instanceof Item) {
                    items.add(pickUp((Item) e));
                }
            }
        }
        if (noCollisions && inFrame) {
            canMoveDown = true;
            canMoveUp = true;
            canMoveRight = true;
            canMoveLeft = true;
        }
    }

    public void obstacleCollision(Rectangle sailor, Obstacle o) {
        Rectangle obstacle = o.getBoundingBox();
        if (obstacle.bottom() >= sailor.top()) {
            this.canMoveUp = false;
        }
        if (obstacle.top() <= sailor.bottom()) {
            this.canMoveDown = false;
        }
        if (obstacle.left() <= sailor.right()) {
            this.canMoveRight = false;
        }
        if (obstacle.right() >= sailor.right()) {
            this.canMoveLeft = false;
        }
        if ((o instanceof Bomb)) {
            Bomb b = (Bomb) o;
            if (!b.getDamageDone()) {
                b.explode();
                damage(b.getDamagePoints());
            }
        }
    }

    public boolean checkInFrame(Rectangle frame, Rectangle nextRect) {
        boolean out = true;
        if (!frame.intersects(nextRect.topLeft())) {
            out = false;
            if (nextRect.top() <= frame.top()) {
                this.canMoveUp = false;
            }
            if (nextRect.top() >= frame.bottom()) {
                this.canMoveDown = false;
            }
            if (nextRect.left() <= frame.left()) {
                this.canMoveLeft = false;
            }
            if (nextRect.left() >= frame.right()) {
                this.canMoveRight = false;
            }
            setCoordinate(oldPoint);
        }
        return out;
    }

    public void updatePos() {
        if (!inFrame) {
            nextPoint = oldPoint;
        }
        oldPoint = nextPoint;
        setCoordinate(nextPoint);
        if (idle) {
            if (getCurrentImage().equals(LEFT_HIT)) {
                setCurrentImage(LEFT);
            } else if (getCurrentImage().equals(RIGHT_HIT)) {
                setCurrentImage(RIGHT);
            }
        }
    }

    public Item pickUp(Item item) {
        System.out.print("Sailor finds "+item.getName()+". Sailor's ");
        if (item instanceof Sword) {
            damagePoints += item.getBonus();
            System.out.println("damage points increased to " + damagePoints);
        } else {
            if (item instanceof Potion) {
                this.setHealthPoints(Math.min(getHealthPoints() + item.getBonus(), getMaxHealth()));
            } else if (item instanceof Elixir) {
                setMaxHealth(getMaxHealth()+item.getBonus());
                setHealthPoints(getMaxHealth());
            }
            System.out.println("current health: " +getHealthPoints()+"/"+getMaxHealth());
        }
        item.setCollected(true);
        return item;
    }

    public boolean isIdle() {return idle;}
    public int getDamagePoints() {return this.damagePoints; }
}
