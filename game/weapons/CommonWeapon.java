package game.weapons;

import game.construction.AShipEquipment;
import game.construction.Placement;
import game.construction.CommonConstruction;
import game.shots.CommonShot;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public abstract class CommonWeapon extends AShipEquipment {
    private int power;
    private int minStrength;
    private int maxStrength;
    private Placement target;
    private double angle;

    public CommonWeapon (String name, int life, int energy, int minStrength, int maxStrength, int energyCost, int costOfEquipment) {
        super(life, name, maxStrength, energyCost, costOfEquipment, 0);
        setPower(energy);
        setMinStrength(minStrength);
        setMaxStrength(maxStrength);
        setIsEnemy(false);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setTarget(Placement target) {
        this.target = target;
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

    public double getAngle() {
        return angle;
    }

    public Placement getTarget() {
        return target;
    }

    public abstract CommonShot getShot(CommonConstruction target, int damage);

    public boolean isWeapon(){
        return true;
    }

}
