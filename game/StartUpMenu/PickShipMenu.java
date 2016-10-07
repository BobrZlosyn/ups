package game.StartUpMenu;

import game.GlobalVariables;
import game.ships.BattleShip;
import game.ships.CommonShip;
import game.ships.CruiserShip;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.sql.Time;
import java.util.ArrayList;


/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class PickShipMenu {
    private GridPane pickship;
    private Pane showArea;
    private Button battleShip, cruiserShip, nextSetup, previous;
    private Label title, nameOfShip;
    private CommonShip choosenShip;
    private int begin, end;
    private boolean isStatusShowedUp;
    private Pane shipStatus;
    private GridPane statuses;
    private Timeline animationOfStatus;
    private SimpleStringProperty titleOfShip;
    private SimpleIntegerProperty life, speed, shield, energy, armor;
    private VBox menuVBox;
    private ArrayList<HBox> hBoxes;


    public PickShipMenu(){
        pickship = createPickingPane();
        showArea = new Pane();
        isStatusShowedUp = false;
        hBoxes = new ArrayList();
        init();
    }

    private void init(){
        createBattleShipButton();
        createCruiserShipButton();
        createNextSetupButton();
        createStatusPane();
        createPreviousButton();
        createNameOfShipLabel();
        createTitleLabel();
        createVBox();
        fillPickingPane();
        marginInPickingPane();
        resize();

        battleShip.fire();
    }

    private void createPreviousButton(){
        previous = new Button("zpět");
        previous.setMaxHeight(Double.MAX_VALUE);
        previous.setMaxWidth(200);
    }

    private void createTitleLabel(){
        title = new Label("Vyberte váš typ lodi");
        title.getStyleClass().add("statusLabel");
        title.textProperty().bind(titleOfShip);
        title.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setMaxHeight(Double.MAX_VALUE);
    }

    private void createNameOfShipLabel(){
        nameOfShip = new Label("Loď není vybrána");
        nameOfShip.getStyleClass().add("statusLabel");
        nameOfShip.textProperty().bind(titleOfShip);
        nameOfShip.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        nameOfShip.setAlignment(Pos.CENTER);
        nameOfShip.setMaxWidth(Double.MAX_VALUE);
        nameOfShip.setMaxHeight(Double.MAX_VALUE);
    }

    private void createBattleShipButton(){
        battleShip = new Button("Battleship");

        battleShip.setMaxWidth(Double.MAX_VALUE);
        battleShip.setMaxHeight(Double.MAX_VALUE);
        battleShip.setMinHeight(70);
        battleShip.setOnAction(event -> {
            BattleShip ship = new BattleShip(false);
            double x = showArea.getWidth()/2;
            double y = showArea.getHeight()/2 - battleShip.getHeight()/2;
            createShip(ship, x, y );
        });
    }

    private void createCruiserShipButton(){

        cruiserShip = new Button("Crusership");

        cruiserShip.setMaxWidth(Double.MAX_VALUE);
        cruiserShip.setMaxHeight(Double.MAX_VALUE);
        cruiserShip.setMinHeight(70);
        cruiserShip.setOnAction(event -> {
            CruiserShip ship = new CruiserShip(false);
            double x = showArea.getWidth()/2  - ship.getWidth()/2;
            double y = showArea.getHeight()/2 - ship.getHeight()/2;
            createShip(ship, x, y);
        });
    }

    private void createShip(CommonShip newShip, double x, double y){

        if(!GlobalVariables.isEmpty(choosenShip)){
            if(choosenShip.getConstructionType().equals(newShip.getConstructionType())){
                return;
            }
            showArea.getChildren().remove(begin, end);
        }

        begin = showArea.getChildren().size();
        choosenShip = newShip;
        newShip.positionOfShip(x, y, showArea);
        end = showArea.getChildren().size();

        titleOfShip.setValue(choosenShip.getName());
        life.set(choosenShip.getTotalLife().intValue());

        animationOfStatus.playFromStart();

        if(hBoxes.isEmpty()){
            fillStatusesPaneWithHboxes();
        }
    }

    private void createVBox(){
        menuVBox = new VBox(5);
        menuVBox.setStyle("-fx-background-color: rgba(0,0,0,0.8)");
        menuVBox.getChildren().addAll(battleShip, cruiserShip);
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

        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(10);

        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints2.setPercentHeight(80);

        RowConstraints rowConstraints6 = new RowConstraints();
        rowConstraints6.setPercentHeight(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(25);
        columnConstraints.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(45);
        columnConstraints1.setHalignment(HPos.LEFT);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(30);
        columnConstraints2.setHalignment(HPos.CENTER);

        pickship.getColumnConstraints().addAll(columnConstraints, columnConstraints1,columnConstraints2);
        pickship.getRowConstraints().addAll(rowConstraints1,rowConstraints2, rowConstraints6);

        return pickship;
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

        titleOfShip = new SimpleStringProperty("");
        life = new SimpleIntegerProperty(10);
        shield = new SimpleIntegerProperty(10);
        armor = new SimpleIntegerProperty(10);
        speed = new SimpleIntegerProperty(10);
        energy = new SimpleIntegerProperty(10);

        Label titleOfShipString = new Label();
        titleOfShipString.textProperty().bind(titleOfShip);
        titleOfShipString.setTextFill(Color.WHITE);

        statuses.add(titleOfShipString, 0, 0);

        statuses.setHgap(10);
        statuses.setHalignment(titleOfShipString, HPos.CENTER);
        animationOfStatus = new Timeline(new KeyFrame(Duration.seconds(0.03), event -> animation() ));
        animationOfStatus.setCycleCount(Animation.INDEFINITE);
    }

    private void fillStatusesPaneWithHboxes(){
        statuses.add(createHBoxStatistic(Color.GREEN, 1000, choosenShip.getTotalLife().intValue()), 0, 2);
        statuses.add(createHBoxStatistic(Color.BLUE, 1000, choosenShip.getShieldMaxLife()), 0, 4);
        statuses.add(createHBoxStatistic(Color.YELLOW, 1000, choosenShip.getArmorMaxValue()), 0, 6);
        statuses.add(createHBoxStatistic(Color.ORANGE, 1000, choosenShip.getSpeedMaxValue()), 0, 8);
        statuses.add(createHBoxStatistic(Color.PURPLE, 1000, choosenShip.getEnergyMaxValue()), 0, 10);
    }


    private HBox createHBoxStatistic(Color color, double maxNumber, double actualNumber){
        HBox statistic = new HBox(3);
        statistic.setAlignment(Pos.CENTER);
        ArrayList <Rectangle> rectangles = new ArrayList<>();

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

    private void fillPickingPane(){
        pickship.add(title, 0, 0);
        pickship.add(nameOfShip, 1, 0, 2, 1);
        pickship.add(menuVBox, 0, 1, 1, 2);
        pickship.add(showArea, 1, 1);
        pickship.add(nextSetup, 2, 2);
        pickship.add(shipStatus, 2, 1);
        pickship.add(previous, 1, 2);

        pickship.setHalignment(nameOfShip, HPos.CENTER);
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

    public Button getPrevious() {
        return previous;
    }

    public CommonShip getChoosenShip(){
        return choosenShip;
    }

    public void clean(){
        GridPane parent = ((GridPane) pickship.getParent());
        if(!GlobalVariables.isEmpty(parent)){
            parent.getChildren().remove(pickship);
        }
    }

    private void resize(){
        showArea.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(GlobalVariables.isEmpty(choosenShip)){
                return;
            }

            double width = newValue.intValue();
            double height = showArea.getHeight()/2;
            choosenShip.resize(0, width, 0, height);
        });

        showArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(GlobalVariables.isEmpty(choosenShip)){
                return;
            }

            double width = showArea.getWidth();
            double height = newValue.intValue()/2 + choosenShip.getHeight()/2;
            choosenShip.resize(0, width, 0, height);
        });
    }
}
