package game.weapons.draggableWeapons;

import game.construction.CommonDraggableObject;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.static_classes.GlobalVariables;
import game.weapons.SimpleLaserWeapon;
import game.weapons.modelsWeapon.ModelCannon;
import game.weapons.modelsWeapon.ModelSimpleLaserWeapon;
import javafx.scene.layout.Pane;

/**
 * Created by BobrZlosyn on 15.11.2016.
 */
public class DraggableSImpleLaser extends CommonDraggableObject {

    public DraggableSImpleLaser(Pane pane, Placement[][] placements, double x, double y){
        init();
        xPosition = x;
        yPosition = y;
        modelInPlace = createModel(pane, new ModelSimpleLaserWeapon());
        modelInPlace.setModelXY(x, y);
        super.isInPlace = false;
        super.placement = null;
        addListeners(modelInPlace, placements);
    }

    public DraggableSImpleLaser(double x, double y){
        init();
        xPosition = x;
        yPosition = y;
        super.isInPlace = false;
        super.placement = null;
    }

    public DraggableSImpleLaser(Pane pane, Placement[][] placements, boolean isInPlace, Placement placement){
        modelInPlace = createModel(pane, new ModelSimpleLaserWeapon());
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
    public void createModel(Pane pane, Placement[][] placements, double x, double y) {
        if(GlobalVariables.isEmpty(modelInPlace)){
            modelInPlace = createModel(pane, new ModelSimpleLaserWeapon());
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
        return new SimpleLaserWeapon();
    }

    @Override
    protected CommonDraggableObject getDraggableObject(Pane showArea, Placement placement) {
        return null;
    }
}
