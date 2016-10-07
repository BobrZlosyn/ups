package game.ships;

import game.GlobalVariables;
import game.construction.CommonDraggableObject;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.construction.CommonConstruction;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public abstract class CommonShip extends CommonConstruction {

    private int energyMaxValue, energyActualValue;
    private int armorMaxValue, armorActualValue;
    private int speedMaxValue, speedActualValue;
    private boolean isEnemy;
    private Placement [][] placements;
    private int shieldMaxLife;
    private int shieldActualLife;
    private SimpleDoubleProperty shieldActualLifeBinding;
    private double shieldRadiusX;
    private double shieldAddX;
    private double shieldAddY;
    private double shieldRadiusY;

    public CommonShip (String name, int life, int energy,int armor, int speed, int shield, boolean isEnemy) {
        super(life, name);
        shieldActualLifeBinding = new SimpleDoubleProperty(0);
        setEnergyMaxValue(energy);
        setArmorMaxValue(armor);
        setSpeedMaxValue(speed);
        setShieldMaxLife(shield);
        setIsEnemy(isEnemy);
        shieldActualLife = 0;
        shieldMaxLife = 0;
        setShieldConstants();
    }

    public void setArmorActualValue(int armorActualValue) {
        this.armorActualValue = armorActualValue;
    }

    public void setArmorMaxValue(int armorMaxValue) {
        this.armorMaxValue = armorMaxValue;
    }

    public void setEnergyActualValue(int energyActualValue) {
        this.energyActualValue = energyActualValue;
    }

    public void setEnergyMaxValue(int energyMaxValue) {
        this.energyMaxValue = energyMaxValue;
    }

    public void setSpeedMaxValue(int speedMaxValue) {
        this.speedMaxValue = speedMaxValue;
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

    public void setPlacements(Placement[][] placements) {
        this.placements = placements;
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

    public int getArmorMaxValue() {
        return armorMaxValue;
    }

    public int getEnergyMaxValue() {
        return energyMaxValue;
    }

    public int getSpeedMaxValue() {
        return speedMaxValue;
    }

    public int getShieldMaxLife() {
        return shieldMaxLife;
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

    public abstract double getWidth();

    public abstract double getHeight();

    public Placement [][] getPlacementPositions(){
        return placements;
    }
}
