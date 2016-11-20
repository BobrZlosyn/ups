package game.static_classes;

/**
 * Created by BobrZlosyn on 07.10.2016.
 */
public class GameBalance {

    /**
     * GLOBAL THINGS
     */

    public static final int ROUND_TIME = 120;


    /**
     *
     * MAXIMUM ON STATISTIC
     *
     */

    public static final int SHIP_LIFE = 1000;
    public static final int SHIP_SHIELDS = 1000;
    public static final int SHIP_ARMOR = 1000;
    public static final int SHIP_SPEED = 1000;
    public static final int SHIP_ENERGY = 1000;


    public static final int EQUIPMENT_LIFE = 250;
    public static final int EQUIPMENT_SHIELDS = 250;
    public static final int EQUIPMENT_ENERGY_COST = 300;
    public static final int EQUIPMENT_STRENGTH = 300;


    /**
     *
     * SHIPS
     *
     * */

    public static final String BATTLE_SHIP_NAME = "Bitevní loď";
    public static final int BATTLE_SHIP_LIFE = 500;
    public static final int BATTLE_SHIP_SHIELDS = 0;
    public static final int BATTLE_SHIP_ARMOR = 0;
    public static final int BATTLE_SHIP_SPEED = 500;
    public static final int BATTLE_SHIP_ENERGY = 500;
    public static final int BATTLE_SHIP_POINTS = 10;

    public static final String CRUISER_SHIP_NAME = "Křižník";
    public static final int CRUISER_SHIP_LIFE = 500;
    public static final int CRUISER_SHIP_SHIELDS = 500;
    public static final int CRUISER_SHIP_ARMOR = 0;
    public static final int CRUISER_SHIP_SPEED = 500;
    public static final int CRUISER_SHIP_ENERGY = 500;
    public static final int CRUISER_SHIP_POINTS = 14;

    public static final String ADMIRAL_SHIP_NAME = "Velitelská loď";
    public static final int ADMIRAL_SHIP_LIFE = 500;
    public static final int ADMIRAL_SHIP_SHIELDS = 500;
    public static final int ADMIRAL_SHIP_ARMOR = 0;
    public static final int ADMIRAL_SHIP_SPEED = 500;
    public static final int ADMIRAL_SHIP_ENERGY = 500;
    public static final int ADMIRAL_SHIP_POINTS = 14;

    /**
     *
     * EQUIPMENTS
     *
     */

    public static final String CANNON_EQUIPMENT_NAME = "Jednoduché dělo";
    public static final int CANNON_EQUIPMENT_LIFE = 200;
    public static final int CANNON_EQUIPMENT_ENERGY_COST = 75;
    public static final int CANNON_EQUIPMENT_MIN_STRENGTH = 40;
    public static final int CANNON_EQUIPMENT_MAX_STRENGTH = 300;
    public static final int CANNON_EQUIPMENT_POINTS_COST = 2;

    public static final String DOUBLE_CANNON_EQUIPMENT_NAME = "Dvojité dělo";
    public static final int DOUBLE_CANNON_EQUIPMENT_LIFE = 250;
    public static final int DOUBLE_CANNON_EQUIPMENT_ENERGY_COST = 110;
    public static final int DOUBLE_CANNON_EQUIPMENT_MIN_STRENGTH = 70;
    public static final int DOUBLE_CANNON_EQUIPMENT_MAX_STRENGTH = 300;
    public static final int DOUBLE_CANNON_EQUIPMENT_POINTS_COST = 2;

    public static final String SIMPLE_LASER_EQUIPMENT_NAME = "Laserové dělo";
    public static final int SIMPLE_LASER_EQUIPMENT_LIFE = 250;
    public static final int SIMPLE_LASER_EQUIPMENT_ENERGY_COST = 110;
    public static final int SIMPLE_LASER_EQUIPMENT_MIN_STRENGTH = 70;
    public static final int SIMPLE_LASER_EQUIPMENT_MAX_STRENGTH = 300;
    public static final int SIMPLE_LASER_EQUIPMENT_POINTS_COST = 2;
    /**
     * 
     * SHIELDS
     * 
     */

    public static final String SHIELD_EQUIPMENT_NAME = "Jednoduchý štít";
    public static final int SHIELD_EQUIPMENT_LIFE = 200;
    public static final int SHIELD_EQUIPMENT_SHIELDS = 150;
    public static final int SHIELD_EQUIPMENT_ENERGY_COST = 100;
    public static final int SHIELD_EQUIPMENT_POINTS_COST = 2;
    public static final int SHIELD_EQUIPMENT_SUCCESS_CHANCE = 4;


    public static final String STRONGER_SHIELD_EQUIPMENT_NAME = "Silnější štít";
    public static final int STRONGER_SHIELD_EQUIPMENT_LIFE = 200;
    public static final int STRONGER_SHIELD_EQUIPMENT_SHIELDS = 150;
    public static final int STRONGER_SHIELD_EQUIPMENT_ENERGY_COST = 100;
    public static final int STRONGER_SHIELD_EQUIPMENT_POINTS_COST = 2;
    public static final int STRONGER_SHIELD_EQUIPMENT_SUCCESS_CHANCE = 4;
}
