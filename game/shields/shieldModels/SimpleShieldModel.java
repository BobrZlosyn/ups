package game.shields.shieldModels;

import game.construction.CommonModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class SimpleShieldModel extends CommonModel {
    private Rectangle shield;

    public SimpleShieldModel(){
        createShiled();
    }

    private void createShiled(){
        shield = new Rectangle(50,50);
        shield.setFill(Color.ORANGE);
    }

    public Rectangle getShield() {
        return shield;
    }

    @Override
    public ArrayList<Shape> getParts(){
        ArrayList<Shape> shieldParts = new ArrayList<>();
        shieldParts.add(shield);
        return shieldParts;
    }

    @Override
    public Pane getParent() {
        return (Pane)shield.getParent();
    }

    @Override
    public void setModelXY(double x, double y) {
        shield.setX(x);
        shield.setY(y);
    }

    @Override
    public double getWidth() {
        return shield.getWidth();
    }
}
