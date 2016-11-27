package game.startUpMenu;

import game.static_classes.GlobalVariables;
import game.static_classes.StyleClasses;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class SettingsMenu extends CommonMenu{
    private TextField ipAdressTF, portTF;
    private Label ipAdressLabel, portLabel, paneTitle;
    private Button confirm, backToMenu;
    private Pane paneOver;

    private final String IP_ADRESS = "Zadejte IP adresu serveru";
    private final String PORT = "Zadejte port serveru";
    private final String BACK_TO_MENU = "Zpět do menu";
    private final String SAVE_CHANGES = "Uložit změny";
    private final String TITLE = "NASTAVENÍ";

    public SettingsMenu(){
        ipAdressLabel = createLabel(IP_ADRESS);
        ipAdressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        portLabel = createLabel(PORT);
        portLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        ipAdressTF = createTextField(GlobalVariables.serverIPAdress.get());
        portTF = createTextField(GlobalVariables.serverPort.get());
        paneTitle = createLabel(TITLE);
        paneTitle.setAlignment(Pos.CENTER);
        paneTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        createButtonBack();
        createButtonConfirm();
        setupWindow();
    }

    private void createButtonConfirm(){
        confirm = createButton(SAVE_CHANGES, StyleClasses.MENU_BUTTONS);
        confirm.setOnAction(event -> {
            if(checkIPAdress()){
                Platform.runLater(() -> GlobalVariables.serverIPAdress.set(ipAdressTF.getText().trim()));

            }else {
                ipAdressTF.setText(GlobalVariables.serverIPAdress.getValue());
            }

            if(checkPort()){
                Platform.runLater(() -> GlobalVariables.serverPort.set(portTF.getText().trim()));

            } else {
                portTF.setText(GlobalVariables.serverPort.getValue());
            }
        });
    }

    private void createButtonBack(){
        backToMenu = createButton(BACK_TO_MENU, StyleClasses.EXIT_BUTTON);
        backToMenu.setOnAction(event -> clean());
    }

    private boolean checkIPAdress(){

        String ipText = ipAdressTF.getText().trim();
        String [] numbers = ipText.split("\\.");
        if ( numbers.length != 4) {
            return false;
        }

        try {
            for (int i = 0; i < numbers.length; i++){
                int number = Integer.parseInt(numbers[i]);
                if (number > 255 || number < 0) {
                    return false;
                }
            }

        }catch (Exception e){
            return false;
        }

        return true;
    }

    private boolean checkPort(){
        try {
            String port = portTF.getText().trim();
            int number = Integer.parseInt(port);
            if (number > 56666 || number < 0) {
                return false;
            }

        }catch (Exception e){
            return false;
        }

        return true;
    }

    private void setupWindow(){

        double [] percentHeight = new double[]{
                20,
                8,
                5,
                10,
                5,
                10,
                14,
                8,
                20
        };
        double [] percentWidth = new double[]{
                45,
                20,
                20,
                15,
        };

        menuPane.getColumnConstraints().addAll(generateColumns(4, percentWidth));
        menuPane.getRowConstraints().addAll(generateRows(9, percentHeight));

        paneOver = new Pane();
        paneOver.setStyle("-fx-background-color: rgba(0, 0, 0, 0.95);");

        menuPane.add(paneOver,      0, 1, GridPane.REMAINING, 7);
        menuPane.add(paneTitle,     0, 2, 1, 5);
        menuPane.add(ipAdressLabel, 1, 2, 2, 1);
        menuPane.add(ipAdressTF,    1, 3, 2, 1);
        menuPane.add(portLabel,     1, 4, 2, 1);
        menuPane.add(portTF,        1, 5, 2, 1);
        menuPane.add(backToMenu,    1, 6, 1, 1);
        menuPane.add(confirm,       2, 6, 1, 1);

        GridPane.setValignment(ipAdressTF, VPos.TOP);
        GridPane.setValignment(portTF, VPos.TOP);
        GridPane.setValignment(paneTitle, VPos.TOP);

        GridPane.setHalignment(confirm, HPos.RIGHT);

        setMargin(confirm,      10, 10, 10, 0);
        setMargin(backToMenu,   10, 10, 10, 0);

        setMargin(ipAdressLabel,0, 60, 0 , 0);
        setMargin(ipAdressTF,   0, 60, 0 , 0);
        setMargin(portLabel,    0, 60, 0 , 0);
        setMargin(portTF,       0, 60, 0 , 0);
    }

}
