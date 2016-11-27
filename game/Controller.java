package game;

import client.TcpApplication;
import client.TcpMessage;
import game.exportImportDataHandlers.DamageHandler;
import game.exportImportDataHandlers.ExportImportShip;
import game.startUpMenu.*;
import game.background.GeneratRandomBackground;
import game.construction.Placement;
import game.gameUI.BottomPanel;
import game.gameUI.Controls;
import game.ships.CommonShip;
import game.static_classes.GlobalVariables;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        errorAlert = new ErrorAlert();
        tcpConnection = new TcpApplication();

        grb = new GeneratRandomBackground();
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

                if (!GlobalVariables.isEmpty(damageHandler)) {
                    Platform.runLater(() ->{
                        damageHandler.importEquipmentStatus(newValue);
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
                window.getChildren().clear();
                GlobalVariables.errorMsg = ErrorAlert.NOT_CONNECTED_TO_SERVER;
                createMainPage();
            }
        });


    }

    public void clearApplication(){

        if (!GlobalVariables.isEmpty(tcpConnection)) {
            tcpConnection.endConnection();
        }

        if (GlobalVariables.isNotEmpty(createMenu)) {
            createMenu.stopAnimation();
        }

        sendingTask = stopTask(sendingTask); // ukoncuji odesilaci vlakno
        tcpConnection.closeReadThread(); // ukoncuji cteci vlakno
        tcpConnection.closeConnectThread(); // ukoncuji hledani pripojeni
        tcpConnection.closeActionThread();
    }


    private Task stopTask(Task task){
        if(!GlobalVariables.isEmpty(task)){
            if(task.isRunning()){
                task.cancel();
            }
        }

        return null;
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
            pickShipMenu.clean();
            createMainPage();
        });

        pickShipMenu.getNextSetup().setOnAction(event -> {
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
            gunsToShipMenu.clean();
            PickShipMenu pickShipMenu = new PickShipMenu();
            window.add(pickShipMenu.getPickship(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
            setupGunsToShipMenu(pickShipMenu);
        });

        gunsToShipMenu.getNextButton().setOnAction(event -> {
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
            GlobalVariables.enemyshipDefinition = "";
            waitingForOponnent.removePane();
            GlobalVariables.sendMessageType = TcpMessage.QUIT;

        });

        GlobalVariables.sendMessageType = TcpMessage.CONNECTION;
    }

    /**
     * nastavi vse potrebne pro zobrazeni hry a jeji ovladani
     */
    private void startGame(){
        GlobalVariables.setGameIsFinished(false);
        window.getChildren().clear();
        gameAreaPane = new Pane();
        window.add(gameAreaPane, 0, 0, GridPane.REMAINING, 1);

        Placement[][] placements = GlobalVariables.choosenShip.getPlacementPositions();
        GlobalVariables.choosenShip.displayShip(gameAreaPane);
        GlobalVariables.choosenShip.fillShipWithEquipment(GlobalVariables.choosenShip, placements, exportImportShip.isFirstExport());
        GlobalVariables.choosenShip.createShield();

        CommonShip enemyShip = exportImportShip.importShip(GlobalVariables.enemyshipDefinition, gameAreaPane);
        enemyShip.createShield();

        endWindowShowUp(GlobalVariables.choosenShip, enemyShip);

        //pozadi
        grb.chooseImage((GridPane) gameAreaPane.getParent(), GlobalVariables.startingID);

        //horni prvky
        sendDataButton = new Button();
        sendDataButton.setDisable(!GlobalVariables.isPlayingNow.getValue());
        controls = new Controls(GlobalVariables.choosenShip, enemyShip, sendDataButton);
        controls.showStatusBars(gameAreaPane);



        //dolni prvky
        bottomPanel = new BottomPanel(sendDataButton);
        bottomPanel.showPanel(window, gameAreaPane);
        bottomPanel.getQuit().setOnAction(event1 -> {
            GlobalVariables.setGameIsFinished(true);
            GlobalVariables.sendMessageType = TcpMessage.LOST;
            ((Button)event1.getSource()).setDisable(true);
            GlobalVariables.choosenShip.takeDamage((int)GlobalVariables.choosenShip.getActualLife());
            GlobalVariables.choosenShip.damageToShield(GlobalVariables.choosenShip.getShieldActualLife());
        });

        damageHandler = new DamageHandler(GlobalVariables.choosenShip, enemyShip, gameAreaPane, sendDataButton);
        sendDataButton.setOnAction(event1 -> {
            if(GlobalVariables.isPlayingNow.get()){
                sendDataButton.setDisable(true);
                String status = damageHandler.exportEquipmentStatus(GlobalVariables.choosenShip.getPlacementPositions());
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
                EndOfGameMenu endOfGame = new EndOfGameMenu(false);
                endWindowSetting(endOfGame, usersShip, enemyShip);
                if(GlobalVariables.isNotEmpty(bottomPanel)){
                    bottomPanel.getQuit().setDisable(true);
                }
                GlobalVariables.sendMessageType = TcpMessage.LOST;
            }
        };

        userWin = (observable, oldValue, newValue) -> {

            if (newValue.doubleValue() <= 0) {
                GlobalVariables.setGameIsFinished(true);
                EndOfGameMenu endOfGame = new EndOfGameMenu(true);
                endWindowSetting(endOfGame, usersShip, enemyShip);
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

                    enemyShip.takeDamage((int) enemyShip.getActualLife());
                    GlobalVariables.enemyLost.set(false);
                });
            }
        });
    }

    /**
     * nastavuje tlacitka na obrazovce s ukoncenou hrou
     * @param endOfGame
     */
    private void endWindowSetting(EndOfGameMenu endOfGame, CommonShip usersShip,CommonShip enemyShip){
        sendDataButton.setDisable(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(3.5), event1 -> {
            endOfGame.setupWindow(window);
            controls.stopAnimations();
            usersShip.getActualLifeBinding().removeListener(userLost);
            enemyShip.getActualLifeBinding().removeListener(userWin);
            GlobalVariables.enemyshipDefinition = "";
            GlobalVariables.setDefaultValues();

            endOfGame.getBackToMenu().setOnAction(event -> {
                tcpConnection.endConnection();
                GlobalVariables.shipDefinition = "";
                createMainPage();
            });

            endOfGame.getNewGame().setOnAction(event -> {
                GlobalVariables.choosenShip.restartValues();
                GlobalVariables.choosenShip.unmarkObject();
                exportImportShip.setFirstExport(false);
                prepareGame();
            });
        }));
        delay.setCycleCount(1);
        delay.playFromStart();
    }

    private void createMainPage(){
        window.getChildren().clear();
        createMenu = new CreateMenu();
        window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        createMenu.setConnectionBinding(tcpConnection.isConnectedProperty());

        setupPickShipMenu(createMenu);
        grb.showWelcomeImage(window);
        errorAlert.showErrorPane(window);
    }

    /**
     * vlakno pro odesilani zprav
     */
    private void sendingThread(){
        if (!GlobalVariables.isEmpty(sendingTask)) {
            return;
        }

        sendingTask = new Task <Boolean>() {
            @Override
            protected Boolean call() throws InterruptedException{
                while (true){
                    if (isCancelled() && GlobalVariables.sendMessageType.isEmpty()) {
                        return false;
                    }

                    if(!tcpConnection.isConnected()){
                        if(GlobalVariables.sendMessageType.equals(TcpMessage.CONNECTION)){
                            waitingForOponnent.setTitleText(WaitingForOponnent.CONNECTING_TO_SERVER);
                        }

                        if(GlobalVariables.sendMessageType.equals(TcpMessage.QUIT)){
                            GlobalVariables.sendMessageType = "";
                        }

                        if (isCancelled()) return false;
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
                            }

                            waitingForOponnent.setTitleText(WaitingForOponnent.CREATING_GAME);
                            tcpConnection.sendMessageToServer(TcpMessage.GAME_START, "start the game please", TcpMessage.END_WAITING);

                            boolean error = false;
                            waitingForOponnent.setTitleText(WaitingForOponnent.WAITING_FOR_OPONENT);
                            while (!TcpMessage.END_WAITING.equals(GlobalVariables.receivedMsg)){

                                if(isCancelled()){
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


}
