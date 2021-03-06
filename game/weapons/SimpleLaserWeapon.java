package game.weapons;

import game.construction.CommonConstruction;
import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.construction.Placement;
import game.shots.CommonShot;
import game.shots.LaserShot;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.weapons.modelsWeapon.ModelSimpleLaserWeapon;
import game.weapons.wrecksWeapons.CannonWreck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Created by Kanto on 15.11.2016.
 */
public class SimpleLaserWeapon extends CommonWeapon{
    private ModelSimpleLaserWeapon model;

    public SimpleLaserWeapon() {
        super(  GameBalance.SIMPLE_LASER_EQUIPMENT_NAME,
                GameBalance.SIMPLE_LASER_EQUIPMENT_LIFE,
                GameBalance.SIMPLE_LASER_EQUIPMENT_ENERGY_COST,
                GameBalance.SIMPLE_LASER_EQUIPMENT_MIN_STRENGTH,
                GameBalance.SIMPLE_LASER_EQUIPMENT_MAX_STRENGTH,
                GameBalance.SIMPLE_LASER_EQUIPMENT_ENERGY_COST,
                GameBalance.SIMPLE_LASER_EQUIPMENT_POINTS_COST);
        createCannon();
    }

    private void createCannon() {
        model = new ModelSimpleLaserWeapon();
    }

    @Override
    public void displayEquipment(Placement place, boolean isEnemy) {
        double width = place.getSize();
        double x = place.getX() + width/2;
        double y = place.getY() + width/2;

        setIsEnemy(isEnemy);
        markShape();
        if(!place.isEmpty()){
            return;
        }

        getModel().setModelXY(x, y);


        if(isEnemy()){
            model.getTower1().setRotate(180);
            model.getTower1().setLayoutX(x - 36 );
            model.getTower1().setLayoutY(y - 3);

            model.getTower2().setRotate(180);
            model.getTower2().setLayoutX(x - 36 );
            model.getTower2().setLayoutY(y - 45);
        }

        Pane gameArea = (Pane) place.getField().getParent();
        gameArea.getChildren().addAll(getModel().getParts());
        place.setIsEmpty(false);
        place.setShipEquipment(this);
        place.setIsWeapon(true);

    }

    @Override
    public CommonModel getModel() {
        return model;
    }

    @Override
    public CommonShot getShot(CommonConstruction target, int damage, boolean intoShields) {
        return new LaserShot(target, this, damage, intoShields);
    }

    @Override
    public CommonWreck getWreck() {
        return new CannonWreck(getCenterX(), getCenterY(), Color.RED);
    }

    @Override
    public void shotFired() {}

    @Override
    public void returnToBeforeFiredPosition() {}

    @Override
    public String getConstructionType() {
        return ConstructionTypes.SIMPLE_LASER_WEAPON;
    }

    @Override
    public void rotateEquipment(double x, double y) {
        rotateToDefaultPosition();
        double cosinus = calculationForRotation(x, y, getCenterX(), getCenterY(), isEnemy());
        double angleNew = cosinus - getAngle();

        setAngle(angleNew);

        model.getTower1().getTransforms().add(new Rotate(cosinus, 0, model.getTower1().getBoundsInLocal().getHeight()));
        model.getTower2().getTransforms().add(new Rotate(cosinus, 0, model.getTower2().getBoundsInLocal().getHeight()));



    }

    @Override
    public void rotateToDefaultPosition() {
        double newAngle = -getAngle();
        setAngle(0);

        model.getTower1().getTransforms().add(new Rotate(newAngle, 0, model.getTower1().getBoundsInLocal().getHeight()));
        model.getTower2().getTransforms().add(new Rotate(newAngle, 0, model.getTower2().getBoundsInLocal().getHeight()));
    }
}
