package game.StartUpMenu;

import game.GlobalVariables;
import game.Placement;
import game.ships.CommonShip;
import game.weapons.CannonWeapon;
import game.weapons.CommonWeapon;
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
        setupClickablePlacementPosition();
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

        Rectangle testDrag = new Rectangle(50, 50, Color.GREEN);
        testDrag.setX(87.5);
        testDrag.setY(87.5);
        pane.getChildren().add(testDrag);
        pokus(pane);


        testDrag.setOnMouseDragged(event -> {
            testDrag.setFill(Color.RED);
            testDrag.setX(event.getSceneX());
            testDrag.setY(event.getY());
            findPosition(event.getSceneX(), event.getY(), 145, 135, 15, 25);
        });

        testDrag.setOnMouseReleased(event -> {
            testDrag.setFill(Color.WHITE);

            testDrag.setX(event.getSceneX());
            testDrag.setY(event.getY());
            Placement bluePlace = findPosition(event.getSceneX(), event.getY(), 145, 135, 15, 25);
            if(GlobalVariables.isEmpty(bluePlace)){
                testDrag.setX(87.5);
                testDrag.setY(87.5);
            }else{
                testDrag.setX(bluePlace.getField().getX() + 160);
                testDrag.setY(bluePlace.getField().getY());
                testDrag.setFill(Color.ORANGE);
            }
        });

        gunsToShipPane.add(pane, 0,1);
    }

    public void pokus(Pane area){

        Circle room = new Circle(25);
        room.setFill(Color.ORANGE);

        Circle head = new Circle(15);
        head.setFill(Color.YELLOW);

        Rectangle cannon = new Rectangle(30, 10);
        cannon.setFill(Color.RED);
        room.setCenterX(100);
        room.setCenterY(100);

        head.setCenterX(100);
        head.setCenterY(100);
        cannon.setX(100 );
        cannon.setY(100 - cannon.getHeight()/2);
        area.getChildren().addAll(room, cannon, head);

        room.setOnMouseDragged(event -> {
            presun(event, room, head, cannon);
        });
        head.setOnMouseDragged(event -> {
            presun(event, room, head, cannon);
        });
        cannon.setOnMouseDragged(event -> {
            presun(event, room, head, cannon);
        });

        room.setOnMouseReleased(event -> {
            vyhodnoceniPresunu(event, room, head, cannon);
        });

        head.setOnMouseReleased(event -> {
            vyhodnoceniPresunu(event, room, head, cannon);
        });

        cannon.setOnMouseReleased(event -> {
            vyhodnoceniPresunu(event, room, head, cannon);
        });
    }

    public void vyhodnoceniPresunu(MouseEvent event, Circle room, Circle head, Rectangle cannon){

        Placement bluePlace = findPosition(event.getSceneX(), event.getY(),150, 160, 0, 10);
        if(!GlobalVariables.isEmpty(bluePlace) && bluePlace.isEmpty()){
            CommonWeapon cannonWeapon = new CannonWeapon();
            cannonWeapon.displayWeapon(bluePlace, false);
            bluePlace.setIsEmpty(false);
        }

        room.setCenterX(100);
        room.setCenterY(100);
        head.setCenterX(100);
        head.setCenterY(100);
        cannon.setX(100 );
        cannon.setY(100 - cannon.getHeight()/2);
    }
    public void presun(MouseEvent event, Circle room, Circle head, Rectangle cannon){

        cannon.setX(event.getSceneX());
        cannon.setY(event.getY() - cannon.getHeight()/2);

        head.setCenterX(event.getSceneX());
        head.setCenterY(event.getY());

        room.setCenterX(event.getSceneX());
        room.setCenterY(event.getY());
        findPosition(event.getSceneX(), event.getY(),150, 160, 0, 10);
    }
    public GridPane getGunsToShipPane() {
        return gunsToShipPane;
    }

    public Button getNextButton() {
        return next;
    }

    private void setupClickablePlacementPosition(){
        Placement[][] placements = ship.getPlacementPositions();
        for(int i = 0; i < placements.length; i++){
            for (int j = 0; j < placements[i].length; j++){
                if(placements[i][j] == null){
                    continue;
                }
            }
        }
    }

    private Placement findPosition(double x, double y, double xAdd1, double xAdd2, double yAdd1, double yAdd2 ){
        Placement[][] placements = ship.getPlacementPositions();
        Placement place = null;
        for(int i = 0; i < placements.length; i++){
            for (int j = 0; j < placements[i].length; j++){
                if(placements[i][j] == null){
                    continue;
                }
                if(!placements[i][j].isEmpty()){
                    continue;
                }

                Rectangle pokus = placements[i][j].getField();
                //pravy horni roh, levy horni roh, PH dolu, LH dolu
                if(pokus.contains(x - xAdd1, y + yAdd1) || pokus.contains(x - xAdd2, y + yAdd1)
                        || pokus.contains(x - xAdd1, y + yAdd2) || pokus.contains(x - xAdd2, y + yAdd2)){
                    pokus.setFill(Color.BLUE);
                    place = placements[i][j];
                }else{
                    pokus.setFill(Color.WHITE);
                }
            }
        }
        return place;
    }

    public void clean(){
        GridPane parent = ((GridPane) gunsToShipPane.getParent());
        if(!GlobalVariables.isEmpty(parent)){
            parent.getChildren().remove(gunsToShipPane);
        }
    }
}
