package game.ships;

import game.weapons.CommonWeapon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CruiserShip extends CommonShip {

    private Rectangle ship;
    private Rectangle [][] shipMapping;
    public CruiserShip() {
        super(150, 100);
        createShip();

    }

    private void createShip(){
        ship = new Rectangle(200, 400);
        ship.setStyle("-fx-background-color: red;");

    }

    public void displayShip(boolean isEnemy, Pane gameArea){

        gameArea.getChildren().add(ship);

        if(isEnemy){
            ship.setX(500);
            ship.setY(80);
        }else{
            ship.setX(100);
            ship.setY(80);
        }
        createMapOfShip();
    }

    public void createMapOfShip(){
        int size = 50;
        int countOfPlaces = (int)(ship.getWidth()-50)/50;
        int countOfPlacesHeight = (int)(ship.getHeight()-30)/50;
        shipMapping = new Rectangle[countOfPlaces][countOfPlacesHeight];

        for( int i = 0; i < countOfPlaces; i++ ){
            for( int j = 0; j < countOfPlacesHeight -1; j++) {
                Rectangle place = new Rectangle(size, size);
                place.setFill(Color.WHITE);
                Pane parent = ((Pane)ship.getParent());
                parent.getChildren().add(place);

                place.setY(ship.getY() + size*j + 10*j + 25);
                place.setX(ship.getX() + size*i + 10*i + 15);
                shipMapping[i][j] = place;
            }
        }
    }

    public Rectangle getPosition(int row, int column){
        return shipMapping[row][column];
    }


}
