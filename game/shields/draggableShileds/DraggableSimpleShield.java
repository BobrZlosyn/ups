package game.shields.draggableShileds;

import game.construction.CommonDraggableObject;
import game.static_classes.GlobalVariables;
import game.construction.CommonModel;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.shields.SimpleShield;
import game.shields.shieldModels.SimpleShieldModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by BobrZlosyn on 01.10.2016.
 */
public class DraggableSimpleShield extends CommonDraggableObject {

    public DraggableSimpleShield(Pane pane, Placement[][] placements){
        init();
        CommonModel model = createModel(pane, createPrototypeModel());
        addListeners(model, placements);
    }

    public DraggableSimpleShield(Pane pane, Placement[][] placements, boolean isInPlace, Placement placement){
        modelInPlace = createModel(pane, createPrototypeModel());
        super.isInPlace = isInPlace;
        super.placement = placement;
        addListeners(modelInPlace, placements);
    }

    public DraggableSimpleShield(double x, double y){
        init();
        xPosition = x;
        yPosition = y;
        super.isInPlace = false;
        super.placement = null;
    }

    private void init(){
        xPosition = 100;
        yPosition = 200;
        addX1 = 120; // z leva do prava
        addX2 = 130; // z prava do leva
        addY1 = -10;
        addY2 = 0;
    }

    @Override
    protected CommonDraggableObject getDraggableObject(Pane showArea, Placement placement) {
        return new DraggableSimpleShield(showArea, placement.getShip().getPlacementPositions(), true, placement);
    }

    protected CommonModel createPrototypeModel(){
        return new SimpleShieldModel();
    }

    @Override
    public void createModel(Pane pane, Placement[][] placements, double x, double y) {
        if(GlobalVariables.isEmpty(modelInPlace)){
            modelInPlace = createModel(pane, createPrototypeModel());
            xPosition = x;
            yPosition = y;
            modelInPlace.setModelXY(xPosition, yPosition);
            addListeners(modelInPlace, placements);
        }else{
            modelInPlace.setModelXY(x, y);
        }

    }

    @Override
    public IShipEquipment getObject() {
        return new SimpleShield();
    }
}
