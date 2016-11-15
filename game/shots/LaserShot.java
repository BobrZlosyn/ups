package game.shots;

import game.construction.CommonConstruction;
import game.construction.CommonWreck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by Kanto on 15.11.2016.
 */
public class LaserShot extends CommonShot {
    private Line shot;

    public LaserShot(CommonConstruction target, CommonConstruction attacker, int damage, boolean intoShields) {
        super(target, attacker, damage, intoShields);

        shot = new Line();

        setXY();
        shot.setStrokeWidth(2.5);
        shot.setFill(Color.RED);
        shot.setStroke(Color.RED);
    }

    public void setXY(){
        //posunuti pred kanon
        for(int i = 0; i < 20; i++){
            pocitatTrasu();
        }

        shot.setStartX(x1);
        shot.setStartY(y1);
        shot.setEndX(x1);
        shot.setEndY(y1);
    }

    @Override
    public void addShot(Pane gameArea) {
        gameArea.getChildren().add(shot);
    }

    @Override
    public void removeShot(Pane gameArea) {
        gameArea.getChildren().remove(shot);
    }

    @Override
    public boolean pocitatTrasu() {
        double [] coordinates = rovnicePrimka(x1,y1,target.getCenterX(),target.getCenterY());

        x1 = coordinates[0];
        y1 = coordinates[1];

        shot.setEndX(x1);
        shot.setEndY(y1);

        if(isIntoShields() && target.getPlacement().getShip().getShieldActualLife() != 0){
            return target.getPlacement().getShip().isOnShield(x1, y1);
        }else {
            setIntoShields(false);
        }

        return target.containsPosition(x1,y1);
    }

    @Override
    public CommonWreck getWreck() {
        return null;
    }
}
