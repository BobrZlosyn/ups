package game.weapons.draggableWeapons;

import game.construction.*;
import game.static_classes.GlobalVariables;
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

    public DraggableDoubleCannon(double x, double y){
        init();
        xPosition = x;
        yPosition = y;
        super.isInPlace = false;
        super.placement = null;
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
            modelInPlace = createModel(pane, new ModelDoubleCannon());
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
        return new DoubleCannonWeapon();
    }

    @Override
    public void isDragSuccesful(MouseEvent event, CommonModel commonModel, Placement[][] placements) {
        double widthPane = commonModel.getParent().getWidth()/2;
        double widthModel = commonModel.getWidth()/2;
        double paneY = commonModel.getParent().getLayoutY();

        double objectPositionX = event.getX() - widthPane + widthModel;
        double objectPositionY = event.getSceneY() - paneY;

        Placement bluePlace = findPosition( placements, objectPositionX  , objectPositionY ,addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.getField().getFill().equals(Color.RED)){
            bluePlace.getField().setFill(Color.WHITE);
            return;
        }

        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            Pane showArea = ((Pane)bluePlace.getField().getParent());
            DraggableDoubleCannon draggableDoubleCannon = new DraggableDoubleCannon(showArea, bluePlace.getShip().getPlacementPositions(), true, bluePlace);
            double x = bluePlace.getX() + bluePlace.getSize()/2;
            double y = bluePlace.getY() + bluePlace.getSize()/2;

            draggableDoubleCannon.getModel().setModelXY(x, y);
            bluePlace.setIsEmpty(false);
            bluePlace.setShipEquipment(draggableDoubleCannon);
            substractPoints();
        }
    }
}
