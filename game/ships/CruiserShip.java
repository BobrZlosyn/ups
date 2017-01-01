package game.ships;

import game.construction.AShipEquipment;
import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.ships.shipModels.CruisershipModel;
import game.ships.wrecksShips.BattleShipWreck;
import game.ships.wrecksShips.CruiserShipWreck;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.construction.Placement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CruiserShip extends CommonShip{

    private CruisershipModel model;

    public CruiserShip(boolean isEnemy) {
        super(
                GameBalance.CRUISER_SHIP_NAME,
                GameBalance.CRUISER_SHIP_LIFE,
                GameBalance.CRUISER_SHIP_ENERGY,
                GameBalance.CRUISER_SHIP_ARMOR,
                GameBalance.CRUISER_SHIP_SPEED,
                GameBalance.CRUISER_SHIP_SHIELDS,
                GameBalance.CRUISER_SHIP_POINTS,
                isEnemy
        );
        createShip();
        setIsMarked(false);
    }


    private void createShip(){
        model = new CruisershipModel();
        markShape();
    }

    @Override
    public void setShieldConstants() {

        if(isEnemy()){
            setShieldAddX(-100);
            setShieldAddY(10);
            setShieldRadiusX(75);
            setShieldRadiusY(250);
        }else {
            setShieldAddX(100);
            setShieldAddY(10);
            setShieldRadiusX(75);
            setShieldRadiusY(250);
        }

    }

    public void displayShip( Pane gameArea){

        double width = ((GridPane)gameArea.getParent()).getWidth() / 2;
        double height = ((GridPane)gameArea.getParent()).getHeight() / 2 - 20;

        if(isEnemy()){
            positionOfShip(width + width/2, height, gameArea);
        }else {
            positionOfShip(width - width/2, height, gameArea);
        }
    }

    public void positionOfShip(double x, double y, Pane gameArea){
        gameArea.getChildren().add(model.getShip());
        model.setModelXY(x, y);
        createMapOfShip();
    }

    private void createMapOfShip(){
        int size = 50;
        int countOfPlaces = (int)(model.getWidth()-50)/50;
        int countOfPlacesHeight = (int)(model.getShip().getHeight()-30)/50 -1;
        Placement[][] shipMapping = new Placement[countOfPlaces][countOfPlacesHeight];

        for( int i = 0; i < countOfPlaces; i++ ){
            for( int j = 0; j < countOfPlacesHeight; j++) {
                Rectangle place = new Rectangle(size, size);
                place.setFill(Color.WHITE);
                Pane parent = model.getParent();
                parent.getChildren().add(place);

                place.setY(countY(0, size, j));
                place.setX(countX(0, size, i));
                shipMapping[i][j] = new Placement(place.getX(), place.getY(), place.getWidth(), this, i, j);
                shipMapping[i][j].setField(place);
            }
        }
        setPlacements(shipMapping);
    }

    protected double countX(double radius, double size, int i){
        return model.getShip().getX() + size*i + 13*i + 12;
    }

    protected double countY(double radius, double size, int j){
        return model.getShip().getY() + size*j + 10*j + 25;
    }

    @Override
    public double getWidth() {
        return model.getWidth();
    }

    @Override
    public double getHeight() {
        return model.getShip().getHeight();
    }

    @Override
    public CommonWreck getWreck() {
        return new CruiserShipWreck(getCenterX(), getCenterY(), Color.WHITE);
    }

    @Override
    public CommonModel getModel() {
        return model;
    }

    @Override
    public Placement getPlacement() {
        return new Placement(getCenterX(), getCenterY(), model.getWidth(), this, -1, -1);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.CRUISER_SHIP;
    }

    @Override
    public Pane getPane() {
        return getModel().getParent();
    }
}
