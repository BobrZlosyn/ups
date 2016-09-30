package game.weapons.modelsWeapon;

/**
 * Created by Kanto on 30.09.2016.
 */

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class ModelDoubleCannon {
    private Circle room, head;
    private Rectangle cannonTop, cannonBottom;

    public ModelDoubleCannon(){
        createCannon();
    }

    private void createCannon(){
        room = new Circle(25);
        room.setFill(Color.ORANGE);

        head = new Circle(15);
        head.setFill(Color.YELLOW);

        cannonTop = new Rectangle(30, 8);
        cannonTop.setFill(Color.RED);

        cannonBottom = new Rectangle(30, 8);
        cannonBottom.setFill(Color.GREEN);

    }

    public void setCannonXY(double x, double y){

        room.setCenterX(x);
        room.setCenterY(y);
        head.setCenterX(x);
        head.setCenterY(y);

        cannonTop.setX(x);
        cannonTop.setY(y - cannonTop.getHeight()/2 - 5);

        cannonBottom.setX(x);
        cannonBottom.setY(y - cannonBottom.getHeight()/2 + 5);

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

    public ArrayList<Shape> getParts(){
        ArrayList<Shape> cannonParts = new ArrayList<>();
        cannonParts.add(room);
        cannonParts.add(cannonTop);
        cannonParts.add(cannonBottom);
        cannonParts.add(head);
        return cannonParts;
    }
}
