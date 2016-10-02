package game.shields;

import game.construction.CommonConstruction;
import game.construction.IShipEquipment;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public abstract class CommonShield extends CommonConstruction implements IShipEquipment {
    public CommonShield(int life, String name) {
        super(life, name);
    }
}
