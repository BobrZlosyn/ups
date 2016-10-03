package game;

import game.StartUpMenu.CreateMenu;
import game.StartUpMenu.GunsToShipMenu;
import game.StartUpMenu.PickShipMenu;
import game.background.GeneratRandomBackground;
import game.construction.Placement;
import game.ships.CommonShip;
import game.ships.CruiserShip;
import game.weapons.CannonWeapon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

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
    GeneratRandomBackground grb;
    public void setConnectionStatusLabel(String text) {
//        connectionStatusLabel.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateMenu createMenu = new CreateMenu();
        window.add(createMenu.getMenu(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        setupPickShipMenu(createMenu);
        windowResize();
        grb = new GeneratRandomBackground();
        grb.showSpacePort(window);
    }

    private void windowResize(){
        window.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(!GlobalVariables.isEmpty(grb)){
                System.out.println("ahoj");
                grb.resizeImage(window, newValue.doubleValue(), window.getHeight());
            }
        });

        window.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (!GlobalVariables.isEmpty(grb)) {
                grb.resizeImage(window, window.getWidth(), newValue.doubleValue());
            }
        });
    }

    private void setupGunsToShipMenu(PickShipMenu pickShipMenu){

        pickShipMenu.getNextSetup().setOnAction(event -> {
            CommonShip ship = pickShipMenu.getChoosenShip();
            pickShipMenu.clean();

            GunsToShipMenu gunsToShipMenu = new GunsToShipMenu(ship);
            window.add(gunsToShipMenu.getGunsToShipPane(),0,0, GridPane.REMAINING, GridPane.REMAINING);
            setupStartButton(gunsToShipMenu);
        });
    }

    private void setupStartButton(GunsToShipMenu gunsToShipMenu){
        gunsToShipMenu.getNextButton().setOnAction(event -> {
            CommonShip commonShip = gunsToShipMenu.getShip();

            gunsToShipMenu.clean();
            gameAreaPane = new Pane();
            window.add(gameAreaPane, 0, 0, GridPane.REMAINING, 1);

            Placement[][] placements = commonShip.getPlacementPositions();
            commonShip.displayShip(gameAreaPane);
            commonShip.fillShipWithEquipment(commonShip, placements);

            ExportImportShip exportImportShip = new ExportImportShip();
            String exportMsg = exportImportShip.exportShip(commonShip);
            CommonShip enemyShip = exportImportShip.importShip(exportMsg, gameAreaPane);

            //pozadi
            grb.findImages();
            grb.chooseImage((GridPane) gameAreaPane.getParent());

            //horni prvky
            Controls controls = new Controls(commonShip, enemyShip);
            controls.showStatusBars(gameAreaPane);

            //dolni prvky
            sendDataButton = new Button();
            BottomPanel bottomPanel = new BottomPanel(sendDataButton);
            bottomPanel.showPanel(window);

            DamageHandler damageHandler = new DamageHandler(commonShip, enemyShip, gameAreaPane);
        });
    }

    private void setupPickShipMenu(CreateMenu createMenu){
        createMenu.getStart().setOnAction(event -> {
            createMenu.clean();
            PickShipMenu pickShipMenu = new PickShipMenu();
            window.add(pickShipMenu.getPickship(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);

            setupGunsToShipMenu(pickShipMenu);
        });
    }
}
