package game.weapons.modelsWeapon;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class ModelCannon {
    private Circle room, head;
    private Rectangle cannon;

    public ModelCannon(){
        createCannon();
    }

    private void createCannon(){
        room = new Circle(25);
        room.setFill(Color.ORANGE);

        head = new Circle(15);
        head.setFill(Color.YELLOW);

        cannon = new Rectangle(30, 10);
        cannon.setFill(Color.RED);

    }

    public void setCannonXY(double x, double y){

        room.setCenterX(x);
        room.setCenterY(y);
        head.setCenterX(x);
        head.setCenterY(y);

        cannon.setX(x);
        cannon.setY(y - cannon.getHeight()/2);
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

    public ArrayList<Shape> getParts(){
        ArrayList<Shape> cannonParts = new ArrayList<>();
        cannonParts.add(room);
        cannonParts.add(cannon);
        cannonParts.add(head);
        return cannonParts;
    }
}
