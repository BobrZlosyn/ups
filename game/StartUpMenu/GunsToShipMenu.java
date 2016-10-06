package game.StartUpMenu;

import game.GlobalVariables;
import game.shields.draggableShileds.DraggableSimpleShield;
import game.ships.CommonShip;
import game.weapons.draggableWeapons.DraggableCannon;
import game.weapons.draggableWeapons.DraggableDoubleCannon;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by Kanto on 29.09.2016.
 */
public class GunsToShipMenu {

    private CommonShip ship;
    private GridPane gunsToShipPane;
    private Button next;
    private Pane showArea;

    public GunsToShipMenu(CommonShip ship){
        this.ship = ship;
        init();
    }

    private void init(){
        createGunsToShipPane();
        createNextButton();
        createShowArea();
        fillGunsToShipPane();
        resize();
    }

    private void createNextButton(){
        next = new Button("pokračovat");
    }

    private void createShowArea(){
        showArea = new Pane();
    }

    private void createGunsToShipPane(){
        gunsToShipPane = new GridPane();
        gunsToShipPane.setMaxWidth(Double.MAX_VALUE);
        gunsToShipPane.setMaxHeight(Double.MAX_VALUE);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(30);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(70);
        columnConstraints2.setHalignment(HPos.RIGHT);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(10);
        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(80);
        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints2.setPercentHeight(10);

        gunsToShipPane.getColumnConstraints().addAll(columnConstraints, columnConstraints2);
        gunsToShipPane.getRowConstraints().addAll(rowConstraints, rowConstraints1, rowConstraints2);
    }

    private void fillGunsToShipPane(){
        gunsToShipPane.add(next, 1,2);
        gunsToShipPane.add(showArea,1,1);
        ship.positionOfShip(ship.getX(), ship.getY(), showArea);

        /*Pane pane = new Pane();
        DraggableCannon draggableCannon = new DraggableCannon(pane, ship.getPlacementPositions());
        DraggableDoubleCannon draggableDoubleCannon = new DraggableDoubleCannon(pane, ship.getPlacementPositions(), 100, 300);
        DraggableSimpleShield draggableSimpleShield = new DraggableSimpleShield(pane, ship.getPlacementPositions());
*/
        VBox items = new VBox(5);


        items.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        Pane pane = new Pane();
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.setMaxHeight(70);
        pane.setMinHeight(70);
        pane.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        DraggableDoubleCannon draggableDoubleCannon = new DraggableDoubleCannon(pane, ship.getPlacementPositions(), 35, 35);
        Label name = new Label("Double Cannon");
        name.setTextFill(Color.WHITE);
        pane.getChildren().addAll(name);
        items.getChildren().add(pane);
        name.setLayoutX(draggableDoubleCannon.getModel().getWidth() + draggableDoubleCannon.getModel().getWidth()/2);
        name.setLayoutY(25);

        Pane pane2 = new Pane();
        pane2.setMaxWidth(Double.MAX_VALUE);
        pane2.setMaxHeight(70);
        pane2.setMinHeight(70);
        pane2.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        DraggableCannon draggableCannon = new DraggableCannon(pane2, ship.getPlacementPositions(), 35, 35);
        Label name2 = new Label("Cannon");
        name2.setTextFill(Color.WHITE);
        pane2.getChildren().addAll(name2);
        items.getChildren().add(pane2);
        name2.setLayoutX(draggableCannon.getModel().getWidth() + draggableCannon.getModel().getWidth()/2);
        name2.setLayoutY(25);
        Pane kryt = new Pane();
        kryt.setStyle("-fx-background-color: red;");
        kryt.setMinHeight(70);
        kryt.setMinWidth(200);
        pane2.getChildren().addAll(kryt);

        gunsToShipPane.add(items, 0,1);
    }

    public GridPane getGunsToShipPane() {
        return gunsToShipPane;
    }

    public Button getNextButton() {
        return next;
    }

    public void clean(){
        GridPane parent = ((GridPane) gunsToShipPane.getParent());
        if(!GlobalVariables.isEmpty(parent)){
            parent.getChildren().remove(gunsToShipPane);
        }
        GlobalVariables.setIsUsersShieldUp(false);
    }

    public CommonShip getShip() {
        return ship;
    }


    private void resize(){
        showArea.widthProperty().addListener((observable, oldValue, newValue) -> {

            ship.resize(0, newValue.intValue(), 0, showArea.getHeight());
        });

        showArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            ship.resize(0, showArea.getWidth(), 0, newValue.intValue());
        });
    }
}
