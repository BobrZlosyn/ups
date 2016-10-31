package game.shots.wrecksShot;

import game.construction.CommonWreck;
import game.static_classes.WrecksHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by BobrZlosyn on 16.10.2016.
 */
public class DoubleShotWreck extends CommonWreck {

    public DoubleShotWreck(double x1, double y1, double x2, double y2, boolean isFromEnemy){
        super(15, false);
        setIsFromEnemy(isFromEnemy);
        createWrecks(x1, y1, x2, y2);
        createMovementPoints();
    }

    protected void createWrecks( double x1, double y1, double x2, double y2){
        wrecks.add(shotInShield(x1, y1));
        wrecks.add(shotInShield(x1, y1));
        wrecks.add(shotInShield(x1, y1));

        wrecks.add(shotInShield(x1, y1));
        wrecks.add(shotInShield(x1, y1));

        wrecks.add(shotInShield(x2, y2));
        wrecks.add(shotInShield(x2, y2));
        wrecks.add(shotInShield(x2, y2));

        wrecks.add(shotInShield(x2, y2));
        wrecks.add(shotInShield(x2, y2));
    }

    private Circle shotInShield( double x, double y){
        Circle shot = new Circle(2);
        shot.setFill(Color.BLUE);
        shot.setCenterX(x);
        shot.setCenterY(y);
        return shot;
    }

    protected void createMovementPoints(){
        moveAddPoint = new double [wrecks.size()][2];


        setMovementPoint(0, -0.9, -2.9);
        setMovementPoint(1, -0.8, -1.3);
        setMovementPoint(2, -0.5, 1.2);

        setMovementPoint(3, -0.5, 1.5);
        setMovementPoint(4, -0.8, 2.9);

        setMovementPoint(5, -0.9, -2.9);
        setMovementPoint(6, -0.8, -1.3);
        setMovementPoint(7, -0.5, 1.2);

        setMovementPoint(8, -0.5, 1.5);
        setMovementPoint(9, -0.8, 2.9);
    }

    protected void setMovementPoint(int indexOfWreck, double x, double y){
        moveAddPoint[indexOfWreck][0] = x;
        moveAddPoint[indexOfWreck][1] = y;
    }

    @Override
    protected void createWrecks() {

    }

    @Override
    public void addWrecksToPane(Pane gameArea, double x, double y) {

        gameArea.getChildren().addAll(wrecks);
        WrecksHandler.addWrecks(this);

    }
}
