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
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CruiserShip extends CommonShip{

    private Rectangle ship;
    private Timeline hit;

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
        createTimelineHit();
    }


    private void createShip(){
        ship = new Rectangle(200, 400);
        ship.setStyle("-fx-background-color: red;");
        ship.setOnMouseClicked(event -> {
            markingHandle(isMarked(), this);
        });
    }

    @Override
    public void setShieldConstants() {

        if(isEnemy()){
            setShieldAddX(-200);
            setShieldAddY(100);
            setShieldRadiusX(75);
            setShieldRadiusY(250);
        }else {
            setShieldAddX(0);
            setShieldAddY(100);
            setShieldRadiusX(75);
            setShieldRadiusY(250);
        }

    }

    public void displayShip( Pane gameArea){

        double width = ((GridPane)gameArea.getParent()).getWidth() / 2;
        if(isEnemy()){
            positionOfShip(width + width/2 - ship.getWidth()/2, 80, gameArea);
        }else {
            positionOfShip(width - width/2 - ship.getWidth()/2, 80, gameArea);
        }
    }

    public void positionOfShip(double x, double y, Pane gameArea){
        gameArea.getChildren().add(ship);
        ship.setX(x);
        ship.setY(y);
        createMapOfShip();
    }

    private void createMapOfShip(){
        int size = 50;
        int countOfPlaces = (int)(ship.getWidth()-50)/50;
        int countOfPlacesHeight = (int)(ship.getHeight()-30)/50 -1;
        Placement[][] shipMapping = new Placement[countOfPlaces][countOfPlacesHeight];

        for( int i = 0; i < countOfPlaces; i++ ){
            for( int j = 0; j < countOfPlacesHeight; j++) {
                Rectangle place = new Rectangle(size, size);
                place.setFill(Color.WHITE);
                Pane parent = ((Pane)ship.getParent());
                parent.getChildren().add(place);

                place.setY(countY(size, j));
                place.setX(countX(size, i));
                shipMapping[i][j] = new Placement(place.getX(), place.getY(), place.getWidth(), this, i, j);
                shipMapping[i][j].setField(place);
            }
        }
        setPlacements(shipMapping);
    }

    private double countX(double size, int i){
        return ship.getX() + size*i + 10*i + 15;
    }

    private double countY(double size, int j){
        return ship.getY() + size*j + 10*j + 25;
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
    public double getWidth() {
        return ship.getWidth();
    }

    @Override
    public double getHeight() {
        return ship.getHeight();
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

        double centerX = (widthEnd - widthStart)/2 + widthStart - ship.getWidth()/2;
        double centerY = (heightEnd - heightStart)/2 + heightStart - ship.getHeight()/2;
        ship.setX(centerX);
        ship.setY(centerY);
        Placement placements [][] = getPlacementPositions();

        for(int i = 0; i < placements.length; i++){
            for(int j = 0; j < placements[i].length; j++){
                Placement placement = getPosition(i,j);
                if(GlobalVariables.isEmpty(placement)){
                    continue;
                }

                placement.resize(countX(placement.getSize(), i), countY(placement.getSize(), j));

            }
        }
    }

    @Override
    public boolean containsPosition(double x, double y){
        return ship.contains(x,y);
    }

    @Override
    public double getCenterX(){
        double middleX = ship.getX()+ ship.getHeight()/2;
        return middleX ;
    }

    @Override
    public double getCenterY(){
        double middleY = ship.getY()+ ship.getWidth()/2;
        return middleY;
    }

    @Override
    public Placement getPlacement() {
        return new Placement(getCenterX(), getCenterY(), ship.getWidth(), this, -1, -1);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.CRUISER_SHIP;
    }
}
