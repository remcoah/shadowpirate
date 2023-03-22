import bagel.*;
import bagel.util.*;

/**
 * Elixir item class.
 * @author Remco Holstege
 */
public class Elixir extends Item {
    public Elixir(int x, int y) {
        final Image ELIXIR = new Image("res/items/elixir.png");
        final Image ELIXIR_ICON = new Image("res/items/elixirIcon.png");
        this.setBonus(35);
        this.setCoordinate(new Point(x, y));
        this.setCurrentImage(ELIXIR);
        this.setIcon(ELIXIR_ICON);
        this.setName("Elixir");
    }
}
