package game.StartUpMenu;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by BobrZlosyn on 27.11.2016.
 */
public class AboutGameMenu extends CommonMenu {

    private Label paneTitle;
    private Button backToMenu;
    private Pane paneOver;
    private Text text;

    private final String TITLE = "O HŘE";
    private final String BACK_TO_MENU = "Zpět do menu";

    public AboutGameMenu(){
        paneTitle = createLabel(TITLE);
        paneTitle.setAlignment(Pos.CENTER);
        paneTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        createButtonBack();
        setupWindow();
    }

    private void createButtonBack(){
        backToMenu = createButton(BACK_TO_MENU, "exitButton");
        backToMenu.setOnAction(event -> clean());
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
        menuPane.add(backToMenu,    1, 6, 1, 1);

        GridPane.setValignment(paneTitle, VPos.TOP);


        setMargin(backToMenu,   10, 10, 10, 10);
    }

}
