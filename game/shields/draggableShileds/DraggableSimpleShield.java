package game.shields.draggableShileds;

import game.construction.CommonDraggableObject;
import game.GlobalVariables;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.shields.SimpleShield;
import game.shields.shieldModels.ModelSimpleShield;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Created by BobrZlosyn on 01.10.2016.
 */
public class DraggableSimpleShield extends CommonDraggableObject {
    private double xPosition, yPosition, addX1, addX2, addY1, addY2;

    public DraggableSimpleShield(Pane pane, Placement[][] placements){
        init();
        ModelSimpleShield model = createShield(pane);
        addListeners(model, placements);
    }

    private void init(){
        xPosition = 100;
        yPosition = 200;
        addX1 = 135; // z leva do prava
        addX2 = 150; // z prava do leva
        addY1 = 20;
        addY2 = 30;
    }

    private  ModelSimpleShield createShield(Pane area){
        ModelSimpleShield simpleShield = new  ModelSimpleShield();
        simpleShield.setShieldXY(xPosition, yPosition);

        area.getChildren().addAll(simpleShield.getParts());
        return simpleShield;

    }

    private void addListeners(ModelSimpleShield modelSimpleShield, Placement[][] placements){
        modelSimpleShield.getParts().forEach(shape -> {
            shape.setOnMouseDragged(event -> {
                moveObject(event, modelSimpleShield, placements);
            });
        });

        modelSimpleShield.getParts().forEach(shape -> {
            shape.setOnMouseReleased(event -> {
                isDragSuccesful(event, modelSimpleShield, placements);
            });
        });
    }

    private void isDragSuccesful(MouseEvent event, ModelSimpleShield modelSimpleShield, Placement [][] placements){

        Placement bluePlace = findPosition( placements, event.getSceneX(), event.getY(),addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            IShipEquipment simpleShield = new SimpleShield();
            simpleShield.displayEquipment(bluePlace, false);
            bluePlace.setIsEmpty(false);
            bluePlace.setCommonWeapon(simpleShield);
        }

        modelSimpleShield.setShieldXY(xPosition, yPosition);
    }

    private void moveObject(MouseEvent event, ModelSimpleShield modelSimpleShield, Placement [][] placements){
        modelSimpleShield.setShieldXY(event.getSceneX(), event.getY());
        findPosition(placements, event.getSceneX(), event.getY(),addX1, addX2, addY1, addY2);
    }
}
