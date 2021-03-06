package game.startUpMenu;

import game.ships.AdmiralShip;
import game.static_classes.GameBalance;
import game.static_classes.GlobalVariables;
import game.ships.BattleShip;
import game.ships.CommonShip;
import game.ships.CruiserShip;
import game.static_classes.StyleClasses;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;


/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class PickShipMenu extends CommonMenu{
    private GridPane pickship;
    private Pane showArea;
    private Button battleShip, cruiserShip, admiralShip, nextSetup, previous;
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
        titleOfShip = new SimpleStringProperty("");
        init();
    }

    private void init(){
        createBattleShipButton();
        createCruiserShipButton();
        createAdmiralShipButton();
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
        previous = createButton("Zpět",StyleClasses.EXIT_BUTTON );
    }

    private void createTitleLabel(){
        title = new Label("Dostupné lodě");
        title.getStyleClass().add("statusLabel");
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
        battleShip = createShipButton(GameBalance.BATTLE_SHIP_NAME,new BattleShip(false));
    }

    private void createAdmiralShipButton(){
        admiralShip = createShipButton(GameBalance.ADMIRAL_SHIP_NAME, new AdmiralShip(false));
    }

    private void createCruiserShipButton(){
        cruiserShip = createShipButton(GameBalance.CRUISER_SHIP_NAME, new CruiserShip(false));
    }

    private Button createShipButton(String text, CommonShip ship) {
        Button button = new Button(text);

        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMinHeight(70);
        button.getStyleClass().add(StyleClasses.MENU_BUTTONS);
        button.setCursor(StyleClasses.HAND_CURSOR);
        button.setOnAction(event -> {
            double x = showArea.getWidth();
            double y = showArea.getHeight()/2 +ship.getHeight()/2;
            createShip(ship, x, y);


        });
        return button;
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

        choosenShip.positionOfShip(x, y, showArea);
        choosenShip.resize(0, x, 0, y);
        end = showArea.getChildren().size();

        titleOfShip.setValue(choosenShip.getName());
        animationOfStatus.playFromStart();
        isStatusShowedUp = true;

        if(hBoxes.isEmpty()){
            fillStatusesPaneWithHboxes();
        }
    }

    private void createVBox(){
        menuVBox = new VBox(10);
        menuVBox.setStyle("-fx-background-color: rgba(0,0,0,0.8)");
        menuVBox.getChildren().addAll(battleShip, cruiserShip, admiralShip);

    }

    private void createNextSetupButton(){
        nextSetup = createButton("Pokračovat", StyleClasses.MENU_BUTTONS);
        GridPane.setHalignment(nextSetup, HPos.RIGHT);
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
        columnConstraints2.setMaxWidth(200);
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

        //nastaveni pozice panelu
        shipStatus.widthProperty().addListener((observable, oldValue, newValue) -> {

            statuses.setLayoutX(newValue.intValue());
            if(isStatusShowedUp){
                statuses.setLayoutX(newValue.intValue() - statuses.getWidth() - 15);
            }
            statuses.setMinWidth(newValue.intValue() - 15);
        });

        statuses.setMinHeight(300);
        statuses.setLayoutY(50);
        statuses.setPadding(new Insets(10,10,10,10));

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
        statuses.getRowConstraints().addAll(lifeLabelRow, lifeRow,
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

        statuses.add(lifeTitle, 0, 0);
        statuses.add(shieldTitle, 0, 2);
        statuses.add(armorTitle, 0, 4);
        statuses.add(speedTitle, 0, 6);
        statuses.add(energyTitle, 0, 8);

        statuses.setVgap(10);
        animationOfStatus = new Timeline(new KeyFrame(Duration.seconds(0.03), event -> animation() ));
        animationOfStatus.setCycleCount(Animation.INDEFINITE);
    }

    private void fillStatusesPaneWithHboxes(){
        statuses.add(createHBoxStatistic(Color.GREEN, GameBalance.SHIP_LIFE, choosenShip.getTotalLife().intValue()), 0, 1);
        statuses.add(createHBoxStatistic(Color.BLUE, GameBalance.SHIP_SHIELDS, choosenShip.getShieldMaxLife()), 0, 3);
        statuses.add(createHBoxStatistic(Color.YELLOW, GameBalance.SHIP_ARMOR, choosenShip.getArmorMaxValue()), 0, 5);
        statuses.add(createHBoxStatistic(Color.ORANGE, GameBalance.SHIP_SPEED, choosenShip.getSpeedMaxValue()), 0, 7);
        statuses.add(createHBoxStatistic(Color.PURPLE, GameBalance.SHIP_ENERGY, choosenShip.getEnergyMaxValue()), 0, 9);

        Circle pointsCircle = new Circle();
        pointsCircle.setRadius(25);
        pointsCircle.setStroke(Color.WHITE);

        Label pointsLabel = new Label(choosenShip.getPointsForEquipment() + "");
        pointsLabel.setFont(Font.font(16));
        pointsLabel.setTextFill(Color.WHITE);

        statuses.add(pointsCircle, 0, 10);
        statuses.add(pointsLabel, 0, 10);

    }

    private HBox createHBoxStatistic(Color color, double maxNumber, double actualNumber){
        HBox statistic = new HBox(3);
        statistic.setAlignment(Pos.CENTER);
        ArrayList <Rectangle> rectangles = new ArrayList<>();

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
            if(statuses.getLayoutX() < 10){
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

        GridPane.setHalignment(shipStatus, HPos.RIGHT);
        pickship.setHalignment(nameOfShip, HPos.CENTER);
    }

    private void marginInPickingPane(){
        menuVBox.setPadding(new Insets(10, 10 ,10 ,10 ));
        GridPane.setMargin(nextSetup, new Insets(0,10,10,10));
        GridPane.setMargin(previous, new Insets(0,10,10,10));
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

            double width = newValue.doubleValue();
            double height = showArea.getHeight()/2 + choosenShip.getHeight()/2;

            choosenShip.resize(0, width, 0, height);
        });

        showArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(GlobalVariables.isEmpty(choosenShip)){
                return;
            }

            double width = showArea.getWidth();
            double height = newValue.doubleValue()/2 + choosenShip.getHeight()/2;

            choosenShip.resize(0, width, 0, height);
        });
    }
}
