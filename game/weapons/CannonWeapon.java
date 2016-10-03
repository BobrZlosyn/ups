package game.weapons;


import game.ConstructionTypes;
import game.GlobalVariables;
import game.construction.CommonConstruction;
import game.construction.IMarkableObject;
import game.construction.Placement;
import game.shots.CommonShot;
import game.shots.SimpleBallShot;
import game.weapons.modelsWeapon.ModelCannon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CannonWeapon extends CommonWeapon{

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
    public void displayEquipment(Placement position, boolean isEnemy) {
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
        position.setShipEquipment(this);
        position.setIsWeapon(true);
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

        if(!GlobalVariables.isEmpty(getTarget())){
            ((IMarkableObject)getTarget().getShipEquipment()).target();
        }
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
        if(!isEnemy()){
            return;
        }

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
    public void rotateEquipment(double x, double y) {
        Rotate rotation = calculationForRotation(x, y, getCenterX(), getCenterY(), isEnemy());
        double angleNew = rotation.getAngle() - getAngle();
        rotation.setAngle(angleNew);
        setAngle(angleNew);

        if(isEnemy()){
            modelCannon.getCannon().getTransforms().add(rotation);
        }else{
            modelCannon.getCannon().getTransforms().add(rotation);
        }

    }
    @Override
    public boolean containsPosition(double x, double y){
        return modelCannon.getRoom().contains(x,y);
    }

    @Override
    public double getCenterX(){
        return modelCannon.getRoom().getCenterX();
    }

    @Override
    public double getCenterY(){
        return modelCannon.getRoom().getCenterY();
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.CANNON_WEAPON;
    }

    @Override
    public CommonShot getShot(CommonConstruction target, int damage) {
        return new SimpleBallShot(target, this, damage);
    }
}
