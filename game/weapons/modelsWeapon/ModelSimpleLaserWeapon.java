package game.weapons.modelsWeapon;

import game.construction.CommonModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 15.11.2016.
 */
public class ModelSimpleLaserWeapon extends CommonModel {

    private Circle room, room2;
    private Polygon tower1, tower2;

    public ModelSimpleLaserWeapon () {
        createCannon();

        addCursorHandClass();
        strokeOnMouseOver();
    }

    private void createCannon(){
        room = new Circle(25);
        room2 = new Circle(20);

        double [] points1 = new double []{
                0.0, 22,
                36, 22,
                36, 19,
                0 ,8
        };

        double [] points2 = new double []{
                0.0, 22 + 5,
                36, 22 + 5,
                36, 25 + 5,
                0 ,42
        };

        tower1 = new Polygon(points1);
        tower2 = new Polygon(points2);

        setDefaultSkin();
    }

    @Override
    public void setModelXY(double x, double y){

        room.setCenterX(x);
        room.setCenterY(y);

        tower1.setLayoutX(x);
        tower2.setLayoutX(x);

        tower1.setLayoutY(y - 27);
        tower2.setLayoutY(y - 23);

        room2.setCenterY(y);
        room2.setCenterX(x);

    }

    @Override
    public ArrayList<Shape> getParts() {
        ArrayList<Shape> cannonParts = new ArrayList<>();
        cannonParts.add(room);
        cannonParts.add(tower1);
        cannonParts.add(tower2);
        cannonParts.add(room2);
        return cannonParts;
    }

    @Override
    public Pane getParent() {
        return (Pane)room.getParent();
    }

    @Override
    public double getWidth() {
        return room.getRadius() *2;
    }

    @Override
    public double getHeight() {
        return getWidth();
    }

    @Override
    public double getCenterX() {
        return room.getCenterX();
    }

    @Override
    public double getCenterY() {
        return room.getCenterY();
    }

    @Override
    public void setDefaultSkin() {

        room.setFill(Color.ORANGE);
        room2.setFill(Color.GREEN);
        tower1.setFill(Color.YELLOW);
        tower2.setFill(Color.RED);
    }

    @Override
    public boolean containsPosition(double x, double y) {
        return room.contains(x, y);
    }

    public Polygon getTower1() {
        return tower1;
    }

    public Polygon getTower2() {
        return tower2;
    }

    public Circle getRoom() {
        return room;
    }
}
