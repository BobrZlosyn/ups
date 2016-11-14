package game.shields.draggableShileds;

import game.construction.CommonDraggableObject;
import game.construction.CommonModel;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.shields.StrongerShield;
import game.shields.shieldModels.StrongerShieldModel;
import javafx.scene.layout.Pane;

/**
 * Created by Kanto on 14.11.2016.
 */
public class DraggableStrongerShield extends DraggableSimpleShield {

    public DraggableStrongerShield(Pane pane, Placement[][] placements) {
        super(pane, placements);
    }

    public DraggableStrongerShield(Pane pane, Placement[][] placements, boolean isInPlace, Placement placement){
        super(pane, placements, isInPlace, placement);
    }

    public DraggableStrongerShield(double x, double y){
        super(x, y);
    }


    @Override
    protected CommonDraggableObject getDraggableObject(Pane showArea, Placement placement) {
        return new DraggableStrongerShield(showArea, placement.getShip().getPlacementPositions(), true, placement);
    }

    @Override
    protected CommonModel createPrototypeModel(){
        return new StrongerShieldModel();
    }

    @Override
    public IShipEquipment getObject() {
        return new StrongerShield();
    }
}
