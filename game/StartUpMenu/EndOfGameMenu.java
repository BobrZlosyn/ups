package game.StartUpMenu;

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
    private final String LOST = "Porážka!";
    private final String NEXT_GAME = "Další bitva";
    private final String BACK_TO_MENU = "Zpět do menu";

    public EndOfGameMenu(boolean isUserWinner){
        createLabel(isUserWinner);
        createBackToMenuButton();
        createNewGameButton();
        createText(isUserWinner);
    }

    private void createLabel(boolean isUserWinner){

        if (isUserWinner) {
            result = new Label(VICTORY);
            result.setTextFill(Color.GREEN);
        }else {
            result = new Label(LOST);
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
            text = new Label(getRandomTextToLoser());
            text.setTextFill(Color.RED);
        }

        text.setMaxWidth(Double.MAX_VALUE);
        text.setAlignment(Pos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(18));
        text.setWrapText(true);
    }

    private void createNewGameButton(){
        newGame = createButton(NEXT_GAME,"menuButtons");
    }

    private void createBackToMenuButton(){
        backToMenu = createButton(BACK_TO_MENU, "exitButton");
    }

    public void setupWindow(GridPane window){
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

        showWindow(window);
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
