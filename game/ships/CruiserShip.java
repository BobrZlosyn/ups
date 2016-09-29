package game.ships;

import game.GlobalVariables;
import game.IMarkableObject;
import game.Placement;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CruiserShip extends CommonShip implements IMarkableObject {

    private Rectangle ship;
    private Placement[][] shipMapping;
    private boolean IsMarked;

    public CruiserShip(boolean isEnemy) {
        super("Cruiser ship", 150, 100, isEnemy, CRUISER_SHIP);
        createShip();
        setIsMarked(false);
    }

    public void setIsMarked(boolean isMarked) {
        IsMarked = isMarked;
    }

    public boolean isMarked() {
        return IsMarked;
    }

    private void createShip(){
        ship = new Rectangle(200, 400);
        ship.setStyle("-fx-background-color: red;");
        ship.setOnMouseClicked(event -> {
            if(isMarked()){
                unmarkObject();
            }else {
                markObject();
            }
        });

    }

    public void displayShip( Pane gameArea){

        if(isEnemy()){
            positionOfShip(500,80, gameArea);
        }else{
            positionOfShip(100, 80, gameArea);
        }
    }

    public void positionOfShip(double x, double y, Pane gameArea){
        gameArea.getChildren().add(ship);
        ship.setX(x);
        ship.setY(y);
        createMapOfShip();
    }

    public void createMapOfShip(){
        int size = 50;
        int countOfPlaces = (int)(ship.getWidth()-50)/50;
        int countOfPlacesHeight = (int)(ship.getHeight()-30)/50;
        shipMapping = new Placement[countOfPlaces][countOfPlacesHeight];

        for( int i = 0; i < countOfPlaces; i++ ){
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
        double middleX = ship.getX()+ ship.getHeight()/2;
        double middleY = ship.getY()+ ship.getWidth()/2;
        return new Placement(middleX, middleY, ship.getWidth());
    }

    @Override
    public double getWidth() {
        return ship.getWidth();
    }
}
