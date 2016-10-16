package game.shields;

import game.construction.CommonModel;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.construction.Placement;
import game.shields.shieldModels.SimpleShieldModel;
import game.ships.CommonShip;
import game.wrecks.SimpleShieldWrecks;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;

/**
 * Created by BobrZlosyn on 01.10.2016.
 */
public class SimpleShield extends CommonShield {
    private SimpleShieldModel commonShieldModel;

    public SimpleShield() {
        super(
                GameBalance.SHIELD_EQUIPMENT_NAME,
                GameBalance.SHIELD_EQUIPMENT_LIFE,
                GameBalance.SHIELD_EQUIPMENT_ENERGY_COST,
                GameBalance.SHIELD_EQUIPMENT_POINTS_COST,
                GameBalance.SHIELD_EQUIPMENT_SHIELDS,
                GameBalance.SHIELD_EQUIPMENT_SUCCESS_CHANCE
        );
        setIsMarked(false);
        createShield();
    }

    private void createShield() {
        commonShieldModel = new SimpleShieldModel();
        commonShieldModel.getParts().forEach(shape -> {
            markShield(shape);
        });
    }

    private void markShield(Shape shape){
        shape.setOnMouseClicked(event -> {
            markingHandle(isMarked(), this);
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

        commonShieldModel.getShield().setX(x);
        commonShieldModel.getShield().setY(y);

        Pane gameArea = (Pane) place.getField().getParent();
        gameArea.getChildren().addAll(commonShieldModel.getParts());

        displayEquipmentExtension(place);
    }

    @Override
    public void markObject() {
        commonShieldModel.getParts().forEach(shape -> {
            shape.setStroke(Color.BLUE);
            shape.setStrokeWidth(1.5);
        });

        setIsMarked(true);

        GlobalVariables.setMarkedObject(this);
        GlobalVariables.setName(getName());
        GlobalVariables.setCanTarget(false);
    }

    @Override
    public void unmarkObject() {
        commonShieldModel.getParts().forEach(shape -> {
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

        commonShieldModel.getParts().forEach(shape -> {
            shape.setStroke(Color.RED);
            shape.setStrokeWidth(1.5);
        });
        GlobalVariables.setTargetObject(this);
    }

    @Override
    public void cancelTarget() {
        commonShieldModel.getParts().forEach(shape -> {
            shape.setStroke(Color.TRANSPARENT);
        });
    }

    @Override
    public double getCenterX() {
        double x = commonShieldModel.getShield().getX() + commonShieldModel.getShield().getWidth()/2;
        return x;
    }

    @Override
    public double getCenterY() {
        double y = commonShieldModel.getShield().getY() + commonShieldModel.getShield().getHeight()/2;
        return y;
    }

    @Override
    public boolean containsPosition(double x, double y){
        return commonShieldModel.getShield().contains(x,y);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.SIMPLE_SHIELD;
    }

    @Override
    public CommonModel getModel() {
        return commonShieldModel;
    }

    @Override
    public void destroy() {
        double x = getPlacement().getX();
        double y = getPlacement().getY();

        Pane gameArea = getModel().getParent();
        gameArea.getChildren().removeAll(getModel().getParts());

        SimpleShieldWrecks wrecks = new SimpleShieldWrecks();
        wrecks.addWrecksToPane(gameArea, x, y);
    }
}
