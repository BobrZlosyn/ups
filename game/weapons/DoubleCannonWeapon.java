package game.weapons;

import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.construction.CommonConstruction;
import game.construction.Placement;
import game.shots.CommonShot;
import game.shots.DoubleBallShot;
import game.weapons.modelsWeapon.ModelDoubleCannon;
import game.weapons.wrecksWeapons.CannonWreck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Created by Kanto on 30.09.2016.
 */
public class DoubleCannonWeapon extends CommonWeapon{
    private ModelDoubleCannon modelDoubleCannon;

    public DoubleCannonWeapon() {
        super(
                GameBalance.DOUBLE_CANNON_EQUIPMENT_NAME,
                GameBalance.DOUBLE_CANNON_EQUIPMENT_LIFE,
                GameBalance.DOUBLE_CANNON_EQUIPMENT_ENERGY_COST,
                GameBalance.DOUBLE_CANNON_EQUIPMENT_MIN_STRENGTH,
                GameBalance.DOUBLE_CANNON_EQUIPMENT_MAX_STRENGTH,
                GameBalance.DOUBLE_CANNON_EQUIPMENT_ENERGY_COST,
                GameBalance.DOUBLE_CANNON_EQUIPMENT_POINTS_COST
        );
        createCannon();
    }

    private void createCannon() {
        modelDoubleCannon = new ModelDoubleCannon();
        modelDoubleCannon.getParts().forEach(shape -> {
            markShape(shape);
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
        position.setIsWeapon(true);
    }

    @Override
    public void rotateEquipment(double x, double y) {
        rotateToDefaultPosition();
        Rotate rotation = calculationForRotation(x, y, getCenterX(), getCenterY(), isEnemy());
        double angleNew = rotation.getAngle() - getAngle();
        rotation.setAngle(angleNew);
        setAngle(angleNew);


        if(isEnemy()){
            modelDoubleCannon.getCannonTop().getTransforms().add(rotation);
            modelDoubleCannon.getCannonBottom().getTransforms().add(rotation);
        }else{
            modelDoubleCannon.getCannonTop().getTransforms().add(rotation);
            modelDoubleCannon.getCannonBottom().getTransforms().add(rotation);
        }
    }

    @Override
    public void rotateToDefaultPosition() {
        double newAngle = -getAngle();
        setAngle(0);
        modelDoubleCannon.getCannonTop().getTransforms().add(new Rotate(newAngle, getCenterX(), getCenterY()));
        modelDoubleCannon.getCannonBottom().getTransforms().add(new Rotate(newAngle, getCenterX(), getCenterY()));
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.DOUBLE_CANNON_WEAPON;
    }

    @Override
    public CommonShot getShot(CommonConstruction target, int damage, boolean intoShields) {
        return new DoubleBallShot(target, this, damage, intoShields);
    }

    @Override
    public CommonModel getModel() {
        return modelDoubleCannon;
    }

    @Override
    public CommonWreck getWreck() {
        return new CannonWreck(getCenterX(), getCenterY(), Color.RED);
    }

}
