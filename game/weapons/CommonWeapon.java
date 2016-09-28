package game.weapons;

import game.Placement;
import game.construction.CommonConstruction;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public class CommonWeapon extends CommonConstruction{
    private int power;
    private int minStrength;
    private int maxStrength;
    private boolean isEnemy;

    public CommonWeapon (String name, int life, int power, int minStrength, int maxStrength) {
        super(life, name);
        setPower(power);
        setMinStrength(minStrength);
        setMaxStrength(maxStrength);
        setIsEnemy(false);
    }

    public void setIsEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
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

    public boolean isEnemy() {
        return isEnemy;
    }

    public void displayWeapon(Placement position, boolean isEnemy) {

    }

    public void rotateWeapon(double x, double y){

    }

}
