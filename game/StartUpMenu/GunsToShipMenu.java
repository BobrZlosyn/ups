package game.StartUpMenu;

import game.GlobalVariables;
import game.Placement;
import game.ships.CommonShip;
import game.weapons.CannonWeapon;
import game.weapons.CommonWeapon;
import game.weapons.draggableWeapons.DraggableCannon;
import game.weapons.draggableWeapons.DraggableDoubleCannon;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
        columnConstraints.setPercentWidth(20);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(80);
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
        Pane pane = new Pane();
        DraggableCannon draggableCannon = new DraggableCannon(pane, ship.getPlacementPositions());
        DraggableDoubleCannon draggableDoubleCannon = new DraggableDoubleCannon(pane, ship.getPlacementPositions(), 100, 300);
        gunsToShipPane.add(pane, 0,1);
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
    }

    public CommonShip getShip() {
        return ship;
    }
}
