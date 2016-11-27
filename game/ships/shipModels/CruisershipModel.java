package game.ships.shipModels;

import game.construction.CommonModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 16.10.2016.
 */
public class CruisershipModel extends CommonModel {

    private Rectangle ship;
    public CruisershipModel(){

        ship = new Rectangle(200, 400);
        addCursorHandClass();
        strokeOnMouseOver();
    }

    public Rectangle getShip() {
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
        ship.setX(x);
        ship.setY(y);
    }

    @Override
    public double getWidth() {
        return ship.getWidth();
    }

    @Override
    public double getHeight() {
        return ship.getHeight();
    }

    @Override
    public double getCenterX() {
        return ship.getX() + getWidth()/2;
    }

    @Override
    public double getCenterY() {
        return ship.getY() + getHeight()/2;
    }

    @Override
    public void setDefaultSkin() {
        System.out.println("ahojkz");
        ship.setStyle("-fx-background-image: url('game/ships/shipModels/pokus.png')");
        //ship.setFill(Color.BLACK);
    }

    @Override
    public boolean containsPosition(double x, double y) {
        return ship.contains(x, y);
    }
}
