package game.weapons.draggableWeapons;

import game.construction.CommonDraggableObject;
import game.GlobalVariables;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.weapons.CommonWeapon;
import game.weapons.DoubleCannonWeapon;
import game.weapons.modelsWeapon.ModelDoubleCannon;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class DraggableDoubleCannon extends CommonDraggableObject{

    private double xPosition, yPosition, addX1, addX2, addY1, addY2;

    public DraggableDoubleCannon(Pane pane, Placement[][] placements){
        init();
        ModelDoubleCannon model = createCannon(pane);
        addListeners(model, placements);
    }

    public DraggableDoubleCannon(Pane pane, Placement[][] placements, double x, double y){
        init();
        ModelDoubleCannon model = createCannon(pane);
        addListeners(model, placements);
        model.setCannonXY(x, y);
        xPosition = x;
        yPosition = y;
    }

    private void init(){
        xPosition = 100;
        yPosition = 100;
        addX1 = 150;
        addX2 = 160;
        addY1 = 0;
        addY2 = 10;
    }

    private ModelDoubleCannon createCannon(Pane area){
        ModelDoubleCannon modelCannon = new ModelDoubleCannon();
        modelCannon.setCannonXY(xPosition,yPosition);

        area.getChildren().addAll(modelCannon.getParts());
        return modelCannon;

    }

    private void addListeners(ModelDoubleCannon modelCannon, Placement[][] placements){
        modelCannon.getParts().forEach(shape -> {
            shape.setOnMouseDragged(event -> {
                moveObject(event, modelCannon, placements);
            });
        });

        modelCannon.getParts().forEach(shape -> {
            shape.setOnMouseReleased(event -> {
                isDragSuccesful(event, modelCannon, placements);
            });
        });
    }

    private void isDragSuccesful(MouseEvent event, ModelDoubleCannon modelCannon, Placement [][] placements){

        Placement bluePlace = findPosition( placements, event.getSceneX(), event.getY(),addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            IShipEquipment cannonWeapon = new DoubleCannonWeapon();
            cannonWeapon.displayEquipment(bluePlace, false);
            bluePlace.setIsEmpty(false);
            bluePlace.setCommonWeapon(cannonWeapon);
        }

        modelCannon.setCannonXY(xPosition, yPosition);
    }

    private void moveObject(MouseEvent event, ModelDoubleCannon modelCannon, Placement [][] placements){
        modelCannon.setCannonXY(event.getSceneX(), event.getY());
        findPosition(placements, event.getSceneX(), event.getY(),addX1, addX2, addY1, addY2);
    }

}
