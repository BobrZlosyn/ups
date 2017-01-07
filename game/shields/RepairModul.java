package game.shields;

import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.construction.Placement;
import game.static_classes.GlobalVariables;
import javafx.scene.layout.Pane;

/**
 * Created by Kanto on 14.11.2016.
 */
public class RepairModul extends CommonShield{


    public RepairModul(String name, int life, int energy, int cost, int shieldBonus, int chance) {
        super(name, life, energy, cost, shieldBonus, chance);
    }

    @Override
    protected CommonWreck getWreck() {
        return null;
    }

    @Override
    public void displayEquipment(Placement place, boolean isEnemy) {

    }

    @Override
    public CommonModel getModel() {
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean containsPosition(double x, double y) {
        return false;
    }

    @Override
    public double getCenterX() {
        return 0;
    }

    @Override
    public double getCenterY() {
        return 0;
    }

    @Override
    public String getConstructionType() {
        return null;
    }
}
