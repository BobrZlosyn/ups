package game.ships;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public class CommonShip {
    private int life;
    private int power;
    private String name;

    public CommonShip (String name, int life, int power) {
        setName(name);
        setLife(life);
        setPower(power);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
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
