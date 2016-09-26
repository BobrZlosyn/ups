package game.ships;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class BattleShip extends CommonShip {
    private Circle ship;
    private ProgressBar life;

    public BattleShip (){
        super(100, 100);
        createShip();
    }

    private void createShip(){
        ship = new Circle(100);
        ship.setStyle("-fx-background-color: red;");

        life = new ProgressBar(1);
        ship.setOnMouseClicked(event -> {
            Pane parent = ((Pane)ship.getParent());
            if(parent.getChildren().contains(life)){
                parent.getChildren().remove(life);
                return;
            }


            parent.getChildren().add(life);
            double x = ship.getCenterX();
            double y = ship.getCenterY();
            double radius = ship.getRadius();

            life.setLayoutX(x - radius/2);
            life.setLayoutY(y + radius + 10);
        });

    }

    public void displayShip(boolean isEnemy, Pane gameArea){
        gameArea.getChildren().add(ship);
        if(isEnemy){
            ship.setCenterX(450);
            ship.setCenterY(280);
        }else {
            ship.setCenterX(200);
            ship.setCenterY(280);
        }
    }
}
