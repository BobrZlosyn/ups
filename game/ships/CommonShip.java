package game.ships;

import game.construction.CommonConstruction;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public class CommonShip extends CommonConstruction {

    private int power;
    private boolean isEnemy;

    public CommonShip (String name, int life, int power, boolean isEnemy) {
        super(life, name);
        setPower(power);
        setIsEnemy(isEnemy);
    }

    public void setIsEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void displayShip(boolean isEnemy, Pane gameArea){
        // it should be empty
    }
}
