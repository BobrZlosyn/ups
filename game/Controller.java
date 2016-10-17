package game;

import game.StartUpMenu.CreateMenu;
import game.StartUpMenu.EndOfGameMenu;
import game.StartUpMenu.GunsToShipMenu;
import game.StartUpMenu.PickShipMenu;
import game.background.GeneratRandomBackground;
import game.construction.Placement;
import game.ships.CommonShip;
import game.static_classes.GlobalVariables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateMenu createMenu = new CreateMenu();
        window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        setupPickShipMenu(createMenu);
        windowResize();
        grb = new GeneratRandomBackground();
        grb.showSpacePort(window);
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

            CreateMenu createMenu = new CreateMenu();
            window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
            setupPickShipMenu(createMenu);
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

            gunsToShipMenu.clean();
            startGame(true);
        });
    }

    private void startGame(boolean isFirstCreated){

        gameAreaPane = new Pane();
        window.add(gameAreaPane, 0, 0, GridPane.REMAINING, 1);

        Placement[][] placements = GlobalVariables.choosenShip.getPlacementPositions();
        GlobalVariables.choosenShip.displayShip(gameAreaPane);
        GlobalVariables.choosenShip.fillShipWithEquipment(GlobalVariables.choosenShip, placements, isFirstCreated);
        GlobalVariables.choosenShip.createShield();

        //vytvari nepratelskou lod
        ExportImportShip exportImportShip = new ExportImportShip();
        String exportMsg = exportImportShip.exportShip(GlobalVariables.choosenShip);
        CommonShip enemyShip = exportImportShip.importShip(exportMsg, gameAreaPane);
        enemyShip.createShield();
        endWindowShowUp(GlobalVariables.choosenShip, enemyShip);

        //pozadi
        grb.findImages();
        grb.chooseImage((GridPane) gameAreaPane.getParent());

        //horni prvky
        sendDataButton = new Button();
        controls = new Controls(GlobalVariables.choosenShip, enemyShip, sendDataButton);
        controls.showStatusBars(gameAreaPane);

        //dolni prvky
        BottomPanel bottomPanel = new BottomPanel(sendDataButton);
        bottomPanel.showPanel(window, gameAreaPane);
        bottomPanel.getQuit().setOnAction(event1 -> {
            GlobalVariables.choosenShip.takeDamage((int)GlobalVariables.choosenShip.getActualLife());
            GlobalVariables.choosenShip.damageToShield(GlobalVariables.choosenShip.getShieldActualLife());
        });

        DamageHandler damageHandler = new DamageHandler(GlobalVariables.choosenShip, enemyShip, gameAreaPane);
        sendDataButton.setOnAction(event1 -> {
            String actions = damageHandler.exportShooting(GlobalVariables.choosenShip.getPlacementPositions());
            damageHandler.doDamage(actions);
            controls.resetAnimation();
        });
    }


    /**
     * zobrazi obrazovku pro vyber lodi
     * @param createMenu
     */
    private void setupPickShipMenu(CreateMenu createMenu){
        createMenu.getStart().setOnAction(event -> {
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
                window.getChildren().clear();
                CreateMenu createMenu = new CreateMenu();
                window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
                setupPickShipMenu(createMenu);
                grb.showSpacePort(window);
                usersShip.getActualLifeBinding().addListener(userLost);

                enemyShip.getActualLifeBinding().addListener(userWin);
            });

            endOfGame.getNewGame().setOnAction(event -> {
                window.getChildren().clear();
                GlobalVariables.choosenShip.restartValues();
                GlobalVariables.choosenShip.unmarkObject();
                startGame(false);
            });
        }));
        delay.setCycleCount(1);
        delay.playFromStart();
    }
}
