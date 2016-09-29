package game.weapons;


import game.GlobalVariables;
import game.IMarkableObject;
import game.Placement;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CannonWeapon extends CommonWeapon implements IMarkableObject {

    private Circle room, head;
    private Rectangle cannon;
    private boolean isMark;

    public CannonWeapon() {
        super("Cannon",100, 100, 25, 35);
        setIsMark(false);
        createCannon();
    }

    public void setIsMark(boolean isMark) {
        this.isMark = isMark;
    }

    public boolean isMark() {
        return isMark;
    }

    private void createCannon() {
        room = new Circle(25);
        room.setFill(Color.ORANGE);

        head = new Circle(15);
        head.setFill(Color.YELLOW);

        cannon = new Rectangle(30, 10);
        cannon.setFill(Color.RED);
        markCannon(room);
        markCannon(head);
        markCannon(cannon);
    }

    public Circle getRoom() {
        return room;
    }

    public Rectangle getCannon() {
        return cannon;
    }

    private void markCannon(Shape shape){
            shape.setOnMouseClicked(event -> {
                if(GlobalVariables.isTargeting){

                    target();
                    return;
                }

                if(!isMark()){
                    markObject();
                }else{
                    unmarkObject();
                }
            });
    }

    @Override
    public void displayWeapon(Placement position, boolean isEnemy) {
        double x = position.getX();
        double y = position.getY();
        double width = position.getSize();
        setIsEnemy(isEnemy);
        if(!position.isEmpty()){
            return;
        }

        room.setCenterX(x + width/2);
        room.setCenterY(y + width/2);

        head.setCenterX(x + width/2);
        head.setCenterY(y + width/2);

        if(isEnemy()){
            cannon.setX(x - 5);
        }else {
            cannon.setX(x + width/2);
        }

        cannon.setY(y + width/2 - cannon.getHeight()/2);
        Pane gameArea = (Pane) position.getField().getParent();
        gameArea.getChildren().add(room);
        gameArea.getChildren().add(cannon);
        gameArea.getChildren().add(head);
        position.setIsEmpty(false);
    }

    @Override
    public void markObject() {
        room.setStroke(Color.BLUE);
        room.setStrokeWidth(1.5);
        head.setStroke(Color.BLUE);
        head.setStrokeWidth(1.5);
        cannon.setStroke(Color.BLUE);
        cannon.setStrokeWidth(1.5);
        setIsMark(true);

        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());
        GlobalVariables.setCanTarget(!isEnemy());
    }

    @Override
    public void unmarkObject() {
        room.setStroke(Color.TRANSPARENT);
        cannon.setStroke(Color.TRANSPARENT);
        head.setStroke(Color.TRANSPARENT);
        setIsMark(false);
        GlobalVariables.setMarkedObject(null);
        GlobalVariables.setName("");
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void target() {
        room.setStroke(Color.RED);
        room.setStrokeWidth(1.5);
        head.setStroke(Color.RED);
        head.setStrokeWidth(1.5);
        cannon.setStroke(Color.RED);
        cannon.setStrokeWidth(1.5);
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        room.setStroke(Color.TRANSPARENT);
        cannon.setStroke(Color.TRANSPARENT);
        head.setStroke(Color.TRANSPARENT);
    }

    @Override
    public Placement getPlacement() {
        return new Placement(room.getCenterX(), room.getCenterY(), room.getRadius());
    }

    @Override
    public void rotateWeapon(double x, double y) {
        double countCx = (x - room.getCenterX()) *(x - room.getCenterX());
        double countAx = (x - room.getCenterX())*(x - room.getCenterX());
        double countAy = (y - room.getCenterY())*(y - room.getCenterY());
        double sideC = Math.sqrt(countCx);
        double sideA = Math.sqrt(countAx + countAy);

        double cosinus = Math.toDegrees(Math.acos(sideC / sideA));

        if(isEnemy()){
            cannon.getTransforms().add(new Rotate(cosinus, room.getCenterX(), room.getCenterY()));
        }else{
            cannon.getTransforms().add(new Rotate(-cosinus, room.getCenterX(), room.getCenterY()));
        }

    }

    public ArrayList<Shape> getConstructsToDrag(){
        ArrayList<Shape> draggableObject = new ArrayList<>();
        draggableObject.add(room);
        draggableObject.add(cannon);
        draggableObject.add(head);

        return  draggableObject;
    }
}
