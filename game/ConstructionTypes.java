package game;

import game.construction.IShipEquipment;
import game.shields.SimpleShield;
import game.ships.BattleShip;
import game.ships.CommonShip;
import game.ships.CruiserShip;
import game.weapons.CannonWeapon;
import game.weapons.DoubleCannonWeapon;

/**
 * Created by Kanto on 03.10.2016.
 */
public class ConstructionTypes {

    private static final String SHIP = "1";
    private static final String WEAPON = "2";
    private static final String SHIELD = "3";
    public static final String BATTLE_SHIP = SHIP + "1";
    public static final String CRUISER_SHIP = SHIP + "2";
    public static final String CANNON_WEAPON = WEAPON + "1";
    public static final String DOUBLE_CANNON_WEAPON = WEAPON + "2";
    public static final String SIMPLE_SHIELD = SHIELD + "1";



    public static CommonShip createShip(String typeOfShip){
        switch (typeOfShip){
            case ConstructionTypes.BATTLE_SHIP: return new BattleShip(true);
            case ConstructionTypes.CRUISER_SHIP: return new CruiserShip(true);
        }
        return null;
    }

    public static IShipEquipment createEquipment(String equipment){
        switch (equipment){
            case ConstructionTypes.CANNON_WEAPON: return new CannonWeapon();
            case ConstructionTypes.DOUBLE_CANNON_WEAPON: return new DoubleCannonWeapon();
            case ConstructionTypes.SIMPLE_SHIELD: return new SimpleShield();

        }
        return null;
    }
}
