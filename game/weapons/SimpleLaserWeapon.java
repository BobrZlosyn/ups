package game.weapons;

import game.construction.CommonConstruction;
import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.construction.Placement;
import game.shots.CommonShot;

/**
 * Created by Kanto on 15.11.2016.
 */
public class SimpleLaserWeapon extends CommonWeapon{
    public SimpleLaserWeapon(String name, int life, int energy, int minStrength, int maxStrength, int energyCost, int costOfEquipment) {
        super(name, life, energy, minStrength, maxStrength, energyCost, costOfEquipment);
    }

    @Override
    public void displayEquipment(Placement place, boolean isEnemy) {

    }

    @Override
    public CommonModel getModel() {
        return null;
    }

    @Override
    public CommonShot getShot(CommonConstruction target, int damage, boolean intoShields) {
        return null;
    }

    @Override
    public CommonWreck getWreck() {
        return null;
    }

    @Override
    public String getConstructionType() {
        return null;
    }
}
