package game.StartUpMenu;

import game.GlobalVariables;
import game.construction.AShipEquipment;
import game.construction.CommonConstruction;
import game.construction.CommonDraggableObject;
import game.construction.CommonModel;
import game.shields.draggableShileds.DraggableSimpleShield;
import game.shields.shieldModels.SimpleShieldModel;
import game.ships.CommonShip;
import game.weapons.draggableWeapons.DraggableCannon;
import game.weapons.draggableWeapons.DraggableDoubleCannon;
import game.weapons.modelsWeapon.ModelCannon;
import game.weapons.modelsWeapon.ModelDoubleCannon;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by Kanto on 29.09.2016.
 */
public class GunsToShipMenu {

    private CommonShip ship;
    private SimpleStringProperty nameOfEquipment;
    private GridPane gunsToShipPane;
    private Button next, previous;
    private Pane showArea, shipStatus;
    private VBox items;
    private Label title, nameOfShip;
    private ArrayList<HBox> hBoxes;
    private Timeline animationOfStatus;
    private boolean isStatusShowedUp;
    private GridPane statuses;
    private AShipEquipment equipment;

    public GunsToShipMenu(CommonShip ship){
        this.ship = ship;
        hBoxes = new ArrayList();
        isStatusShowedUp = false;
        nameOfEquipment = new SimpleStringProperty("");
        init();
    }

    private void init(){
        createGunsToShipPane();
        createNextButton();
        createShowArea();
        createTitle();
        createNameOfShip();
        createPreviousButton();
        createStatusPane();
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

    private void fillGunsToShipPane(){
        gunsToShipPane.add(title, 0, 0);
        gunsToShipPane.add(nameOfShip, 1, 0, 2, 1);
        gunsToShipPane.add(next, 2,2);
        gunsToShipPane.add(previous, 1, 2);
        gunsToShipPane.add(showArea,1,1);
        gunsToShipPane.add(shipStatus,2,1);
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

            if(count == 1){
                equipment = (AShipEquipment)draggableObject.getObject();
                nameOfEquipment.set(((CommonConstruction)draggableObject.getObject()).getName());
                animationOfStatus.playFromStart();
                if(hBoxes.isEmpty()){
                    fillStatusesPaneWithHboxes();
                }
            }

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














    private void createStatusPane(){
        shipStatus = new Pane();
        statuses = new GridPane();
        shipStatus.getChildren().add(statuses);
        isStatusShowedUp = false;
        statuses.setStyle("-fx-background-color: rgba(0,0,0,0.8);");

        shipStatus.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(!isStatusShowedUp){
                statuses.setLayoutX(newValue.intValue());
            }
            statuses.setMinWidth(newValue.intValue() - 15);
        });

        statuses.setMinHeight(300);
        statuses.setLayoutY(50);
        statuses.setPadding(new Insets(10,10,10,10));

        RowConstraints nameRow = new RowConstraints(); //name of ship
        nameRow.setPercentHeight(8);

        RowConstraints lifeLabelRow = new RowConstraints(); //life
        lifeLabelRow.setPercentHeight(8);
        RowConstraints lifeRow = new RowConstraints(); //life
        lifeRow.setPercentHeight(8);

        RowConstraints shieldLabelRow = new RowConstraints(); //shield
        shieldLabelRow.setPercentHeight(8);
        RowConstraints shieldRow = new RowConstraints(); //shield stat
        shieldLabelRow.setPercentHeight(8);


        RowConstraints armorLabelRow = new RowConstraints(); //armor
        armorLabelRow.setPercentHeight(8);
        RowConstraints armorRow = new RowConstraints(); //armor
        armorRow.setPercentHeight(8);

        RowConstraints speedLabelRow = new RowConstraints(); //speed
        speedLabelRow.setPercentHeight(8);
        RowConstraints speedRow = new RowConstraints(); //speed
        speedRow.setPercentHeight(8);

        RowConstraints energyLabelRow = new RowConstraints(); //energy
        energyLabelRow.setPercentHeight(8);
        RowConstraints energyRow = new RowConstraints(); //energy
        energyRow.setPercentHeight(8);

        RowConstraints descriptionRow = new RowConstraints(); //description
        descriptionRow.setPercentHeight(12);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100);
        columnConstraints.setHalignment(HPos.CENTER);


