package game.wrecks;

import game.static_classes.Wrecks_Handler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 15.10.2016.
 */
public class SimpleShieldWrecks extends CommonWreck{
    private ArrayList<Polygon> wrecks;
    private double moveAddPoint [][];
    private int countOfMovements;
    private int xIndex, yIndex;

    public SimpleShieldWrecks(){
        wrecks = new ArrayList<>();
        countOfMovements = 0;
        createWrecks();
        createMovementPoints();
    }

    private void createMovementPoints(){
        moveAddPoint = new double [wrecks.size()][2];
        xIndex = 0;
        yIndex = 1;

        setMovementPoint(0, -2, -2);
        setMovementPoint(1, 2, -1);
        setMovementPoint(2, 1, -2);

        setMovementPoint(3, -1, -1);
        setMovementPoint(4, 0, 1);
        setMovementPoint(5, 2, -2);

        setMovementPoint(6, -1, 1);
        setMovementPoint(7, 3, 2);
        setMovementPoint(8, 3, 1);
    }

    private void setMovementPoint(int indexOfWreck, double x, double y){
        moveAddPoint[indexOfWreck][xIndex] = x;
        moveAddPoint[indexOfWreck][yIndex] = y;
    }

    private void createWrecks(){

        double points [] = {
                0.0, 0.0,
                10.0, -5.0,
                15.0, 6.0,
                11.0, 12.0,
                3.0, 14.0
        };


        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));

        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));

        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
    }

    private Polygon createWreckPolygon(double [] points){
        Polygon wreck = new Polygon(points);
        wreck.setFill(Color.ORANGE);
        return wreck;
    }

    public ArrayList<Polygon> getWrecks() {
        return wrecks;
    }

    public void addWrecksToPane(Pane gameArea, double x, double y){

        for (int i = 0, j = 0, k = 0; i < wrecks.size();  i++, k++){
            if(i%3==0 && i != 0){
                j ++;
                k = 0;
            }

            Polygon wreck = wrecks.get(i);
            wreck.setLayoutX(x + k*15 + 2);
            wreck.setLayoutY(y + j*15 + 5);
        }

        gameArea.getChildren().addAll(wrecks);
        Wrecks_Handler.addWrecks(this);
    }

    public boolean wrecksMovement(){

        for ( int i = 0; i < wrecks.size(); i++){
            wrecks.get(i).setLayoutX(wrecks.get(i).getLayoutX() + moveAddPoint[i][xIndex]);
            wrecks.get(i).setLayoutY(wrecks.get(i).getLayoutY() + moveAddPoint[i][yIndex]);
            wrecks.get(i).setOpacity(wrecks.get(i).getOpacity() - 0.01);
        }
        countOfMovements ++;

        return (countOfMovements == 100);

    }

    public void removeWrecks(){
        Pane pane = (Pane)wrecks.get(0).getParent();
        pane.getChildren().removeAll(wrecks);
    }
}
