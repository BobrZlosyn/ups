package game;

import game.construction.AShipEquipment;
import game.construction.CommonConstruction;
import game.construction.IMarkableObject;
import game.shields.CommonShield;
import game.ships.CommonShip;
import game.static_classes.ConstructionTypes;
import game.static_classes.GlobalVariables;
import game.weapons.CommonWeapon;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;


/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class BottomPanel {

    private Button quit, activateShield;
    private Button targeting, cancelTarget;
    private GridPane panel;
    private Label name, lifeLabel, shieldLabel, energyCostLabel, strenghtLabel;
    private boolean setTarget;
    private ProgressBar lifeProgress;
    private String background;

    private ProgressBar avaibleShieldProgress;


    public BottomPanel(Button sendOrders){
        createButtonQuit();
        createButtonSend(sendOrders);
        createName();
        createActivationShield();
        createButtonTargeting();
        createLifeProgressBar();
        createCancelTargetButton();
        createEnergyCostLabel();
        createStrengthLabel();
        createPanel(sendOrders);
        createNamePane();

    }

    public Button getQuit() {
        return quit;
    }

    private void createEnergyCostLabel(){
        energyCostLabel = new Label();
        energyCostLabel.getStyleClass().add("statusLabel");
        energyCostLabel.setMaxWidth(Double.MAX_VALUE);
        energyCostLabel.setMaxHeight(Double.MAX_VALUE);
        energyCostLabel.setAlignment(Pos.CENTER);
        energyCostLabel.setStyle("-fx-background-color: purple;");
        energyCostLabel.visibleProperty().bind(lifeProgress.visibleProperty());
        energyCostLabel.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue()){
                energyCostLabel.setText(GlobalVariables.markedObject.getEnergyCost() + "");
            }
        });

    }

    private void createStrengthLabel(){
        strenghtLabel = new Label();
        strenghtLabel.getStyleClass().add("statusLabel");
        strenghtLabel.setVisible(false);
        strenghtLabel.setMaxWidth(Double.MAX_VALUE);
        strenghtLabel.setMaxHeight(Double.MAX_VALUE);
        strenghtLabel.setAlignment(Pos.CENTER);
        strenghtLabel.setStyle("-fx-background-color: red;");
        strenghtLabel.visibleProperty().bind((shieldLabel.visibleProperty().not()).and(lifeProgress.visibleProperty()));
        strenghtLabel.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue() && ConstructionTypes.isEquipment(GlobalVariables.markedObject.getConstructionType())){
                strenghtLabel.setText(((AShipEquipment)GlobalVariables.markedObject).getMaxStrength() + "");
            }
        });

    }

    private void createButtonSend(Button sendOrders){
        sendOrders.setText("DÁT ROZKAZ K ÚTOKU");
        sendOrders.setMaxWidth(Double.MAX_VALUE);
        sendOrders.setMaxHeight(Double.MAX_VALUE);
        sendOrders.disableProperty().bind(GlobalVariables.isPlayingNow.not());
    }

    private void createActivationShield(){
        activateShield = new Button("Deaktivovat štít");
        activateShield.setMaxWidth(Double.MAX_VALUE);
        activateShield.setMaxHeight(Double.MAX_VALUE);
        activateShield.setVisible(false);
        GlobalVariables.isSelected.addListener((observable, oldValue, newValue) -> {
            avaibleShieldProgress.progressProperty().unbind();

            if (newValue.booleanValue()) {
                CommonConstruction construction = GlobalVariables.getMarkedObject();
                // test zda je vybaveni
                if (construction.isEnemy() || ConstructionTypes.isShip(construction.getConstructionType())) {
                    return;
                }

                AShipEquipment equipment = (AShipEquipment) construction;
                if (equipment.isShield()) { // zda je stit
                    activateShield.setVisible(newValue.booleanValue());
                    if (((CommonShield) equipment).isActive()) { // pokud je aktivni
                        activateShield.setText("Deaktivovat štít");
                        activateShield.setDisable(false);
                    } else { // pokud neni aktivni
                        activateShield.setText("Aktivovat štít");
                        notEnoughEnergy(GlobalVariables.getMarkedObject(), activateShield, "Aktivovat štít");
                    }

                    avaibleShieldProgress.progressProperty().bind(equipment.shieldProgressProperty());
                } else { // pokud neni stit
                    activateShield.setVisible(!newValue.booleanValue());

                }
            } else { // neni vybrano nic
                activateShield.setVisible(newValue.booleanValue());
            }
        });

        activateShieldOnActionEvent();
        availibleShieldProgress();
    }

    private void activateShieldOnActionEvent(){
        activateShield.setOnAction(event -> {
            CommonShield shield = ((CommonShield) GlobalVariables.getMarkedObject());
            if (shield.isActive()) {
                activateShield.setText("Aktivovat štít");
                shield.getPlacement().getShip().setActualEnergy(-shield.getEnergyCost());
                shield.setIsActive(!shield.isActive());
            } else {
                if (shield.getPlacement().getShip().getActualEnergyLevel() >= shield.getEnergyCost()) {
                    activateShield.setText("Deaktivovat štít");
                    shield.getPlacement().getShip().setActualEnergy(shield.getEnergyCost());
                    shield.setIsActive(!shield.isActive());
                }
            }
        });
    }

    private void availibleShieldProgress(){
        avaibleShieldProgress = new ProgressBar(1);
        avaibleShieldProgress.setVisible(false);
        GlobalVariables.isSelected.addListener((observable, oldValue, newValue) -> {
            avaibleShieldProgress.setVisible(false);
            if(newValue.booleanValue()){
                if(ConstructionTypes.isEquipment(GlobalVariables.getMarkedObject().getConstructionType())){
                    if(((AShipEquipment)GlobalVariables.getMarkedObject()).isWeapon()){
                        return;
                    }
                }
                avaibleShieldProgress.setVisible(true);
            }
        });

        avaibleShieldProgress.getStyleClass().add("shieldStatus");
        avaibleShieldProgress.setMaxWidth(Double.MAX_VALUE);
        avaibleShieldProgress.setMaxHeight(Double.MAX_VALUE);
        shieldLabel = new Label();
        shieldLabel.visibleProperty().bind(avaibleShieldProgress.visibleProperty());
        shieldLabel.getStyleClass().add("statusLabel");
        shieldLabel.setMaxWidth(Double.MAX_VALUE);
        shieldLabel.setAlignment(Pos.CENTER);

        avaibleShieldProgress.visibleProperty().addListener((observable, oldValue, newValue) -> {
            avaibleShieldProgress.progressProperty().unbind();
            if (newValue.booleanValue()) {
                CommonConstruction construction = GlobalVariables.getMarkedObject();

                if(ConstructionTypes.isShip(construction.getConstructionType())){
                    CommonShip ship = (CommonShip) construction;
                    shieldLabel.setText(ship.getShieldActualLife() + "/" + ship.getShieldMaxLife());
                    avaibleShieldProgress.progressProperty().bind(ship.getShieldActualLifeBinding());
                    avaibleShieldProgress.progressProperty().addListener((observable1, oldValue1, newValue1) -> {
                        if(ConstructionTypes.isShip(GlobalVariables.getMarkedObject().getConstructionType())){
                            int actualShield = (int) (newValue1.doubleValue() * ship.getShieldMaxLife());
                            shieldLabel.setText(actualShield + "/" + ship.getShieldMaxLife());
                        }
                    });
                }else {
                    AShipEquipment equipment = (AShipEquipment) construction;
                    shieldLabel.setText(equipment.getActualShieldBonus() + "/" + equipment.getShieldBonus());
                    avaibleShieldProgress.progressProperty().bind(equipment.shieldProgressProperty());
                    avaibleShieldProgress.progressProperty().addListener((observable1, oldValue1, newValue1) -> {
                        if(ConstructionTypes.isEquipment(GlobalVariables.getMarkedObject().getConstructionType())){
                            int actualShield = (int) (newValue1.doubleValue() * equipment.getShieldBonus());
                            shieldLabel.setText(actualShield + "/" + equipment.getShieldBonus());
                        }
                    });
                }
            }
        });
    }


    private void createCancelTargetButton(){
        cancelTarget = new Button("Zrušit cíl");
        cancelTarget.setMaxWidth(Double.MAX_VALUE);
        cancelTarget.setMaxHeight(Double.MAX_VALUE);
        cancelTarget.setVisible(false);
        cancelTarget.setOnAction(event -> {
            CommonWeapon weapon = (CommonWeapon)GlobalVariables.getMarkedObject();
            weapon.setTarget(null);
            weapon.rotateToDefaultPosition();
            weapon.getPlacement().getShip().setActualEnergy(-weapon.getEnergyCost());
            weapon.unmarkObject();
            cancelTarget.setVisible(false);
        });

    }

    private void createLifeProgressBar(){
        lifeProgress = new ProgressBar(1);
        lifeProgress.setMaxWidth(Double.MAX_VALUE);
        lifeProgress.setMaxHeight(Double.MAX_VALUE);
        lifeProgress.getStyleClass().add("lifeStatus");
        lifeProgress.setVisible(false);

        lifeLabel = new Label("život konstrukce");
        lifeLabel.getStyleClass().add("statusLabel");
        lifeLabel.setMaxWidth(Double.MAX_VALUE);
        lifeLabel.setAlignment(Pos.CENTER);
        lifeProgress.visibleProperty().bind(GlobalVariables.isSelected);
        lifeLabel.visibleProperty().bind(GlobalVariables.isSelected);

        //nastavi binding na zivot kliknuteho objektu nebo binding odstrani
        lifeProgress.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue()){
                CommonConstruction construction = GlobalVariables.getMarkedObject();
                lifeLabel.setText((int)construction.getActualLife() + "/" + (int)construction.getTotalLife().get());
                lifeProgress.progressProperty().bind(construction.getActualLifeBinding());
                lifeProgress.progressProperty().addListener((observable1, oldValue1, newValue1) -> {

                    int actualLife = (int) (newValue1.doubleValue() * construction.getTotalLife().get());
                    lifeLabel.setText(actualLife + "/" + (int) construction.getTotalLife().get());
                });

            }else {
                lifeProgress.progressProperty().unbind();
            }
        });
    }

    private void createButtonTargeting(){
        targeting = new Button();
        targeting.setText("Zaměřit cíl");
        targeting.visibleProperty().bind(GlobalVariables.canTarget);
        targeting.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue()){
                CommonWeapon weapon = (CommonWeapon) GlobalVariables.getMarkedObject();
                notEnoughEnergy(GlobalVariables.getMarkedObject(), targeting, "Zaměřit cíl");
                if(GlobalVariables.isEmpty(weapon.getTarget())){ //zbran ma vybrany cil
                    cancelTarget.setVisible(false);
                    return;
                }

                cancelTarget.setVisible(true);
                targeting.setText("Změnit cíl");
                targeting.setDisable(false);
                IMarkableObject equipment =(IMarkableObject) weapon.getTarget().getShipEquipment();
                if(GlobalVariables.isEmpty(equipment)){ // cilem je lod
                    weapon.getTarget().getShip().target();
                }else { // cilem je vybaveni na lodi
                    equipment.target();
                }
            }else {
                cancelTarget.setVisible(false);
            }
        });

        targeting.setMaxWidth(Double.MAX_VALUE);
        targeting.setMaxHeight(Double.MAX_VALUE);

        targeting.setOnAction(event -> {
            if(!setTarget){
                startTargeting();
            }else {
                acknoledgeTarget();
            }
        });

    }

    private void startTargeting(){
        setTarget = true;
        GlobalVariables.setIsTargeting(true);
        targeting.setText("Potvrdit cíl");
    }

    private void notEnoughEnergy(CommonConstruction commonConstruction, Button button, String textIfFalse){
        int energy = commonConstruction.getPlacement().getShip().getActualEnergyLevel();
        int cost = commonConstruction.getEnergyCost();
        if(energy < cost){
            button.setText("Nedostatek energie");
            button.setDisable(true);
        }else{
            button.setText(textIfFalse);
            button.setDisable(false);
        }
    }


    /**
     * potvrdi cil a zameri ho
     */
    private void acknoledgeTarget(){
        CommonWeapon weapon = (CommonWeapon) GlobalVariables.getMarkedObject();
        int energyCost = weapon.getEnergyCost();
        CommonShip ship = weapon.getPlacement().getShip();
        if(energyCost > ship.getActualEnergyLevel()){
            setTarget = false;
            GlobalVariables.setIsTargeting(false);
            targeting.setText("Zaměřit cíl");
            return;
        }

        if(!GlobalVariables.isEmpty(GlobalVariables.getTargetObject())){
            CommonConstruction construction = GlobalVariables.getTargetObject();
            weapon.rotateEquipment(construction.getCenterX(), construction.getCenterY());
            GlobalVariables.setTargetObject(construction);
            construction.target();
            //odeber energii pokud zbran nema vybrano cil
            if(GlobalVariables.isEmpty(weapon.getTarget())){
                ship.setActualEnergy(energyCost);
            }

            weapon.setTarget(construction.getPlacement());
            cancelTarget.setVisible(true);
            targeting.setText("Změnit cíl");
        }else {
            if(!GlobalVariables.isEmpty(weapon.getTarget())){
                weapon.setTarget(null);
                ship.setActualEnergy(-energyCost);
                weapon.rotateToDefaultPosition();
            }
            cancelTarget.setVisible(false);
            targeting.setText("Zaměřit cíl");
        }


        setTarget = false;
        GlobalVariables.setIsTargeting(false);

    }

    private void createButtonQuit(){
        quit = new Button("UTÉCT Z BOJE");
        quit.setMaxWidth(Double.MAX_VALUE);
        quit.setMaxHeight(Double.MAX_VALUE);
    }

    private void createName(){
        name = new Label("Název kliknutého objektu");
        name.textProperty().bind(GlobalVariables.name);
        name.visibleProperty().bind(GlobalVariables.isSelected);
        name.getStyleClass().add("statusLabel");
    }

    private void createPanel(Button sendOrders){
        panel = new GridPane();
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.getStyleClass().add("bottomPanel");

        RowConstraints rowConstraints1 = new RowConstraints(50);
        RowConstraints rowConstraints2 = new RowConstraints(50);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(20);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(20);
        columnConstraints1.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(20);
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setPercentWidth(20);
        columnConstraints3.setHalignment(HPos.RIGHT);
        ColumnConstraints columnConstraints4 = new ColumnConstraints();
        columnConstraints4.setPercentWidth(20);


        panel.getColumnConstraints().addAll(columnConstraints, columnConstraints1, columnConstraints2, columnConstraints3, columnConstraints4);
        panel.getRowConstraints().addAll(rowConstraints1, rowConstraints2);

        panel.add(sendOrders, 4, 0);
        panel.add(quit, 4, 1);
        panel.add(targeting, 3, 1);
        panel.add(activateShield, 3, 1);
        panel.add(cancelTarget, 3, 0);
        panel.add(lifeProgress, 0, 0);
        panel.add(avaibleShieldProgress, 0, 1);
        panel.add(shieldLabel, 0, 1);
        panel.add(lifeLabel, 0, 0);
        panel.add(energyCostLabel, 1,1);
        panel.add(strenghtLabel, 0,1);

        panel.setMargin(sendOrders, new Insets(15, 10, 5, 10));
        panel.setMargin(cancelTarget, new Insets(15,10,5,10));
        panel.setMargin(quit, new Insets(10,10,10,10));
        panel.setMargin(targeting, new Insets(10,10,10,10));
        panel.setMargin(lifeProgress, new Insets(10,10,10,10));
        panel.setMargin(activateShield, new Insets(10,10,10,10));

        panel.setMargin(avaibleShieldProgress, new Insets(10,10,10,10));
        panel.setMargin(energyCostLabel, new Insets(15,10,15,10));

        panel.setMargin(strenghtLabel, new Insets(15,10,15,10));
    }

    public void showPanel(GridPane window, Pane gameArea){
        window.add(panel,0,1,GridPane.REMAINING,GridPane.REMAINING);
        gameArea.getChildren().addAll(name);
        name.setLayoutX(gameArea.getWidth()/2 - 200/2);
        name.setLayoutY(gameArea.getHeight() - 35);

        gameArea.widthProperty().addListener((observable, oldValue, newValue) -> {
            name.setLayoutX(newValue.doubleValue()/2 - 200/2);
        });
        gameArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            name.setLayoutY(newValue.doubleValue() - 35);
        });
    }
    private void createNamePane(){

        name.setMaxWidth(200);
        name.setMinWidth(200);
        name.setMaxHeight(35);
        name.setMinHeight(35);
        name.setAlignment(Pos.CENTER);
        name.getStyleClass().add("bottomPanel");
        name.setStyle("-fx-border-radius: 100 100 0 0;" +
                        "-fx-background-radius: 100 100 0 0;");
    }

    public void removePanel(GridPane window){
        window.getChildren().remove(panel);
    }
}
