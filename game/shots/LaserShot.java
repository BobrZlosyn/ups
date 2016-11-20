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
    private int countHit;

    public LaserShot(CommonConstruction target, CommonConstruction attacker, int damage, boolean intoShields) {
        super(target, attacker, damage, intoShields);

        shot = new Line();
        countHit = 0;
        setXY();
        shot.setStrokeWidth(2.5);
        shot.setFill(Color.RED);
        shot.setStroke(Color.RED);
    }

    public void setXY(){
        //posunuti pred kanon
        for(int i = 0; i < 15; i++){
            double [] coordinates = rovnicePrimka(x1,y1,target.getCenterX(),target.getCenterY());
            x1 = coordinates[0];
            y1 = coordinates[1];
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
        boolean resultShield = false;
        boolean resultTarget;

        for (int i = 0; i < 3; i++){
            if(countHit != 0 || resultShield){
                break;
            }

            double [] coordinates = rovnicePrimka(x1,y1,target.getCenterX(),target.getCenterY());
            x1 = coordinates[0];
            y1 = coordinates[1];

            if(isIntoShields() && target.getPlacement().getShip().getShieldActualLife() != 0){
                resultShield = target.getPlacement().getShip().isOnShield(x1, y1);
            }else {
                setIntoShields(false);
            }
        }

        shot.setEndX(x1);
        shot.setEndY(y1);
        resultTarget = target.containsPosition(x1,y1);

        if(countHit <= 25 && (resultTarget || resultShield)){
            countHit ++;
            hittingTarget(resultTarget);
            return false;
        } else if (countHit > 25) {
            return true;
        }

        return false;
    }

    private void hittingTarget(boolean resultTarget){
        if (resultTarget){
            if(countHit % 4 == 0) {
                target.getModel().setDefaultSkin();
            }else {
                target.getModel().getParts().forEach(shape -> shape.setFill(Color.RED));
            }
        }
    }


    @Override
    public CommonWreck getWreck() {
        return null;
    }
}
