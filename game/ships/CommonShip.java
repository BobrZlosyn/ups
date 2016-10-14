package game.ships;

import game.construction.*;
import game.shields.CommonShield;
import game.static_classes.GlobalVariables;
import game.weapons.CommonWeapon;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

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
    private int shieldMaxLifeOrigin;
    private int shieldActualLife;
    private SimpleDoubleProperty shieldActualLifeBinding;
    private SimpleIntegerProperty availablePoints;
    private SimpleDoubleProperty actualEnergy;
    private ArrayList <CommonShield> shields;
    private double shieldRadiusX;
    private double shieldAddX;
    private double shieldAddY;
    private double shieldRadiusY;
    private Arc shieldFieldArc;

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
        shields = new ArrayList();
        shieldMaxLifeOrigin = shield;
    }

    public void setActualEnergy(int cost) {
        energyActualValue -= cost;
        this.actualEnergy.set(((double)energyActualValue)/energyMaxValue);
    }

    public void setShieldActualLife(int shieldActualLife) {
        this.shieldActualLife = shieldActualLife;
        this.shieldActualLifeBinding.set(((double) shieldActualLife) / shieldMaxLife);

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

    public void addShieldBonus(CommonShield shield){

        shields.add(shield);
        setShieldMaxLife(shield.getShieldBonus());
        shield.isActiveProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue()){
                setShieldActualLife(getShieldActualLife() + shield.getActualShieldBonus());
            }else{
                setShieldActualLife(getShieldActualLife() - shield.getActualShieldBonus());
            }
        });
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
        if(newShieldLife < 0) {
            newShieldLife = 0;
        }

        shieldActualLife = newShieldLife;
        shieldActualLifeBinding.set(((double)shieldActualLife)/shieldMaxLife);

        if(shields.size() > 0){
            for(int i = shields.size() - 1; i >= 0; i--){
                if(!shields.get(i).isActive()){
                    continue;
                }

                int shieldBonus = shields.get(i).getActualShieldBonus();
                if(shieldBonus == 0){
                    continue;
                }

                if(shieldBonus < damage){
                    damage -=shieldBonus;
                    shields.get(i).setActualShieldBonus(0);
                    shields.remove(i);
                    continue;
                }

                shields.get(i).setActualShieldBonus(shieldBonus - damage);
                if(damage == shieldBonus){
                    shields.remove(i);
                    break;
                }
            }
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
                    ((AShipEquipment) shipEquipment).setActualShieldBonus(((AShipEquipment) shipEquipment).getActualShieldBonus());
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

    public abstract Pane getPane();

    public Placement [][] getPlacementPositions(){
        return placements;
    }


    public void restartValues(){
        setEnergyActualValue(getEnergyMaxValue());
        setActualLife(getTotalLife().get());
        shieldMaxLife = shieldMaxLifeOrigin;
        shieldActualLife = shieldMaxLifeOrigin;
        shieldActualLifeBinding.set(((double) shieldActualLife) / shieldMaxLife);
        actualEnergy.set(1);
        shields.clear();
    }

    public void createShield(){
        createShieldField();
        showShield(getShieldActualLife());
        showShieldBinding();
    }

    private void showShieldBinding(){
        getShieldActualLifeBinding().addListener((observable, oldValue, newValue) -> {
            showShield(newValue.doubleValue());
        });
    }

    private void showShield(double shieldLife){
        if(shieldLife == 0){
            if(getPane().getChildren().contains(shieldFieldArc)){
                getPane().getChildren().remove(shieldFieldArc);
            }
        }else {
            if(!getPane().getChildren().contains(shieldFieldArc)){
                getPane().getChildren().add(shieldFieldArc);
            }
        }
    }

    public boolean isOnShield(double x, double y){
        return shieldFieldArc.contains(x, y);
    }

    private void createShieldField(){
        shieldFieldArc = new Arc();

        shieldFieldArc.setCenterX(getCenterX() + getShieldAddX());
        shieldFieldArc.setCenterY(getCenterY() + getShieldAddY());

        if(isEnemy()){
            shieldFieldArc.setStartAngle(90);
            shieldFieldArc.setStyle("-fx-fill: linear-gradient(to right, rgba(0,0,255,0.8) 0%, rgba(0,0,255,0) 50%)");
        }else {
            shieldFieldArc.setStartAngle(270);
            shieldFieldArc.setStyle("-fx-fill: linear-gradient(to right, rgba(0,0,255,0) 50%, rgba(0,0,255,0.8) 100%)");
        }

        shieldFieldArc.setLength(180.0);
        shieldFieldArc.setRadiusX(getShieldRadiusX());
        shieldFieldArc.setRadiusY(getShieldRadiusY());
        shieldFieldArc.setType(ArcType.ROUND);

    }
}
