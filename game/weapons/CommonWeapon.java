package game.weapons;

import game.construction.*;
import game.shots.CommonShot;
import game.static_classes.GlobalVariables;
import game.weapons.wrecksWeapons.CannonWreck;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public abstract class CommonWeapon extends AShipEquipment {
    private int power;
    private int minStrength;
    private int maxStrength;
    private Placement target;
    private double angle;
    private Timeline hit;
    private ChangeListener <Number> targetLife;

    public CommonWeapon (String name, int life, int energy, int minStrength, int maxStrength, int energyCost, int costOfEquipment) {
        super(life, name, maxStrength, energyCost, costOfEquipment, 0);
        setTarget(null);
        setPower(energy);
        setMinStrength(minStrength);
        setMaxStrength(maxStrength);
        setIsEnemy(false);
        setIsMarked(false);

        targetLife = (observable, oldValue, newValue) -> {
            if(newValue.doubleValue() <= 0) {
                rotateToDefaultPosition();
                setTarget(null);
            }
        };
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setTarget(Placement target) {
        if(!GlobalVariables.isEmpty(target) && !GlobalVariables.isEmpty(target.getShipEquipment())){
            ((CommonConstruction)target.getShipEquipment()).getActualLifeBinding().addListener(targetLife);
        }else {
            if(!GlobalVariables.isEmpty(this.target) && !GlobalVariables.isEmpty(this.target.getShipEquipment())){
                ((CommonConstruction)this.target.getShipEquipment()).getActualLifeBinding().removeListener(targetLife);
            }
        }
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

    public abstract CommonShot getShot(CommonConstruction target, int damage, boolean intoShields);

    public boolean isWeapon(){
        return true;
    }

    public abstract CommonWreck getWreck();

    @Override
    public void destroy() {
        double x = getPlacement().getX();
        double y = getPlacement().getY();

        if (!GlobalVariables.isEmpty(getTarget())){
            getPlacement().getShip().setActualEnergy(-getEnergyCost());
        }

        setTarget(null);
        Pane gameArea = getModel().getParent();
        if(GlobalVariables.isEmpty(gameArea)){
            return;
        }

        gameArea.getChildren().removeAll(getModel().getParts());

        CommonWreck wrecks = getWreck();
        gameArea.getChildren().add(wrecks.getFlashCircle());
        wrecks.explosion(x, y, 45, 5, getModel());
    }


    @Override
    public void markObject() {
        getModel().getParts().forEach(shape -> {
            shape.setStroke(Color.BLUE);
            shape.setStrokeWidth(1.5);
        });

        setIsMarked(true);

        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());
        GlobalVariables.setCanTarget(!isEnemy());

    }

    @Override
    public void unmarkObject() {
        getModel().getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });

        setIsMarked(false);
        GlobalVariables.setMarkedObject(null);
        GlobalVariables.setName("");
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void target() {
        if(!isEnemy()){
            return;
        }

        getModel().getParts().forEach(shape -> {
            shape.setStroke(Color.RED);
            shape.setStrokeWidth(1.5);
        });
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        getModel().getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });
    }

}
