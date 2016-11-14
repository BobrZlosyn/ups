package game.construction;

import game.static_classes.ConstructionTypes;
import game.static_classes.GlobalVariables;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Shape;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public abstract class CommonConstruction implements IMarkableObject{
    private SimpleDoubleProperty totalLife;
    private String name;
    private boolean isEnemy, isMarked;
    private SimpleDoubleProperty actualLife;
    private SimpleDoubleProperty actualLifeBinding;
    private Placement placement;
    private int energyCost;


    public CommonConstruction(int totalLife, String name){
        setName(name);
        this.totalLife = new SimpleDoubleProperty(totalLife);
        this.actualLife = new SimpleDoubleProperty(totalLife);
        this.actualLifeBinding = new SimpleDoubleProperty(1);
        isMarked = false;
    }

    public void setIsMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalLife(double totalLife) {
        this.totalLife.set(totalLife);
    }

    public void setActualLife(double actualLife) {
        this.actualLifeBinding.set(actualLife /totalLife.get());
        this.actualLife.set(actualLife);
    }

    public void setActualLifeBinding(double actualLifeBinding) {
        this.actualLifeBinding.set(actualLifeBinding);
    }

    public void setIsEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }

    public String getName() {
        return name;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public SimpleDoubleProperty getTotalLife() {
        return totalLife;
    }

    public SimpleDoubleProperty getActualLifeBinding() {
        return actualLifeBinding;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public double getActualLife() {
        return actualLife.get();
    }

    public void takeDamage(int damage){
        double life = getActualLife() - damage;
        if(life > 0){
            setActualLife(life);
            setActualLifeBinding(actualLife.get()/totalLife.get());
        }else {
            setActualLife(0);
            setActualLifeBinding(0);
        }

        if(damage != 0){
            damageHit();
        }

        if (getActualLife() <= 0){
            destroy();

            if(ConstructionTypes.isEquipment(getConstructionType())){
                getPlacement().getShip().takeDamage(50);
            }

            if(!GlobalVariables.isEmpty(GlobalVariables.getMarkedObject()) && GlobalVariables.getMarkedObject().equals(this)){
                GlobalVariables.setMarkedObject(null);
            }

            if(!GlobalVariables.isEmpty(GlobalVariables.getTargetObject()) && GlobalVariables.getTargetObject().equals(this)){
                GlobalVariables.setTargetObject(this);
            }
        }
    }

    public abstract void destroy();

    public abstract void damageHit();

    public void resize(double widthStart, double widthEnd, double heightStart, double heightEnd){
        return;
    }

    public abstract boolean containsPosition(double x, double y);

    public abstract double getCenterX();

    public abstract double getCenterY();

    public Placement getPlacement() {
        return placement;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public abstract String getConstructionType();

    protected void markShape(Shape shape){
        shape.setOnMouseClicked(event -> {
            markingHandle(isMarked(), this);
        });
    }
}
