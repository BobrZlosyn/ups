package game.startUpMenu;

import game.static_classes.GlobalVariables;
import game.static_classes.StyleClasses;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Created by Kanto on 07.11.2016.
 */
public class ErrorAlert extends CommonMenu {

    private Label errorLabel;
    public static final String NOT_CONNECTED_TO_SERVER = "Omlouváme se, ale nastala chyba s připojením na server";
    public static final String NO_PLAYER_WAS_FOUND = "Omlouváme se, ale v současné chvíli se nedáří nalézt volného hráče. " +
                                                    "Zkuste to prosím později.";
    public static final String NOT_VALID_SHIP_DEFINITION = "Omlouváme se, nastala chyba při získávání dat ze serveru";
    public static final String ERROR2 = "CHYBA 2";

    public ErrorAlert(){
        createErrorPane();
    }

    private void createErrorPane(){
        menuPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(30);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(40);
        center.setMaxWidth(400);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(30);

        RowConstraints top = new RowConstraints();
        top.setPercentHeight(35);
        RowConstraints error = new RowConstraints();
        error.setPercentHeight(20);
        RowConstraints button = new RowConstraints();
        button.setPercentHeight(10);
        RowConstraints bottom = new RowConstraints();
        bottom.setPercentHeight(35);

        menuPane.getColumnConstraints().addAll(left, center, right);
        menuPane.getRowConstraints().addAll(top, error, button, bottom);

        errorLabel = new Label("empty");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setTextAlignment(TextAlignment.CENTER);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setFont(Font.font(16));
        errorLabel.setWrapText(true);

        Button closeButton = createButton("Zavřít",StyleClasses.EXIT_BUTTON );
        closeButton.setOnAction(event -> clean());

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: rgb(0, 0, 0, 0.75);");

        menuPane.add(pane, 1, 1, 1, 2);
        menuPane.add(errorLabel, 1, 1);
        menuPane.add(closeButton, 1, 2);

        GridPane.setHalignment(closeButton, HPos.CENTER);
        setMargin(errorLabel, 10, 20, 10, 20);
        setMargin(closeButton, 10, 20, 10, 20);
    }


    public void showErrorPaneWithText(GridPane window, String text) {
        GlobalVariables.errorMsg = text;
        showErrorPane(window);
    }

    public void showErrorPane(GridPane window) {
        if (GlobalVariables.errorMsg.isEmpty()){
            return;
        }

        if(!GlobalVariables.isEmpty(window) && !window.getChildren().contains(menuPane)){
            errorLabel.setText(GlobalVariables.errorMsg);
            window.add(menuPane,0,0, GridPane.REMAINING, GridPane.REMAINING);
        }

        GlobalVariables.errorMsg = "";
    }
}
