package game.weapons;

import game.GlobalVariables;
import game.IMarkableObject;
import game.Placement;
import game.weapons.modelsWeapon.ModelCannon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Created by Kanto on 30.09.2016.
 */
public class DoubleCannonWeapon extends CommonWeapon implements IMarkableObject {
    private ModelCannon modelDoubleCannon;
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
        modelDoubleCannon = new ModelCannon();
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
    public void displayWeapon(Placement position, boolean isEnemy) {
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
            modelDoubleCannon.getCannon().setX(x - 5);
        }else {
            modelDoubleCannon.getCannon().setX(x + width/2);
        }

        modelDoubleCannon.getCannon().setY(y + width/2 - modelDoubleCannon.getCannon().getHeight()/2);
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
        return new Placement(modelDoubleCannon.getRoom().getCenterX(), modelDoubleCannon.getRoom().getCenterY(), modelDoubleCannon.getRoom().getRadius());
    }

    @Override
    public void rotateWeapon(double x, double y) {
        double centerX = modelDoubleCannon.getRoom().getCenterX();
        double countCx = (x - centerX)*(x - centerX);
        double countAx = (x - centerX)*(x - centerX);
        double countAy = (y - centerX)*(y - centerX);
        double sideC = Math.sqrt(countCx);
        double sideA = Math.sqrt(countAx + countAy);

        double cosinus = Math.toDegrees(Math.acos(sideC / sideA));

        if(isEnemy()){
            modelDoubleCannon.getCannon().getTransforms().add(new Rotate(cosinus, centerX, modelDoubleCannon.getRoom().getCenterY()));
        }else{
            modelDoubleCannon.getCannon().getTransforms().add(new Rotate(-cosinus, centerX, modelDoubleCannon.getRoom().getCenterY()));
        }

    }
}
