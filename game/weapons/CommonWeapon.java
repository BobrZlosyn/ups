package game.weapons;

import javafx.scene.shape.Rectangle;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public class CommonWeapon {
    private int life;
    private int power;
    private int minStrength;
    private int maxStrength;

    public CommonWeapon (int life, int power, int minStrength, int maxStrength) {
        setLife(life);
        setPower(power);
        setMinStrength(minStrength);
        setMaxStrength(maxStrength);
    }

    public void setLife(int life) {
        this.life = life;
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

    public int getLife() {
        return life;
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

    public void displayWeapon(Rectangle position, boolean isEnemy) {

    }
}
