package game.construction;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by Kanto on 30.09.2016.
 */
public abstract class CommonModel {

    public abstract ArrayList<Shape> getParts();

    public abstract Pane getParent();

    public abstract void setModelXY(double x, double y);


}
