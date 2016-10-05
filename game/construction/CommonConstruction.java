package game.construction;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public abstract class CommonConstruction implements IMarkableObject{
    private SimpleDoubleProperty totalLife;
    private String name;
    private boolean isEnemy;
    private SimpleDoubleProperty actualLife;
    private SimpleDoubleProperty actualLifeBinding;
    private Placement placement;
    private int energyCost;

    public CommonConstruction(int totalLife, String name){
        setName(name);
        this.totalLife = new SimpleDoubleProperty(totalLife);
        this.actualLife = new SimpleDoubleProperty(totalLife);
        this.actualLifeBinding = new SimpleDoubleProperty(1);
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

    public double getActualLife() {
        return actualLife.get()/totalLife.get();
    }

    public void takeDamage(int damage){
        double life = getTotalLife().get() - damage;
        if(life > 0){
            setActualLife(life);
            setActualLifeBinding(getActualLife());
        }else {
            setActualLife(0);
            setActualLifeBinding(0);
        }
    }

    public void destroy(){
        setActualLife(0);
        setActualLifeBinding(0);
    }

    public void resize(double widthStart, double widthEnd, double heightStart, double heightEnd){
        return;
    }

    public boolean containsPosition(double x, double y){
        return false;
    }

    public abstract double getCenterX();

    public abstract double getCenterY();

    public Placement getPlacement() {
        return placement;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public abstract String getConstructionType();
}
