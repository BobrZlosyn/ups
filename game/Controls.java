package game;

import game.ships.CommonShip;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
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
    private ProgressBar shipIntegrityProgress, shipPowerProgress, shipShieldProgress, enemyshipShieldProgress, enemyshipIntegrityProgress;
    private Label life, power, shield, enemyShield, enemyLife, messageToPlayer;
    private Button settings;
    private Circle circle;
    private Label time;
    private SimpleIntegerProperty timeValue;
    private Timeline roundTimeAnimation, messageToUserAnimation;
    private ProgressIndicator progress;
    private int countOfTextView;
    private final double MESSAGE_WIDTH = 400;

    public static final String WELCOME_MESSAGE = "VÍTEJTE VE HŘE";
    public static final String USER_IS_PLAYING = "JSTE NA ŘADĚ VELITELI!";
    public static final String ENEMY_IS_PLAYING = "NA ŘADĚ JE NEPŘÍTEL!";
    public static final String USERS_SHIELDS_ARE_OUT = "VELITELI, ŠTÍTY SELHALY!";
    public static final String ENEMYS_SHIELDS_ARE_OUT = "NEPŘÁTELSKÉ ŠTÍTY JSOU DOLE!";

    public Controls(CommonShip userShip, CommonShip enemyShip, Button sendOrders){
        createProgress(userShip);
        createEnemyIntegrityProgress(enemyShip);
        createInfoMessage();
        createPowerProgress(userShip);
        createShieldProgress(userShip);
        createEnemyShieldProgress(enemyShip);
        createSettingsButton();
        createTimeRemaining(sendOrders);

    }

    private void createInfoMessage(){
        messageToPlayer = new Label();
        messageToPlayer.setWrapText(true);
        messageToPlayer.setPrefWidth(MESSAGE_WIDTH);
        messageToPlayer.setTextFill(Color.GREEN);
        messageToPlayer.setAlignment(Pos.CENTER);
        messageToUserAnimation = new Timeline(new KeyFrame(Duration.seconds(0.05), event -> {
            double maxCount = 70;
            messageToPlayer.setOpacity( 1 - countOfTextView/maxCount);
            countOfTextView++;

            if (countOfTextView == maxCount) {
                messageToPlayer.setVisible(false);
                messageToUserAnimation.stop();
            }
        }));
        messageToUserAnimation.setCycleCount(Animation.INDEFINITE);
    }

    public void setMessageToPlayer(String text){
       Platform.runLater(() -> {
           messageToPlayer.setText(text);
           messageToPlayer.setFont(Font.font(26));
           countOfTextView = 0;
           messageToPlayer.setVisible(true);
           messageToPlayer.setOpacity(1);
           Pane parent = (Pane) messageToPlayer.getParent();
           messageToPlayer.setLayoutY(100);
           messageToPlayer.setLayoutX(parent.getWidth()/2 - MESSAGE_WIDTH/2);
           messageToUserAnimation.playFromStart();
       });
    }

    private void createPowerProgress(CommonShip userShip){
        shipPowerProgress = new ProgressBar(0.5);
        shipPowerProgress.progressProperty().bind(userShip.getActualEnergy());
        power = new Label();

        shipPowerProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
            power.setMinWidth(newValue1.intValue());
        });

        power.setAlignment(Pos.CENTER);
        power.setText(userShip.getActualEnergyLevel() + "/" + userShip.getEnergyMaxValue());

        shipPowerProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder text = new StringBuilder();
            text.append(userShip.getActualEnergyLevel());
            text.append("/");
            text.append(userShip.getEnergyMaxValue());
            power.setText(text.toString());
        });

    }

    private void createShieldProgress(CommonShip userShip){

        if(userShip.getShieldMaxLife() == 0) {
            return;
        }

        shipShieldProgress = new ProgressBar(0.5);
        shipShieldProgress.progressProperty().bind(userShip.getShieldActualLifeBinding());

        shield = new Label();
        shield.setAlignment(Pos.CENTER);
        shipShieldProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
            shield.setMinWidth(newValue1.doubleValue());
        });

        shield.setText(userShip.getShieldActualLife() + "/" + userShip.getShieldMaxLife());
        shipShieldProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder text = new StringBuilder();
            text.append(userShip.getShieldActualLife());
            text.append("/");
            text.append(userShip.getShieldMaxLife());
            shield.setText(text.toString());
        });
    }

    private void createProgress(CommonShip userShip){

        shipIntegrityProgress = new ProgressBar(0.5);
        shipIntegrityProgress.progressProperty().bind(userShip.getActualLifeBinding());


        life = new Label();
        shipIntegrityProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
            life.setMinWidth(newValue1.intValue());
        });

        life.setAlignment(Pos.CENTER);
        life.setText((int)userShip.getTotalLife().get() + "/" + (int)userShip.getTotalLife().get());

        shipIntegrityProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder text = new StringBuilder();
            text.append((int)(newValue.doubleValue() * userShip.getTotalLife().get()));
            text.append("/");
            text.append((int)userShip.getTotalLife().get());
            life.setText(text.toString());
        });

    }

    private void createEnemyIntegrityProgress(CommonShip enemyShip){

        enemyshipIntegrityProgress = new ProgressBar(0.5);
        enemyshipIntegrityProgress.progressProperty().bind(enemyShip.getActualLifeBinding());


        enemyLife = new Label();

        enemyshipIntegrityProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
            enemyLife.setMinWidth(newValue1.intValue());
        });

        enemyLife.setAlignment(Pos.CENTER);

        enemyLife.setText((int)enemyShip.getActualLife() + "/" + (int) enemyShip.getTotalLife().get());

        enemyshipIntegrityProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder text = new StringBuilder();
            text.append((int)(newValue.doubleValue() * enemyShip.getTotalLife().get()));
            text.append("/");
            text.append((int) enemyShip.getTotalLife().get());
            enemyLife.setText(text.toString());
        });

    }

    private void createEnemyShieldProgress(CommonShip enemyShip){

        if(enemyShip.getShieldMaxLife() == 0) {
            return;
        }

        enemyshipShieldProgress = new ProgressBar(0.5);
        enemyshipShieldProgress.progressProperty().bind(enemyShip.getShieldActualLifeBinding());

        enemyShield = new Label();
        enemyShield.setAlignment(Pos.CENTER);
        enemyshipShieldProgress.widthProperty().addListener((observable1, oldValue1, newValue1) -> {
            enemyShield.setMinWidth(newValue1.doubleValue());
        });

        enemyShield.setText(enemyShip.getShieldActualLife() + "/" + enemyShip.getShieldMaxLife());
        enemyshipShieldProgress.progressProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder text = new StringBuilder();
            text.append(enemyShip.getShieldActualLife());
            text.append("/");
            text.append(enemyShip.getShieldMaxLife());
            enemyShield.setText(text.toString());
        });
    }

    private void createSettingsButton(){
        settings = new Button("Nastavení");
    }

    private void setProgressBarSize(ProgressBar progress, String classStyle){

        progress.setMinHeight(30);
        progress.setMinWidth(150);
        progress.setMaxWidth(150);
        progress.getStyleClass().add(classStyle);
    }

    public void showStatusBars(Pane gameArea){
        gameArea.getChildren().addAll(shipIntegrityProgress, shipPowerProgress, life, power,
                                        enemyshipIntegrityProgress, enemyLife, messageToPlayer);
        setMessageToPlayer(WELCOME_MESSAGE);
        life.setLayoutX(15);
        life.setLayoutY(17);
        life.getStyleClass().add("statusLabel");

        enemyLife.setLayoutY(17);
        enemyLife.getStyleClass().add("statusLabel");

        power.setLayoutX(15);
        power.setLayoutY(52);
        power.getStyleClass().add("statusLabel");

        shipIntegrityProgress.setLayoutX(15);
        shipIntegrityProgress.setLayoutY(10);
        enemyshipIntegrityProgress.setLayoutY(10);
        setProgressBarSize(shipIntegrityProgress, "lifeStatus");
        setProgressBarSize(enemyshipIntegrityProgress, "lifeStatus");

        shipPowerProgress.setLayoutX(15);
        shipPowerProgress.setLayoutY(45);
        setProgressBarSize(shipPowerProgress, "energyStatus");

        if(!GlobalVariables.isEmpty(shipShieldProgress)){
            gameArea.getChildren().addAll(shipShieldProgress, shield);

            shield.setLayoutX(170);
            shield.setLayoutY(17);
            shield.getStyleClass().add("statusLabel");

            shipShieldProgress.setLayoutX(170);
            shipShieldProgress.setLayoutY(10);
            setProgressBarSize(shipShieldProgress, "shieldStatus");
        }

        if(!GlobalVariables.isEmpty(enemyshipShieldProgress)){
            gameArea.getChildren().addAll(enemyshipShieldProgress, enemyShield);
            enemyShield.setLayoutY(17);
            enemyShield.getStyleClass().add("statusLabel");

            enemyshipShieldProgress.setLayoutY(10);
            setProgressBarSize(enemyshipShieldProgress,"shieldStatus");
        }

        setTimeRemaining(gameArea);
        resize(gameArea);
    }

    public void setShipIntegrity(double shipIntegrity) {
        this.shipIntegrity = shipIntegrity;
    }

    private void createTimeRemaining(Button sendOrders){
        circle = new Circle(32);
        circle.setCenterY(circle.getRadius() + 19);
        circle.setStroke(Color.WHITE);

        progress = new ProgressIndicator(0);
        progress.setMinSize(100,100);
        progress.setLayoutY(10);
        progress.getStyleClass().add("timeIndicator");

        timeValue = new SimpleIntegerProperty(GameBalance.ROUND_TIME);
        timeValue.addListener((observable, oldValue, newValue) -> {
            double number = 1 - ((double)newValue.intValue())/GameBalance.ROUND_TIME;
            progress.setProgress(number);
        });

        time = new Label();
        time.setMinWidth(circle.getRadius() * 2);
        time.textProperty().bind(timeValue.asString());
        time.setTextFill(Color.WHITE);
        time.setAlignment(Pos.CENTER);
        time.setFont(Font.font(22));
        time.setLayoutY(circle.getRadius() + 3);

        roundTimeAnimation = new Timeline(new KeyFrame(Duration.seconds(1), event -> roundTimeAnimation(sendOrders)));
        roundTimeAnimation.setCycleCount(GameBalance.ROUND_TIME);
        roundTimeAnimation.playFromStart();
    }

    private void setTimeRemaining(Pane gameArea){
        gameArea.getChildren().addAll(progress, circle, time);
        progress.setLayoutX(gameArea.getWidth()/2 - 50);
        circle.setCenterX(gameArea.getWidth()/2);
        time.setLayoutX(gameArea.getWidth()/2 - circle.getRadius());

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

    private void roundTimeAnimation(Button sendOrders){

        if(timeValue.get() > 1){
            timeValue.set(timeValue.get() - 1);
        }else {
            sendOrders.fire();
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

        Platform.runLater(() -> {
            timeValue.set(GameBalance.ROUND_TIME);
            roundTimeAnimation.playFromStart();
        });
    }


    private void resize(Pane gameArea){
        enemyshipIntegrityProgress.setLayoutX(gameArea.getWidth() - 15 - 150);
        enemyLife.setLayoutX(gameArea.getWidth() - 15 - 150);

        if(!GlobalVariables.isEmpty(enemyshipShieldProgress)){
            enemyshipShieldProgress.setLayoutX(gameArea.getWidth() - 20 - 150*2);
            enemyShield.setLayoutX(gameArea.getWidth() - 20 - 150*2);
        }


        gameArea.widthProperty().addListener((observable, oldValue, newValue) -> {
            enemyshipIntegrityProgress.setLayoutX(newValue.doubleValue() - 15 - 150);
            enemyLife.setLayoutX(newValue.doubleValue() - 15 - 150);

            //double messageXWidth = messageToPlayer.getBoundsInParent().getMaxX() - messageToPlayer.getBoundsInParent().getMinX();
            messageToPlayer.setLayoutX(newValue.doubleValue()/2 - MESSAGE_WIDTH/2);
            if(!GlobalVariables.isEmpty(enemyshipShieldProgress)){
                enemyshipShieldProgress.setLayoutX(newValue.doubleValue() - 20 - 150*2);
                enemyShield.setLayoutX(newValue.doubleValue() - 20 - 150*2);
            }
        });
    }
}
