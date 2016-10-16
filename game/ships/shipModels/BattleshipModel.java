package game.ships.shipModels;

import game.construction.CommonModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 16.10.2016.
 */
public class BattleshipModel extends CommonModel{
    private Circle ship;

    public BattleshipModel(){
        ship = new Circle(150);
        ship.setStyle("-fx-background-color: red;");
    }

    public Circle getShip() {
        return ship;
    }

    @Override
    public ArrayList<Shape> getParts() {
        ArrayList <Shape> parts = new ArrayList<>();
        parts.add(ship);
        return parts;
    }

    @Override
    public Pane getParent() {
        return (Pane) ship.getParent();
    }

    @Override
    public void setModelXY(double x, double y) {
        ship.setCenterX(x);
        ship.setCenterY(y);
    }

    @Override
    public double getWidth() {
        return ship.getRadius() * 2;
    }

    @Override
    public void setDefaultSkin() {
        ship.setFill(Color.BLACK);
    }

}
