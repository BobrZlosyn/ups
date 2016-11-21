package game.StartUpMenu;

import game.static_classes.GlobalVariables;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 17.10.2016.
 */
public class WaitingForOponnent {

    private ProgressIndicator waitingIndicator;
    private Label title;
    private Button cancel;
    private GridPane waitingPane;

    public static final String WAITING_FOR_OPONENT = "ČEKÁNÍ NA PROTIHRÁČE";
    public static final String CONNECTING_TO_SERVER = "PŘIPOJUJI SE K SEVERU";
    public static final String CREATING_GAME = "VYTVÁŘÍM MÍSTNOST S HROU";
    public static final String STARTING_GAME = "PŘESUNUJI SE NA SOUŘADNICE STŘETNUTÍ";

    public WaitingForOponnent(){
        createPane();
    }

    private void createPane(){
        waitingPane = new GridPane();
        waitingPane.setMaxWidth(Double.MAX_VALUE);
        waitingPane.setMaxHeight(Double.MAX_VALUE);
        waitingPane.setStyle("-fx-background-color:black;");

        waitingPane.getColumnConstraints().addAll(generateColumns(3));
        waitingPane.getRowConstraints().addAll(generateRows(5));

        title = new Label(WAITING_FOR_OPONENT);
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setMaxHeight(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        waitingIndicator = new ProgressIndicator(-1);
        cancel = new Button("Zrušit");


        waitingPane.add(cancel, 2, 0);
        waitingPane.add(title, 1, 1);
        waitingPane.add(waitingIndicator, 1, 2);
    }

    public void showWaitingForOponnent(GridPane window) {
        if(GlobalVariables.isEmpty(window)
                || GlobalVariables.isEmpty(waitingPane)){
            return;
        }

        if(window.getChildren().contains(waitingPane)) {
            return;
        }

        window.add(waitingPane, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
    }

    private ArrayList<RowConstraints> generateRows(int rowsCount){
        ArrayList<RowConstraints> rows = new ArrayList<>();

        for (int i = 0; i < rowsCount; i++){

            rows.add(new RowConstraints());
            rows.get(i).setPercentHeight(100 / rowsCount);
        }

        return rows;
    }

    private ArrayList<ColumnConstraints> generateColumns(int columnsCount){
        ArrayList<ColumnConstraints> columns = new ArrayList<>();

        for (int i = 0; i < columnsCount; i++){
            columns.add(new ColumnConstraints());
            columns.get(i).setPercentWidth(100 / columnsCount);
        }

        return columns;
    }

    public Button getCancel() {
        return cancel;
    }

    public void removePane(){
        GridPane parent = (GridPane)waitingPane.getParent();
        if(!GlobalVariables.isEmpty(parent)){
            parent.getChildren().removeAll(waitingPane);
        }
    }

    public void setTitleText(String text){
        Platform.runLater(() -> {
            title.setText(text);
        });
    }
}
