package game.weapons.modelsWeapon;

/**
 * Created by Kanto on 30.09.2016.
 */

import game.construction.CommonModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class ModelDoubleCannon extends CommonModel{
    private Circle room, head;
    private Rectangle cannonTop, cannonBottom;

    public ModelDoubleCannon(){
        createCannon();
        strokeOnMouseOver();
    }

    private void createCannon(){
        room = new Circle(25);
        head = new Circle(15);
        cannonTop = new Rectangle(30, 8);
        cannonBottom = new Rectangle(30, 8);
        setDefaultSkin();
    }

    public Rectangle getCannonTop() {
        return cannonTop;
    }

    public Rectangle getCannonBottom() {
        return cannonBottom;
    }

    public Circle getRoom() {
        return room;
    }

    public Circle getHead() {
        return head;
    }

    @Override
    public ArrayList<Shape> getParts(){
        ArrayList<Shape> cannonParts = new ArrayList<>();
        cannonParts.add(room);
        cannonParts.add(cannonTop);
        cannonParts.add(cannonBottom);
        cannonParts.add(head);
        return cannonParts;
    }

    @Override
    public Pane getParent() {
        return (Pane) room.getParent();
    }


    @Override
    public void setModelXY(double x, double y) {
        room.setCenterX(x);
        room.setCenterY(y);
        head.setCenterX(x);
        head.setCenterY(y);

        cannonTop.setX(x);
        cannonTop.setY(y - cannonTop.getHeight()/2 - 5);

        cannonBottom.setX(x);
        cannonBottom.setY(y - cannonBottom.getHeight()/2 + 5);
    }

    @Override
    public double getWidth() {
        return room.getRadius()*2;
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
        head.setFill(Color.YELLOW);
        cannonTop.setFill(Color.RED);
        cannonBottom.setFill(Color.GREEN);
    }

    @Override
    public boolean containsPosition(double x, double y) {
        return room.contains(x ,y);
    }
}
