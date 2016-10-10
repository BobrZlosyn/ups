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
        CommonModel model = createModel(pane, new SimpleShieldModel());
        addListeners(model, placements);
    }

    public DraggableSimpleShield(Pane pane, Placement[][] placements, boolean isInPlace, Placement placement){
        modelInPlace = createModel(pane, new SimpleShieldModel());
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
        addX1 = 115; // z leva do prava
        addX2 = 130; // z prava do leva
        addY1 = -10;
        addY2 = 0;
    }

    public void isDragSuccesful(MouseEvent event, CommonModel commonModel, Placement[][] placements){
        double widthPane = commonModel.getParent().getWidth()/2;
        double widthModel = commonModel.getWidth()/2;
        double paneY = commonModel.getParent().getLayoutY();

        Placement bluePlace = findPosition( placements, event.getX() - widthPane + widthModel, event.getSceneY() - paneY,addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.getField().getFill().equals(Color.RED)){
            bluePlace.getField().setFill(Color.WHITE);
            return;
        }

        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            Pane showArea = ((Pane)bluePlace.getField().getParent());
            DraggableSimpleShield simpleShield = new DraggableSimpleShield(showArea, bluePlace.getShip().getPlacementPositions(), true, bluePlace);
            double x = bluePlace.getX() + commonModel.getWidth()/2;
            double y = bluePlace.getY() + commonModel.getWidth()/2;

            simpleShield.getModel().setModelXY(x, y);
            bluePlace.setIsEmpty(false);
            bluePlace.setShipEquipment(simpleShield);
            substractPoints();
        }
    }

    @Override
    public void createModel(Pane pane, Placement[][] placements, double x, double y) {
        if(GlobalVariables.isEmpty(modelInPlace)){
            modelInPlace = createModel(pane, new SimpleShieldModel());
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
