package game.startUpMenu;

import game.static_classes.StyleClasses;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Created by BobrZlosyn on 27.11.2016.
 */
public class AboutGameMenu extends CommonMenu {

    private Label paneTitle;
    private Button backToMenu;
    private Pane paneOver;
    private Label text;

    private final String TITLE = "O HŘE";
    private final String BACK_TO_MENU = "Zpět do menu";

    public AboutGameMenu(){
        paneTitle = createLabel(TITLE);
        paneTitle.setAlignment(Pos.CENTER);
        paneTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        createButtonBack();
        createText();
        setupWindow();
    }

    private void createButtonBack(){
        backToMenu = createButton(BACK_TO_MENU, StyleClasses.EXIT_BUTTON);
        backToMenu.setOnAction(event -> {
            clean();
            CommonMenu.clickSound();
        });
    }

    private void createText(){
        String about = "Hra Space battles byla vytvořena v roce 2016 pro předmět UPS na Západočeské univerzitě v Plzni. " +
                "Hra je určena pro dva hráče, kteří si sami vyberou a vybaví svoji loď a poté se snaží" +
                " zničit nepřátelskou loď v souboji. Každá loď i vybavení mají jiné vlastnosti," +
                " a proto je dobré si promyslet čím loď vybavit. Vytvoření hry sloužilo k seznámení fungování síťového " +
                "připojení klienta a serveru pomocí TCP. \n \n" +
                "Vytvořil : Martin Kantořík ";

        text = new Label(about);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        text.setAlignment(Pos.TOP_LEFT);
        text.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        text.setTextFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text.setWrapText(true);


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
        menuPane.add(text,          1, 2, 2, 4);
        menuPane.add(backToMenu,    1, 6, 1, 1);

        GridPane.setValignment(paneTitle, VPos.TOP);
        setMargin(backToMenu,   10, 10, 10, 0);
    }

}
