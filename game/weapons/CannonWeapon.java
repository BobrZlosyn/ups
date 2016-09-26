package game.weapons;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CannonWeapon extends CommonWeapon{

    private Circle room;
    private Rectangle cannon;

    public CannonWeapon() {
        super(100, 100, 25, 35);
    }

    private void createCannon() {
        room = new Circle(25);
        room.setFill(Color.ORANGE);

        cannon = new Rectangle(10, 50);
        cannon.setFill(Color.RED);
    }

    public Circle getRoom() {
        return room;
    }

    public Rectangle getCannon() {
        return cannon;
    }

    @Override
    public void displayWeapon(Rectangle position, boolean isEnemy) {
        double x = position.getX();
        double y = position.getY();
        double width = position.getWidth();

        room.setCenterX(x + width/2);
        room.setCenterY(y + width/2);

        if(isEnemy){
            cannon.setX(x - width/2);
            cannon.setY(y - width/2);
        }else {

        }
    }
}
