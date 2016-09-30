package game.ships;

import game.GlobalVariables;
import game.Placement;
import game.construction.CommonConstruction;
import game.weapons.CannonWeapon;
import game.weapons.CommonWeapon;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public class CommonShip extends CommonConstruction {

    public static final int BATTLE_SHIP = 1;
    public static final int CRUISER_SHIP = 2;

    private int power;
    private boolean isEnemy;
    private final int type;
    private Placement [][] placements;

    public CommonShip (String name, int life, int power, boolean isEnemy, int type) {
        super(life, name);
        setPower(power);
        setIsEnemy(isEnemy);
        this.type = type;
    }

    public void setIsEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setPlacements(Placement[][] placements) {
        this.placements = placements;
    }

    public int getPower() {
        return power;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void displayShip(Pane gameArea){
        // it should be empty
    }

    public void positionOfShip(double x, double y, Pane gameArea){
    }

    public void fillShipWithEquipment(CommonShip ship, Placement [][] oldShipPlacements){
        for (int i = 0; i < oldShipPlacements.length; i++){
            for (int j = 0; j < oldShipPlacements[i].length; j++){
                Placement place = oldShipPlacements[i][j];
                if(place == null || place.isEmpty()){
                    continue;
                }
                CommonWeapon commonWeapon = place.getCommonWeapon();
                if(GlobalVariables.isEmpty(commonWeapon)){
                    continue;
                }
                placements[i][j].setIsEmpty(false);
                placements[i][j].setCommonWeapon(commonWeapon);
                commonWeapon.displayWeapon(placements[i][j], ship.isEnemy());
            }
        }
    }

    public double getX(){
        return 0;
    }

    public double getY(){
        return 0;
    }

    public double getWidth(){
        return 0;
    }

    public int getType(){
        return type;
    }

    public Placement [][] getPlacementPositions(){
        return placements;
    }
}
