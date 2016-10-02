package game.shields.draggableShileds;

import game.construction.CommonDraggableObject;
import game.GlobalVariables;
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
        CommonModel model = createModel(pane, new SimpleShieldModel());
        addListeners(model, placements);
    }

    public DraggableSimpleShield(Pane pane, Placement[][] placements, boolean isInPlace, Placement placement){
        modelInPlace = createModel(pane, new SimpleShieldModel());
        super.isInPlace = isInPlace;
        super.placement = placement;
        addListeners(modelInPlace, placements);
    }

    private void init(){
        xPosition = 100;
        yPosition = 200;
        addX1 = 135; // z leva do prava
        addX2 = 150; // z prava do leva
        addY1 = 20;
        addY2 = 30;
    }

    protected void isDragSuccesful(MouseEvent event, CommonModel commonModel, Placement [][] placements){

        Placement bluePlace = findPosition( placements, event.getSceneX(), event.getY(),addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            Pane showArea = ((Pane)bluePlace.getField().getParent());
            DraggableSimpleShield simpleShield = new DraggableSimpleShield(showArea, bluePlace.getShip().getPlacementPositions(), true, bluePlace);
            double x = bluePlace.getX();
            double y = bluePlace.getY();

            simpleShield.getModel().setModelXY(x, y);
            bluePlace.setIsEmpty(false);
            bluePlace.setShipEquipment(simpleShield);
        }

        commonModel.setModelXY(xPosition, yPosition);
    }

    @Override
    protected void moveToAnotherPlace(MouseEvent event, CommonModel commonModel, Placement[][] placements) {
        Placement bluePlace = findPosition( placements, event.getX(), event.getY(),addX1, addX2, addY1, addY2);

        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.getRow() == placement.getRow() && bluePlace.getColumn() == placement.getColumn()){
            double x = bluePlace.getX();
            double y = bluePlace.getY();
            commonModel.setModelXY(x, y);

            return;
        }

        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){

            //mazani stareho
            placement.setIsEmpty(true);
            placement.setShipEquipment(null);
            placement.getField().setFill(Color.WHITE);

            double x = bluePlace.getX();
            double y = bluePlace.getY();
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
        return new SimpleShield();
    }
}
