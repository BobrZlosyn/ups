package game.StartUpMenu;

import game.GlobalVariables;
import game.ships.BattleShip;
import game.ships.CommonShip;
import game.ships.CruiserShip;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.sql.Time;


/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class PickShipMenu {
    private GridPane pickship;
    private Pane showArea;
    private Button battleShip, cruiserShip, nextSetup;
    private Label title, nameOfShip;
    private CommonShip choosenShip;
    private int begin, end;
    private boolean isStatusShowedUp;

    public PickShipMenu(){
        pickship = createPickingPane();
        showArea = new Pane();
        isStatusShowedUp = false;
        init();
    }

    private void init(){
        createBattleShipButton();
        createCruiserShipButton();
        createNextSetupButton();
        createNameOfShipLabel();
        createTitleLabel();
        createStatusPane();
        fillPickingPane();
        marginInPickingPane();
        resize();

        battleShip.fire();
    }

    private void createTitleLabel(){
        title = new Label("Vyberte váš typ lodi");
        title.getStyleClass().add("statusLabel");
    }

    private void createNameOfShipLabel(){
        nameOfShip = new Label("Loď není vybrána");
        nameOfShip.getStyleClass().add("statusLabel");
    }

    private void createBattleShipButton(){
        battleShip = new Button("Battleship");

        battleShip.setMaxWidth(Double.MAX_VALUE);
        battleShip.setMaxHeight(Double.MAX_VALUE);
        battleShip.setOnAction(event -> {
            BattleShip ship = new BattleShip(false);
            double x = showArea.getWidth()/2;
            double y = 250;
            createShip(ship, x, y );
            animationOfStatus.playFromStart();
        });
    }

    private void createCruiserShipButton(){

        cruiserShip = new Button("Crusership");

        cruiserShip.setMaxWidth(Double.MAX_VALUE);
        cruiserShip.setMaxHeight(Double.MAX_VALUE);
        cruiserShip.setOnAction(event -> {
            CruiserShip ship = new CruiserShip(false);
            double x = showArea.getWidth()/2  - ship.getWidth()/2;
            double y = 80;
            createShip(ship, x, y);
            animationOfStatus.playFromStart();
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
        nameOfShip.setText(newShip.getName());


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
        rowConstraints2.setPercentHeight(20);

        RowConstraints rowConstraints3 = new RowConstraints();
        rowConstraints3.setPercentHeight(20);

        RowConstraints rowConstraints4 = new RowConstraints();
        rowConstraints4.setPercentHeight(20);

        RowConstraints rowConstraints5 = new RowConstraints();
        rowConstraints5.setPercentHeight(30);

        RowConstraints rowConstraints6 = new RowConstraints();
        rowConstraints6.setPercentHeight(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(20);
        columnConstraints.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(50);
        columnConstraints1.setHalignment(HPos.CENTER);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(30);
        columnConstraints2.setHalignment(HPos.CENTER);

        pickship.getColumnConstraints().addAll(columnConstraints, columnConstraints1,columnConstraints2);
        pickship.getRowConstraints().addAll(rowConstraints1,rowConstraints2, rowConstraints3, rowConstraints4, rowConstraints5, rowConstraints6);

        return pickship;
    }

    private Pane shipStatus;
    private GridPane statuses;
    private Timeline animationOfStatus;
    private void createStatusPane(){
        shipStatus = new Pane();
        statuses = new GridPane();
        shipStatus.getChildren().add(statuses);

        statuses.setStyle("-fx-background-color: red;");
        statuses.add(new Label("aaaaaaaa"), 0,0);
        statuses.setLayoutX(300);

        animationOfStatus = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> animation() ));
        animationOfStatus.setCycleCount(Animation.INDEFINITE);
    }

    private void animation(){
        if(isStatusShowedUp){
            statuses.setLayoutX(statuses.getLayoutX() + 5);
            if(statuses.getLayoutX() > shipStatus.getWidth()){
                isStatusShowedUp = false;
                animationOfStatus.stop();
                animationOfStatus.playFromStart();
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
        pickship.add(nameOfShip, 1, 0);
        pickship.add(battleShip, 0, 1);
        pickship.add(cruiserShip, 0, 2);
        pickship.add(showArea, 1, 0, 1, 5);
        pickship.add(nextSetup,2,5);
        pickship.add(shipStatus,2,1);
    }

    private void marginInPickingPane(){
        pickship.setMargin(battleShip, new Insets(5,5,5,5));
        pickship.setMargin(cruiserShip, new Insets(5,5,5,5));
    }

    public Button getBattleShip() {
        return battleShip;
    }

    public Button getCruiserShip() {
        return cruiserShip;
    }

    public GridPane getPickship() {
        return pickship;
    }

    public Button getNextSetup() {
        return nextSetup;
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

            choosenShip.resize(0, newValue.intValue(), 0, showArea.getHeight());
        });

        showArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(GlobalVariables.isEmpty(choosenShip)){
                return;
            }

            choosenShip.resize(0, showArea.getWidth(), 0, newValue.intValue());
        });
    }
}