        statuses.getColumnConstraints().addAll(columnConstraints);
        statuses.getRowConstraints().addAll(nameRow,lifeLabelRow, lifeRow,
                shieldLabelRow, shieldRow, armorLabelRow, armorRow, speedLabelRow,
                speedRow, energyLabelRow, energyRow, descriptionRow);

        Label lifeTitle = new Label("INTEGRITA TRUPU:");
        lifeTitle.setTextFill(Color.WHITE);
        Label shieldTitle = new Label("SÍLA ŠTÍTŮ:");
        shieldTitle.setTextFill(Color.WHITE);
        Label armorTitle = new Label("SÍLÁ PANCÍŘE:");
        armorTitle.setTextFill(Color.WHITE);
        Label speedTitle = new Label("RYCHLOST:");
        speedTitle.setTextFill(Color.WHITE);
        Label energyTitle = new Label("ENERGIE:");
        energyTitle.setTextFill(Color.WHITE);

        statuses.add(lifeTitle, 0, 1);
        statuses.add(shieldTitle, 0, 3);
        statuses.add(armorTitle, 0, 5);
        statuses.add(speedTitle, 0, 7);
        statuses.add(energyTitle, 0, 9);


        Label titleOfShipString = new Label();
        titleOfShipString.textProperty().bind(nameOfEquipment);
        titleOfShipString.setTextFill(Color.WHITE);

        statuses.add(titleOfShipString, 0, 0);

        statuses.setHgap(10);
        statuses.setHalignment(titleOfShipString, HPos.CENTER);
        animationOfStatus = new Timeline(new KeyFrame(Duration.seconds(0.03), event -> animation() ));
        animationOfStatus.setCycleCount(Animation.INDEFINITE);
    }

    private void fillStatusesPaneWithHboxes(){
        statuses.add(createHBoxStatistic(Color.GREEN, 1000, equipment.getTotalLife().intValue()), 0, 2);
        statuses.add(createHBoxStatistic(Color.PURPLE, 1000, equipment.getEnergyCost()), 0, 4);
        statuses.add(createHBoxStatistic(Color.RED, 1000, equipment.getMaxStrength()), 0, 6);
        statuses.add(createHBoxStatistic(Color.BLUE, 1000, equipment.getShieldBonus()), 0, 8);
    }


    private HBox createHBoxStatistic(Color color, double maxNumber, double actualNumber){
        HBox statistic = new HBox(3);
        statistic.setAlignment(Pos.CENTER);
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        int percentOfStatus = (int)((actualNumber/maxNumber) * 10);

        for(int i = 0; i < 10; i++){
            Rectangle point = new Rectangle(10, 10);

            if(i > percentOfStatus ){
                point.setFill(Color.WHITE);
            }else{
                point.setFill(color);
            }

            rectangles.add(point);
        }

        statistic.getChildren().addAll(rectangles);

        hBoxes.add(statistic);
        return statistic;
    }

    private void animation(){
        if(isStatusShowedUp){
            statuses.setLayoutX(statuses.getLayoutX() + 10);
            if(statuses.getLayoutX() > shipStatus.getWidth()){
                isStatusShowedUp = false;
                statuses.getChildren().removeAll(hBoxes);
                animationOfStatus.stop();
                animationOfStatus.playFromStart();
                fillStatusesPaneWithHboxes();
            }
        }else{
            statuses.setLayoutX(statuses.getLayoutX()-5);
            if(statuses.getLayoutX() < 0){
                isStatusShowedUp = true;
                animationOfStatus.stop();
            }
        }
    }



}
