package game.weapons;


import game.construction.CommonModel;
import game.construction.CommonWreck;
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
        createCannon();
    }


    private void createCannon() {
        modelCannon = new ModelCannon();
        modelCannon.getParts().forEach(shape -> {
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

        getModel().setModelXY(x + width/2, y + width/2);
        if(isEnemy()){
            modelCannon.getCannon().setX(x - 5);
        }

        Pane gameArea = (Pane) position.getField().getParent();
        gameArea.getChildren().addAll(getModel().getParts());
        position.setIsEmpty(false);
        position.setShipEquipment(this);
        position.setIsWeapon(true);
    }

    @Override
    public void rotateEquipment(double x, double y) {
        rotateToDefaultPosition();
        double cosinus = calculationForRotation(x, y, getCenterX(), getCenterY(), isEnemy());
        double angleNew = cosinus - getAngle();
        Rotate rotation = new Rotate(cosinus, getCenterX(), getCenterY());
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
    public CommonWreck getWreck() {
        return new CannonWreck(getCenterX(), getCenterY(), Color.RED);
    }
}
