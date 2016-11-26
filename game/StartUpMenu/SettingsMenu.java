package game.StartUpMenu;

import game.static_classes.GlobalVariables;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class SettingsMenu {
    private TextField ipAdressTF, portTF;
    private Label ipAdressLabel, portLabel;
    private Button confirm, backToMenu;
    private Pane paneOver;
    private GridPane settingsPane;

    private final String IP_ADRESS = "Zadejte IP adresu serveru";
    private final String PORT = "Zadejte port serveru";
    private final String BACK_TO_MENU = "Zpět do menu";
    private final String SAVE_CHANGES = "Uložit změny";

    public SettingsMenu(){
        ipAdressLabel = createLabel(IP_ADRESS);
        portLabel = createLabel(PORT);
        ipAdressTF = createTextField(GlobalVariables.serverIPAdress.get());
        portTF = createTextField(GlobalVariables.serverPort.get());
        createButtonBack();
        createButtonConfirm();
        setupWindow();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setMaxSize(Double.MAX_VALUE, 30);
        label.setTextFill(Color.WHITE);
        return label;
    }

    private TextField createTextField(String text) {
        TextField textField = new TextField(text);
        textField.setMaxSize(Double.MAX_VALUE, 30);
        return textField;
    }


    private void createButtonConfirm(){
        confirm = createButton(SAVE_CHANGES, "menuButtons");
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
        backToMenu = createButton(BACK_TO_MENU, "exitButton");
        backToMenu.setOnAction(event -> clean());
    }

    private Button createButton(String text, String styleClass){
        Button button = new Button(text);
        button.setMaxSize(200, 50);
        button.getStyleClass().add(styleClass);
        return button;
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
                28,
                5,
                10,
                5,
                10,
                14,
                28
        };

        settingsPane = new GridPane();
        settingsPane.getColumnConstraints().addAll(generateColumns(4));
        settingsPane.getRowConstraints().addAll(generateRows(7, percentHeight));
        settingsPane.setMaxWidth(Double.MAX_VALUE);
        settingsPane.setMaxHeight(Double.MAX_VALUE);

        paneOver = new Pane();
        paneOver.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");

        settingsPane.add(paneOver, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        settingsPane.add(ipAdressLabel, 1,1,2,1);
        settingsPane.add(ipAdressTF, 1,2,2,1);
        settingsPane.add(portLabel, 1,3,2,1);
        settingsPane.add(portTF, 1,4,2,1);
        settingsPane.add(backToMenu, 1,5,1,1);
        settingsPane.add(confirm, 2,5,1,1);

        GridPane.setValignment(ipAdressTF, VPos.TOP);
        GridPane.setValignment(portTF, VPos.TOP);

        GridPane.setHalignment(confirm, HPos.RIGHT);

        setMargin(confirm, 10, 10, 10 ,10);
        setMargin(backToMenu, 10, 10, 10 ,10);

        setMargin(ipAdressLabel,0, 30, 0 ,30);
        setMargin(ipAdressTF,   0, 30, 0 ,30);
        setMargin(portLabel,    0, 30, 0 ,30);
        setMargin(portTF,       0, 30, 0 ,30);
    }

    private void setMargin(Node node, int top, int right, int bottom, int left) {
        GridPane.setMargin(node, new Insets(top, right, bottom, left));
    }

    public void showWindow(GridPane window){
        window.add(settingsPane, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
    }

    private ArrayList<RowConstraints> generateRows(int rowsCount, double [] percentHeight){
        ArrayList<RowConstraints> rows = new ArrayList<>();

        for (int i = 0; i < rowsCount; i++){

            rows.add(new RowConstraints());
            if(GlobalVariables.isEmpty(percentHeight) || percentHeight.length != rowsCount){
                rows.get(i).setPercentHeight(100.0 / rowsCount);
            }else {
                rows.get(i).setPercentHeight(percentHeight[i]);
            }
        }

        return rows;
    }

    private ArrayList<ColumnConstraints> generateColumns(int columnsCount){
        ArrayList<ColumnConstraints> columns = new ArrayList<>();

        for (int i = 0; i < columnsCount; i++){
            columns.add(new ColumnConstraints());
            columns.get(i).setPercentWidth(100.0 / columnsCount);
        }

        return columns;
    }



    public void clean() {
        settingsPane.getChildren().clear();
        ((GridPane) settingsPane.getParent()).getChildren().remove(settingsPane);
    }

}
