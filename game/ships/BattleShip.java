package game.ships;

import game.GlobalVariables;
import game.IMarkableObject;
import game.Placement;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class BattleShip extends CommonShip implements IMarkableObject {
    private Circle ship;
    private Placement[][] shipMapping;
    private boolean isMarked;

    public BattleShip (boolean isEnemy){
        super("Battle ship", 100, 100, isEnemy);
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
                unmarkObject();
            } else {
                markObject();
            }
        });

    }

    public void displayShip(Pane gameArea){
        gameArea.getChildren().add(ship);
        if(isEnemy()){
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
        shipMapping = new Placement[countOfPlaces][4];

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
                shipMapping[i][j] = new Placement(place.getX(), place.getY(), place.getWidth());
                shipMapping[i][j].setField(place);
            }

        }
    }

    public Placement getPosition(int row, int column){
        return shipMapping[row][column];
    }
    @Override
    public void markObject() {
        ship.setStroke(Color.BLUE);
        ship.setStrokeWidth(1.5);
        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());
        setIsMarked(true);
    }

    @Override
    public void unmarkObject() {
        ship.setStroke(Color.TRANSPARENT);
        GlobalVariables.setMarkedObject(null);
        GlobalVariables.setName("");
        setIsMarked(false);

    }

    @Override
    public void target() {

    }

    @Override
    public void cancelTarget() {

    }

    @Override
    public Placement getPlacement() {
        return new Placement(ship.getCenterX(), ship.getCenterY(), ship.getRadius());
    }
}
