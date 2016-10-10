package game.shields;

import game.construction.AShipEquipment;
import game.construction.CommonConstruction;
import game.construction.CommonModel;
import game.construction.IShipEquipment;
import game.static_classes.GlobalVariables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public abstract class CommonShield extends AShipEquipment {
    private double chance;
    private int shieldLife;
    private Timeline hit;

    public CommonShield( String name, int life, int energy, int cost, int shieldBonus, int chance) {
        super(life, name, 0, energy, cost, shieldBonus );
        this.chance = chance;
        createTimelineHit();
    }

    private void createTimelineHit(){
        hit = new Timeline(new KeyFrame(javafx.util.Duration.seconds(GlobalVariables.damageHitDuration),event -> {
            getModel().setDefaultSkin();
        }));
        hit.setCycleCount(1);
    }

    public double getChance() {
        return chance;
    }

    public int getShieldLife() {
        return shieldLife;
    }

    public abstract CommonModel getModel();

    @Override
    public void destroy() {

    }

    @Override
    public void damageHit() {
        getModel().getParts().forEach(shape -> shape.setFill(GlobalVariables.damageHit));
        hit.playFromStart();
    }
}
