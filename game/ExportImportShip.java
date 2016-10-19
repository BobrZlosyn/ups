package game;

import game.construction.*;
import game.ships.CommonShip;
import game.static_classes.ConstructionTypes;
import game.static_classes.GlobalVariables;
import javafx.scene.layout.Pane;

/**
 * Created by Kanto on 03.10.2016.
 */
public class ExportImportShip {

    public ExportImportShip(){

    }

    public String exportShip(CommonShip ship){

        StringBuilder shipInformation = new StringBuilder();
        shipInformation.append(ship.getConstructionType() + ";;");

        Placement[][] placements = ship.getPlacementPositions();
        for(int i = 0; i < placements.length; i++){
            for(int j = 0; j < placements[i].length; j++){
                Placement placement = placements[i][j];
                if(GlobalVariables.isEmpty(placement) || placement.isEmpty()){
                    continue;
                }

                int column = placements.length - i - 1;
                shipInformation.append(column + "," + j + "," );
                IShipEquipment construction = ((CommonDraggableObject)placement.getShipEquipment()).getObject();
                shipInformation.append(((AShipEquipment)construction).getConstructionType() + ";");
            }
        }

        return shipInformation.toString();
    }

    public CommonShip importShip(String msg, Pane gameArea){
        String shipData [] = msg.split(";;");

        if(shipData.length < 1 ){
            return null;
        }

        CommonShip enemyShip = ConstructionTypes.createShip(shipData[0]);
        if(GlobalVariables.isEmpty(enemyShip)){
            return null;
        }
        enemyShip.displayShip(gameArea);

        if(shipData.length < 2){
            return enemyShip;
        }

        Placement [][] placements = enemyShip.getPlacementPositions();
        String [] equipments = shipData[1].split(";");
        for(int i = 0; i < equipments.length; i++){
            String [] equipmentData = equipments[i].split(",");
            IShipEquipment equipment = ConstructionTypes.createEquipment(equipmentData[2]);
            if(GlobalVariables.isEmpty(equipment)){
                continue;
            }

            int row = Integer.parseInt(equipmentData[0]);
            int column = Integer.parseInt(equipmentData[1]);
            equipment.displayEquipment(placements[row][column], enemyShip.isEnemy());
            placements[row][column].setShipEquipment(equipment);
            placements[row][column].setIsEmpty(false);
            placements[row][column].setIsWeapon(equipment.isWeapon());
            ((CommonConstruction)equipment).setPlacement(placements[row][column]);
        }

        return enemyShip;
    }
}
