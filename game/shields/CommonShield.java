package game.shields;

import game.construction.AShipEquipment;
import game.construction.CommonConstruction;
import game.construction.IShipEquipment;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public abstract class CommonShield extends AShipEquipment {
    private double chance;
    private int shieldLife;

    public CommonShield( String name, int life, int energy, int cost, int shieldBonus, int chance) {
        super(life, name, 0, energy, cost, shieldBonus );
        this.chance = chance;
    }

    public double getChance() {
        return chance;
    }

    public int getShieldLife() {
        return shieldLife;
    }
}
