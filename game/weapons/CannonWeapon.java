package game.weapons;


import game.Placement;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CannonWeapon extends CommonWeapon{

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
                room.setStyle("-fx-border-insets: 5; -fx-border-width: 3; -fx-border-color: blue;");
                if(!isMark()){
                    room.setStroke(Color.BLUE);
                    room.setStrokeWidth(1.5);
                    head.setStroke(Color.BLUE);
                    head.setStrokeWidth(1.5);
                    cannon.setStroke(Color.BLUE);
                    cannon.setStrokeWidth(1.5);
                    setIsMark(true);
                }else{
                    room.setStroke(Color.TRANSPARENT);
                    cannon.setStroke(Color.TRANSPARENT);
                    head.setStroke(Color.TRANSPARENT);
                    setIsMark(false);
                }
            });
    }

    @Override
    public void displayWeapon(Placement position, boolean isEnemy) {
        double x = position.getX();
        double y = position.getY();
        double width = position.getSize();

        if(!position.isEmpty()){
            return;
        }

        room.setCenterX(x + width/2);
        room.setCenterY(y + width/2);

        head.setCenterX(x + width/2);
        head.setCenterY(y + width/2);

        if(isEnemy){
            cannon.setX(x - 5);
        }else {
            cannon.setX(x + width/2 + 5);
        }

        cannon.setY(y + width/2 - cannon.getHeight()/2);
       // cannon.getTransforms().add(new Rotate(-10, cannon.getX()+20, cannon.getY()));

        Pane gameArea = (Pane) position.getField().getParent();
        gameArea.getChildren().add(room);
        gameArea.getChildren().add(cannon);
        gameArea.getChildren().add(head);
        position.setIsEmpty(false);
    }
}
