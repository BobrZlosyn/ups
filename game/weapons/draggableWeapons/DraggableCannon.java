package game.weapons.draggableWeapons;

import game.construction.CommonDraggableObject;
import game.GlobalVariables;
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

    public DraggableCannon(Pane pane, Placement[][] placements){
        init();
        CommonModel model = createModel(pane, new ModelCannon());
        super.isInPlace = false;
        addListeners(model, placements);
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
        addX1 = 150;
        addX2 = 160;
        addY1 = 0;
        addY2 = 10;
    }


    protected void isDragSuccesful(MouseEvent event, CommonModel modelCannon, Placement [][] placements){

        Placement bluePlace = findPosition( placements, event.getSceneX(), event.getY(),addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            Pane showArea = ((Pane)bluePlace.getField().getParent());
            DraggableCannon cannonWeapon = new DraggableCannon(showArea, bluePlace.getShip().getPlacementPositions(), true, bluePlace);
            double x = bluePlace.getX() + bluePlace.getSize()/2;
            double y = bluePlace.getY() + bluePlace.getSize()/2;

            cannonWeapon.getModel().setModelXY(x, y);
            bluePlace.setIsEmpty(false);
            bluePlace.setShipEquipment(cannonWeapon);
        }

        modelCannon.setModelXY(xPosition, yPosition);

    }


    protected void moveToAnotherPlace(MouseEvent event, CommonModel commonModel, Placement [][] placements){
        Placement bluePlace = findPosition( placements, event.getX(), event.getY(),addX1, addX2, addY1, addY2);

        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.getRow() == placement.getRow() && bluePlace.getColumn() == placement.getColumn()){
            double x = bluePlace.getX() + bluePlace.getSize()/2;
            double y = bluePlace.getY() + bluePlace.getSize()/2;
            commonModel.setModelXY(x, y);

            return;
        }

        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){

            //mazani stareho
            placement.setIsEmpty(true);
            placement.setShipEquipment(null);
            placement.getField().setFill(Color.WHITE);

            double x = bluePlace.getX() + bluePlace.getSize()/2;
            double y = bluePlace.getY() + bluePlace.getSize()/2;
            commonModel.setModelXY(x, y);

            //pridani noveho
            placement = bluePlace;
            placement.setIsEmpty(false);
            placement.setShipEquipment(this);


        }else{
            removeObject();
        }
    }

    @Override
    public IShipEquipment getObject() {
        return new CannonWeapon();
    }

}
