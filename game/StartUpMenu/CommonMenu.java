package game.StartUpMenu;

import game.static_classes.GlobalVariables;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 27.11.2016.
 */
public class CommonMenu {
    protected GridPane menuPane;

    public CommonMenu(){
        menuPane = new GridPane();
        menuPane.setMaxWidth(Double.MAX_VALUE);
        menuPane.setMaxHeight(Double.MAX_VALUE);
    }


    public void showWindow(GridPane window){
        window.add(menuPane, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
    }

    protected Label createLabel(String text) {
        Label label = new Label(text);
        label.setMaxSize(Double.MAX_VALUE, 30);
        label.setTextFill(Color.WHITE);
        return label;
    }

    protected Button createButton(String text, String styleClass){
        Button button = new Button(text);
        button.setMaxSize(200, 50);
        button.getStyleClass().add(styleClass);
        return button;
    }

    protected TextField createTextField(String text) {
        TextField textField = new TextField(text);
        textField.setMaxSize(Double.MAX_VALUE, 30);
        return textField;
    }

    protected void setMargin(Node node, int top, int right, int bottom, int left) {
        GridPane.setMargin(node, new Insets(top, right, bottom, left));
    }

    protected ArrayList<RowConstraints> generateRows(int rowsCount, double [] percentHeight){
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

    protected ArrayList<ColumnConstraints> generateColumns(int columnsCount, double [] percentWidth) {
        ArrayList<ColumnConstraints> columns = new ArrayList<>();

        for (int i = 0; i < columnsCount; i++) {
            columns.add(new ColumnConstraints());
            if (GlobalVariables.isEmpty(percentWidth) || percentWidth.length != columnsCount) {
                columns.get(i).setPercentWidth(100.0 / columnsCount);
            } else {
                columns.get(i).setPercentWidth(percentWidth[i]);
            }
        }
        return columns;
    }

    public void clean() {
        menuPane.getChildren().clear();
        ((GridPane) menuPane.getParent()).getChildren().remove(menuPane);
    }
}
