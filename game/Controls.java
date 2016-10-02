package game;

import game.ships.CommonShip;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class Controls {

    private double shipIntegrity;
    private double shipPower;
    private ProgressBar shipIntegrityProgress, shipPowerProgress, shipShieldProgress;
    private Label life, power, shield;
    private Button settings;
    public Controls(CommonShip userShip, CommonShip enemyShip){
        createProgress(userShip);
        createSettingsButton();
    }

    private void createProgress(CommonShip userShip){

        shipIntegrityProgress = new ProgressBar(0.5);
        shipIntegrityProgress.progressProperty().bind(userShip.getActualLifeBinding());

        shipShieldProgress = new ProgressBar(0.5);
        shipShieldProgress.progressProperty().bind(userShip.getShieldActualLifeBinding());


        shipPowerProgress = new ProgressBar(0.5);
        life = new Label("INTEGRITA TRUPU");
        shield = new Label("SÍLA ŠTÍTŮ");
        power = new Label("ZBYLÁ ENERGIE");

    }

    private void createSettingsButton(){
        settings = new Button("Nastavení");
    }

    public void showStatusBars(Pane gameArea){
        gameArea.getChildren().addAll(shipIntegrityProgress, shipPowerProgress, shipShieldProgress, life, power, shield);
        life.setLayoutX(35);
        life.setLayoutY(17);
        life.getStyleClass().add("statusLabel");
        power.setLayoutX(40);
        power.setLayoutY(52);
        power.getStyleClass().add("statusLabel");
        shield.setLayoutX(195);
        shield.setLayoutY(17);
        shield.getStyleClass().add("statusLabel");

        shipIntegrityProgress.setLayoutX(15);
        shipIntegrityProgress.setLayoutY(10);
        shipIntegrityProgress.setMinHeight(30);
        shipIntegrityProgress.setMinWidth(150);
        shipIntegrityProgress.setMaxWidth(150);
        shipIntegrityProgress.getStyleClass().add("lifeStatus");

        shipPowerProgress.setLayoutX(15);
        shipPowerProgress.setLayoutY(45);
        shipPowerProgress.setMinHeight(30);
        shipPowerProgress.setMinWidth(150);
        shipPowerProgress.setMaxWidth(150);
        shipPowerProgress.getStyleClass().add("energyStatus");


        shipShieldProgress.setLayoutX(170);
        shipShieldProgress.setLayoutY(10);
        shipShieldProgress.setMinHeight(30);
        shipShieldProgress.setMinWidth(110);
        shipShieldProgress.setMaxWidth(110);
        shipShieldProgress.getStyleClass().add("shieldStatus");


        ((GridPane)gameArea.getParent()).add(settings, 1, 0);
        ((GridPane)gameArea.getParent()).getColumnConstraints().get(1).setHalignment(HPos.RIGHT);
        ((GridPane)gameArea.getParent()).getRowConstraints().get(0).setValignment(VPos.TOP);
        ((GridPane)gameArea.getParent()).setMargin(settings, new Insets(10,10,10,10));
    }

    public void setShipIntegrity(double shipIntegrity) {
        this.shipIntegrity = shipIntegrity;
    }

    public void setShipPower(double shipPower) {
        this.shipPower = shipPower;
    }

    public double getShipIntegrity() {
        return shipIntegrity;
    }

    public double getShipPower() {
        return shipPower;
    }
}
