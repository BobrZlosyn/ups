package game.shields;

import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.construction.Placement;
import game.shields.shieldModels.SimpleShieldModel;
import game.shields.wrecksShields.SimpleShieldWrecks;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
            markShape(shape);
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
        place.setIsShield(true);
        displayEquipmentExtension(place);
    }


    @Override
    public double getCenterX() {
        return commonShieldModel.getShield().getX() + commonShieldModel.getShield().getWidth()/2;
    }

    @Override
    public double getCenterY() {
        return commonShieldModel.getShield().getY() + commonShieldModel.getShield().getHeight()/2;
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
    protected CommonWreck getWreck() {
        return new SimpleShieldWrecks(getCenterX(), getCenterY(), Color.RED);
    }
}
