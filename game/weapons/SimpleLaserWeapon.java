package game.weapons;

import game.construction.CommonConstruction;
import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.construction.Placement;
import game.shots.CommonShot;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.weapons.modelsWeapon.ModelSimpleLaserWeapon;
import game.weapons.wrecksWeapons.CannonWreck;
import javafx.scene.paint.Color;

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
        model.getParts().forEach(shape -> {
            markShape(shape);
        });

    }

    @Override
    public void displayEquipment(Placement place, boolean isEnemy) {

    }

    @Override
    public CommonModel getModel() {
        return model;
    }

    @Override
    public CommonShot getShot(CommonConstruction target, int damage, boolean intoShields) {
        return null;
    }

    @Override
    public CommonWreck getWreck() {
        return new CannonWreck(getCenterX(), getCenterY(), Color.RED);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.SIMPLE_LASER_WEAPON;
    }

    @Override
    public void rotateEquipment(double x, double y) {

    }

    @Override
    public void rotateToDefaultPosition() {

    }
}
