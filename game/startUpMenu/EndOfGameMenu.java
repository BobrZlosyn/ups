package game.startUpMenu;

import game.static_classes.StyleClasses;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 10.10.2016.
 */
public class EndOfGameMenu extends CommonMenu{

    private Label result, text;
    private Button newGame, backToMenu;
    private Pane pane;

    private final String VICTORY = "Vítězství!";
    private final String EVEN = "Remíza!";
    private final String LOST = "Porážka!";
    private final String NEXT_GAME = "Další bitva";
    private final String BACK_TO_MENU = "Zpět do menu";

    public EndOfGameMenu(boolean isUserWinner){
        createLabel();
        createBackToMenuButton();
        createNewGameButton();
        createText();
        setUserIsWinner(isUserWinner, false);
        setupWindow();
    }

    public EndOfGameMenu(boolean isUserWinner, boolean isEven){
        createLabel();
        createBackToMenuButton();
        createNewGameButton();
        createText();
        setUserIsWinner(isUserWinner, isEven);
        setupWindow();
    }

    public void setUserIsWinner(boolean isWinner, boolean isEven) {
        if (isEven) {
            evenSetting();
            return;
        }

        if (isWinner) {
            winnerSetting();
        }else {
            lostSetting();
        }
    }

    private void winnerSetting() {
        result.setText(VICTORY);
        result.setTextFill(Color.GREEN);
        text.setText(getRandomTextToWinner());
        text.setTextFill(Color.GREEN);
    }

    private void lostSetting() {
        result.setText(LOST);
        result.setTextFill(Color.RED);
        text.setText(getRandomTextToLoser());
        text.setTextFill(Color.RED);
    }

    private void evenSetting() {
        result.setText(EVEN);
        result.setTextFill(Color.YELLOW);
        text.setText(getRandomTextToLoser());
        text.setTextFill(Color.YELLOW);
    }

    private void createLabel(){

        result = new Label();
        result.setMaxWidth(Double.MAX_VALUE);
        result.setAlignment(Pos.CENTER);
        result.setFont(Font.font(45));
    }


    private void createText(){

        text = new Label();
        text.setMaxWidth(Double.MAX_VALUE);
        text.setAlignment(Pos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(18));
        text.setWrapText(true);
    }

    private void createNewGameButton(){
        newGame = createButton(NEXT_GAME, StyleClasses.MENU_BUTTONS);
    }

    private void createBackToMenuButton(){
        backToMenu = createButton(BACK_TO_MENU, StyleClasses.EXIT_BUTTON);
    }

    public void setupWindow(){
        menuPane.getColumnConstraints().addAll(generateColumns(4, null));
        menuPane.getRowConstraints().addAll(generateRows(5, null));

        pane = new Pane();
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9);");

        menuPane.add(pane, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        menuPane.add(result, 1,1,2,1);
        menuPane.add(backToMenu,1,3);
        menuPane.add(newGame,2,3);
        menuPane.add(text,1,2,2,1);

        setMargin(newGame, 15, 15, 15, 15);
        setMargin(backToMenu, 15, 15, 15, 15);
        GridPane.setHalignment(backToMenu, HPos.RIGHT);

    }


    private String getRandomTextToWinner(){
        ArrayList <String> texts = new ArrayList();
        texts.add("Blahopřejeme kapitáne, zničil jste nepřátele s ledovým klidem.");
        texts.add("Rebelové byli rozprášeni do všech koutů vesmíru jen díky vám.");
        texts.add("Krasný večerní program zakončený ohňostrojem, přesně jak má býti.");

        int random = (int) (Math.random() * texts.size());
        return texts.get(random);

    }


    private String getRandomTextToLoser(){
        ArrayList <String> texts = new ArrayList();
        texts.add("Jste ostudou naší flotily.");
        texts.add("Snad budete lepším velitelem v příštím životě.");
        texts.add("Pokuste se moc nedýchat, pomoc je již na cestě.");
        texts.add("Když jste padli vy, další na řadě je země.");

        int random = (int) (Math.random() * texts.size());
        return texts.get(random);
    }

    public Button getBackToMenu() {
        return backToMenu;
    }

    public Button getNewGame() {
        return newGame;
    }

}
