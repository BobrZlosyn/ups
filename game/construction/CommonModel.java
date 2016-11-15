package game.construction;

import game.static_classes.GlobalVariables;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by Kanto on 30.09.2016.
 */
public abstract class CommonModel {

    public abstract ArrayList<Shape> getParts();

    public abstract Pane getParent();

    public void removeModel(){
        Pane pane = getParent();
        if(!GlobalVariables.isEmpty(pane)){
            pane.getChildren().removeAll(getParts());
        }
    }

    public abstract void setModelXY(double x, double y);

    public abstract double getWidth();

    public abstract double getHeight();

    public abstract double getCenterX();

    public abstract double getCenterY();

    public abstract void setDefaultSkin();

    public abstract boolean containsPosition(double x, double y);


}
