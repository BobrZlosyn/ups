package game.StartUpMenu;

import game.GlobalVariables;
import game.construction.CommonDraggableObject;
import game.construction.CommonModel;
import game.shields.draggableShileds.DraggableSimpleShield;
import game.shields.shieldModels.SimpleShieldModel;
import game.ships.CommonShip;
import game.weapons.draggableWeapons.DraggableCannon;
import game.weapons.draggableWeapons.DraggableDoubleCannon;
import game.weapons.modelsWeapon.ModelCannon;
import game.weapons.modelsWeapon.ModelDoubleCannon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Created by Kanto on 29.09.2016.
 */
public class GunsToShipMenu {

    private CommonShip ship;
    private GridPane gunsToShipPane;
    private Button next, previous;
    private Pane showArea;
    private VBox items;
    private Label title, nameOfShip;

    public GunsToShipMenu(CommonShip ship){
        this.ship = ship;
        init();
    }

    private void init(){
        createGunsToShipPane();
        createNextButton();
        createShowArea();
        createTitle();
        createNameOfShip();
        createPreviousButton();
        fillGunsToShipPane();
        resize();
    }

    private void createTitle(){
        title = new Label("Dostupné vybavení");
        title.getStyleClass().add("statusLabel");
        title.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setMaxHeight(Double.MAX_VALUE);
    }

    private void createNameOfShip(){
        nameOfShip = new Label(ship.getName());
        nameOfShip.getStyleClass().add("statusLabel");
        nameOfShip.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        nameOfShip.setAlignment(Pos.CENTER);
        nameOfShip.setMaxWidth(Double.MAX_VALUE);
        nameOfShip.setMaxHeight(Double.MAX_VALUE);
    }

    private void createPreviousButton(){
        previous = new Button("Zpět");
        previous.setMaxHeight(Double.MAX_VALUE);
        previous.setMaxWidth(200);
    }

    private void createNextButton(){
        next = new Button("pokračovat");
        next.setMaxHeight(Double.MAX_VALUE);
        next.setMaxWidth(200);
    }

    private void createShowArea(){
        showArea = new Pane();
    }

    private void createGunsToShipPane(){
        gunsToShipPane = new GridPane();
        gunsToShipPane.setMaxWidth(Double.MAX_VALUE);
        gunsToShipPane.setMaxHeight(Double.MAX_VALUE);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(25);
        columnConstraints.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(45);
        columnConstraints2.setHalignment(HPos.LEFT);
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setPercentWidth(30);
        columnConstraints3.setHalignment(HPos.RIGHT);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(10);
        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(80);
        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints2.setPercentHeight(10);

        gunsToShipPane.getColumnConstraints().addAll(columnConstraints, columnConstraints2, columnConstraints3);
        gunsToShipPane.getRowConstraints().addAll(rowConstraints, rowConstraints1, rowConstraints2);
    }
    private Pane statusPanel = new Pane();
    private void fillGunsToShipPane(){
        gunsToShipPane.add(title, 0, 0);
        gunsToShipPane.add(nameOfShip, 1, 0, 2, 1);
        gunsToShipPane.add(next, 2,2);
        gunsToShipPane.add(previous, 1, 2);
        gunsToShipPane.add(showArea,1,1);
        gunsToShipPane.add(statusPanel,2,1);
        gunsToShipPane.setHalignment(nameOfShip, HPos.CENTER);

        ship.positionOfShip(ship.getX(), ship.getY(), showArea);

        items = new VBox(5);
        items.setStyle("-fx-background-color: rgba(0,0,0,0.8);");

        items.getChildren().add(createItem(new ModelDoubleCannon(), "Double Cannon", 35, 35, new DraggableDoubleCannon(35, 35)));
        items.getChildren().add(createItem(new ModelCannon(), "Cannon", 35, 35, new DraggableCannon(35, 35)));
        items.getChildren().add(createItem(new SimpleShieldModel(), "Simple shield", 35, 35, new DraggableSimpleShield(35, 35)));

        gunsToShipPane.add(items, 0,1, 1, 2);
    }

    private Pane createItem(CommonModel commonModel, String name, double x, double y, CommonDraggableObject draggableObject){

        Pane item = new Pane();
        item.setMaxWidth(Double.MAX_VALUE);
        item.setMaxHeight(70);
        item.setMinHeight(70);
        item.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        //create object
        commonModel.getParts().forEach(shape -> {
            item.getChildren().add(shape);
        });
        commonModel.setModelXY(x, y);

        //label with name of equipment
        Label nameOfItem = new Label(name);
        nameOfItem.setTextFill(Color.WHITE);
        nameOfItem.setLayoutX(commonModel.getWidth() + commonModel.getWidth() / 2);
        nameOfItem.setLayoutY(25);

        item.getChildren().add(nameOfItem);

        //pane over item for draggable purpose
        Pane overPane = new Pane();
        item.heightProperty().addListener((observable, oldValue, newValue) -> {
            overPane.setMinHeight(newValue.intValue());
            overPane.setMaxHeight(newValue.intValue());
        });
        item.widthProperty().addListener((observable, oldValue, newValue) -> {
            overPane.setMinWidth(newValue.intValue());
            overPane.setMaxWidth(newValue.intValue());
        });

        Pane paneOnTop = new Pane();
        //create hidden model for dragging
        overPane.setOnMousePressed(event -> {
            int count = 0;

            count = event.getClickCount();

            if (count > 1 && event.isPrimaryButtonDown()) {
                gunsToShipPane.add(paneOnTop, 0, 1);
                draggableObject.createModel(paneOnTop, ship.getPlacementPositions(), event.getX(), event.getSceneY() - items.getLayoutY());
            }
        });

        //set onDrag methods for item
        overPane.setOnMouseDragged(event -> {
            if(GlobalVariables.isEmpty(draggableObject.getModel())){
                return;
            }
            draggableObject.moveObject(event, draggableObject.getModel(), ship.getPlacementPositions());
        });

        overPane.setOnMouseReleased(event -> {
            gunsToShipPane.getChildren().remove(paneOnTop);

            if(GlobalVariables.isEmpty(draggableObject.getModel())){
                return;
            }

            draggableObject.isDragSuccesful(event, draggableObject.getModel(), ship.getPlacementPositions());


        });

        item.getChildren().addAll(overPane);
        return item;
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

    public Button getPrevious() {
        return previous;
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
