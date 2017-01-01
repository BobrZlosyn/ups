package game.ships;

import game.construction.*;
import game.ships.shipModels.BattleshipModel;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.ships.wrecksShips.BattleShipWreck;
import game.weapons.SimpleLaserWeapon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by Kanto on 26.09.2016.
 */
public class BattleShip extends CommonShip{
    private BattleshipModel model;

    public BattleShip (boolean isEnemy){
        super(
                GameBalance.BATTLE_SHIP_NAME,
                GameBalance.BATTLE_SHIP_LIFE,
                GameBalance.BATTLE_SHIP_ENERGY,
                GameBalance.BATTLE_SHIP_ARMOR,
                GameBalance.BATTLE_SHIP_SPEED,
                GameBalance.BATTLE_SHIP_SHIELDS,
                GameBalance.BATTLE_SHIP_POINTS,
                isEnemy
        );
        createShip();
        setIsMarked(false);
    }

    private void createShip(){
        model = new BattleshipModel();
        markShape();
    }

    @Override
    public void setShieldConstants() {
        if(isEnemy()){
            setShieldAddX(-150);
            setShieldAddY(0);
        }else{
            setShieldAddX(150);
            setShieldAddY(0);
        }

        setShieldRadiusX(38);
        setShieldRadiusY(180);

    }

    @Override
    public void displayShip(Pane gameArea){
        double width = ((GridPane)gameArea.getParent()).getWidth() / 2;
        if(isEnemy()){
            positionOfShip(width + width/2, 280, gameArea);
        }else {
            positionOfShip(width - width/2, 280, gameArea);
        }

    }

    @Override
    public void positionOfShip(double x, double y, Pane gameArea){
        gameArea.getChildren().add(model.getShip());
        model.setModelXY(x, y);
        createMapOfShip();
    }

    public void createMapOfShip(){
        int size = 50;
        int countOfPlaces = (int)(getModel().getWidth() - 50 )/50 -1;
        Placement[][] shipMapping = new Placement[countOfPlaces][4];

        double radius = model.getShip().getRadius();
        for ( int i = 0; i < countOfPlaces; i++ ){

            for ( int j = 0; j < 4; j++ ){
                if((j == 0 && i == 0) || (j == 0 && i == countOfPlaces - 1)){
                    continue;
                }

                if((j == 3 && i == 0) || (j == 3 && i == countOfPlaces - 1)){
                    continue;
                }

                Rectangle place = new Rectangle(size, size);
                place.setFill(Color.WHITE);
                Pane parent = model.getParent();
                parent.getChildren().add(place);

                place.setY(countY(radius, size, j));
                place.setX(countX(radius, size, i));
                shipMapping[i][j] = new Placement(place.getX(), place.getY(), place.getWidth(), this, i, j);
                shipMapping[i][j].setField(place);
            }

        }
        setPlacements(shipMapping);
    }

    protected double countX(double radius, double size, int i){
        return getCenterX() - radius + size*i + 13*i + 30;
    }

    protected double countY(double radius, double size, int j){
        return getCenterY() - radius + size*j + 10*j + 35;
    }


    @Override
    public double getX() {
        return model.getShip().getCenterX();
    }

    @Override
    public double getY() {
        return model.getShip().getCenterY();
    }

    @Override
    public double getWidth() {
        return model.getWidth();
    }

    @Override
    public double getHeight() {
        return getWidth();
    }

    @Override
    public Pane getPane() {
        return model.getParent();
    }

    @Override
    public Placement getPlacement() {
        return new Placement(model.getShip().getCenterX(), model.getShip().getCenterY(), model.getShip().getRadius(), this, -1, -1);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.BATTLE_SHIP;
    }

    @Override
    public CommonWreck getWreck() {
        return new BattleShipWreck(model.getShip().getCenterX(),model.getShip().getCenterY(), Color.WHITE);
    }

    @Override
    public CommonModel getModel() {
        return model;
    }

}
