package game.StartUpMenu;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class CreateMenu {
    private GridPane menu;
    private Button start, settings, about, exit;
    private Label connection;
    private Circle indicator;
    ChangeListener <Boolean> connectionListener;

    public CreateMenu(){
        menu = createGridpane();
        init();
    }

    private void init(){

        createStartButton();
        createStartAbout();
        createStartExit();
        createStartSettings();
        createConnectionIndicator();
        fillMenuPane();
        marginInMenuPane();
        setConnectionListener();
    }

    private GridPane createGridpane(){
        GridPane menu = new GridPane();
        menu.setMaxWidth(Double.MAX_VALUE);
        menu.setMaxHeight(Double.MAX_VALUE);

        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(25);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(5);

        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints2.setPercentHeight(10);
        rowConstraints2.setMaxHeight(100);

        RowConstraints rowConstraints3 = new RowConstraints();
        rowConstraints3.setPercentHeight(10);
        rowConstraints3.setMaxHeight(100);

        RowConstraints rowConstraints4 = new RowConstraints();
        rowConstraints4.setPercentHeight(10);
        rowConstraints4.setMaxHeight(100);

        RowConstraints rowConstraints5 = new RowConstraints();
        rowConstraints5.setPercentHeight(10);
        rowConstraints5.setMaxHeight(100);

        RowConstraints rowConstraints7 = new RowConstraints();
        rowConstraints7.setPercentHeight(5);

        RowConstraints rowConstraints6 = new RowConstraints();
        rowConstraints6.setPercentHeight(25);
        rowConstraints6.setValignment(VPos.BOTTOM);

        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(37.5);
        columnConstraints1.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(5);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(25);
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setPercentWidth(5);
        ColumnConstraints columnConstraints4 = new ColumnConstraints();
        columnConstraints4.setPercentWidth(37.5);
        columnConstraints4.setHalignment(HPos.RIGHT);

        menu.getColumnConstraints().addAll(columnConstraints1,columnConstraints, columnConstraints2, columnConstraints3, columnConstraints4);
        menu.getRowConstraints().addAll(rowConstraints1, rowConstraints, rowConstraints2, rowConstraints3, rowConstraints4, rowConstraints5, rowConstraints7, rowConstraints6);

        return menu;
    }

    private void createStartButton(){
        start = createButton("NOVÁ HRA");
    }

    private void createStartSettings(){
        settings = createButton("NASTAVENÍ");
    }

    private void createStartAbout(){
        about = createButton("O HŘE");
    }

    private void createStartExit() {
        exit = createButton("ODEJÍT");
        exit.getStyleClass().add("exitButton");
        exit.setOnAction(event -> {
            Platform.exit();
        });
    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.getStyleClass().add("menuButtons");
        return button;
    }
    private void createConnectionIndicator(){
        connection = new Label("Neřipojeno");
        connection.setTextFill(Color.RED);
        connection.setStyle("-fx-background-color: rgba(0,0,0,0.75);");
        connection.setPadding(new Insets( 10, 0, 10, 0));
        connection.setMinWidth(125);
        connection.setAlignment(Pos.CENTER);
        indicator = new Circle(3, Color.RED);

    }


    private void fillMenuPane(){
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        menu.add(pane,1,1,3,6);
        menu.add(start,2,2);
        menu.add(settings,2,3);
        menu.add(about,2,4);
        menu.add(exit,2,5);
        menu.add(connection, 4, 7);
        menu.add(indicator, 4, 7);

    }

    private void marginInMenuPane(){
        menu.setMargin(start, new Insets(5, 0, 5, 0));
        menu.setMargin(settings, new Insets(5, 0, 5, 0));
        menu.setMargin(about, new Insets(5, 0, 5, 0));
        menu.setMargin(exit, new Insets(5, 0, 5, 0));
        menu.setMargin(indicator, new Insets(0, 100, 15, 0));
    }

    public GridPane getMenu() {
        return menu;
    }

    public Button getStart() {
        return start;
    }

    public void clean() {
        menu.setVisible(false);
        ((GridPane) menu.getParent()).getChildren().remove(menu);
    }

    private void setConnectionListener(){
        connectionListener = (observable, oldValue, newValue) -> {
            System.out.println("listener "+ newValue);
            if(newValue.booleanValue()){
                setSuccesfulConnection();
            }else{
                setFailedConnection();
            }
        };
    }

    private void setSuccesfulConnection(){
        connection.setText("Připojeno");
        connection.setTextFill(Color.GREEN);
        indicator.setFill(Color.GREEN);
    }

    private void setFailedConnection(){
        connection.setText("Nepřipojeno");
        connection.setTextFill(Color.RED);
        indicator.setFill(Color.RED);
    }

    public void setConnectionBinding(SimpleBooleanProperty connectionStatus) {
        if(connectionStatus.get()){
            setSuccesfulConnection();
        }

        connectionStatus.addListener(connectionListener);
    }

    public void removeConnectionBinding(SimpleBooleanProperty connectionStatus) {
        connectionStatus.removeListener(connectionListener);
    }

}
