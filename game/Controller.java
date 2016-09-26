package game;

import client.TcpClient;
import client.TcpMessage;
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

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    Label connectionStatusLabel;
    @FXML
    Button connectButton;
    @FXML
    Pane gameAreaPane;
    @FXML
    GridPane window;

    private Button sendDataButton;
    public void setConnectionStatusLabel(String text) {
//        connectionStatusLabel.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setConnectionStatusLabel("Nepřipojeno");
        sendDataButton = new Button();
        sendDataButton.setOnAction(event -> {
            BattleShip testShip = new BattleShip();
            CruiserShip testShip2 = new CruiserShip();
            testShip.displayShip(false, gameAreaPane);
            testShip2.displayShip(true, gameAreaPane);
            CannonWeapon testWeapon = new CannonWeapon();
            testWeapon.displayWeapon(testShip2.getPosition(2,2), true);
            GeneratRandomBackground grb = new GeneratRandomBackground();
            grb.findImages();
            grb.chooseImage((GridPane)gameAreaPane.getParent());
        });
        Controls controls = new Controls();
        controls.showStatusBars(gameAreaPane);
        BottomPanel bottomPanel = new BottomPanel(sendDataButton);
        bottomPanel.showPanel(window);
    }
}
