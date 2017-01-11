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

    public String exportShip(CommonShip ship, boolean exportCreatedShip){

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
                AShipEquipment construction;
                if(exportCreatedShip){
                    construction = (AShipEquipment) ((CommonDraggableObject)placement.getShipEquipment()).getObject();
                }else {
                    construction = (AShipEquipment) placement.getShipEquipment();
                }

                if(construction.isDestroyed()){
                    continue;
                }

                shipInformation.append(column + "," + j + "," );
                shipInformation.append(construction.getConstructionType() + ",");
                shipInformation.append(construction.getActualLife() + ";");
            }
        }

        return shipInformation.toString();
    }

    private int parseIntegerValue(String value, int defaultValue){
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            return defaultValue;
        }
    }

    private double parseDoubleValue(String value, double defaultValue){
        try {
            return Double.parseDouble(value);
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
            enemyShip.setActualLife(parseIntegerValue(shipValues[1], (int) enemyShip.getActualLife()));
            enemyShip.setEnergyActualValue(parseIntegerValue(shipValues[2], enemyShip.getEnergyMaxValue()));
            enemyShip.setShieldActualLife(parseIntegerValue(shipValues[3], enemyShip.getShieldActualLife()));
            enemyShip.setArmorActualValue(parseIntegerValue(shipValues[4], enemyShip.getArmorActualValue()));
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
            ((CommonConstruction)equipment).setActualLife(parseIntegerValue(equipmentData[3],
                    (int)((CommonConstruction)equipment).getActualLife()));
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


    /**
     * nastaveni lodi podle aktualniho stavu
     * @param shipToSet
     * @param settings
     * @param isReversed
     */
    public void importReconnectionStatus( CommonShip shipToSet, String settings, boolean isReversed){
        Pane gameArea = shipToSet.getPane();
        if(GlobalVariables.isEmpty(gameArea)) {
            return;
        }

        String [] information = settings.split(";;");
        String [] shipInfo = information[0].split(",");
        if(shipInfo.length == 5){
            shipToSet.setActualLife(parseDoubleValue(shipInfo[1], (int) shipToSet.getActualLife()));
            shipToSet.setEnergyActualValue(parseIntegerValue(shipInfo[2], shipToSet.getEnergyMaxValue()));
            shipToSet.setShieldActualLife(parseIntegerValue(shipInfo[3], shipToSet.getShieldActualLife()));
            shipToSet.setArmorActualValue(parseIntegerValue(shipInfo[4], shipToSet.getArmorActualValue()));
        }

        if(information.length != 2) {
                return;
        }

        information = information[1].split(";");
        Placement [][] placements = shipToSet.getPlacementPositions();
        for(int i = 0; i < information.length; i++){
            shipInfo = information[i].split(",");

            if(shipInfo.length != 4){
                continue;
            }

            int row, column;
            if(isReversed){
                row = placements.length - Integer.parseInt(shipInfo[0]) - 1;
            }else {
                row = Integer.parseInt(shipInfo[0]);
            }
            column = Integer.parseInt(shipInfo[1]);

            Placement placement = placements[row][column];
            IShipEquipment equipment = null;
            if(GlobalVariables.isNotEmpty(placement)) {
                equipment = placement.getShipEquipment();
            }

            if(((CommonConstruction)equipment).isDestroyed()){
                equipment = ConstructionTypes.createEquipment(shipInfo[2]);
                shipToSet.getPlacementPositions()[row][column].setIsEmpty(true);
                shipToSet.addEquipmentToShip(row, column, (AShipEquipment) equipment);
            }

            CommonConstruction construction = (CommonConstruction) equipment;
            construction.setActualLife(parseDoubleValue(shipInfo[3], ((CommonConstruction) equipment).getActualLife()));
            construction.setActualLifeBinding(construction.getActualLife() / construction.getTotalLife().get());
        }
    }
}
