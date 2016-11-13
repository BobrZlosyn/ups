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
    GridPane window;

    private Button sendDataButton;
    private Pane gameAreaPane;
    private GeneratRandomBackground grb;
    private Controls controls;
    private ChangeListener <Number> userLost;
    private ChangeListener <Number> userWin;
    private TcpApplication tcpConnection;
    private Task findGame, waitingTask;
    private DamageHandler damageHandler;
    private Task <Boolean> sendingTask;
    private ExportImportShip exportImportShip;
    private WaitingForOponnent waitingForOponnent;
    private String sendMessageType = "";
    private ErrorAlert errorAlert;

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

        findGame = stopTask(findGame); //ukoncuji hledani hry
        waitingTask = stopTask(waitingTask); // ukoncuji smycku pri cekani na pozadovanou zpravu
        sendingTask = stopTask(sendingTask); // ukoncuji odesilaci vlakno
        tcpConnection.closeReadThread(); // ukoncuji cteci vlakno
        tcpConnection.closeConnectThread(); // ukoncuji hledani pripojeni
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

        SimpleBooleanProperty isConnected = new SimpleBooleanProperty(false);
        waitingForOponnent.showWaitingForOponnent(window);
        waitingForOponnent.getCancel().setOnAction(event -> {
            GlobalVariables.enemyshipDefinition = "";
            waitingForOponnent.removePane();
            sendMessageType = TcpMessage.QUIT;

            if(!GlobalVariables.isEmpty(findGame) && findGame.isRunning()){
                isConnected.unbind();
                isConnected.set(false);
                findGame.cancel();
                findGame = null;
            }
        });

        waitingTask = stopTask(waitingTask);
        waitingThread();

        sendMessageType = TcpMessage.CONNECTION;
    }

    /**
     * nastavi vse potrebne pro zobrazeni hry a jeji ovladani
     */
    private void startGame(){
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
            sendMessageType = TcpMessage.LOST;
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
            GlobalVariables.enemyshipDefinition = "";

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
        CreateMenu createMenu = new CreateMenu();
        window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        createMenu.setConnectionBinding(tcpConnection.isConnectedProperty());

        setupPickShipMenu(createMenu);
        grb.showSpacePort(window);
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

                    if (isCancelled() && sendMessageType.isEmpty()) {
                        return false;
                    }

                    if(!tcpConnection.isConnected()){

                        if (isCancelled()) return false;
                        Thread.sleep(1000);
                        continue;
                    }


                    if(sendMessageType.isEmpty()){
                        Thread.sleep(100);
                        continue;
                    }

                    switch (sendMessageType){
                        case TcpMessage.CONNECTION: {

                            if (!tcpConnection.getMessage().hasId()) {
                                tcpConnection.sendMessageToServer(TcpMessage.CONNECTION, GlobalVariables.shipDefinition, TcpMessage.IDENTITY);
                                Thread.sleep(1000);
                            }

                            tcpConnection.sendMessageToServer(TcpMessage.GAME_START, "start the game please", TcpMessage.END_WAITING);

                            while (!TcpMessage.END_WAITING.equals(GlobalVariables.receivedMsg)){
                                Thread.sleep(100);
                                if(isCancelled()){
                                    return false;
                                }
                            }

                            GlobalVariables.receivedMsg = "";
                            Platform.runLater(() -> {
                                System.out.println("ahoj");
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

                    sendMessageType = "";
                }
            }
        };

        new Thread(sendingTask).start();
    }


}
