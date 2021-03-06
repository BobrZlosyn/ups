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
import java.io.InterruptedIOException;
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
    private CommonShip enemyShip;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        errorAlert = new ErrorAlert();
        tcpConnection = new TcpApplication();
        window.setCursor(StyleClasses.NORMAL_CURSOR);
        endOfGame = new EndOfGameMenu(false);
        setupEndOfGameMenu();

        //cekani na znovu pripojeni nepotrebne zatim
        //opponentLostMenu = new OpponentLostMenu(OpponentLostMenu.WAIT_FOR_OPONNENT_RECONNECTION);
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
            /* pro znovu pripojeni - zatim nepotrebne
            if (!newValue && !GlobalVariables.enemyshipDefinition.isEmpty()){
                if (!opponentLostMenu.getTimeExpiredProperty().get()) {
                    GlobalVariables.reconnection.set(true);
                }

            }else if(newValue && !GlobalVariables.enemyshipDefinition.isEmpty()){
                tcpConnection.sendMessageToServer(TcpMessage.RESULT, createMessageForReconnection(), TcpMessage.NONE);
            }*/

            if(GlobalVariables.enemyshipDefinition.isEmpty()){
                return;
            }

            if(!newValue){
                Platform.runLater(() -> {
                    GlobalVariables.errorMsg = ErrorAlert.NOT_CONNECTED_TO_SERVER;
                    tcpConnection.endConnection();
                    createMainPage();
                });

            }
        });

        /* poslouchani zda se ma spustit reconnect akce - nepotrebne zatim
        GlobalVariables.reconnection.addListener((observable, oldValue, newValue) -> {
            if (newValue){
                Platform.runLater(() -> {
                    opponentLostMenu.showWindow(window);
                    controls.pauseAnimations();
                });
            }else {
                Platform.runLater(() -> {
                    if(tcpConnection.getMessage().getType().equals(TcpMessage.RECONNECTION_BACK)){
                        handleReconnectionMessage(tcpConnection.getMessage().getData());
                    }
                });
            }
        });

        opponentLostMenu.getQuit().setOnAction(event -> {
            GlobalVariables.sendMessageType = TcpMessage.LOST;
            opponentLostMenu.clean();
            endOfGame.setUserIsWinner(false, true);
            endOfGame.showWindow(window);
        });
        */


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
            GlobalVariables.shipDefinition = exportImportShip.exportShip(GlobalVariables.choosenShip, true);
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

        enemyShip = exportImportShip.importShip(GlobalVariables.enemyshipDefinition, gameAreaPane);
        if(GlobalVariables.isEmpty(enemyShip)){
            GlobalVariables.enemyshipDefinition = "";
            GlobalVariables.errorMsg = ErrorAlert.NOT_VALID_SHIP_DEFINITION;
            createMainPage();
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

        //waitingForReconnectAction(); - akce pro zhodnoceni zbyvajiciho casu - nepotrebne zatim

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
        GlobalVariables.setDefaultValues();
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
            protected Boolean call() {
                while (true){

                    if (GlobalVariables.APLICATION_EXIT && GlobalVariables.sendMessageType.isEmpty()) {
                        return false;
                    }

                    if(!tcpConnection.isConnected()){
                        //zapnuti cekani hrace na pridani do hry
                        if(GlobalVariables.sendMessageType.equals(TcpMessage.CONNECTION)){
                            waitingForOponnent.setTitleText(WaitingForOponnent.CONNECTING_TO_SERVER);
                        }

                        //neni potreba ukoncit kdyz neni pripojen
                        if(GlobalVariables.sendMessageType.equals(TcpMessage.QUIT)){
                            GlobalVariables.sendMessageType = "";
                        }

                        //ukonceni aplikace
                        if (GlobalVariables.APLICATION_EXIT) return false;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }


                    if(GlobalVariables.sendMessageType.isEmpty()){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    switch (GlobalVariables.sendMessageType){
                        case TcpMessage.CONNECTION: {
                            tcpConnection.getMessage().removeID();


                            while (!tcpConnection.getMessage().hasId()){

                                if (GlobalVariables.APLICATION_EXIT) {
                                    return false;
                                }

                                waitingForOponnent.setTitleText(WaitingForOponnent.CREATING_GAME);
                                tcpConnection.getMessage().removeID();
                                tcpConnection.sendMessageToServer(TcpMessage.CONNECTION, GlobalVariables.shipDefinition, TcpMessage.IDENTITY);

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }


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
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
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


    /**
     * pro budouci dynamicke rozliseni - zatim nepotrebne
     */
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


    private void waitingForReconnectAction(){
        opponentLostMenu.getTimeExpiredProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue || GlobalVariables.APLICATION_EXIT) {
                return;
            }

            GlobalVariables.reconnection.set(false);
            if (tcpConnection.isConnected() ) {
                tcpConnection.endConnection();
                enemyShip.takeDamage((int) enemyShip.getActualLife() + enemyShip.getArmorActualValue());
                opponentLostMenu.clean();
            } else {
                GlobalVariables.setDefaultValues();
                tcpConnection.getMessage().removeID();
                GlobalVariables.errorMsg = ErrorAlert.NOT_CONNECTED_TO_SERVER;
                createMainPage();
            }
        });
    }
    /**
     * vytvori reconnect zpravu - zatim nepotrebne
     */
    private String createMessageForReconnection(){
        String separator = ";;;";
        StringBuilder reconnection = new StringBuilder();
        reconnection.append(controls.getTime());
        reconnection.append(separator);

        reconnection.append(tcpConnection.getMessage().getId());
        reconnection.append(separator);

        if(GlobalVariables.isPlayingNow.get()) {
            reconnection.append(1);
        }else {
            tcpConnection.sendMessageToServer(TcpMessage.ATTACK, TcpMessage.NONE, TcpMessage.NONE);
            reconnection.append(0);
        }
        reconnection.append(separator);
        reconnection.append(exportImportShip.exportShip(GlobalVariables.choosenShip, false));
        reconnection.append(";;");
        reconnection.append(exportImportShip.exportShip(enemyShip , false));

        return reconnection.toString();
    }

    /**
     * zpracuje reconnect informace - zatim nepotrebne
     * @param reconnectionMessage
     */
    private void handleReconnectionMessage(String reconnectionMessage) {
        String [] information = reconnectionMessage.split(";;;");

        //parsing time of game information
        try {
            controls.setTime(Integer.parseInt(information[0]));
        }catch (Exception e){
            controls.setTime(5);
        }


        //parsing who is playing information
        if (tcpConnection.getMessage().getId().equals(information[1])) {
            if(information[2].equals("1")){
                GlobalVariables.isPlayingNow.set(true);
            }else {
                GlobalVariables.isPlayingNow.set(false);
            }
            exportImportShip.importReconnectionStatus(GlobalVariables.choosenShip, information[3], true);
            exportImportShip.importReconnectionStatus(enemyShip, information[4], true);
        }else {
            if(information[2].equals("0")){
                GlobalVariables.isPlayingNow.set(true);
            }else {
                GlobalVariables.isPlayingNow.set(false);
            }
            exportImportShip.importReconnectionStatus(GlobalVariables.choosenShip, information[4], false);
            exportImportShip.importReconnectionStatus(enemyShip, information[3], false);
        }
        sendDataButton.setDisable(!GlobalVariables.isPlayingNow.get());

        opponentLostMenu.clean();
        controls.resumeAnimations();
    }
}
