package game.ships.shipModels;

import game.construction.CommonModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by Kanto on 14.11.2016.
 */
public class AdmiralshipModel extends CommonModel {
    private Polygon ship;
    private int width;
    private int height;

    public AdmiralshipModel(){
        width = 270;
        height = 350;
        createShip();
    }

    private void createShip(){
        double points [] = {
                80.0, 0.0,
                190.0, 0.0,
                270.0, 240.0,
                270.0, 290.0,
                270.0, 350.0, //spodni pravy roh
                245.0, 350.0,
                245.0, 310.0, //konce rohu
                190.0, 350.0,
                80.0, 350.0,
                25.0, 310.0,
                25.0, 350.0,
                0.0, 350.0,
                0.0, 290.0,
                0.0, 240.0
        };
        ship = new Polygon(points);
    }


    public Polygon getShip(){
        return ship;
    }

    @Override
    public ArrayList<Shape> getParts() {
        ArrayList<Shape> shapes = new ArrayList<>();
        shapes.add(ship);
        return shapes;
    }

    @Override
    public Pane getParent() {
        return (Pane)ship.getParent();
    }

    @Override
    public void setModelXY(double x, double y) {
        ship.setLayoutX(x - getWidth()/2);
        ship.setLayoutY(y - getHeight()/2);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getCenterX() {
        return ship.getLayoutX() + getWidth()/2;
    }

    @Override
    public double getCenterY() {
        return ship.getLayoutY() - getHeight()/2;
    }

    @Override
    public void setDefaultSkin() {
        ship.setFill(Color.BLACK);
    }
}
