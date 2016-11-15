package game.ships;

import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.construction.Placement;
import game.ships.shipModels.AdmiralshipModel;
import game.ships.wrecksShips.BattleShipWreck;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 14.11.2016.
 */
public class AdmiralShip extends CommonShip{

    private AdmiralshipModel model;
    private Timeline hit;

    public AdmiralShip(boolean isEnemy) {
        super(GameBalance.ADMIRAL_SHIP_NAME,
                GameBalance.ADMIRAL_SHIP_LIFE,
                GameBalance.ADMIRAL_SHIP_ENERGY,
                GameBalance.ADMIRAL_SHIP_ARMOR,
                GameBalance.ADMIRAL_SHIP_SPEED,
                GameBalance.ADMIRAL_SHIP_SHIELDS,
                GameBalance.ADMIRAL_SHIP_POINTS,
                isEnemy);
        createShip();
        setIsMarked(false);
        createTimelineHit();
    }

    private void createShip(){
        model = new AdmiralshipModel();

        model.getShip().setOnMouseClicked(event -> {
            markingHandle(isMarked(), this);
        });
    }

    private void createTimelineHit(){
        hit = new Timeline(new KeyFrame(javafx.util.Duration.seconds(GlobalVariables.damageHitDuration), event -> {
            model.getShip().setFill(Color.BLACK);
        }));
        hit.setCycleCount(1);
    }

    @Override
    public void damageHit() {
        model.getShip().setFill(GlobalVariables.damageHit);
        hit.playFromStart();
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.ADMIRAL_SHIP;
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
    public void displayShip(Pane gameArea) {

        double width = ((GridPane)gameArea.getParent()).getWidth() / 2;
        if(isEnemy()){
            positionOfShip(width + width/2, 280, gameArea);
        }else {
            positionOfShip(width - width/2, 280, gameArea);
        }

    }

    @Override
    public void positionOfShip(double x, double y, Pane gameArea) {
        gameArea.getChildren().add(model.getShip());
        model.setModelXY(x, y);
        createMapOfShip();
    }
    public void createMapOfShip(){
        int size = 50;
        int countOfPlaces = 4;
        Placement[][] shipMapping = new Placement[countOfPlaces][5];

        for ( int i = 0; i < countOfPlaces; i++ ){

            for ( int j = 0; j < shipMapping[i].length; j++ ){
                if(i == 0 || i == countOfPlaces - 1) {
                    if( j != shipMapping[i].length - 2 ){
                        continue;
                    }
                }

                Rectangle place = new Rectangle(size, size);
                place.setFill(Color.WHITE);
                Pane parent = model.getParent();
                parent.getChildren().add(place);

                place.setY(countY(model.getHeight()/2, size, j));
                place.setX(countX(model.getWidth()/2 , size, i));
                shipMapping[i][j] = new Placement(place.getX(), place.getY(), place.getWidth(), this, i, j);
                shipMapping[i][j].setField(place);
            }
        }
        setPlacements(shipMapping);
    }

    public Placement getPosition(int row, int column){
        return getPlacementPositions()[row][column];
    }

    @Override
    public Placement getPlacement() {
        return new Placement(getCenterX(), getCenterY(), getWidth(), this, -1, -1);
    }

    private double countX(double radius, double size, int i){
        return model.getCenterX() - radius + size*i + 10*i + 20;
    }

    private double countY(double radius, double size, int j){
        return model.getCenterY() - radius + size*j + 10*j + 35;
    }

    @Override
    public double getWidth() {
        return getModel().getWidth();
    }

    @Override
    public double getHeight() {
        return model.getWidth();
    }

    @Override
    public Pane getPane() {
        return model.getParent();
    }

    @Override
    public CommonModel getModel() {
        return model;
    }

    @Override
    public CommonWreck getWreck() {
        return new BattleShipWreck(getCenterX(),getCenterY(), Color.WHITE);
    }


    @Override
    public void resize(double widthStart, double widthEnd, double heightStart, double heightEnd){

        double centerX = (widthEnd - widthStart)/2 + widthStart;
        double centerY = (heightEnd - heightStart)/2 + heightStart;
        model.setModelXY(centerX, centerY);
        Placement placements [][] = getPlacementPositions();

        for(int i = 0; i < placements.length; i++){
            for(int j = 0; j < placements[i].length; j++){
                Placement placement = getPosition(i,j);
                if(GlobalVariables.isEmpty(placement)){
                    continue;
                }

                placement.resize(countX(model.getWidth()/2, placement.getSize(), i), countY(model.getHeight()/2, placement.getSize(), j));

            }
        }
    }

}