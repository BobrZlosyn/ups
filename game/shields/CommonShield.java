package game.shields;

import game.construction.*;
import game.static_classes.GlobalVariables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public abstract class CommonShield extends AShipEquipment {
    private double chance;
    private Timeline hit;
    private SimpleBooleanProperty isActive;

    public CommonShield( String name, int life, int energy, int cost, int shieldBonus, int chance) {
        super(life, name, 0, energy, cost, shieldBonus );
        this.chance = chance;
        isActive = new SimpleBooleanProperty(true);
        createTimelineHit();
    }

    private void createTimelineHit(){
        hit = new Timeline(new KeyFrame(javafx.util.Duration.seconds(GlobalVariables.damageHitDuration),event -> {
            getModel().setDefaultSkin();
        }));
        hit.setCycleCount(1);
    }

    public void setIsActive( boolean isActive) {
        this.isActive.set(isActive);
    }

    public boolean isActive() {
        return isActive.get();
    }

    public SimpleBooleanProperty isActiveProperty() {
        return isActive;
    }

    public double getChance() {
        return chance;
    }


    protected void displayEquipmentExtension(Placement place) {
        place.setIsEmpty(false);

        place.getShip().addShieldBonus(this);
        if(place.getShip().getActualEnergyLevel() >= getEnergyCost()){
            place.getShip().setActualEnergy(getEnergyCost());
            setIsActive(true);
        }else {
            setIsActive(false);
        }
    }



    public boolean isShield(){
        return true;
    }


    @Override
    public void damageHit() {
        getModel().getParts().forEach(shape -> shape.setFill(GlobalVariables.damageHit));
        hit.playFromStart();
    }
}
