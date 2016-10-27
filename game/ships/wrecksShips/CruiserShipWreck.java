package game.ships.wrecksShips;

import game.construction.CommonWreck;
import game.static_classes.WrecksHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by BobrZlosyn on 16.10.2016.
 */
public class CruiserShipWreck extends CommonWreck {

    public CruiserShipWreck(double x, double y, Color color){
        super(x, y, color, 200, true);
        createWrecks();
        createMovementPoints();
    }


    private Polygon createWrecksOfShip(double points []){
        Polygon polygon = new Polygon(points);
        polygon.setFill(Color.WHITE);
        return  polygon;
    }

    @Override
    public void addWrecksToPane(Pane gameArea, double x, double y) {
        gameArea.getChildren().addAll(wrecks);
        wrecks.get(0).setLayoutY(y - 80);
        wrecks.get(0).setLayoutX(x - 60);

        wrecks.get(1).setLayoutY(y + 80);
        wrecks.get(1).setLayoutX(x + 20);

        wrecks.get(2).setLayoutY(y);
        wrecks.get(2).setLayoutX(x - 60);

        wrecks.get(3).setLayoutY(y);
        wrecks.get(3).setLayoutX(x + 20);

        wrecks.get(4).setLayoutY(y + 80);
        wrecks.get(4).setLayoutX(x - 60);

        wrecks.get(5).setLayoutY(y + 80);
        wrecks.get(5).setLayoutX(x + 20);


        WrecksHandler.addWrecks(this);
    }

    @Override
    public void removeWrecks() {

    }

    @Override
    protected void createWrecks() {
        double points [] = {
                0.0, 0.0,
                25.0, -15.0,
                50.0, -10.0,
                65.0, -20.0,
                75.0, -2.0,
                78.0, 10.0,
                60.0, 25.0,
                50.0, 20.0,
                32.0, 15.0,
                5.0, 3.0
        };


        wrecks.add(createWrecksOfShip(points));
        wrecks.add(createWrecksOfShip(points));
        wrecks.add(createWrecksOfShip(points));

        wrecks.add(createWrecksOfShip(points));
        wrecks.add(createWrecksOfShip(points));
        wrecks.add(createWrecksOfShip(points));
    }

    @Override
    protected void createMovementPoints() {
        moveAddPoint = new double [wrecks.size()][2];

        setMovementPoint(0, -2, -2);
        setMovementPoint(1, 2, -1);
        setMovementPoint(2, 1, -2);

        setMovementPoint(3, -1, -1);
        setMovementPoint(4, 0, 1);
        setMovementPoint(5, 2, -2);

    }
}
