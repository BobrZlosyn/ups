package game.construction;


import game.static_classes.GlobalVariables;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 10.10.2016.
 */
public abstract class CommonWreck extends WrecksModels {

    private Timeline flashAnimation;
    private Circle flashCircle;
    private boolean wrecksAdd, rotation;
    private int xIndex, yIndex;
    private int countOfMovements, maxCountOfMovements;
    protected ArrayList<Shape> wrecks;
    protected double moveAddPoint [][];
    private int countOfShakes, maxCountOfShakes, marginOfShake;
    protected boolean isFromEnemy;


    public CommonWreck(int maxCountOfMovements, boolean allowRotation) {
        this.maxCountOfMovements = maxCountOfMovements;
        init();
        rotation = allowRotation;
    }

    public CommonWreck(double x, double y, Color color, int maxCountOfMovements, boolean allowRotation){

        this.maxCountOfMovements = maxCountOfMovements;
        flashCircle = new Circle(1, color);
        flashCircle.setCenterX(x);
        flashCircle.setCenterY(y);
        rotation = allowRotation;
        init();
    }

    private void init(){
        wrecksAdd = false;
        wrecks = new ArrayList<>();
        xIndex = 0;
        yIndex = 1;
        countOfMovements = 0;
        countOfShakes = 0;
        maxCountOfShakes = 10;
        marginOfShake = 5;
        isFromEnemy = false;
    }

    public Circle getFlashCircle() {
        return flashCircle;
    }

    public ArrayList<Shape> getWrecks() {
        return wrecks;
    }

    public boolean wrecksMovement(){

        for ( int i = 0; i < wrecks.size(); i++){
            wrecks.get(i).setLayoutX(wrecks.get(i).getLayoutX() + moveAddPoint[i][xIndex]);
            wrecks.get(i).setLayoutY(wrecks.get(i).getLayoutY() + moveAddPoint[i][yIndex]);
            wrecks.get(i).setOpacity(wrecks.get(i).getOpacity() - 1.3/maxCountOfMovements);

            if(rotation){
                wrecks.get(i).getTransforms().add(new Rotate(1));
            }
        }
        countOfMovements ++;

        return (countOfMovements == maxCountOfMovements);

    }

    public void explosion(double x, double y, double radiusMax, double addToRadius, CommonModel model){
        if (GlobalVariables.isNotEmpty(flashAnimation)) {
            return;
        }

        flashAnimation = new Timeline(new KeyFrame(Duration.seconds(0.02), event -> {
            double radius = flashCircle.getRadius() + addToRadius;
            shakeOfShip();
            if(radius > radiusMax || wrecksAdd){
                if(!wrecksAdd){

                    Pane pane = (Pane) flashCircle.getParent();
                    pane.getChildren().removeAll(model.getParts());
                    addWrecksToPane(pane, x, y);
                    pane.getChildren().remove(flashCircle);
                    pane.getChildren().add(flashCircle);
                    wrecksAdd = true;
                }

                double opacity = flashCircle.getOpacity() - addToRadius/radiusMax;
                flashCircle.setRadius(flashCircle.getRadius() - addToRadius);
                flashCircle.setOpacity(opacity);

                if(opacity <= 0 ){
                    Pane pane = (Pane) flashCircle.getParent();
                    pane.getChildren().remove(flashCircle);
                    flashAnimation.stop();
                    flashAnimation = null;
                }
            }else{
                flashCircle.setRadius(radius);
            }
        }));

        flashAnimation.setCycleCount(Animation.INDEFINITE);
        flashAnimation.playFromStart();
    }

    protected void setMovementPoint(int indexOfWreck, double x, double y){
        moveAddPoint[indexOfWreck][xIndex] = x;
        moveAddPoint[indexOfWreck][yIndex] = y;
    }

    public void removeWrecks() {
        Pane pane = (Pane)wrecks.get(0).getParent();
        pane.getChildren().removeAll(wrecks);
    }

    private void shakeOfShip(){
        if(countOfShakes <= maxCountOfShakes){
            if(countOfShakes%2 == 0){
                GridPane.setMargin(flashCircle.getParent(), new Insets(0,0,0,marginOfShake));
            }else {
                GridPane.setMargin(flashCircle.getParent(), new Insets(0,marginOfShake,0, 0));
            }
            countOfShakes++;
        }else{
            GridPane.setMargin(flashCircle.getParent(), new Insets(0,0,0,0));
        }
    }

    public void setIsFromEnemy(boolean isFromEnemy) {
        this.isFromEnemy = isFromEnemy;
    }

    public boolean isFromEnemy() {
        return isFromEnemy;
    }

    protected abstract void createWrecks();

    protected abstract void createMovementPoints();

    public abstract void addWrecksToPane(Pane gameArea, double x, double y);

    protected ArrayList <double []> getModelsOfWreck(){
        ArrayList <double []> wrecks = new ArrayList<>();
        wrecks.add(wreckModel1());
        wrecks.add(wreckModel2());
        return wrecks;
    }


}
