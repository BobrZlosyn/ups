package game.weapons;


import game.GlobalVariables;
import game.IMarkableObject;
import game.Placement;
import game.weapons.modelsWeapon.ModelCannon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CannonWeapon extends CommonWeapon implements IMarkableObject {

    private ModelCannon modelCannon;
    private boolean isMark;

    public CannonWeapon() {
        super("Cannon",100, 100, 25, 35);
        setIsMark(false);
        createCannon();
    }

    public void setIsMark(boolean isMark) {
        this.isMark = isMark;
    }

    public boolean isMark() {
        return isMark;
    }

    private void createCannon() {
        modelCannon = new ModelCannon();
        modelCannon.getParts().forEach(shape -> {
            markCannon(shape);
        });

    }

    private void markCannon(Shape shape){
            shape.setOnMouseClicked(event -> {
                if(GlobalVariables.isTargeting){

                    target();
                    return;
                }

                if(!isMark()){
                    markObject();
                }else{
                    unmarkObject();
                }
            });
    }

    @Override
    public void displayWeapon(Placement position, boolean isEnemy) {
        double x = position.getX();
        double y = position.getY();
        double width = position.getSize();
        setIsEnemy(isEnemy);
        if(!position.isEmpty()){
            return;
        }

        modelCannon.getRoom().setCenterX(x + width/2);
        modelCannon.getRoom().setCenterY(y + width/2);

        modelCannon.getHead().setCenterX(x + width/2);
        modelCannon.getHead().setCenterY(y + width/2);

        if(isEnemy()){
            modelCannon.getCannon().setX(x - 5);
        }else {
            modelCannon.getCannon().setX(x + width/2);
        }

        modelCannon.getCannon().setY(y + width/2 - modelCannon.getCannon().getHeight()/2);
        Pane gameArea = (Pane) position.getField().getParent();
        gameArea.getChildren().addAll(modelCannon.getParts());
        position.setIsEmpty(false);
    }

    @Override
    public void markObject() {
        modelCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.BLUE);
            shape.setStrokeWidth(1.5);
        });

        setIsMark(true);

        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());
        GlobalVariables.setCanTarget(!isEnemy());
    }

    @Override
    public void unmarkObject() {
        modelCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });

        setIsMark(false);
        GlobalVariables.setMarkedObject(null);
        GlobalVariables.setName("");
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void target() {
        modelCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.RED);
            shape.setStrokeWidth(1.5);
        });
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        modelCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });
    }

    @Override
    public Placement getPlacement() {
        return new Placement(modelCannon.getRoom().getCenterX(), modelCannon.getRoom().getCenterY(), modelCannon.getRoom().getRadius());
    }

    @Override
    public void rotateWeapon(double x, double y) {
        double centerX = modelCannon.getRoom().getCenterX();
        double countCx = (x - centerX)*(x - centerX);
        double countAx = (x - centerX)*(x - centerX);
        double countAy = (y - centerX)*(y - centerX);
        double sideC = Math.sqrt(countCx);
        double sideA = Math.sqrt(countAx + countAy);

        double cosinus = Math.toDegrees(Math.acos(sideC / sideA));

        if(isEnemy()){
            modelCannon.getCannon().getTransforms().add(new Rotate(cosinus, centerX, modelCannon.getRoom().getCenterY()));
        }else{
            modelCannon.getCannon().getTransforms().add(new Rotate(-cosinus, centerX, modelCannon.getRoom().getCenterY()));
        }

    }
}
