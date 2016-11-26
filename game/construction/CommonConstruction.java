package game.construction;

import game.static_classes.ConstructionTypes;
import game.static_classes.GlobalVariables;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public abstract class CommonConstruction implements IMarkableObject{
    private SimpleDoubleProperty totalLife;
    private String name;
    private boolean isEnemy, isMarked, firstHit;
    private SimpleDoubleProperty actualLife;
    private SimpleDoubleProperty actualLifeBinding;
    private Placement placement;
    private int energyCost;
    private Timeline hit;
    private ArrayList <Label> damage;
    private ArrayList <Integer> hitCount;



    public CommonConstruction(int totalLife, String name){
        setName(name);
        this.totalLife = new SimpleDoubleProperty(totalLife);
        this.actualLife = new SimpleDoubleProperty(totalLife);
        this.actualLifeBinding = new SimpleDoubleProperty(1);
        isMarked = false;
        createTimelineHit();
        damage = new ArrayList();
        hitCount = new ArrayList();
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

    public void setDamageLabel(int damage) {

        Pane parent = getModel().getParent();
        if(GlobalVariables.isEmpty(parent)) {
             return;
        }

        Label labelDamage = new Label("-" + damage);
        labelDamage.setTextFill(Color.RED);
        parent.getChildren().add(labelDamage);
        labelDamage.setLayoutX(getModel().getCenterX());
        labelDamage.setLayoutY(getModel().getCenterY() - getModel().getHeight() / 2 + 10);

        this.damage.add(labelDamage);
        hitCount.add(0);
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
            setDamageLabel(damage);
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

    protected void createTimelineHit(){

        hit = new Timeline(new KeyFrame(Duration.seconds(GlobalVariables.damageHitDuration), event -> {

            if((hitCount.isEmpty() || hitCount.get(0).equals(5))){
                getModel().setDefaultSkin();
            }

            //zastaveni timeline
            if(damage.isEmpty()){
                hit.stop();
            }

            for (int i = 0; i < damage.size(); i++) {

                Label label = damage.get(i);
                label.setOpacity(1 - hitCount.get(i)/100);

                //pohyb prvku na plose
                label.setLayoutX(label.getLayoutX() + 0.1);
                label.setLayoutY(label.getLayoutY() - 0.3);

                //zmizeni prvku s plochy
                if(hitCount.get(i).equals(80)){
                    Pane parent = ((Pane)label.getParent());
                    if(GlobalVariables.isNotEmpty(parent)) {
                        parent.getChildren().remove(label);
                    }

                    damage.remove(label);
                    hitCount.remove(hitCount.get(i));
                    i--;
                }else {
                    hitCount.set(i, hitCount.get(i) + 1);
                }
            }


        }));
        hit.setCycleCount(Animation.INDEFINITE);
    }

    public void damageHit() {
        firstHit = true;
        getModel().getParts().forEach(shape -> shape.setFill(GlobalVariables.damageHit));
        hit.playFromStart();
    }

    public void resize(double widthStart, double widthEnd, double heightStart, double heightEnd){
        return;
    }

    public boolean containsPosition(double x, double y){
        return getModel().containsPosition(x, y);
    }

    public double getCenterX(){
        return getModel().getCenterX();
    }

    public double getCenterY() {
        return getModel().getCenterY();
    }

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

    public abstract CommonModel getModel();

    @Override
    public void markObject() {
        if(!GlobalVariables.isGameStartUp()){
            return;
        }

        getModel().getParts().forEach(shape -> {
            shape.setStroke(MARKED_OBJECT_COLOR);
            shape.setStrokeWidth(1.5);
        });

        setIsMarked(true);

        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());

        boolean canTarget = !isEnemy() && getPlacement().isWeapon();
        GlobalVariables.setCanTarget(canTarget);

    }

    @Override
    public void unmarkObject() {
        getModel().getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });

        setIsMarked(false);
        GlobalVariables.setMarkedObject(null);
        GlobalVariables.setName("");
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void target() {
        if(!isEnemy()){
            return;
        }

        getModel().getParts().forEach(shape -> {
            shape.setStroke(TARGET_OBJECT_COLOR);
            shape.setStrokeWidth(1.5);
        });
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        getModel().getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });
    }

}
