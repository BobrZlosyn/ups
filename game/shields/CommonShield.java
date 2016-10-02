package game.shields;

import game.construction.CommonConstruction;
import game.construction.IShipEquipment;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public abstract class CommonShield extends CommonConstruction implements IShipEquipment {
    private double chance;
    private int shieldLife;

    public CommonShield(int life, String name, double chance, int shieldLife) {
        super(life, name);
        this.chance = chance;
        this.shieldLife = shieldLife;
    }

    public double getChance() {
        return chance;
    }

    public int getShieldLife() {
        return shieldLife;
    }
}
