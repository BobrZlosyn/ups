package game.shields.wrecksShields;

import game.static_classes.WrecksHandler;
import game.construction.CommonWreck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * Created by BobrZlosyn on 15.10.2016.
 */
public class SimpleShieldWrecks extends CommonWreck {

    public SimpleShieldWrecks(double x, double y, Color color){
        super(x, y, color, 100);
        createWrecks();
        createMovementPoints();
    }

    protected void createMovementPoints(){
        moveAddPoint = new double [wrecks.size()][2];

        setMovementPoint(0, -2, -2);
        setMovementPoint(1, 2, -1);
        setMovementPoint(2, 1, -2);

        setMovementPoint(3, -1, -1);
        setMovementPoint(4, 0, 1);
        setMovementPoint(5, 2, -2);

        setMovementPoint(6, -1, 1);
        setMovementPoint(7, 3, 2);
        setMovementPoint(8, 3, 1);
    }

    protected void createWrecks(){
        double points [] = {
                0.0, 0.0,
                10.0, -5.0,
                15.0, 6.0,
                11.0, 12.0,
                3.0, 14.0
        };

        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));

        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));

        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
        wrecks.add(createWreckPolygon(points));
    }

    private Polygon createWreckPolygon(double [] points){
        Polygon wreck = new Polygon(points);
        wreck.setFill(Color.ORANGE);
        return wreck;
    }

    @Override
    public void addWrecksToPane(Pane gameArea, double x, double y){

        for (int i = 0, j = 0, k = 0; i < wrecks.size();  i++, k++){
            if(i%3==0 && i != 0){
                j ++;
                k = 0;
            }

            Shape wreck = wrecks.get(i);
            wreck.setLayoutX(x + k*15 + 2);
            wreck.setLayoutY(y + j*15 + 5);
        }

        gameArea.getChildren().addAll(wrecks);
        WrecksHandler.addWrecks(this);
    }
}
