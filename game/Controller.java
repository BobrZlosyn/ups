package game;

import client.TcpClient;
import client.TcpMessage;
import game.StartUpMenu.CreateMenu;
import game.StartUpMenu.PickShipMenu;
import game.background.GeneratRandomBackground;
import game.ships.BattleShip;
import game.ships.CruiserShip;
import game.weapons.CannonWeapon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

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
    }

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

    private void setupStartButton(PickShipMenu pickShipMenu){
        pickShipMenu.getNextSetup().setOnAction(event -> {
            pickShipMenu.clean();

            gameAreaPane = new Pane();
            window.add(gameAreaPane, 0, 0, GridPane.REMAINING, 1);
            BattleShip testShip = new BattleShip(false);
            CruiserShip testShip2 = new CruiserShip(true);
            testShip.displayShip(gameAreaPane);
            testShip2.displayShip(gameAreaPane);
            CannonWeapon testWeapon = new CannonWeapon();
            testWeapon.displayWeapon(testShip2.getPosition(2, 2), testShip2.isEnemy());
            CannonWeapon testWeapon2 = new CannonWeapon();
            testWeapon2.displayWeapon(testShip.getPosition(2, 2), testShip.isEnemy());

            //pozadi
            grb = new GeneratRandomBackground();
            grb.findImages();
            grb.chooseImage((GridPane) gameAreaPane.getParent());

            //horni prvky
            Controls controls = new Controls();
            controls.showStatusBars(gameAreaPane);

            //dolni prvky
            sendDataButton = new Button();
            BottomPanel bottomPanel = new BottomPanel(sendDataButton);
            bottomPanel.showPanel(window);
        });
    }

    private void setupPickShipMenu(CreateMenu createMenu){
        createMenu.getStart().setOnAction(event -> {
            createMenu.clean();
            PickShipMenu pickShipMenu = new PickShipMenu();
            window.add(pickShipMenu.getPickship(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
            setupStartButton(pickShipMenu);
        });
    }
}
