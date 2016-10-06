package game.weapons.draggableWeapons;

import game.construction.*;
import game.GlobalVariables;
import game.weapons.DoubleCannonWeapon;
import game.weapons.modelsWeapon.ModelDoubleCannon;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class DraggableDoubleCannon extends CommonDraggableObject{

    public DraggableDoubleCannon(Pane pane, Placement[][] placements, boolean isInPlace, Placement placement){
       // init();
        modelInPlace = createModel(pane, new ModelDoubleCannon());
        super.isInPlace = isInPlace;
        super.placement = placement;
        addListeners(modelInPlace, placements);
    }

    public DraggableDoubleCannon(Pane pane, Placement[][] placements, double x, double y){
        init();
        modelInPlace = createModel(pane, new ModelDoubleCannon());
        modelInPlace.setModelXY(x, y);
        xPosition = x;
        yPosition = y;
        super.isInPlace = false;
        super.placement = null;
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

    @Override
    public IShipEquipment getObject() {
        return new DoubleCannonWeapon();
    }

    @Override
    protected void isDragSuccesful(MouseEvent event, CommonModel commonModel, Placement[][] placements) {
        double widthPane = commonModel.getParent().getWidth()/2;
        double widthModel = commonModel.getWidth()/2;
        double heightPane = commonModel.getParent().getLayoutY();

        Placement bluePlace = findPosition( placements, event.getX() - widthPane + widthModel, event.getY() + heightPane,addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            Pane showArea = ((Pane)bluePlace.getField().getParent());
            DraggableDoubleCannon draggableDoubleCannon = new DraggableDoubleCannon(showArea, bluePlace.getShip().getPlacementPositions(), true, bluePlace);
            double x = bluePlace.getX() + bluePlace.getSize()/2;
            double y = bluePlace.getY() + bluePlace.getSize()/2;

            draggableDoubleCannon.getModel().setModelXY(x, y);
            bluePlace.setIsEmpty(false);
            bluePlace.setShipEquipment(draggableDoubleCannon);
        }

        commonModel.setModelXY(xPosition, yPosition);
    }

    @Override
    protected void moveToAnotherPlace(MouseEvent event, CommonModel commonModel, Placement[][] placements) {
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
}
