package game.weapons;

import game.construction.AShipEquipment;
import game.construction.CommonModel;
import game.construction.Placement;
import game.construction.CommonConstruction;
import game.shots.CommonShot;
import game.static_classes.GlobalVariables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
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
        createTimelineHit();
        targetLife = (observable, oldValue, newValue) -> {
            if(newValue.doubleValue() <= 0) {
                rotateToDefaultPosition();
                setTarget(null);
            }
        };
    }

    private void createTimelineHit(){
        hit = new Timeline(new KeyFrame(javafx.util.Duration.seconds(GlobalVariables.damageHitDuration),event -> {
            getModel().setDefaultSkin();
        }));
        hit.setCycleCount(1);
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

    @Override
    public void damageHit() {
        getModel().getParts().forEach(shape -> shape.setFill(GlobalVariables.damageHit));
        hit.playFromStart();
    }

}
