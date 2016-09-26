package game;

import client.TcpClient;
import client.TcpMessage;
import game.ships.BattleShip;
import game.ships.CruiserShip;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    Label connectionStatusLabel;
    @FXML
    Button connectButton;
    @FXML
    Pane gameAreaPane;


    public void setConnectionStatusLabel(String text) {
        connectionStatusLabel.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setConnectionStatusLabel("Nepřipojeno");
        connectButton.setOnAction(event -> {
            TcpClient client = new TcpClient( "localhost", 1234 );
            TcpMessage msgSend = new TcpMessage("connect");
            client.putMessage( msgSend );
            TcpMessage msgRecv = client.getMessage();
            String msg = msgRecv.getMessage();
            System.out.println(msg);


            if(msg.trim().equals("success")){
                setConnectionStatusLabel("Připojeno");
            }
            client.close();


            BattleShip testShip = new BattleShip();
            CruiserShip testShip2 = new CruiserShip();
            testShip.displayShip(false, gameAreaPane);
            testShip2.displayShip(true, gameAreaPane);
        });

    }
}
