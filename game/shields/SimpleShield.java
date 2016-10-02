package game.shields;

import game.GlobalVariables;
import game.construction.IMarkableObject;
import game.construction.Placement;
import game.shields.shieldModels.ModelSimpleShield;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;

/**
 * Created by BobrZlosyn on 01.10.2016.
 */
public class SimpleShield extends CommonShield implements IMarkableObject {
    private ModelSimpleShield modelSimpleShield;
    private boolean isMark;

    public SimpleShield() {
        super(150, "Simple shield");
        setIsMark(false);
        createShield();
    }

    public void setIsMark(boolean isMark) {
        this.isMark = isMark;
    }

    public boolean isMark() {
        return isMark;
    }

    private void createShield() {
        modelSimpleShield = new ModelSimpleShield();
        modelSimpleShield.getParts().forEach(shape -> {
            markShield(shape);
        });

    }

    private void markShield(Shape shape){
        shape.setOnMouseClicked(event -> {
            if(GlobalVariables.isTargeting){
                target();
                return;
            }

            if(!isMark()){
                markObject();
            }else{
                unmarkObject();
            }
        });
    }

    @Override
    public void displayEquipment(Placement place, boolean isEnemy) {
        double x = place.getX();
        double y = place.getY();
        setIsEnemy(isEnemy);
        if(!place.isEmpty()){
            return;
        }

        modelSimpleShield.getShield().setX(x);
        modelSimpleShield.getShield().setY(y);

        Pane gameArea = (Pane) place.getField().getParent();
        gameArea.getChildren().addAll(modelSimpleShield.getParts());
        if(!GlobalVariables.isShieldUp()){
            Arc arc = createShieldField(place);
            gameArea.getChildren().add(arc);
            GlobalVariables.setIsShieldUp(true);
        }

        place.setIsEmpty(false);
    }

    private Arc createShieldField(Placement place){
        IMarkableObject ship = (IMarkableObject) place.getShip();
        Arc arc = new Arc();

        if(isEnemy()){
            arc.setCenterX(ship.getPlacement().getX() - 200);
            arc.setCenterY(ship.getPlacement().getY() + 115);
            arc.setStartAngle(90);
            arc.setLength(180.0);
            arc.setStyle("-fx-fill: linear-gradient(to right, rgba(0,0,255,1) 0%, rgba(0,0,0,0) 65%)");
        }else {
            arc.setCenterX(ship.getPlacement().getX() + 150);
            arc.setCenterY(ship.getPlacement().getY() - 0);
            arc.setStartAngle(270);
            arc.setLength(180.0);
            arc.setStyle("-fx-fill: linear-gradient(to right, rgba(0,0,0,0) 50%, rgba(0,0,255,0.8) 100%)");
        }

        arc.setRadiusX(50);
        arc.setRadiusY(250);

        arc.setType(ArcType.ROUND);

        return arc;
    }

    @Override
    public void markObject() {
        modelSimpleShield.getParts().forEach(shape -> {
            shape.setStroke(Color.BLUE);
            shape.setStrokeWidth(1.5);
        });

        setIsMark(true);

        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void unmarkObject() {
        modelSimpleShield.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });

        setIsMark(false);
        GlobalVariables.setMarkedObject(null);
        GlobalVariables.setName("");
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void target() {
        modelSimpleShield.getParts().forEach(shape -> {
            shape.setStroke(Color.RED);
            shape.setStrokeWidth(1.5);
        });
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        modelSimpleShield.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });
    }

    @Override
    public Placement getPlacement() {
        double x = modelSimpleShield.getShield().getX() - modelSimpleShield.getShield().getWidth()/2;
        double y = modelSimpleShield.getShield().getY() - modelSimpleShield.getShield().getHeight()/2;
        return new Placement(x, y, modelSimpleShield.getShield().getWidth(), null);
    }
}
