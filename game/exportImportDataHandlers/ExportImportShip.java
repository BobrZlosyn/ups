package game.exportImportDataHandlers;

import client.TcpMessage;
import game.construction.*;
import game.shields.CommonShield;
import game.ships.CommonShip;
import game.static_classes.ConstructionTypes;
import game.static_classes.GlobalVariables;
import javafx.scene.layout.Pane;

/**
 * Created by Kanto on 03.10.2016.
 */
public class ExportImportShip {

    private boolean isFirstExport;
    private CommonShip enemyShip;

    public ExportImportShip(){
        isFirstExport = true;
    }

    public boolean isFirstExport() {
        return isFirstExport;
    }

    public void setFirstExport(boolean firstExport) {
        isFirstExport = firstExport;
    }

    public String exportShip(CommonShip ship){

        StringBuilder shipInformation = new StringBuilder();
        shipInformation.append(ship.getConstructionType() + ",");
        //informace o lodi
        shipInformation.append(ship.getActualLife() + ",");
        shipInformation.append(ship.getActualEnergy().get() * ship.getEnergyMaxValue() + ",");
        shipInformation.append(ship.getShieldActualLife()+ ",");
        shipInformation.append(ship.getArmorActualValue()+ ";;");

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
                shipInformation.append(((AShipEquipment)construction).getConstructionType() + ",");
                shipInformation.append(((AShipEquipment) construction).getActualLife() + ";");

            }
        }

        return shipInformation.toString();
    }

    private int parseNumberValue(String value, int defaultValue){
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public CommonShip importShip(String msg, Pane gameArea){
        String shipData [] = msg.split(";;");

        if(shipData.length < 1 ){
            return null;
        }

        String shipValues [] = shipData[0].split(",");
        enemyShip = ConstructionTypes.createShip(shipValues[0]);
        if(GlobalVariables.isEmpty(enemyShip)){
            return null;
        }
        enemyShip.displayShip(gameArea);

        if(shipValues.length == 5){
            enemyShip.setActualLife(parseNumberValue(shipValues[1], (int) enemyShip.getActualLife()));
            enemyShip.setEnergyActualValue(parseNumberValue(shipValues[2], enemyShip.getEnergyMaxValue()));
            enemyShip.setShieldActualLife(parseNumberValue(shipValues[3], enemyShip.getShieldActualLife()));
            enemyShip.setArmorActualValue(parseNumberValue(shipValues[4], enemyShip.getArmorActualValue()));
        }


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
            ((CommonConstruction)equipment).setPlacement(placements[row][column]);
            ((CommonConstruction)equipment).setActualLife(parseNumberValue(equipmentData[3], (int)((CommonConstruction)equipment).getActualLife()));
        }

        return enemyShip;
    }


    public String exportEquipmentStatus(Placement [][] placements){
        StringBuilder status = new StringBuilder();
        Placement placement;
        for(int i = 0; i < placements.length; i++){
            for(int j = 0; j < placements[i].length; j++){
                placement = placements[i][j];

                if(GlobalVariables.isEmpty(placement) || !placement.isShield()){
                    continue;
                }

                CommonShield shield = ((CommonShield) placement.getShipEquipment());
                int isActive;
                if(shield.isActive()){
                    isActive = 1;
                }else{
                    isActive = 0;
                }

                status.append(i + "," + j + ","); // pridani vybaveni
                status.append(isActive);
                status.append(";"); //ukonceni zbrane
            }
        }

        return status.toString();
    }

    public void importEquipmentStatus( String status){

        if(GlobalVariables.isEmpty(status) || GlobalVariables.isEmpty(enemyShip)){
            return;
        }

        String [] equipments = status.split(TcpMessage.SEPARATOR);
        Placement placement;

        for (int i = 0; i < equipments.length; i++){
            String [] parts = equipments[i].split(",");

            int iPosition = enemyShip.getPlacementPositions().length - Integer.parseInt(parts[0]) - 1;
            int jPosition = Integer.parseInt(parts[1]);
            int isActive = Integer.parseInt(parts[2]);


            placement = enemyShip.getPlacementPositions()[iPosition][jPosition];
            if(GlobalVariables.isEmpty(placement)){
                continue;
            }

            IShipEquipment equipment = placement.getShipEquipment();
            if(GlobalVariables.isEmpty(equipment) || !equipment.isShield()){
                continue;
            }

            CommonShield commonShield = (CommonShield) equipment;
            if(isActive == 1){
                commonShield.setIsActive(true);
            }else{
                commonShield.setIsActive(false);
            }

        }
    }


    public void importReconnectionStatus( CommonShip shipToSet, String settings){


    }
}
