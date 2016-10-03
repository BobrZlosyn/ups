package game.ships;

import game.ConstructionTypes;
import game.GlobalVariables;
import game.construction.IMarkableObject;
import game.construction.Placement;
import game.weapons.CommonWeapon;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 26.09.2016.
 */
public class BattleShip extends CommonShip{
    private Circle ship;
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

    @Override
    public void setShieldConstants() {
        if(isEnemy()){
            setShieldAddX(-130);
            setShieldAddY(0);
            setShieldRadiusX(50);
            setShieldRadiusY(180);
        }else{
            setShieldAddX(130);
            setShieldAddY(0);
            setShieldRadiusX(50);
            setShieldRadiusY(180);
        }

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
    }

    @Override
    public void cancelTarget() {

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
    public Placement getPlacement() {
        return new Placement(ship.getCenterX(), ship.getCenterY(), ship.getRadius(), this, -1, -1);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.BATTLE_SHIP;
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
