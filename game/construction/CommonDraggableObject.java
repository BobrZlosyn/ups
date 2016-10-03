package game.construction;

import game.weapons.modelsWeapon.ModelCannon;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 30.09.2016.
 */
public abstract class CommonDraggableObject implements IShipEquipment{

    protected CommonModel modelInPlace;
    protected boolean isInPlace;
    protected Placement placement;
    protected double xPosition, yPosition, addX1, addX2, addY1, addY2;

    protected Placement findPosition(Placement[][] placements, double x, double y, double xAdd1, double xAdd2, double yAdd1, double yAdd2 ){
        Placement place = null;
        for(int i = 0; i < placements.length; i++){
            for (int j = 0; j < placements[i].length; j++){
                if(placements[i][j] == null){
                    continue;
                }

                Rectangle pokus = placements[i][j].getField();
                //pravy horni roh, levy horni roh, PH dolu, LH dolu
                if(pokus.contains(x - xAdd1, y + yAdd1) || pokus.contains(x - xAdd2, y + yAdd1)
                        || pokus.contains(x - xAdd1, y + yAdd2) || pokus.contains(x - xAdd2, y + yAdd2)){
                    pokus.setFill(Color.BLUE);
                    place = placements[i][j];
                }else{
                    if(placements[i][j].isEmpty()){
                        pokus.setFill(Color.WHITE);
                    }
                }
            }
        }

        return place;
    }

    public void setModelInPlace(CommonModel modelInPlace) {
        this.modelInPlace = modelInPlace;
    }

    public CommonModel getModel() {
        return modelInPlace;
    }

    public abstract IShipEquipment getObject();

    @Override
    public void displayEquipment(Placement place, boolean isEnemy) {}

    protected CommonModel createModel(Pane area, CommonModel commonModel){
        commonModel.setModelXY(xPosition, yPosition);

        area.getChildren().addAll(commonModel.getParts());
        return commonModel;
    }

    protected void moveObject(MouseEvent event, CommonModel commonModel, Placement [][] placements){
        if(isInPlace){
            placement.getField().setFill(Color.WHITE);
            commonModel.setModelXY(event.getX(), event.getY());
            findPosition(placements, event.getX(), event.getY(),addX1, addX2, addY1, addY2);
        }else {
            commonModel.setModelXY(event.getSceneX(), event.getY());
            findPosition(placements, event.getSceneX(), event.getY(),addX1, addX2, addY1, addY2);
        }

    }

    protected void addListeners(CommonModel commonModel, Placement[][] placements){
        if(isInPlace){
            modelInPlace.getParts().forEach(shape -> {
                shape.setOnMousePressed(event -> {
                    Pane showArea = modelInPlace.getParent();
                    showArea.getChildren().removeAll(modelInPlace.getParts());
                    showArea.getChildren().addAll(modelInPlace.getParts());
                });
            });
        }

        commonModel.getParts().forEach(shape -> {
            shape.setOnMouseDragged(event -> {
                moveObject(event,commonModel, placements);
            });
        });

        commonModel.getParts().forEach(shape -> {
            shape.setOnMouseReleased(event -> {
                if (isInPlace) {
                    moveToAnotherPlace(event, commonModel, placements);
                } else {
                    isDragSuccesful(event, commonModel, placements);
                }
            });
        });
    }

    protected abstract void isDragSuccesful(MouseEvent event, CommonModel commonModel, Placement [][] placements);

    protected void removeObject(){
        Pane showArea = modelInPlace.getParent();
        showArea.getChildren().removeAll(modelInPlace.getParts());

        placement.setIsEmpty(true);
        placement.setShipEquipment(null);
        placement.getField().setFill(Color.WHITE);
    }

    protected abstract void moveToAnotherPlace(MouseEvent event, CommonModel commonModel, Placement [][] placements);
}