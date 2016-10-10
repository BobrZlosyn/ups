package game;

import game.ships.CommonShip;
import game.static_classes.GlobalVariables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;


/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class Controls {

    private double shipIntegrity;
    private double shipPower;
    private ProgressBar shipIntegrityProgress, shipPowerProgress, shipShieldProgress;
    private Label life, power, shield;
    private Button settings;
    private Circle circle;
    private Label time;
    private SimpleIntegerProperty timeValue;
    private Timeline roundTimeAnimation;
    private ProgressIndicator progress;

    public Controls(CommonShip userShip, CommonShip enemyShip){
        createProgress(userShip);
        createSettingsButton();
        createTimeRemaining();
    }

    private void createProgress(CommonShip userShip){

        shipIntegrityProgress = new ProgressBar(0.5);
        shipIntegrityProgress.progressProperty().bind(userShip.getActualLifeBinding());

        shipPowerProgress = new ProgressBar(0.5);
        shipPowerProgress.progressProperty().bind(userShip.getActualEnergy());

        power = new Label();
        life = new Label();

        shipIntegrityProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
            life.setMinWidth(newValue1.intValue());
        });
        shipPowerProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
            power.setMinWidth(newValue1.intValue());
        });

        power.setAlignment(Pos.CENTER);
        life.setAlignment(Pos.CENTER);

        life.setText((int)userShip.getTotalLife().get() + "/" + (int)userShip.getTotalLife().get());
        power.setText(userShip.getEnergyMaxValue() + "/" + userShip.getEnergyMaxValue());

        shipIntegrityProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder text = new StringBuilder();
            text.append((int)userShip.getActualLife());
            text.append("/");
            text.append((int)userShip.getTotalLife().get());
            life.setText(text.toString());
        });

        shipPowerProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder text = new StringBuilder();
            text.append(userShip.getActualEnergyLevel());
            text.append("/");
            text.append(userShip.getEnergyMaxValue());
            power.setText(text.toString());
        });


        if(userShip.getShieldMaxLife() != 0){
            shipShieldProgress = new ProgressBar(0.5);
            shipShieldProgress.progressProperty().bind(userShip.getShieldActualLifeBinding());

            shield = new Label();
            shield.setAlignment(Pos.CENTER);
            shipShieldProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
                shield.setMinWidth(newValue1.intValue());
            });

            shield.setText(userShip.getShieldMaxLife() + "/" + userShip.getShieldMaxLife());
            shipShieldProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
                StringBuilder text = new StringBuilder();
                text.append(userShip.getShieldActualLife());
                text.append("/");
                text.append(userShip.getShieldMaxLife());
                shield.setText(text.toString());
            });


        }

        //userShip.setActualEnergy(150);
        //userShip.takeDamage(250);
    }

    private void createSettingsButton(){
        settings = new Button("Nastavení");
    }

    public void showStatusBars(Pane gameArea){
        gameArea.getChildren().addAll(shipIntegrityProgress, shipPowerProgress, life, power);
        life.setLayoutX(15);
        life.setLayoutY(17);
        life.getStyleClass().add("statusLabel");
        power.setLayoutX(15);
        power.setLayoutY(52);
        power.getStyleClass().add("statusLabel");

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

        ((GridPane)gameArea.getParent()).add(settings, 1, 0);
        ((GridPane)gameArea.getParent()).getColumnConstraints().get(1).setHalignment(HPos.RIGHT);
        ((GridPane)gameArea.getParent()).getRowConstraints().get(0).setValignment(VPos.TOP);
        ((GridPane)gameArea.getParent()).setMargin(settings, new Insets(10,10,10,10));


        if(!GlobalVariables.isEmpty(shipShieldProgress)){
            gameArea.getChildren().addAll(shipShieldProgress, shield);

            shield.setLayoutX(170);
            shield.setLayoutY(17);
            shield.getStyleClass().add("statusLabel");

            shipShieldProgress.setLayoutX(170);
            shipShieldProgress.setLayoutY(10);
            shipShieldProgress.setMinHeight(30);
            shipShieldProgress.setMinWidth(110);
            shipShieldProgress.setMaxWidth(110);
            shipShieldProgress.getStyleClass().add("shieldStatus");
        }

        setTimeRemaining(gameArea);
    }

    public void setShipIntegrity(double shipIntegrity) {
        this.shipIntegrity = shipIntegrity;
    }

    private void createTimeRemaining(){
        circle = new Circle(32);
        circle.setCenterY(circle.getRadius() + 19);
        circle.setStroke(Color.WHITE);

        progress = new ProgressIndicator(0);
        progress.setMinSize(100,100);
        progress.setLayoutY(10);
        progress.getStyleClass().add("timeIndicator");

        timeValue = new SimpleIntegerProperty(120);
        timeValue.addListener((observable, oldValue, newValue) -> {
            double number = 1 - ((double)newValue.intValue())/120;
            progress.setProgress(number);
        });

        time = new Label();
        time.setMinWidth(circle.getRadius() * 2);
        time.textProperty().bind(timeValue.asString());
        time.setTextFill(Color.WHITE);
        time.setAlignment(Pos.CENTER);
        time.setFont(Font.font(22));
        time.setLayoutY(circle.getRadius() + 3);

        roundTimeAnimation = new Timeline(new KeyFrame(Duration.seconds(1), event -> roundTimeAnimation()));
        roundTimeAnimation.setCycleCount(120);
        roundTimeAnimation.playFromStart();
    }

    private void setTimeRemaining(Pane gameArea){
        gameArea.getChildren().addAll(progress, circle, time);
        gameArea.widthProperty().addListener((observable, oldValue, newValue) -> {
            progress.setLayoutX(newValue.intValue()/2 - 50);
            circle.setCenterX(newValue.intValue()/2);
            time.setLayoutX(newValue.intValue()/2 - circle.getRadius());
        });
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

    private void roundTimeAnimation(){

        if(timeValue.get() > 1){
            timeValue.set(timeValue.get() - 1);
        }else {
            resetAnimation();
        }
    }

    public void stopAnimations(){
        if(GlobalVariables.isEmpty(roundTimeAnimation)){
            return;
        }

        roundTimeAnimation.stop();
        roundTimeAnimation = null;
    }

    public void resetAnimation(){
        if(GlobalVariables.isEmpty(roundTimeAnimation)){
            return;
        }

        timeValue.set(120);
        roundTimeAnimation.playFromStart();
    }


}
