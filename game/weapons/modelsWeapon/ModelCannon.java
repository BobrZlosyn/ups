package game.weapons.modelsWeapon;

import game.construction.CommonModel;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class ModelCannon extends CommonModel{
    private Circle room, head;
    private Rectangle cannon;

    public ModelCannon(){
        createCannon();
        addCursorHandClass();
        strokeOnMouseOver();
    }

    private void createCannon(){
        room = new Circle(25);
        head = new Circle(15);
        cannon = new Rectangle(30, 10);
        setDefaultSkin();
    }

    public void setModelXY(double x, double y){

        room.setCenterX(x);
        room.setCenterY(y);
        head.setCenterX(x);
        head.setCenterY(y);

        cannon.setX(x);
        cannon.setY(y - cannon.getHeight()/2);
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
        room.setFill(new ImagePattern(
                new Image(getClass().getResource("/game/background/textures/pokus.png").toExternalForm()), 0, 0, 1, 1, true));
        head.setFill(Color.YELLOW);
        cannon.setFill(Color.RED);
    }

    @Override
    public boolean containsPosition(double x, double y) {
        return room.contains(x ,y);
    }

    public Rectangle getCannon() {
        return cannon;
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
        cannonParts.add(cannon);
        cannonParts.add(head);
        return cannonParts;
    }

    @Override
    public Pane getParent() {
        return  (Pane)room.getParent();
    }
}
