package game.shots;

import game.construction.CommonConstruction;
import javafx.scene.layout.Pane;

/**
 * Created by BobrZlosyn on 02.10.2016.
 */
public abstract class CommonShot {

    private double time;
    protected CommonConstruction target, attacker;
    protected double x1, y1;
    private int damage;

    public CommonShot (CommonConstruction target, CommonConstruction attacker, int damage){
        this.target = target;
        this.attacker = attacker;
        x1 = attacker.getCenterX();
        y1 = attacker.getCenterY();
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public CommonConstruction getTarget() {
        return target;
    }

    protected double getX1() {
        return x1;
    }

    protected double getY1() {
        return y1;
    }

    protected double[] rovnicePrimka(double x1, double y1, double x2, double y2){

        double u1 = Math.abs(x1-x2);
        double u2 = Math.abs(y1-y2);

        double x = (x1 - x2)*(x1-x2);
        time = Math.sqrt(x+(y1-y2)*(y1-y2));
        time = 2/time;

        double X1 = souradnice(x1,x2, u1);
        double Y1 = souradnice(y1,y2,u2);

        double[] coordinates = {X1,Y1};
        return coordinates;
    }

    private double souradnice(double x1, double x2, double u1){
        if(x1<x2){
            return x1 + time * u1;
        }else{
            return x1 - time * u1;
        }
    }

    public abstract void addShot(Pane gameArea);

    public abstract void removeShot(Pane gameArea);

    public abstract boolean pocitatTrasu();
}