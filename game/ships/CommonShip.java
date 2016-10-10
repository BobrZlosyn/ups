package game.ships;

import game.static_classes.GlobalVariables;
import game.construction.CommonDraggableObject;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.construction.CommonConstruction;
import game.weapons.CommonWeapon;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;

/**
 * Created by BobrZlosyn on 25.09.2016.
 */
public abstract class CommonShip extends CommonConstruction {

    private int energyMaxValue, energyActualValue;
    private int armorMaxValue, armorActualValue;
    private int speedMaxValue, speedActualValue;
    private int pointsForEquipment;
    private boolean isEnemy;
    private Placement [][] placements;
    private int shieldMaxLife;
    private int shieldActualLife;
    private SimpleDoubleProperty shieldActualLifeBinding;
    private SimpleIntegerProperty availablePoints;
    private SimpleDoubleProperty actualEnergy;
    private double shieldRadiusX;
    private double shieldAddX;
    private double shieldAddY;
    private double shieldRadiusY;

    public CommonShip (String name, int life, int energy,int armor, int speed, int shield, int pointsForEquipment, boolean isEnemy) {
        super(life, name);
        if(shield == 0){
            shieldActualLifeBinding = new SimpleDoubleProperty(0);
        }else {
            shieldActualLifeBinding = new SimpleDoubleProperty(1);
        }

        actualEnergy = new SimpleDoubleProperty(1);
        energyActualValue = energy;
        setEnergyMaxValue(energy);
        setArmorMaxValue(armor);
        setSpeedMaxValue(speed);
        setShieldMaxLife(shield);
        setPointsForEquipment(pointsForEquipment);
        setIsEnemy(isEnemy);
        availablePoints = new SimpleIntegerProperty(pointsForEquipment);
        shieldActualLife = shield;
        setShieldConstants();
    }

    public void setActualEnergy(int cost) {
        energyActualValue -= cost;
        this.actualEnergy.set(((double)energyActualValue)/energyMaxValue);
    }

    public void setShieldActualLife(int shieldActualLife) {
        this.shieldActualLifeBinding.set(((double) shieldActualLife) / shieldMaxLife);
        this.shieldActualLife = shieldActualLife;
    }

    public void setAvailablePoints(int costPoints) {
        availablePoints.set(availablePoints.getValue() - costPoints);
    }

    public void setPointsForEquipment(int pointsForEquipment) {
        this.pointsForEquipment = pointsForEquipment;
    }

    public void setArmorActualValue(int armorActualValue) {
        this.armorActualValue = armorActualValue;
    }

    public void setArmorMaxValue(int armorMaxValue) {
        this.armorMaxValue = armorMaxValue;
    }

    public void setEnergyActualValue(int energyActualValue) {
        this.energyActualValue = energyActualValue;
        this.shieldActualLifeBinding.set(((double)energyActualValue) / energyMaxValue);
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

    public SimpleDoubleProperty getActualEnergy() {
        return actualEnergy;
    }

    public abstract void displayShip(Pane gameArea);

    public abstract void positionOfShip(double x, double y, Pane gameArea);

    public double getShieldAddX() {
        return shieldAddX;
    }

    public int getShieldActualLife() {
        return shieldActualLife;
    }

    public double getShieldAddY() {
        return shieldAddY;
    }

    public int getAvailablePoints() {
        return availablePoints.get();
    }

    public int getActualEnergyLevel(){
        return energyActualValue;
    }

    public SimpleIntegerProperty getAvailablePointsProperty() {
        return availablePoints;
    }

    public int getPointsForEquipment() {
        return pointsForEquipment;
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

    public void fillShipWithEquipment(CommonShip ship, Placement [][] oldShipPlacements, boolean isFirstCreation){
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

                if(isFirstCreation){
                    IShipEquipment construction = ((CommonDraggableObject) shipEquipment).getObject();
                    construction.displayEquipment(placements[i][j], ship.isEnemy());
                    placements[i][j].setIsEmpty(false);
                    placements[i][j].setShipEquipment(construction);
                    ((CommonConstruction)construction).setPlacement(placements[i][j]);

                }else {
                    shipEquipment.displayEquipment(placements[i][j], ship.isEnemy());
                    placements[i][j].setIsEmpty(false);
                    placements[i][j].setShipEquipment(shipEquipment);
                    ((CommonConstruction) shipEquipment).setPlacement(placements[i][j]);
                    ((CommonConstruction) shipEquipment).setActualLife(((CommonConstruction) shipEquipment).getTotalLife().get());
                    ((CommonConstruction) shipEquipment).unmarkObject();
                    ((CommonConstruction) shipEquipment).cancelTarget();

                    if (shipEquipment.isWeapon()) {
                        shipEquipment.rotateToDefaultPosition();
                        ((CommonWeapon) shipEquipment).setTarget(null);
                    }
                }
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


    public void restartValues(){
        setEnergyActualValue(getEnergyMaxValue());
        setActualLife(getTotalLife().get());
        setShieldActualLife(getShieldMaxLife());
    }
}
