package game.ships;

import game.GlobalVariables;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.construction.CommonConstruction;
import javafx.scene.layout.Pane;

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
    private int shieldLife;

    public CommonShip (String name, int life, int power, boolean isEnemy, int type) {
        super(life, name);
        setPower(power);
        setIsEnemy(isEnemy);
        this.type = type;
        setShieldLife(0);
    }

    public void setShieldLife(int shieldLife) {
        this.shieldLife = shieldLife;
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

    public int getShieldLife() {
        return shieldLife;
    }

    public void displayShip(Pane gameArea){
        // it should be empty
    }

    public void positionOfShip(double x, double y, Pane gameArea){
    }

    public void damageToShield(int damage){
        int newShieldLife = getShieldLife() - damage;
        if(newShieldLife > 0){
            setShieldLife(newShieldLife);
        }else {
            setShieldLife(0);
        }

    }

    public void fillShipWithEquipment(CommonShip ship, Placement [][] oldShipPlacements){
        for (int i = 0; i < oldShipPlacements.length; i++){
            for (int j = 0; j < oldShipPlacements[i].length; j++){
                Placement place = oldShipPlacements[i][j];
                if(place == null || place.isEmpty()){
                    continue;
                }
                IShipEquipment shipEquipment = place.getShipEquipment();
                if(GlobalVariables.isEmpty(shipEquipment)){
                    continue;
                }

                shipEquipment.displayEquipment(placements[i][j], ship.isEnemy());
                placements[i][j].setIsEmpty(false);
                placements[i][j].setCommonWeapon(shipEquipment);
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
