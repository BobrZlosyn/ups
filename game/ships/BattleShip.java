package game.ships;

import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.construction.Placement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.time.Duration;

/**
 * Created by Kanto on 26.09.2016.
 */
public class BattleShip extends CommonShip{
    private Circle ship;
    private Timeline hit;

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
        createTimelineHit();
    }

    private void createShip(){
        ship = new Circle(150);
        ship.setStyle("-fx-background-color: red;");


        ship.setOnMouseClicked(event -> {
            markingHandle(isMarked(), this);
        });

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

    public void displayShip(Pane gameArea){

        double width = ((GridPane)gameArea.getParent()).getWidth() / 2;
        if(isEnemy()){
            positionOfShip(width + width/2, 280, gameArea);
        }else {
            positionOfShip(width - width/2, 280, gameArea);
        }

    }

    public void positionOfShip(double x, double y, Pane gameArea){
        gameArea.getChildren().add(ship);
        ship.setCenterX(x);
        ship.setCenterY(y);
        createMapOfShip();
    }

    public void createMapOfShip(){
        int size = 50;
        int countOfPlaces = (int)(ship.getRadius()*2 - 50 )/50 -1;
        Placement[][] shipMapping = new Placement[countOfPlaces][4];

        double radius = ship.getRadius();
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
                Pane parent = ((Pane)ship.getParent());
                parent.getChildren().add(place);

                place.setY(countY(radius, size, j));
                place.setX(countX(radius, size, i));
                shipMapping[i][j] = new Placement(place.getX(), place.getY(), place.getWidth(), this, i, j);
                shipMapping[i][j].setField(place);
            }

        }
        setPlacements(shipMapping);
    }

    private double countX(double radius, double size, int i){
        return ship.getCenterX() - radius + size*i + 15*i + 30;
    }

    private double countY(double radius, double size, int j){
        return ship.getCenterY() - radius + size*j + 10*j + 35;
    }

    public Placement getPosition(int row, int column){
        return getPlacementPositions()[row][column];
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
        if(!isEnemy()){
            return;
        }

        ship.setStroke(Color.RED);
        ship.setStrokeWidth(1.5);
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        ship.setStroke(Color.TRANSPARENT);
    }

    @Override
    public double getX() {
        return ship.getCenterX();
    }

    @Override
    public double getY() {
        return ship.getCenterY();
    }

    @Override
    public double getWidth() {
        return ship.getRadius()*2;
    }

    @Override
    public double getHeight() {
        return getWidth();
    }

    @Override
    public Placement getPlacement() {
        return new Placement(ship.getCenterX(), ship.getCenterY(), ship.getRadius(), this, -1, -1);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.BATTLE_SHIP;
    }

    @Override
    public void destroy() {

    }

    private void createTimelineHit(){
        hit = new Timeline(new KeyFrame(javafx.util.Duration.seconds(GlobalVariables.damageHitDuration),event -> {
            ship.setFill(Color.BLACK);
        }));
        hit.setCycleCount(1);
    }

    @Override
    public void damageHit() {
        ship.setFill(GlobalVariables.damageHit);
        hit.playFromStart();
    }

    @Override
    public void resize(double widthStart, double widthEnd, double heightStart, double heightEnd){

        double centerX = (widthEnd - widthStart)/2 + widthStart;
        double centerY = (heightEnd - heightStart)/2 + heightStart;
        ship.setCenterX(centerX);
        ship.setCenterY(centerY);
        Placement placements [][] = getPlacementPositions();

        for(int i = 0; i < placements.length; i++){
            for(int j = 0; j < placements[i].length; j++){
                Placement placement = getPosition(i,j);
                if(GlobalVariables.isEmpty(placement)){
                    continue;
                }

                placement.resize(countX(ship.getRadius(), placement.getSize(), i), countY(ship.getRadius(), placement.getSize(), j));

            }
        }
    }

    @Override
    public boolean containsPosition(double x, double y){
        return ship.contains(x,y);
    }

    @Override
    public double getCenterX(){
        return ship.getCenterX();
    }

    @Override
    public double getCenterY(){
        return ship.getCenterY();
    }
}
