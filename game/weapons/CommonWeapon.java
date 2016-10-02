package game.weapons;

import game.construction.IShipEquipment;
import game.construction.Placement;
import game.construction.CommonConstruction;
import game.shots.CommonShot;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public abstract class CommonWeapon extends CommonConstruction implements IShipEquipment{
    private int power;
    private int minStrength;
    private int maxStrength;

    public CommonWeapon (String name, int life, int power, int minStrength, int maxStrength) {
        super(life, name);
        setPower(power);
        setMinStrength(minStrength);
        setMaxStrength(maxStrength);
        setIsEnemy(false);
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setMinStrength(int strength) {
        this.minStrength = strength;
    }

    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }

    public int getPower() {
        return power;
    }

    public int getMinStrength() {
        return minStrength;
    }

    public int getMaxStrength() {
        return maxStrength;
    }

    public abstract CommonShot getShot(CommonConstruction target, int damage);

}
