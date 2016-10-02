package game.ships;

import game.GlobalVariables;
import game.construction.CommonDraggableObject;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.construction.CommonConstruction;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public abstract class CommonShip extends CommonConstruction {

    public static final int BATTLE_SHIP = 1;
    public static final int CRUISER_SHIP = 2;

    private int power;
    private boolean isEnemy;
    private final int type;
    private Placement [][] placements;
    private int shieldMaxLife;
    private int shieldActualLife;
    private SimpleDoubleProperty shieldActualLifeBinding;
    private double shieldRadiusX;
    private double shieldAddX;
    private double shieldAddY;
    private double shieldRadiusY;

    public CommonShip (String name, int life, int power, boolean isEnemy, int type) {
        super(life, name);
        setPower(power);
        setIsEnemy(isEnemy);
        this.type = type;
        shieldActualLife = 0;
        shieldMaxLife = 0;
        setShieldConstants();
        shieldActualLifeBinding = new SimpleDoubleProperty(0);
    }

    protected void setShieldAddX(double shieldAddX) {
        this.shieldAddX = shieldAddX;
    }

    protected void setShieldAddY(double shieldAddY) {
        this.shieldAddY = shieldAddY;
    }

    protected void setShieldRadiusX(double shieldRadiusX) {
        this.shieldRadiusX = shieldRadiusX;
    }

    protected void setShieldRadiusY(double shieldRadiusY) {
        this.shieldRadiusY = shieldRadiusY;
    }

    public abstract void setShieldConstants();

    public void setShieldMaxLife(int shieldLife) {
        this.shieldMaxLife += shieldLife;
        shieldActualLife += shieldLife;
        shieldActualLifeBinding.set(1);
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

    public SimpleDoubleProperty getShieldActualLifeBinding() {
        return shieldActualLifeBinding;
    }

    public abstract void displayShip(Pane gameArea);

    public abstract void positionOfShip(double x, double y, Pane gameArea);

    public double getShieldAddX() {
        return shieldAddX;
    }

    public double getShieldAddY() {
        return shieldAddY;
    }

    public double getShieldRadiusX() {
        return shieldRadiusX;
    }

    public double getShieldRadiusY() {
        return shieldRadiusY;
    }

    public void damageToShield(int damage){
        int newShieldLife = shieldActualLife - damage;
        if(newShieldLife > 0){
            shieldActualLife = newShieldLife;
            shieldActualLifeBinding.set(shieldActualLife/shieldMaxLife);
        }else {
            shieldActualLife = 0;
            shieldActualLifeBinding.set(shieldActualLife);
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

                IShipEquipment construction = ((CommonDraggableObject) shipEquipment).getObject();
                construction.displayEquipment(placements[i][j], ship.isEnemy());
                placements[i][j].setIsEmpty(false);
                placements[i][j].setShipEquipment(construction);
                ((CommonConstruction)construction).setPlacement(placements[i][j]);

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
