package game.startUpMenu;

import game.exportImportDataHandlers.LoadSettings;
import game.exportImportDataHandlers.WriteSettings;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class SettingsMenu extends CommonMenu{
    private TextField ipAdressTF, portTF;
    private Label ipAdressLabel, portLabel, paneTitle, errror;
    private Button confirm, backToMenu;
    private Pane paneOver;

    private final String IP_ADRESS = "Zadejte IP adresu serveru";
    private final String PORT = "Zadejte port serveru";
    private final String BACK_TO_MENU = "Zpět do menu";
    private final String SAVE_CHANGES = "Uložit změny";
    private final String TITLE = "NASTAVENÍ";
    private final String ERROR_TRUE = "Hodnoty byly vyplněny špatně!";
    private final String ERROR_FALSE = "Změny úšpěně provedeny!";

    public SettingsMenu(){
        ipAdressLabel = createLabel(IP_ADRESS);
        ipAdressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        portLabel = createLabel(PORT);
        portLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        errror = createLabel("");
        errror.setFont(Font.font("Arial", FontWeight.BOLD, 13));

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
            CommonMenu.clickSound();
            boolean error = false;
            if(LoadSettings.checkIPAdress(ipAdressTF.getText())){
                GlobalVariables.serverIPAdress.set(ipAdressTF.getText().trim());
                ipAdressTF.setStyle("-fx-border-color: rgba(255 , 0, 0, 0); -fx-text-fill: black;");
            }else {
                ipAdressTF.setStyle("-fx-border-color: rgba(255 , 0, 0, 1); -fx-text-fill: red;");
                error = true;
            }

            if(LoadSettings.checkPort(portTF.getText())){
                GlobalVariables.serverPort.set(portTF.getText().trim());
                portTF.setStyle("-fx-border-color: rgba(255 , 0, 0, 0); -fx-text-fill: black;");
            } else {
                portTF.setStyle("-fx-border-color: rgba(255 , 0, 0, 1); -fx-text-fill: red;");
                error = true;
            }

            if (error) {
                this.errror.setText(ERROR_TRUE);
                this.errror.setTextFill(Color.RED);
            }else {
                this.errror.setText(ERROR_FALSE);
                this.errror.setTextFill(Color.GREEN);
                WriteSettings.writeSettings();
            }
        });
    }

    private void createButtonBack(){
        backToMenu = createButton(BACK_TO_MENU, StyleClasses.EXIT_BUTTON);
        backToMenu.setOnAction(event -> {
            clean();
            CommonMenu.clickSound();
        });
    }


    private void setupWindow(){

        double [] percentHeight = new double[]{
                9,
                15,
                11,
                15,
                11,
                11,
                20,
                9,
        };
        double [] percentWidth = new double[]{
                45,
                20,
                20,
                15,
        };

        menuPane.getColumnConstraints().addAll(generateColumns(percentWidth.length, percentWidth));
        menuPane.getRowConstraints().addAll(generateRows(percentHeight.length, percentHeight));
        menuPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.95);");

        menuPane.add(paneTitle,     0, 1, 1, 5);
        menuPane.add(ipAdressLabel, 1, 1, 2, 1);
        menuPane.add(ipAdressTF,    1, 2, 2, 1);
        menuPane.add(portLabel,     1, 3, 2, 1);
        menuPane.add(portTF,        1, 4, 2, 1);
        menuPane.add(errror,        1, 5, 2, 1);
        menuPane.add(backToMenu,    1, 6, 1, 1);
        menuPane.add(confirm,       2, 6, 1, 1);

        GridPane.setValignment(ipAdressTF, VPos.TOP);
        GridPane.setValignment(portTF, VPos.TOP);
        GridPane.setValignment(paneTitle, VPos.TOP);
        GridPane.setValignment(errror, VPos.CENTER);

        GridPane.setHalignment(confirm, HPos.RIGHT);

        setMargin(confirm,      5, 0, 5, 10);
        setMargin(backToMenu,   5, 10, 5, 0);
        setMargin(ipAdressLabel,0, 60, 0 , 0);
        setMargin(ipAdressTF,   0, 60, 0 , 0);
        setMargin(portLabel,    0, 60, 0 , 0);
        setMargin(portTF,       0, 60, 0 , 0);
    }

    @Override
    public void showWindow(GridPane window) {
        if(GlobalVariables.isNotEmpty(window) && !window.getChildren().contains(menuPane)) {

            window.add(menuPane, 0, 1, GridPane.REMAINING, 6 );

        }
    }
}
