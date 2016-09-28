package game;

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
    private ProgressBar shipIntegrityProgress, shipPowerProgress;
    private Label life, power;
    private Button settings;
    public Controls(){
        createProgress();
    }

    private void createProgress(){
        shipIntegrityProgress = new ProgressBar(0.5);
        shipPowerProgress = new ProgressBar(0.5);
        life = new Label("INTEGRITA TRUPU");
        power = new Label("ZBYLÁ ENERGIE");
        createSettingsButton();
    }

    private void createSettingsButton(){
        settings = new Button("Nastavení");
    }

    public void showStatusBars(Pane gameArea){
        gameArea.getChildren().addAll(shipIntegrityProgress, shipPowerProgress, life, power);
        life.setLayoutX(60);
        life.setLayoutY(22);
        life.getStyleClass().add("statusLabel");
        power.setLayoutX(65);
        power.setLayoutY(57);
        power.getStyleClass().add("statusLabel");

        shipIntegrityProgress.setLayoutX(15);
        shipIntegrityProgress.setLayoutY(15);
        shipIntegrityProgress.setMinHeight(30);
        shipIntegrityProgress.setMinWidth(200);
        shipIntegrityProgress.getStyleClass().add("lifeStatus");

        shipPowerProgress.setLayoutX(15);
        shipPowerProgress.setLayoutY(50);
        shipPowerProgress.setMinHeight(30);
        shipPowerProgress.setMinWidth(200);
        shipPowerProgress.getStyleClass().add("energyStatus");


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
