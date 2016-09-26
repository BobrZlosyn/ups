package game.ships;

import game.Placement;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class BattleShip extends CommonShip {
    private Circle ship;
    private Placement[][] shipMapping;
    private boolean isMarked;

    public BattleShip (){
        super("Battle ship", 100, 100);
        createShip();
        setIsMarked(false);
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setIsMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

    private void createShip(){
        ship = new Circle(150);
        ship.setStyle("-fx-background-color: red;");


        ship.setOnMouseClicked(event -> {
            if(isMarked()){
                ship.setStroke(Color.TRANSPARENT);
                setIsMarked(false);
            } else {
                ship.setStroke(Color.BLUE);
                ship.setStrokeWidth(1.5);
                setIsMarked(true);
            }

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
        createMapOfShip();
    }

    public void createMapOfShip(){
        int size = 50;
        int countOfPlaces = (int)(ship.getRadius()*2 - 50 )/50;
        /*int countOfPlacesHeight = (int)(ship.getHeight()-30)/50;
        shipMapping = new Placement[countOfPlaces][countOfPlacesHeight];
*/
        double radius = ship.getRadius();
        for ( int i = 0; i < countOfPlaces - 1; i++ ){

            for ( int j = 0; j < 4; j++ ){
                if((j == 0 && i == 0) || (j == 0 && i == countOfPlaces - 2)){
                    continue;
                }

                if((j == 3 && i == 0) || (j == 3 && i == countOfPlaces - 2)){
                    continue;
                }
                Rectangle place = new Rectangle(size, size);
                place.setFill(Color.WHITE);
                Pane parent = ((Pane)ship.getParent());
                parent.getChildren().add(place);

                place.setY(ship.getCenterY() - radius + size*j + 10*j + 35);
                place.setX(ship.getCenterX() - radius + size*i + 15*i + 30);
            }

        }

        /*for( int i = 0; i < countOfPlaces; i++ ){
            for( int j = 0; j < countOfPlacesHeight -1; j++) {
                Rectangle place = new Rectangle(size, size);
                place.setFill(Color.WHITE);
                Pane parent = ((Pane)ship.getParent());
                parent.getChildren().add(place);

                place.setY(ship.getY() + size*j + 10*j + 25);
                place.setX(ship.getX() + size*i + 10*i + 15);
                shipMapping[i][j] = new Placement(place.getX(), place.getY(), place.getWidth());
                shipMapping[i][j].setField(place);
            }
        }*/
    }
}
