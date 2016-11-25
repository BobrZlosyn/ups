package game.construction;

import game.static_classes.GlobalVariables;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    /**
     * priradi novy styl na cursor
     */
    protected void addCursorHandClass() {
        getParts().forEach(shape -> shape.getStyleClass().add("hand-cursor"));
    }

    /**
     * zvyrazni model barevne pokud neni vybrano uzivatelem
     */
    protected void strokeOnMouseOver() {
        getParts().forEach(shape -> {
            shape.setOnMouseEntered(event -> getParts().forEach(shape1 -> {
                if (GlobalVariables.isEmpty(GlobalVariables.getMarkedObject())
                        || isNotMarkedObject()
                        || isNotTargetObject()){

                    shape1.setStroke(Color.YELLOW);
                }
            }));

            shape.setOnMouseExited(event -> getParts().forEach(shape1 -> {

                if (GlobalVariables.isEmpty(GlobalVariables.getMarkedObject())
                        || isNotMarkedObject()
                        || isNotTargetObject()){

                    shape1.setStroke(Color.TRANSPARENT);
                }
            }));
        });


    }

    private boolean isNotMarkedObject(){
        boolean isMarkedObject = GlobalVariables.isNotEmpty(GlobalVariables.getMarkedObject()) &&
                                 !GlobalVariables.getMarkedObject().getModel().equals(this);
        return isMarkedObject;
    }

    private boolean isNotTargetObject(){
        boolean isTargetObject = GlobalVariables.isNotEmpty(GlobalVariables.getTargetObject()) &&
                                 !GlobalVariables.getTargetObject().getModel().equals(this);
        return isTargetObject;
    }
}
