package game;

import client.TcpApplication;
import client.TcpMessage;
import game.exportImportDataHandlers.LoadSettings;
import game.gameUI.DamageHandler;
import game.exportImportDataHandlers.ExportImportShip;
import game.gameUI.OpponentLostMenu;
import game.startUpMenu.*;
import game.background.GeneratRandomBackground;
import game.construction.Placement;
import game.gameUI.BottomPanel;
import game.gameUI.Controls;
import game.ships.CommonShip;
import game.static_classes.GlobalVariables;
import game.static_classes.StyleClasses;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    GridPane window;

    private Button sendDataButton;
    private Pane gameAreaPane;
    private GeneratRandomBackground grb;
    private Controls controls;
    private ChangeListener <Number> userLost;
    private ChangeListener <Number> userWin;
    private TcpApplication tcpConnection;
    private DamageHandler damageHandler;
    private Task <Boolean> sendingTask;
    private ExportImportShip exportImportShip;
    private WaitingForOponnent waitingForOponnent;
    private ErrorAlert errorAlert;
    private BottomPanel bottomPanel;
    private CreateMenu createMenu;
    private OpponentLostMenu opponentLostMenu;
    private EndOfGameMenu endOfGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        errorAlert = new ErrorAlert();
        tcpConnection = new TcpApplication();
        window.setCursor(StyleClasses.NORMAL_CURSOR);
        endOfGame = new EndOfGameMenu(false);
        setupEndOfGameMenu();

        opponentLostMenu = new OpponentLostMenu(OpponentLostMenu.WAIT_FOR_OPONNENT_RECONNECTION);
        grb = new GeneratRandomBackground();
        LoadSettings.loadSettings(0);
        createMainPage();
        windowResize();
        tcpConnection.connectThread();
        exportImportShip = new ExportImportShip();
        waitingForOponnent = new WaitingForOponnent();
        sendingThread();

        GlobalVariables.attackDefinition.addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){

                if(!GlobalVariables.isEmpty(damageHandler) && !GlobalVariables.isEmpty(controls)){
                    Platform.runLater(() -> {
                        damageHandler.doDamage(newValue);
                        GlobalVariables.attackDefinition.set("");
                        controls.resetAnimation();
                    });
                }
            }
        });

        GlobalVariables.equipmentStatus.addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {

                if (!GlobalVariables.isEmpty(exportImportShip)) {
                    Platform.runLater(() ->{
                        exportImportShip.importEquipmentStatus(newValue);
                        GlobalVariables.equipmentStatus.set("");
                    });
                }
            }
        });

        GlobalVariables.isPlayingNow.addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                GlobalVariables.expectedMsg = TcpMessage.ATTACK;
            }

            //napise zpravu hraci o tom kdo hraje
            if (GlobalVariables.isNotEmpty(controls)){
                if (newValue) {
                    controls.setMessageToPlayer(Controls.USER_IS_PLAYING);
                }else {
                    controls.setMessageToPlayer(Controls.ENEMY_IS_PLAYING);
                }
            }
        });

        tcpConnection.isConnectedProperty().addListener((observable, oldValue, newValue) ->{
            if (!newValue && !GlobalVariables.enemyshipDefinition.isEmpty()){
                Platform.runLater(() -> {
                    opponentLostMenu.showWindow(window);
                    controls.pauseAnimations();
                    /*window.getChildren().clear();
                    GlobalVariables.errorMsg = ErrorAlert.NOT_CONNECTED_TO_SERVER;
                    createMainPage();*/
                });

            }else if(newValue && !GlobalVariables.enemyshipDefinition.isEmpty()){
                Platform.runLater(() -> {
                    opponentLostMenu.clean();
                    tcpConnection.sendMessageToServer(TcpMessage.RESULT, "pripojeni zpet do hry", TcpMessage.NONE);
                    controls.resumeAnimations();
                });
            }
        });

        GlobalVariables.reconnection.addListener((observable, oldValue, newValue) -> {
            if (newValue){
                Platform.runLater(() -> {
                    opponentLostMenu.showWindow(window);
                    controls.pauseAnimations();
                });
            }
        });

        opponentLostMenu.getQuit().setOnAction(event -> {
            opponentLostMenu.clean();
            endOfGame.setUserIsWinner(false, true);
            endOfGame.showWindow(window);
        });


    }

    public void clearApplication(){

        if (!GlobalVariables.isEmpty(tcpConnection)) {
            tcpConnection.endConnection();
        }

        GlobalVariables.APLICATION_EXIT = true;
    }

    /**
     * prekresluje pozadi pri zmene velikosti
     */
    private void windowResize(){
        window.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(!GlobalVariables.isEmpty(grb)){
                grb.resizeImage(window, newValue.doubleValue(), window.getHeight());
            }
        });

        window.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (!GlobalVariables.isEmpty(grb)) {
                grb.resizeImage(window, window.getWidth(), newValue.doubleValue());
            }
        });
    }

    /**
     * zobrazi obrazovku pro vybaveni lodi
     * @param pickShipMenu
     */
    private void setupGunsToShipMenu(PickShipMenu pickShipMenu){
        pickShipMenu.getPrevious().setOnAction(event1 -> {
            CommonMenu.clickSound();
            pickShipMenu.clean();
            createMainPage();
        });

        pickShipMenu.getNextSetup().setOnAction(event -> {
            CommonMenu.clickSound();
            GlobalVariables.choosenShip = pickShipMenu.getChoosenShip();
            pickShipMenu.clean();

            GunsToShipMenu gunsToShipMenu = new GunsToShipMenu(GlobalVariables.choosenShip);
            window.add(gunsToShipMenu.getGunsToShipPane(),0,0, GridPane.REMAINING, GridPane.REMAINING);
            setupStartButton(gunsToShipMenu);
        });
    }


    /**
     * spusti obrazovku se hrou
     * @param gunsToShipMenu
     */
    private void setupStartButton(GunsToShipMenu gunsToShipMenu){
        gunsToShipMenu.getPrevious().setOnAction(event1 -> {
            CommonMenu.clickSound();
            gunsToShipMenu.clean();
            PickShipMenu pickShipMenu = new PickShipMenu();
            window.add(pickShipMenu.getPickship(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
            setupGunsToShipMenu(pickShipMenu);
        });

        gunsToShipMenu.getNextButton().setOnAction(event -> {
            CommonMenu.clickSound();
            exportImportShip.setFirstExport(true);
            prepareGame();
        });
    }

    /**
     * zde se zapne obrazovka pro hledani hrace a pri tom se hledani spusti
     */
    private void prepareGame(){

        //vytvari nepratelskou lod
        if(exportImportShip.isFirstExport()){
            GlobalVariables.shipDefinition = exportImportShip.exportShip(GlobalVariables.choosenShip);
        }

        waitingForOponnent.showWaitingForOponnent(window);
        waitingForOponnent.getCancel().setOnAction(event -> {
            CommonMenu.clickSound();
            GlobalVariables.enemyshipDefinition = "";
            waitingForOponnent.clean();
            GlobalVariables.sendMessageType = TcpMessage.QUIT;

        });

        GlobalVariables.sendMessageType = TcpMessage.CONNECTION;
    }

    /**
     * nastavi vse potrebne pro zobrazeni hry a jeji ovladani
     */
    private void startGame(){
        if (GlobalVariables.isNotEmpty(waitingForOponnent)) {
            waitingForOponnent.stopAnimation();
        }

        GlobalVariables.setGameIsFinished(false);
        window.getChildren().clear();
        // pozadi
        grb.chooseImage(window, GlobalVariables.startingID);

        gameAreaPane = new Pane();
        window.add(gameAreaPane, 0, 0, GridPane.REMAINING, 1);

        Placement[][] placements = GlobalVariables.choosenShip.getPlacementPositions();
        GlobalVariables.choosenShip.displayShip(gameAreaPane);
        GlobalVariables.choosenShip.fillShipWithEquipment(GlobalVariables.choosenShip, placements, exportImportShip.isFirstExport());
        GlobalVariables.choosenShip.createShield();

        CommonShip enemyShip = exportImportShip.importShip(GlobalVariables.enemyshipDefinition, gameAreaPane);
        if(GlobalVariables.isEmpty(enemyShip)){
            GlobalVariables.enemyshipDefinition = "";
            createMainPage();
            errorAlert.showErrorPaneWithText(window, "Omlouváme se, nastala chyba při získávání dat ze serveru");
        }
        enemyShip.createShield();

        endWindowShowUp(GlobalVariables.choosenShip, enemyShip);



        //dolni prvky
        bottomPanel = new BottomPanel();
        sendDataButton = bottomPanel.getSendData();
        bottomPanel.showPanel(window, gameAreaPane);
        bottomPanel.getQuit().setOnAction(event1 -> {
            CommonMenu.clickSound();
            GlobalVariables.setGameIsFinished(true);
            GlobalVariables.sendMessageType = TcpMessage.LOST;
            ((Button)event1.getSource()).setDisable(true);
            GlobalVariables.choosenShip.takeDamage((int)GlobalVariables.choosenShip.getActualLife() + GlobalVariables.choosenShip.getArmorActualValue());
            GlobalVariables.choosenShip.damageToShield(GlobalVariables.choosenShip.getShieldActualLife());
        });


        controls = new Controls(GlobalVariables.choosenShip, enemyShip, sendDataButton);
        controls.showStatusBars(gameAreaPane);

        damageHandler = new DamageHandler(GlobalVariables.choosenShip, enemyShip, gameAreaPane, sendDataButton);
        sendDataButton.setOnAction(event1 -> {
            if(GlobalVariables.isPlayingNow.get()){
                CommonMenu.clickSound();
                sendDataButton.setDisable(true);
                String status = exportImportShip.exportEquipmentStatus(GlobalVariables.choosenShip.getPlacementPositions());
                tcpConnection.sendMessageToServer(TcpMessage.EQUIPMENT_STATUS, status, TcpMessage.WAITING);
                String actions = damageHandler.exportShooting(GlobalVariables.choosenShip.getPlacementPositions());
                tcpConnection.sendMessageToServer(TcpMessage.ATTACK, actions, TcpMessage.ATTACK);
            }
        });
    }

    /**
     * zobrazi obrazovku pro vyber lodi
     * @param createMenu
     */
    private void setupPickShipMenu(CreateMenu createMenu){
        createMenu.getStart().setOnAction(event -> {
            CommonMenu.clickSound();
            grb.showSpacePort(window);
            createMenu.removeConnectionBinding(tcpConnection.isConnectedProperty());
            createMenu.clean();
            PickShipMenu pickShipMenu = new PickShipMenu();
            window.add(pickShipMenu.getPickship(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);

            setupGunsToShipMenu(pickShipMenu);
        });
    }

    /**
     * zobrazi obrazovku na konci hry pokud nektera hra je znicena
     * @param usersShip
     * @param enemyShip
     */
    private void endWindowShowUp(CommonShip usersShip, CommonShip enemyShip){

        userLost = (observable, oldValue, newValue) -> {

            if(newValue.doubleValue() <= 0){
                GlobalVariables.setGameIsFinished(true);
                endOfGame.setUserIsWinner(false, false);
                endWindowSetting(usersShip, enemyShip);
                if(GlobalVariables.isNotEmpty(bottomPanel)){
                    bottomPanel.getQuit().setDisable(true);
                }
                GlobalVariables.sendMessageType = TcpMessage.LOST;
            }
        };

        userWin = (observable, oldValue, newValue) -> {

            if (newValue.doubleValue() <= 0) {
                GlobalVariables.setGameIsFinished(true);
                endOfGame.setUserIsWinner(true, false);
                endWindowSetting(usersShip, enemyShip);
                if(GlobalVariables.isNotEmpty(bottomPanel)){
                    bottomPanel.getQuit().setDisable(true);
                }
            }
        };

        usersShip.getActualLifeBinding().addListener(userLost);
        enemyShip.getActualLifeBinding().addListener(userWin);
        GlobalVariables.enemyLost.addListener((observable, oldValue, newValue) -> {
            if(newValue && !GlobalVariables.gameIsFinished.get()){
                Platform.runLater(() -> {
                    enemyShip.takeDamage((int) enemyShip.getActualLife() + enemyShip.getArmorActualValue());
                    GlobalVariables.enemyLost.set(false);
                });
            }
        });


        opponentLostMenu.getTimeExpiredProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue) {
                return;
            }

            if (tcpConnection.isConnected() ) {
                enemyShip.takeDamage((int) enemyShip.getActualLife() + enemyShip.getArmorActualValue());
                opponentLostMenu.clean();
            } else {
                tcpConnection.endConnection();
                GlobalVariables.setDefaultValues();
                GlobalVariables.errorMsg = ErrorAlert.NOT_CONNECTED_TO_SERVER;
                GlobalVariables.reconnection.set(false);
                createMainPage();
            }

        });
    }

    /**
     * nastavuje tlacitka na obrazovce s ukoncenou hrou
     */
    private void endWindowSetting(CommonShip usersShip,CommonShip enemyShip){
        sendDataButton.setDisable(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(3.5), event1 -> {
            endOfGame.showWindow(window);
            controls.stopAnimations();
            usersShip.getActualLifeBinding().removeListener(userLost);
            enemyShip.getActualLifeBinding().removeListener(userWin);
            GlobalVariables.enemyshipDefinition = "";
            GlobalVariables.setDefaultValues();
        }));
        delay.setCycleCount(1);
        delay.playFromStart();
    }

    private void setupEndOfGameMenu() {
        endOfGame.getBackToMenu().setOnAction(event -> {
            CommonMenu.clickSound();
            tcpConnection.endConnection();
            GlobalVariables.shipDefinition = "";
            createMainPage();
        });

        endOfGame.getNewGame().setOnAction(event -> {
            CommonMenu.clickSound();
            GlobalVariables.choosenShip.restartValues();
            GlobalVariables.choosenShip.unmarkObject();
            exportImportShip.setFirstExport(false);
            prepareGame();
        });
    }

    private void createMainPage(){

        grb.showWelcomeImage(window);
        window.getChildren().clear();
        if (GlobalVariables.isEmpty(createMenu)) {
            createMenu = new CreateMenu();
        }

        createMenu.showWindow(window);
        createMenu.setConnectionBinding(tcpConnection.isConnectedProperty());


        setupPickShipMenu(createMenu);
        errorAlert.showErrorPane(window);
    }

    /**
     * vlakno pro odesilani zprav
     */
    private void sendingThread(){
        if (!GlobalVariables.isEmpty(sendingTask) || GlobalVariables.APLICATION_EXIT) {
            return;
        }

        sendingTask = new Task <Boolean>() {
            @Override
            protected Boolean call() throws InterruptedException{
                while (true){

                    if (GlobalVariables.APLICATION_EXIT && GlobalVariables.sendMessageType.isEmpty()) {
                        return false;
                    }

                    if(!tcpConnection.isConnected()){
                        if(GlobalVariables.sendMessageType.equals(TcpMessage.CONNECTION)){
                            waitingForOponnent.setTitleText(WaitingForOponnent.CONNECTING_TO_SERVER);
                        }

                        if(GlobalVariables.sendMessageType.equals(TcpMessage.QUIT)){
                            GlobalVariables.sendMessageType = "";
                        }

                        if (GlobalVariables.APLICATION_EXIT) return false;
                        Thread.sleep(1000);
                        continue;
                    }


                    if(GlobalVariables.sendMessageType.isEmpty()){
                        Thread.sleep(100);
                        continue;
                    }

                    switch (GlobalVariables.sendMessageType){
                        case TcpMessage.CONNECTION: {

                            if (!tcpConnection.getMessage().hasId()) {
                                tcpConnection.sendMessageToServer(TcpMessage.CONNECTION, GlobalVariables.shipDefinition, TcpMessage.IDENTITY);
                                Thread.sleep(1000);
                            }else {
                                tcpConnection.sendMessageToServer(TcpMessage.RESULT, GlobalVariables.shipDefinition, TcpMessage.IDENTITY);
                                Thread.sleep(1000);
                            }

                            waitingForOponnent.setTitleText(WaitingForOponnent.CREATING_GAME);
                            tcpConnection.sendMessageToServer(TcpMessage.GAME_START, "start the game please", TcpMessage.END_WAITING);

                            boolean error = false;
                            waitingForOponnent.setTitleText(WaitingForOponnent.WAITING_FOR_OPONENT);
                            while (!TcpMessage.END_WAITING.equals(GlobalVariables.receivedMsg)){

                                if(GlobalVariables.APLICATION_EXIT){
                                    return false;
                                }

                                if(!tcpConnection.isConnected()){
                                    error = true;
                                    break;
                                }

                                if(GlobalVariables.sendMessageType.equals(TcpMessage.QUIT)){
                                    error = true;
                                    break;
                                }
                                Thread.sleep(100);
                            }
                            if (error){
                                continue;
                            }

                            waitingForOponnent.setTitleText(WaitingForOponnent.STARTING_GAME);
                            GlobalVariables.receivedMsg = "";
                            Platform.runLater(() -> {
                                startGame();
                            });

                        }break;

                        case TcpMessage.QUIT: {
                            tcpConnection.endConnection();
                        }break;

                        case TcpMessage.LOST: {
                            tcpConnection.sendMessageToServer(TcpMessage.LOST,  "vzdavam se", TcpMessage.LOST);
                        }break;

                        case TcpMessage.DESTROY_CONNECTION: {
                            return true;
                        }

                    }

                    GlobalVariables.sendMessageType = "";
                }
            }
        };

        new Thread(sendingTask).start();
    }


    private void resizeGameArea(){
        gameAreaPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            CommonShip usersShip = GlobalVariables.choosenShip;

            if(GlobalVariables.isEmpty(usersShip)){
                return;
            }
            usersShip.resize(0, newValue.doubleValue()/2, 0, gameAreaPane.getHeight());
        });

        gameAreaPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            CommonShip usersShip = GlobalVariables.choosenShip;

            if(GlobalVariables.isEmpty(usersShip)){
                return;
            }
            usersShip.resize(0, gameAreaPane.getWidth()/2, 0, newValue.doubleValue());
        });
    }

}
