package game.ships;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public class CommonShip {
    private int life;
    private int power;

    public CommonShip (int life, int power) {
        setLife(life);
        setPower(power);
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getLife() {
        return life;
    }

    public int getPower() {
        return power;
    }

    public void displayShip(boolean isEnemy, Pane gameArea){
        // it should be empty
    }
}
