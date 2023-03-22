import bagel.*;
import bagel.util.*;

/**
 * An abstract entity class for Shadow Pirate.
 * All things visible and interactive in the game are entities.
 *
 * @author Remco Hosltege
 */
public abstract class Entity {
    private Point coordinate;
    private Rectangle boundingBox;
    private Image currentImage;
    private String name;

    /**
     * Draws the entity on the game window.
     * Also updates the entities bounding box based on its coordinate and current image.
     */
    public void drawEntity() {
        currentImage.drawFromTopLeft(coordinate.x, coordinate.y);
        Point centreImage = new Point(coordinate.x + currentImage.getWidth()/2, coordinate.y+ currentImage.getHeight()/2);
        this.boundingBox = currentImage.getBoundingBoxAt(centreImage);
    }

    /**
     * Performs a state update.
     */
    public void update() {
        this.drawEntity();
    }

    /**
     * Getter for the name of the entity
     * @return entity's name
     */
    public String getName() {return name; }

    /**
     * Setter for the name of the entity
     * @param name entity's name
     */
    public void setName(String name) {this.name = name; }

    /**
     * Getter for the Point coordinate of the entity
     * @return entity's Point coordinate
     */
    public Point getCoordinate() {return coordinate;}

    /**
     * Setter for the Point coordinate of the entity
     * @param coordinate entity's Point coordinate
     */
    public void setCoordinate(Point coordinate) {this.coordinate = coordinate;}

    /**
     * Getter for the Rectangle bounding box of the entity
     * @return entity's Rectangle bounding box
     */
    public Rectangle getBoundingBox() {return boundingBox;}

    /**
     * Setter for the Rectangle bounding box of the entity
     * @param boundingBox entity's Rectangle bounding box
     */
    public void setBoundingBox(Rectangle boundingBox) {this.boundingBox = boundingBox;}

    /**
     * Getter for the current display image of the entity
     * @return entity's current display image
     */
    public Image getCurrentImage() {return currentImage;}

    /**
     * Setter for the current display image of the entity
     * @param currentImage entity's current display image
     */
    public void setCurrentImage(Image currentImage) {this.currentImage = currentImage;}
}
