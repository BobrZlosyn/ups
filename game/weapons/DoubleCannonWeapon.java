package game.weapons;

import game.GlobalVariables;
import game.construction.CommonConstruction;
import game.construction.IMarkableObject;
import game.construction.Placement;
import game.shots.CommonShot;
import game.shots.DoubleBallShot;
import game.weapons.modelsWeapon.ModelDoubleCannon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Created by Kanto on 30.09.2016.
 */
public class DoubleCannonWeapon extends CommonWeapon implements IMarkableObject {
    private ModelDoubleCannon modelDoubleCannon;
    private boolean isMark;

    public DoubleCannonWeapon() {
        super("Double cannon",100, 100, 25, 35);
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
        modelDoubleCannon = new ModelDoubleCannon();
        modelDoubleCannon.getParts().forEach(shape -> {
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
    public void displayEquipment(Placement position, boolean isEnemy) {
        double x = position.getX();
        double y = position.getY();
        double width = position.getSize();
        setIsEnemy(isEnemy);
        if(!position.isEmpty()){
            return;
        }

        modelDoubleCannon.getRoom().setCenterX(x + width/2);
        modelDoubleCannon.getRoom().setCenterY(y + width/2);

        modelDoubleCannon.getHead().setCenterX(x + width/2);
        modelDoubleCannon.getHead().setCenterY(y + width/2);


        if(isEnemy()){
            modelDoubleCannon.getCannonTop().setX(x - 5);
            modelDoubleCannon.getCannonBottom().setX(x - 5);
        }else {
            modelDoubleCannon.getCannonTop().setX(x + width/2 );
            modelDoubleCannon.getCannonBottom().setX(x + width/2 );
        }

        modelDoubleCannon.getCannonTop().setY(y + width/2 - modelDoubleCannon.getCannonTop().getHeight()/2 - 5);
        modelDoubleCannon.getCannonBottom().setY(y + width/2 - modelDoubleCannon.getCannonBottom().getHeight()/2 + 5);
        Pane gameArea = (Pane) position.getField().getParent();
        gameArea.getChildren().addAll(modelDoubleCannon.getParts());
        position.setIsEmpty(false);
    }

    @Override
    public void markObject() {
        modelDoubleCannon.getParts().forEach(shape -> {
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
        modelDoubleCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });

        setIsMark(false);
        GlobalVariables.setMarkedObject(null);
        GlobalVariables.setName("");
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void target() {
        modelDoubleCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.RED);
            shape.setStrokeWidth(1.5);
        });
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        modelDoubleCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });
    }

    @Override
    public Placement getPlacement() {
        return new Placement(modelDoubleCannon.getRoom().getCenterX(), modelDoubleCannon.getRoom().getCenterY(), modelDoubleCannon.getRoom().getRadius(), null);
    }

    @Override
    public void rotateEquipment(double x, double y) {
        Rotate rotation = calculationForRotation(x, y, getCenterX(), getCenterY(), isEnemy());

        if(isEnemy()){
            modelDoubleCannon.getCannonTop().getTransforms().add(rotation);
            modelDoubleCannon.getCannonBottom().getTransforms().add(rotation);
        }else{
            modelDoubleCannon.getCannonTop().getTransforms().add(rotation);
            modelDoubleCannon.getCannonBottom().getTransforms().add(rotation);
        }
    }

    @Override
    public boolean containsPosition(double x, double y){
        return modelDoubleCannon.getRoom().contains(x, y);
    }


    @Override
    public double getCenterX(){
        return modelDoubleCannon.getRoom().getCenterX();
    }

    @Override
    public double getCenterY(){
        return modelDoubleCannon.getRoom().getCenterY();
    }

    @Override
    public CommonShot getShot(CommonConstruction target, int damage) {
        return new DoubleBallShot(target, this, damage);
    }
}
