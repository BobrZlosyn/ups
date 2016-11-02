package game.shots.wrecksShot;

import game.static_classes.WrecksHandler;
import game.construction.CommonWreck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by BobrZlosyn on 16.10.2016.
 */
public class SimpleShotWreck extends CommonWreck {

    public SimpleShotWreck(boolean isFromEnemy){
        super(15, false);
        setIsFromEnemy(isFromEnemy);
    }

    protected void createWrecks( double x, double y){
        wrecks.add(shotInShield(x, y));
        wrecks.add(shotInShield(x, y));
        wrecks.add(shotInShield(x, y));

        wrecks.add(shotInShield(x, y));
        wrecks.add(shotInShield(x, y));
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

        int koeficien = 1;
        if(isFromEnemy){
            koeficien = -1;
        }
        setMovementPoint(0, -0.9 * koeficien, -2.9);
        setMovementPoint(1, -0.8 * koeficien, -1.3);
        setMovementPoint(2, -0.5 * koeficien, 1.2);

        setMovementPoint(3, -0.5 * koeficien, 1.5);
        setMovementPoint(4, -0.8 * koeficien, 2.9);
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

        createWrecks(x, y);
        createMovementPoints();
        gameArea.getChildren().addAll(wrecks);

        WrecksHandler.addWrecks(this);
    }
}
