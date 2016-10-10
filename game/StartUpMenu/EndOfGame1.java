package game.StartUpMenu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 10.10.2016.
 */
public class EndOfGame1 {

    private Label result, text;
    private Button newGame, backToMenu;
    private Pane pane;
    private GridPane endWindow;

    public EndOfGame1(boolean isUserWinner){
        createLabel(isUserWinner);
        createBackToMenuButton();
        createNewGameButton();
        createText(isUserWinner);
    }

    private void createLabel(boolean isUserWinner){

        if (isUserWinner) {
            result = new Label("Vítězství!");
            result.setTextFill(Color.GREEN);
        }else {
            result = new Label("Porážka!");
            result.setTextFill(Color.RED);
        }

        result.setMaxWidth(Double.MAX_VALUE);
        result.setAlignment(Pos.CENTER);
        result.setFont(Font.font(45));
    }


    private void createText(boolean isUserWinner){
        if(isUserWinner){
            text = new Label(getRandomTextToWinner());
            text.setTextFill(Color.GREEN);
        }else{
            text = new Label("Jste ostudou naší flotily.");
            text.setTextFill(Color.RED);
        }

        text.setMaxWidth(Double.MAX_VALUE);
        text.setAlignment(Pos.CENTER);
        text.setFont(Font.font(18));
        text.setWrapText(true);
    }

    private void createNewGameButton(){
        newGame = new Button("Další bitva");
        newGame.setMaxWidth(Double.MAX_VALUE);
        newGame.setMaxHeight(100);
    }

    private void createBackToMenuButton(){
        backToMenu = new Button("Zpět do menu");
        backToMenu.setMaxWidth(Double.MAX_VALUE);
        backToMenu.setMaxHeight(100);
    }

    public void setupWindow(GridPane window){
        endWindow = new GridPane();
        endWindow.getColumnConstraints().addAll(generateColumns(4));
        endWindow.getRowConstraints().addAll(generateRows(5));
        endWindow.setMaxWidth(Double.MAX_VALUE);
        endWindow.setMaxHeight(Double.MAX_VALUE);


        pane = new Pane();
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9);");

        endWindow.add(pane, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        endWindow.add(result, 1,1,2,1);
        endWindow.add(backToMenu,1,3);
        endWindow.add(newGame,2,3);
        endWindow.add(text,1,2,2,1);

        endWindow.setMargin(newGame, new Insets(15, 15, 15, 15));
        endWindow.setMargin(backToMenu, new Insets(15, 15, 15, 15));

        window.add(endWindow, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
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


    private String getRandomTextToWinner(){
        ArrayList <String> texts = new ArrayList();
        texts.add("Blahopřejeme kapitáne, zničil jste nepřátele s ledovým klidem.");
        texts.add("Rebelové byli rozprášeni do všech koutů vesmíru jen díky vám.");

        return texts.get(1);

    }


    private String getRandomTextToLoser(){
        ArrayList <String> texts = new ArrayList();
        texts.add("Jste ostudou naší flotily.");
        return texts.get(0);
    }

    public void clean(){
        ((GridPane)endWindow.getParent()).getChildren().remove(endWindow);
    }

    public Button getBackToMenu() {
        return backToMenu;
    }

    public Button getNewGame() {
        return newGame;
    }


}
