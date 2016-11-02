package game;

import client.TcpApplication;
import client.TcpMessage;
import game.StartUpMenu.*;
import game.background.GeneratRandomBackground;
import game.construction.Placement;
import game.ships.CommonShip;
import game.static_classes.GlobalVariables;
import game.weapons.CannonWeapon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    Label connectionStatusLabel;
    @FXML
    Button connectButton;
    @FXML
    GridPane window;

    private Button sendDataButton;
    private Pane gameAreaPane;
    private GeneratRandomBackground grb;
    private Controls controls;
    private ChangeListener <Number> userLost;
    private ChangeListener <Number> userWin;
    private ChangeListener <Boolean> waitingForOponentAttack;
    private TcpApplication tcpConnection;
    private Task findGame, waitingTask;
    private final int WAITING_FOR_OPONNENT = 100;
    private DamageHandler damageHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tcpConnection = new TcpApplication();
        CreateMenu createMenu = new CreateMenu();
        createMenu.setConnectionBinding(tcpConnection.isConnectedProperty());
        window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        setupPickShipMenu(createMenu);
        windowResize();
        grb = new GeneratRandomBackground();
        grb.showSpacePort(window);
        tcpConnection.connectThread();


        GlobalVariables.attackDefinition.addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){

                if(!GlobalVariables.isEmpty(damageHandler) && !GlobalVariables.isEmpty(controls)){
                    damageHandler.doDamage(newValue);
                    GlobalVariables.attackDefinition.set("");
                    controls.resetAnimation();
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
        });

    }

    public void clearApplication(){

        findGame = stopTask(findGame);
        waitingTask = stopTask(waitingTask);

        tcpConnection.closeConnectThread();
        tcpConnection.closeReadThread();

        if (!GlobalVariables.isEmpty(tcpConnection)) {
            tcpConnection.endConnection();
        }
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

            //gunsToShipMenu.clean();
            prepareGame(true);
        });
    }


    private void prepareGame(boolean isFirstCreated){

        //vytvari nepratelskou lod
        String exportMsg;
        ExportImportShip exportImportShip = new ExportImportShip();
        GlobalVariables.enemyshipDefinition = "";

        if(GlobalVariables.shipDefinition.isEmpty()){
            exportMsg = exportImportShip.exportShip(GlobalVariables.choosenShip);
            GlobalVariables.shipDefinition = exportMsg;
        }else{
            exportMsg = GlobalVariables.shipDefinition;
        }

        SimpleBooleanProperty isConnected = new SimpleBooleanProperty(false);
        WaitingForOponnent waitingForOponnent = new WaitingForOponnent(window);
        waitingForOponnent.getCancel().setOnAction(event -> {
            GlobalVariables.shipDefinition = "";
            waitingForOponnent.removePane();
            tcpConnection.endConnection();

            if(!GlobalVariables.isEmpty(findGame) && findGame.isRunning()){
                isConnected.unbind();
                isConnected.set(false);
                findGame.cancel();
                findGame = null;
            }
        });

        waitingTask = stopTask(waitingTask);
        waitingThread();

        findGame = new Task<Void>() {
            @Override public Void call() {
                boolean registrationSent = false;
                boolean registrationReceived = false;
                boolean gameStart = false;

                while(true) {
                    if (isCancelled()) {
                        tcpConnection.closeConnection();
                        Platform.exit();
                        break;
                    }

                    while (!tcpConnection.isConnected()){
                        registrationSent = false;
                        registrationReceived = false;
                        gameStart = false;
                    }

                    if (!registrationSent) {
                        registrationSent = tcpConnection.sendMessageToServer(TcpMessage.CONNECTION, exportMsg, TcpMessage.IDENTITY);
                    }

                    if(!registrationReceived && registrationSent){
                        registrationReceived = TcpMessage.IDENTITY.equals(GlobalVariables.receivedMsg);
                    }

                    if(registrationReceived && !gameStart) {
                        gameStart = tcpConnection.sendMessageToServer(TcpMessage.GAME_START, "start the game please", TcpMessage.END_WAITING);
                    }

                    if(gameStart){
                        if(TcpMessage.END_WAITING.equals(GlobalVariables.receivedMsg)){
                            break;
                        }
                    }



                    //pridat timeout
                    try {
                        Thread.sleep(WAITING_FOR_OPONNENT);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }



                }
                return null;
            }
        };
        isConnected.bind(findGame.runningProperty().not());
        new Thread(findGame).start();

        isConnected.addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue() && !GlobalVariables.enemyshipDefinition.isEmpty()){
                startGame(exportImportShip, isFirstCreated);
                waitingForOponnent.removePane();
            }
        });

    }


    private void startGame(ExportImportShip exportImportShip, boolean isFirstCreated){
        window.getChildren().clear();
        gameAreaPane = new Pane();
        window.add(gameAreaPane, 0, 0, GridPane.REMAINING, 1);

        Placement[][] placements = GlobalVariables.choosenShip.getPlacementPositions();
        GlobalVariables.choosenShip.displayShip(gameAreaPane);
        GlobalVariables.choosenShip.fillShipWithEquipment(GlobalVariables.choosenShip, placements, isFirstCreated);
        GlobalVariables.choosenShip.createShield();

        CommonShip enemyShip = exportImportShip.importShip(GlobalVariables.enemyshipDefinition, gameAreaPane);
        enemyShip.createShield();

        endWindowShowUp(GlobalVariables.choosenShip, enemyShip);

        //pozadi
        grb.findImages();
        grb.chooseImage((GridPane) gameAreaPane.getParent(), GlobalVariables.startingID);

        //horni prvky
        sendDataButton = new Button();
        controls = new Controls(GlobalVariables.choosenShip, enemyShip, sendDataButton);
        controls.showStatusBars(gameAreaPane);

        //dolni prvky
        BottomPanel bottomPanel = new BottomPanel(sendDataButton);
        bottomPanel.showPanel(window, gameAreaPane);
        bottomPanel.getQuit().setOnAction(event1 -> {
            tcpConnection.sendMessageToServer(TcpMessage.LOST,  "vzdavam se", TcpMessage.LOST);
            ((Button)event1.getSource()).setDisable(true);
            GlobalVariables.choosenShip.takeDamage((int)GlobalVariables.choosenShip.getActualLife());
            GlobalVariables.choosenShip.damageToShield(GlobalVariables.choosenShip.getShieldActualLife());
        });

        damageHandler = new DamageHandler(GlobalVariables.choosenShip, enemyShip, gameAreaPane);
        sendDataButton.setOnAction(event1 -> {
            if(GlobalVariables.isPlayingNow.get()){
                String status = damageHandler.exportEquipmentStatus(GlobalVariables.choosenShip.getPlacementPositions());
                tcpConnection.sendMessageToServer(TcpMessage.EQUIPMENT_STATUS, status, TcpMessage.WAITING);
                String actions = damageHandler.exportShooting(GlobalVariables.choosenShip.getPlacementPositions());
                tcpConnection.sendMessageToServer(TcpMessage.ATTACK, actions, TcpMessage.ATTACK);
            }
        });


    }

    private void waitingThread(){

        if(GlobalVariables.expectedMsg.isEmpty()){
            GlobalVariables.expectedMsg = TcpMessage.WAITING;
        }

        waitingTask = new Task() {
            @Override
            protected Object call() throws Exception {

                while (true){
                    if(isCancelled()){
                        break;
                    }

                    if(tcpConnection.listenForMessage(GlobalVariables.expectedMsg)){
                        GlobalVariables.receivedMsg = GlobalVariables.expectedMsg;
                        GlobalVariables.expectedMsg = TcpMessage.WAITING;
                    }
                }

                return null;
            }
        };

        new Thread(waitingTask).start();
    }


    /**
     * zobrazi obrazovku pro vyber lodi
     * @param createMenu
     */
    private void setupPickShipMenu(CreateMenu createMenu){
        createMenu.getStart().setOnAction(event -> {
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
                EndOfGameMenu endOfGame = new EndOfGameMenu(false);
                endWindowSetting(endOfGame, usersShip, enemyShip);
            }
        };

        userWin = (observable, oldValue, newValue) -> {

            if (newValue.doubleValue() <= 0) {
                EndOfGameMenu endOfGame = new EndOfGameMenu(true);
                endWindowSetting(endOfGame, usersShip, enemyShip);
            }
        };

        usersShip.getActualLifeBinding().addListener(userLost);
        enemyShip.getActualLifeBinding().addListener(userWin);
        GlobalVariables.enemyLost.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Platform.runLater(() -> {
                    enemyShip.takeDamage((int) enemyShip.getActualLife());
                });
            }
        });
    }

    /**
     * nastavuje tlacitka na obrazovce s ukoncenou hrou
     * @param endOfGame
     */
    private void endWindowSetting(EndOfGameMenu endOfGame, CommonShip usersShip,CommonShip enemyShip){
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(3.5), event1 -> {
            endOfGame.setupWindow(window);
            controls.stopAnimations();
            usersShip.getActualLifeBinding().removeListener(userLost);
            enemyShip.getActualLifeBinding().removeListener(userWin);

            endOfGame.getBackToMenu().setOnAction(event -> {
                tcpConnection.endConnection();
                GlobalVariables.shipDefinition = "";
                createMainPage();
            });

            endOfGame.getNewGame().setOnAction(event -> {
                window.getChildren().clear();
                GlobalVariables.choosenShip.restartValues();
                GlobalVariables.choosenShip.unmarkObject();
                prepareGame(false);
            });
        }));
        delay.setCycleCount(1);
        delay.playFromStart();
    }



    private void createMainPage(){
        window.getChildren().clear();
        CreateMenu createMenu = new CreateMenu();
        window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        createMenu.setConnectionBinding(tcpConnection.isConnectedProperty());

        setupPickShipMenu(createMenu);
        grb.showSpacePort(window);

    }
}
