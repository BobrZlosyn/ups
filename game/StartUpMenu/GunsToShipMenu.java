package game.StartUpMenu;

import game.shields.draggableShileds.DraggableStrongerShield;
import game.shields.shieldModels.StrongerShieldModel;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.construction.AShipEquipment;
import game.construction.CommonConstruction;
import game.construction.CommonDraggableObject;
import game.construction.CommonModel;
import game.shields.draggableShileds.DraggableSimpleShield;
import game.shields.shieldModels.SimpleShieldModel;
import game.ships.CommonShip;
import game.weapons.draggableWeapons.DraggableCannon;
import game.weapons.draggableWeapons.DraggableDoubleCannon;
import game.weapons.draggableWeapons.DraggableSimpleLaser;
import game.weapons.modelsWeapon.ModelCannon;
import game.weapons.modelsWeapon.ModelDoubleCannon;
import game.weapons.modelsWeapon.ModelSimpleLaserWeapon;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
        previous.getStyleClass().add("prevButton");
    }

    private void createNextButton(){
        next = new Button("Pokračovat");
        next.setMaxHeight(Double.MAX_VALUE);
        next.getStyleClass().add("nextButton");
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
        gunsToShipPane.add(nameOfShip, 1, 0);
        gunsToShipPane.add(next, 2,2);
        gunsToShipPane.add(previous, 1, 2);
        gunsToShipPane.add(showArea,1,1);
        gunsToShipPane.add(shipStatus,2,1);
        gunsToShipPane.setHalignment(nameOfShip, HPos.CENTER);


        Circle pointsCircle = new Circle();
        pointsCircle.setRadius(25);
        pointsCircle.setFill(Color.TRANSPARENT);
        pointsCircle.setStroke(Color.WHITE);

        Label pointsLabel = new Label();
        pointsLabel.textProperty().bind(GlobalVariables.choosenShip.getAvailablePointsProperty().asString());
        pointsLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            int points = Integer.parseInt(newValue);
            if(points < 2){
                pointsLabel.setTextFill(Color.RED);
                pointsCircle.setStroke(Color.RED);
            }else {
                pointsCircle.setStroke(Color.WHITE);
                pointsLabel.setTextFill(Color.WHITE);
            }
        });

        pointsLabel.setFont(Font.font(16));
        pointsLabel.setMaxWidth(Double.MAX_VALUE);
        pointsLabel.setMaxHeight(Double.MAX_VALUE);
        pointsLabel.setAlignment(Pos.CENTER);
        pointsLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        pointsLabel.setTextFill(Color.WHITE);

        gunsToShipPane.add(pointsLabel, 2, 0);
        gunsToShipPane.add(pointsCircle, 2, 0);
        gunsToShipPane.setHalignment(pointsLabel, HPos.CENTER);
        gunsToShipPane.setHalignment(pointsCircle, HPos.CENTER);

        ship.positionOfShip(ship.getX(), ship.getY(), showArea);
        items = new VBox(5);
        items.setStyle("-fx-background-color: rgba(0,0,0,0.8);");

        /* pridavani vybaveni do panelu*/
        items.getChildren().add(createItem(new ModelDoubleCannon(), GameBalance.DOUBLE_CANNON_EQUIPMENT_NAME, 35, 35, new DraggableDoubleCannon(35, 35)));
        items.getChildren().add(createItem(new ModelCannon(), GameBalance.CANNON_EQUIPMENT_NAME, 35, 35, new DraggableCannon(35, 35)));
        items.getChildren().add(createItem(new SimpleShieldModel(), GameBalance.SHIELD_EQUIPMENT_NAME, 35, 35, new DraggableSimpleShield(35, 35)));
        items.getChildren().add(createItem(new StrongerShieldModel(), GameBalance.STRONGER_SHIELD_EQUIPMENT_NAME, 35, 35, new DraggableStrongerShield(35, 35)));
        items.getChildren().add(createItem(new ModelSimpleLaserWeapon(), GameBalance.SIMPLE_LASER_EQUIPMENT_NAME, 35, 35, new DraggableSimpleLaser(35, 35)));

        gunsToShipPane.add(items, 0,1, 1, 2);
        GridPane.setMargin(next, new Insets(0,10,10,10));
        GridPane.setMargin(previous, new Insets(0,10,10,10));
    }


    private Pane createItem(CommonModel commonModel, String name, double x, double y, CommonDraggableObject draggableObject){

        Pane item = new Pane();
        item.setMaxWidth(Double.MAX_VALUE);
        item.setMaxHeight(70);
        item.setMinHeight(70);


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
                nameOfEquipment.set(((CommonConstruction)draggableObject.getObject()).getName());
                AShipEquipment shipEquipment = (AShipEquipment)draggableObject.getObject();
                if(GlobalVariables.isEmpty(equipment) || !(shipEquipment.getConstructionType()).equals(equipment.getConstructionType())){
                    animationOfStatus.playFromStart();
                    isStatusShowedUp = true;
                }

                equipment = shipEquipment;
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
            draggableObject.removeModel(paneOnTop);
        });

        overPane.setOnMouseEntered(event -> item.setStyle("-fx-background-color: rgba(0,0,0,0.9);  -fx-cursor: hand;"));
        overPane.setOnMouseExited(event -> item.setStyle("-fx-background-color: rgba(0,0,0,0); -fx-cursor: pointer;"));
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

            double width = newValue.intValue();
            double height = showArea.getHeight()/2 + ship.getHeight()/2;
            ship.resize(0, width, 0, height);
        });

        showArea.heightProperty().addListener((observable, oldValue, newValue) -> {

            double width = showArea.getWidth();
            double height = newValue.intValue()/2 + ship.getHeight()/2;
            ship.resize(0, width, 0, height);
        });
    }


    /**
     * metody pro animaci prijezdu panelu
     *
     */
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

        RowConstraints lifeLabelRow = new RowConstraints(); //life
        RowConstraints lifeRow = new RowConstraints(); //life

        RowConstraints shieldLabelRow = new RowConstraints(); //shield
        RowConstraints shieldRow = new RowConstraints(); //shield stat


        RowConstraints armorLabelRow = new RowConstraints(); //armor
        RowConstraints armorRow = new RowConstraints(); //armor

        RowConstraints speedLabelRow = new RowConstraints(); //speed
        RowConstraints speedRow = new RowConstraints(); //speed

        RowConstraints energyLabelRow = new RowConstraints(); //energy
        RowConstraints energyRow = new RowConstraints(); //energy

        RowConstraints descriptionRow = new RowConstraints(); //description

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100);
        columnConstraints.setHalignment(HPos.CENTER);


        statuses.getColumnConstraints().addAll(columnConstraints);
        statuses.getRowConstraints().addAll(nameRow,lifeLabelRow, lifeRow,
                shieldLabelRow, shieldRow, armorLabelRow, armorRow, speedLabelRow,
                speedRow, energyLabelRow, energyRow, descriptionRow);

        Label lifeTitle = new Label("ZDRAVÍ VYBAVENÍ");
        lifeTitle.setTextFill(Color.WHITE);
        Label shieldTitle = new Label("SPOTŘEBA ENERGIE");
        shieldTitle.setTextFill(Color.WHITE);
        Label armorTitle = new Label("ÚTOČNÁ SÍLA");
        armorTitle.setTextFill(Color.WHITE);
        Label speedTitle = new Label("ŠTÍTY");
        speedTitle.setTextFill(Color.WHITE);

        statuses.add(lifeTitle, 0, 1);
        statuses.add(shieldTitle, 0, 3);
        statuses.add(armorTitle, 0, 5);
        statuses.add(speedTitle, 0, 7);


        Label titleOfShipString = new Label();
        titleOfShipString.textProperty().bind(nameOfEquipment);
        titleOfShipString.setTextFill(Color.WHITE);

        statuses.add(titleOfShipString, 0, 0);

        statuses.setVgap(10);
        statuses.setHalignment(titleOfShipString, HPos.CENTER);
        animationOfStatus = new Timeline(new KeyFrame(Duration.seconds(0.03), event -> animation() ));
        animationOfStatus.setCycleCount(Animation.INDEFINITE);
    }

    private void fillStatusesPaneWithHboxes(){
        statuses.add(createHBoxStatistic(Color.GREEN, GameBalance.EQUIPMENT_LIFE, equipment.getTotalLife().intValue()), 0, 2);
        statuses.add(createHBoxStatistic(Color.PURPLE, GameBalance.EQUIPMENT_ENERGY_COST, equipment.getEnergyCost()), 0, 4);
        statuses.add(createHBoxStatistic(Color.RED, GameBalance.EQUIPMENT_STRENGTH, equipment.getMaxStrength()), 0, 6);
        statuses.add(createHBoxStatistic(Color.BLUE, GameBalance.EQUIPMENT_SHIELDS, equipment.getShieldBonus()), 0, 8);

        Circle pointsCircle = new Circle();
        pointsCircle.setRadius(25);
        pointsCircle.setStroke(Color.WHITE);

        Label pointsLabel = new Label(equipment.getCostOfEquipment() + "");
        pointsLabel.setFont(Font.font(16));
        pointsLabel.setTextFill(Color.WHITE);

        statuses.add(pointsCircle, 0, 10);
        statuses.add(pointsLabel, 0, 10);
    }


    private HBox createHBoxStatistic(Color color, double maxNumber, double actualNumber){
        HBox statistic = new HBox(3);
        statistic.setAlignment(Pos.CENTER);
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        int percentOfStatus = (int)((actualNumber/maxNumber) * 10);

        for(int i = 0; i < 10; i++){
            Rectangle point = new Rectangle(10, 10);

            if(i >= percentOfStatus ){
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
            if(statuses.getLayoutX() < 12){
                isStatusShowedUp = true;
                animationOfStatus.stop();
            }
        }
    }
}
