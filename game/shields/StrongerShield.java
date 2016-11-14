package game.shields;

import game.construction.CommonModel;
import game.construction.CommonWreck;
import game.construction.Placement;
import game.shields.shieldModels.SimpleShieldModel;
import game.shields.shieldModels.StrongerShieldModel;
import game.shields.wrecksShields.SimpleShieldWrecks;
import game.static_classes.ConstructionTypes;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Created by Kanto on 14.11.2016.
 */
public class StrongerShield extends CommonShield {
    private StrongerShieldModel model;

    public StrongerShield() {
        super(  GameBalance.STRONGER_SHIELD_EQUIPMENT_NAME,
                GameBalance.STRONGER_SHIELD_EQUIPMENT_LIFE,
                GameBalance.STRONGER_SHIELD_EQUIPMENT_ENERGY_COST,
                GameBalance.STRONGER_SHIELD_EQUIPMENT_POINTS_COST,
                GameBalance.STRONGER_SHIELD_EQUIPMENT_SHIELDS,
                GameBalance.STRONGER_SHIELD_EQUIPMENT_SUCCESS_CHANCE
            );

        setIsMarked(false);
        createShield();
    }

    private void createShield() {
        model = new StrongerShieldModel();
        model.getParts().forEach(shape -> {
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

        model.getShield().setX(x);
        model.getShield().setY(y);

        Pane gameArea = (Pane) place.getField().getParent();
        gameArea.getChildren().addAll(model.getParts());
        place.setIsShield(true);
        displayEquipmentExtension(place);
    }
    @Override
    public CommonModel getModel() {
        return model;
    }

    @Override
    public double getCenterX() {
        return  model.getShield().getX() + model.getShield().getWidth()/2;
    }

    @Override
    public double getCenterY() {
        return  model.getShield().getY() + model.getShield().getHeight()/2;
    }

    @Override
    public boolean containsPosition(double x, double y){
        return model.getShield().contains(x,y);
    }

    @Override
    public String getConstructionType() {
        return ConstructionTypes.STRONGER_SHIELD;
    }

    @Override
    protected CommonWreck getWreck() {
        return new SimpleShieldWrecks(getCenterX(), getCenterY(), Color.RED);
    }
}
