package game.weapons;


import game.construction.CommonModel;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.construction.CommonConstruction;
import game.construction.Placement;
import game.shots.CommonShot;
import game.shots.SimpleBallShot;
import game.weapons.modelsWeapon.ModelCannon;
import game.weapons.wrecksWeapons.CannonWreck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Created by Kanto on 26.09.2016.
 */
public class CannonWeapon extends CommonWeapon{

    private ModelCannon modelCannon;

    public CannonWeapon() {
        super(
                GameBalance.CANNON_EQUIPMENT_NAME,
                GameBalance.CANNON_EQUIPMENT_LIFE,
                GameBalance.CANNON_EQUIPMENT_ENERGY_COST,
                GameBalance.CANNON_EQUIPMENT_MIN_STRENGTH,
                GameBalance.CANNON_EQUIPMENT_MAX_STRENGTH,
                GameBalance.CANNON_EQUIPMENT_ENERGY_COST,
                GameBalance.CANNON_EQUIPMENT_POINTS_COST
        );
        setIsMarked(false);
        createCannon();
    }

    private void createCannon() {
        modelCannon = new ModelCannon();
        modelCannon.getParts().forEach(shape -> {
            markCannon(shape);
        });

    }

    private void markCannon(Shape shape){
            shape.setOnMouseClicked(event -> {
                markingHandle(isMarked(), this);
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

        setIsMarked(true);

        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());
        GlobalVariables.setCanTarget(!isEnemy());

    }

    @Override
    public void unmarkObject() {
        modelCannon.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });

        setIsMarked(false);
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
        rotateToDefaultPosition();
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
    public void rotateToDefaultPosition() {
        double newAngle = -getAngle();
        setAngle(0);
        modelCannon.getCannon().getTransforms().add(new Rotate(newAngle, getCenterX(), getCenterY()));
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
    public CommonShot getShot(CommonConstruction target, int damage, boolean intoShields) {
        return new SimpleBallShot(target, this, damage, intoShields);
    }

    @Override
    public CommonModel getModel() {
        return modelCannon;
    }

    @Override
    public void destroy() {
        double x = getPlacement().getX();
        double y = getPlacement().getY();

        Pane gameArea = getModel().getParent();
        gameArea.getChildren().removeAll(getModel().getParts());

        CannonWreck wrecks = new CannonWreck(getCenterX(), getCenterY(), Color.RED);
        gameArea.getChildren().add(wrecks.getFlashCircle());
        wrecks.explosion(x, y, 45, 5, getModel());
    }
}
