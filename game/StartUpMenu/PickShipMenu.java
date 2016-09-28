package game.StartUpMenu;

import game.GlobalVariables;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;


/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class PickShipMenu {
    private GridPane pickship;
    private Pane showArea;
    private Button battleShip, cruiserShip, nextSetup;
    private Label title, nameOfShip;

    public PickShipMenu(){
        pickship = createPickingPane();
        showArea = new Pane();
        init();
    }

    private void init(){
        createBattleShipButton();
        createCruiserShipButton();
        createNextSetupButton();
        title = new Label("Vyberte váš typ lodi");
        title.getStyleClass().add("statusLabel");
        fillPickingPane();
        marginInPickingPane();
    }

    private void createBattleShipButton(){
        battleShip = new Button("Battleship");

        battleShip.setMaxWidth(Double.MAX_VALUE);
        battleShip.setMaxHeight(Double.MAX_VALUE);
    }

    private void createCruiserShipButton(){

        cruiserShip = new Button("Crusership");

        cruiserShip.setMaxWidth(Double.MAX_VALUE);
        cruiserShip.setMaxHeight(Double.MAX_VALUE);
    }

    private void createNextSetupButton(){
        nextSetup = new Button("Pokračovat");

        nextSetup.setMaxWidth(Double.MAX_VALUE);
        nextSetup.setMaxHeight(Double.MAX_VALUE);
    }

    private GridPane createPickingPane(){
        GridPane pickship = new GridPane();
        pickship.setMaxWidth(Double.MAX_VALUE);
        pickship.setMaxHeight(Double.MAX_VALUE);

        double height = 83/3;
        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(7);

        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints2.setPercentHeight(height);

        RowConstraints rowConstraints3 = new RowConstraints();
        rowConstraints3.setPercentHeight(height);

        RowConstraints rowConstraints4 = new RowConstraints();
        rowConstraints4.setPercentHeight(height);

        RowConstraints rowConstraints5 = new RowConstraints();
        rowConstraints5.setPercentHeight(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(30);
        columnConstraints.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(70);
        columnConstraints1.setHalignment(HPos.CENTER);

        pickship.getColumnConstraints().addAll(columnConstraints, columnConstraints1);
        pickship.getRowConstraints().addAll(rowConstraints1,rowConstraints2, rowConstraints3, rowConstraints4);

        return pickship;
    }

    private void fillPickingPane(){
        pickship.add(title, 0, 0);
        pickship.add(battleShip, 0, 1);
        pickship.add(cruiserShip, 0, 2);
        pickship.add(showArea, 1, 0, 1, 4);
        pickship.add(nextSetup,1,4);
    }

    private void marginInPickingPane(){
        pickship.setMargin(battleShip, new Insets(5,5,5,5));
        pickship.setMargin(cruiserShip, new Insets(5,5,5,5));
    }

    public GridPane getPickship() {
        return pickship;
    }

    public Button getNextSetup() {
        return nextSetup;
    }

    public void clean(){
        GridPane parent = ((GridPane) pickship.getParent());
        if(!GlobalVariables.isEmpty(parent)){
            parent.getChildren().remove(pickship);
        }
    }
}
