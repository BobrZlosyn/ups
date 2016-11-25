package game.shields;

import game.construction.*;
import game.shields.wrecksShields.SimpleShieldWrecks;
import game.static_classes.GlobalVariables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public abstract class CommonShield extends AShipEquipment {
    private double chance;
    private SimpleBooleanProperty isActive;

    public CommonShield( String name, int life, int energy, int cost, int shieldBonus, int chance) {
        super(life, name, 0, energy, cost, shieldBonus );
        this.chance = chance;
        isActive = new SimpleBooleanProperty(true);
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

    protected abstract CommonWreck getWreck();

    @Override
    public void destroy() {
        double x = getPlacement().getX();
        double y = getPlacement().getY();

        if (isActive()){
            getPlacement().getShip().setActualEnergy(-getEnergyCost());
        }

        Pane gameArea = getModel().getParent();
        gameArea.getChildren().removeAll(getModel().getParts());

        CommonWreck wrecks = getWreck();
        gameArea.getChildren().add(wrecks.getFlashCircle());
        wrecks.explosion(x, y, 45, 5, getModel());
    }
}
