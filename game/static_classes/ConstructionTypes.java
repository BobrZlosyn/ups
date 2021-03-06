package game.static_classes;

import game.construction.IShipEquipment;
import game.shields.SimpleShield;
import game.shields.StrongerShield;
import game.ships.AdmiralShip;
import game.ships.BattleShip;
import game.ships.CommonShip;
import game.ships.CruiserShip;
import game.weapons.CannonWeapon;
import game.weapons.DoubleCannonWeapon;
import game.weapons.SimpleLaserWeapon;

/**
 * Created by Kanto on 03.10.2016.
 */
public class ConstructionTypes {

    private static final String SHIP = "1";
    private static final String WEAPON = "2";
    private static final String SHIELD = "3";
    public static final String BATTLE_SHIP = SHIP + "1";
    public static final String CRUISER_SHIP = SHIP + "2";
    public static final String ADMIRAL_SHIP = SHIP + "3";
    public static final String CANNON_WEAPON = WEAPON + "1";
    public static final String DOUBLE_CANNON_WEAPON = WEAPON + "2";
    public static final String SIMPLE_LASER_WEAPON = WEAPON + "3";
    public static final String SIMPLE_SHIELD = SHIELD + "1";
    public static final String STRONGER_SHIELD = SHIELD + "2";



    public static CommonShip createShip(String typeOfShip){
        switch (typeOfShip){
            case ConstructionTypes.BATTLE_SHIP: return new BattleShip(true);
            case ConstructionTypes.CRUISER_SHIP: return new CruiserShip(true);
            case ConstructionTypes.ADMIRAL_SHIP: return new AdmiralShip(true);
        }
        return null;
    }

    public static IShipEquipment createEquipment(String equipment){
        switch (equipment){
            case ConstructionTypes.CANNON_WEAPON: return new CannonWeapon();
            case ConstructionTypes.DOUBLE_CANNON_WEAPON: return new DoubleCannonWeapon();
            case ConstructionTypes.SIMPLE_LASER_WEAPON: return new SimpleLaserWeapon();
            case ConstructionTypes.SIMPLE_SHIELD: return new SimpleShield();
            case ConstructionTypes.STRONGER_SHIELD: return new StrongerShield();

        }
        return null;
    }

    public static boolean isEquipment(String type){
        return !isShip(type);
    }

    public static boolean isShip(String type){
        return type.startsWith(SHIP);
    }

}
