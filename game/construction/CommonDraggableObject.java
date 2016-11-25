package game.construction;

import game.static_classes.GlobalVariables;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

                    if(GlobalVariables.choosenShip.getAvailablePoints() < ((AShipEquipment)getObject()).getCostOfEquipment()){
                        if( placements[i][j].isFullColorSet()){
                            continue;
                        }
                        placements[i][j].setErrorPlaceColor();
                        place = placements[i][j];

                    }else{
                        placements[i][j].setFullPlaceColor();
                        place = placements[i][j];
                    }
                }else{
                    if(placements[i][j].isEmpty()){
                        placements[i][j].setClearPlaceColor();
                    }
                }
            }
        }

        return place;
    }

    public void setModelInPlace(CommonModel modelInPlace) {
        this.modelInPlace = modelInPlace;
    }

    public abstract void createModel(Pane pane, Placement [][] placements, double x, double y);

    public void removeModel(Pane pane){
        pane.getChildren().removeAll(modelInPlace.getParts());
        modelInPlace = null;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
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

    public void moveObject(MouseEvent event, CommonModel commonModel, Placement [][] placements){
        double widthPane = commonModel.getParent().getWidth()/2;
        double widthModel = commonModel.getWidth()/2;
        double paneY = commonModel.getParent().getLayoutY();
        if(isInPlace){
            placement.setClearPlaceColor();
            commonModel.setModelXY(event.getX(), event.getY());
            findPosition(placements, event.getX(), event.getY(),addX1, addX2, addY1, addY2);
        }else {
            commonModel.setModelXY(event.getX(), event.getSceneY() - paneY);
            findPosition(placements, event.getX() - widthPane + widthModel, event.getSceneY() - paneY,addX1, addX2, addY1, addY2);
        }
    }

    public void addListeners(CommonModel commonModel, Placement[][] placements){
        if(isInPlace){
            modelInPlace.getParts().forEach(shape -> {
                shape.setOnMousePressed(event -> {
                    addPoints();
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

    protected void removeObject(){
        Pane showArea = modelInPlace.getParent();
        showArea.getChildren().removeAll(modelInPlace.getParts());

        placement.setIsEmpty(true);
        placement.setShipEquipment(null);
        placement.setClearPlaceColor();
    }

    protected void moveToAnotherPlace(MouseEvent event, CommonModel commonModel, Placement[][] placements) {

        Placement bluePlace = findPosition( placements, event.getX(), event.getY(),addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isWarningColorSet()){
            bluePlace.setClearPlaceColor();
            return;
        }

        //pridani na stejnou pozici
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.getRow() == placement.getRow() && bluePlace.getColumn() == placement.getColumn()){
            double x = bluePlace.getX() + bluePlace.getSize()/2;
            double y = bluePlace.getY() + bluePlace.getSize()/2;
            commonModel.setModelXY(x, y);
            substractPoints();
            return;
        }

        //presunuti na jinou pozici
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            substractPoints();
            //mazani stareho
            placement.setIsEmpty(true);
            placement.setShipEquipment(null);
            placement.setClearPlaceColor();

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

    protected void substractPoints(){
        AShipEquipment equipment = (AShipEquipment) getObject();
        GlobalVariables.choosenShip.setAvailablePoints(equipment.getCostOfEquipment());
    }

    protected void addPoints(){
        if(isInPlace){
            AShipEquipment equipment = (AShipEquipment) getObject();
            GlobalVariables.choosenShip.setAvailablePoints(-equipment.getCostOfEquipment());
        }
    }

    protected abstract CommonDraggableObject getDraggableObject(Pane showArea, Placement placement);

    public void isDragSuccesful(MouseEvent event, CommonModel commonModel, Placement [][] placements){
        double widthPane = commonModel.getParent().getWidth()/2;
        double widthModel = commonModel.getWidth()/2;
        double paneY = commonModel.getParent().getLayoutY();

        double objectPositionX = event.getX() - widthPane + widthModel;
        double objectPositionY = event.getSceneY() - paneY;

        Placement bluePlace = findPosition( placements, objectPositionX , objectPositionY,addX1, addX2, addY1, addY2);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isWarningColorSet()){
            bluePlace.setClearPlaceColor();
            return;
        }

        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            Pane showArea = ((Pane)bluePlace.getField().getParent());
            CommonDraggableObject draggableObject = getDraggableObject(showArea, bluePlace);
            double x = bluePlace.getX() + bluePlace.getSize()/2;
            double y = bluePlace.getY() + bluePlace.getSize()/2;

            draggableObject.getModel().setModelXY(x, y);
            bluePlace.setIsEmpty(false);
            bluePlace.setShipEquipment(draggableObject);
            substractPoints();
        }
    }
}
