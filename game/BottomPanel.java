package game;

import game.construction.CommonConstruction;
import game.construction.IMarkableObject;
import game.ships.CommonShip;
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

    private Button quit;
    private Button targeting, cancelTarget;
    private GridPane panel;
    private Label name, lifeLabel;
    private boolean setTarget;
    private ProgressBar lifeProgress;
    private String background;


    public BottomPanel(Button sendOrders){
        createButtonQuit();
        createButtonSend(sendOrders);
        createName();
        createButtonTargeting();
        createLifeProgressBar();
        createCancelTargetButton();
        createPanel(sendOrders);
        createNamePane();
    }

    public Button getQuit() {
        return quit;
    }

    private void createButtonSend(Button sendOrders){
        sendOrders.setText("DÁT ROZKAZ K ÚTOKU");
        sendOrders.setMaxWidth(Double.MAX_VALUE);
        sendOrders.setMaxHeight(Double.MAX_VALUE);
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
                notEnoughEnergy(GlobalVariables.getMarkedObject());

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

    private void notEnoughEnergy(CommonConstruction commonConstruction){
        int energy = commonConstruction.getPlacement().getShip().getActualEnergyLevel();
        int cost = commonConstruction.getEnergyCost();
        if(energy < cost){
            targeting.setText("Nedostatek energie");
            targeting.setDisable(true);
        }else{
            targeting.setText("Zaměřit cíl");
            targeting.setDisable(false);
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
        columnConstraints1.setPercentWidth(40);
        columnConstraints1.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(20);
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setPercentWidth(20);
        columnConstraints3.setHalignment(HPos.RIGHT);


        panel.getColumnConstraints().addAll(columnConstraints, columnConstraints1, columnConstraints2, columnConstraints3);
        panel.getRowConstraints().addAll(rowConstraints1, rowConstraints2);

        panel.add(sendOrders, 3, 0);
        panel.add(quit, 3, 1);
        panel.add(targeting, 2, 1);
        panel.add(cancelTarget, 2, 0);
        panel.add(lifeProgress, 0, 0);
        panel.add(lifeLabel, 0, 0);

        panel.setMargin(sendOrders, new Insets(15,10,5,10));
        panel.setMargin(cancelTarget, new Insets(15,10,5,10));
        panel.setMargin(quit, new Insets(10,10,10,10));
        panel.setMargin(targeting, new Insets(10,10,10,10));
        panel.setMargin(lifeProgress, new Insets(10,10,10,10));
    }

    public void showPanel(GridPane window, Pane gameArea){
        window.add(panel,0,1,GridPane.REMAINING,GridPane.REMAINING);
        gameArea.getChildren().addAll(name);

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
