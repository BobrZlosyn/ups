package game.StartUpMenu;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class CreateMenu {
    private GridPane menu;
    private Button start, settings, about, exit;

    public CreateMenu(){
        menu = createGridpane();
        init();
    }

    private void init(){
        createStartButton();
        createStartAbout();
        createStartExit();
        createStartSettings();
        fillMenuPane();
        marginInMenuPane();
    }

    private GridPane createGridpane(){
        GridPane menu = new GridPane();
        menu.setMaxWidth(Double.MAX_VALUE);
        menu.setMaxHeight(Double.MAX_VALUE);

        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(30);

        RowConstraints rowConstraints2 = new RowConstraints(50);
        rowConstraints2.setPercentHeight(10);
        rowConstraints2.setMaxHeight(100);

        RowConstraints rowConstraints3 = new RowConstraints(50);
        rowConstraints3.setPercentHeight(10);
        rowConstraints3.setMaxHeight(100);

        RowConstraints rowConstraints4 = new RowConstraints(50);
        rowConstraints4.setPercentHeight(10);
        rowConstraints4.setMaxHeight(100);

        RowConstraints rowConstraints5 = new RowConstraints(50);
        rowConstraints5.setPercentHeight(10);
        rowConstraints5.setMaxHeight(100);

        RowConstraints rowConstraints6 = new RowConstraints();
        rowConstraints6.setPercentHeight(30);

        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(30);
        columnConstraints1.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(35);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(35);

        menu.getColumnConstraints().addAll(columnConstraints, columnConstraints1, columnConstraints2);
        menu.getRowConstraints().addAll(rowConstraints1,rowConstraints2, rowConstraints3, rowConstraints4, rowConstraints5, rowConstraints6);

        return menu;
    }

    private void createStartButton(){
        start = new Button("HRÁT");
        start.setMaxWidth(Double.MAX_VALUE);
        start.setMaxHeight(Double.MAX_VALUE);
    }

    private void createStartSettings(){
        settings = new Button("NASTAVENÍ");
        settings.setMaxWidth(Double.MAX_VALUE);
        settings.setMaxHeight(Double.MAX_VALUE);
    }

    private void createStartAbout(){
        about = new Button("O HŘE");
        about.setMaxWidth(Double.MAX_VALUE);
        about.setMaxHeight(Double.MAX_VALUE);
    }

    private void createStartExit() {
        exit = new Button("ODEJÍT");
        exit.setMaxWidth(Double.MAX_VALUE);
        exit.setMaxHeight(Double.MAX_VALUE);
        exit.setOnAction(event -> {
            Platform.exit();
        });
    }


    private void fillMenuPane(){
        menu.add(start,1,1);
        menu.add(settings,1,2);
        menu.add(about,1,3);
        menu.add(exit,1,4);
    }

    private void marginInMenuPane(){
        menu.setMargin(start, new Insets(5, 0, 5, 0));
        menu.setMargin(settings, new Insets(5, 0, 5, 0));
        menu.setMargin(about, new Insets(5, 0, 5, 0));
        menu.setMargin(exit, new Insets(5, 0, 5, 0));
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

}
