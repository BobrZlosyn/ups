package game.weapons.draggableWeapons;

import game.construction.CommonDraggableObject;
import game.static_classes.GlobalVariables;
import game.construction.CommonModel;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.weapons.CannonWeapon;
import game.weapons.modelsWeapon.ModelCannon;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class DraggableCannon extends CommonDraggableObject{

    public DraggableCannon(Pane pane, Placement[][] placements, double x, double y){
        init();
        xPosition = x;
        yPosition = y;
        modelInPlace = createModel(pane, new ModelCannon());
        modelInPlace.setModelXY(x, y);
        super.isInPlace = false;
        super.placement = null;
        addListeners(modelInPlace, placements);
    }

    public DraggableCannon(double x, double y){
        init();
        xPosition = x;
        yPosition = y;
        super.isInPlace = false;
        super.placement = null;
    }

    public DraggableCannon(Pane pane, Placement[][] placements, boolean isInPlace, Placement placement){
        modelInPlace = createModel(pane, new ModelCannon());
        super.isInPlace = isInPlace;
        super.placement = placement;
        addListeners(modelInPlace, placements);
    }

    private void init(){
        xPosition = 100;
        yPosition = 100;
        addX1 = 120;
        addX2 = 130;
        addY1 = 0;
        addY2 = 10;
    }

    @Override
    protected CommonDraggableObject getDraggableObject(Pane showArea, Placement placement) {
        return new DraggableCannon(showArea, placement.getShip().getPlacementPositions(), true, placement);
    }

    @Override
    public void createModel(Pane pane, Placement [][] placements, double x, double y) {
        if(GlobalVariables.isEmpty(modelInPlace)){
            modelInPlace = createModel(pane, new ModelCannon());
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
        return new CannonWeapon();
    }

}
