package game;

import javafx.scene.shape.Rectangle;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class Placement {

    private boolean isEmpty;
    private double x, y, size;
    private Rectangle field;

    public Placement (double x, double y, double size) {
        setX(x);
        setY(y);
        setSize(size);
        setIsEmpty(true);
    }

    public void setField(Rectangle field) {
        this.field = field;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getX() {
        return x;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Rectangle getField() {
        return field;
    }
}
